package com.cos.blog.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.dto.SendTmpPwdDto;
import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;
	
	private final JavaMailSender javaMailSender;
	
	@Value("${secrect.key}")
	private String secrectKey;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Value("${spring.mail.username}")
	private String sendFrom;

	@Transactional
	public void join(UserRequestDto userDto) {
		User user = User.builder()
				.username(userDto.getUsername())
				.password(encoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.email(userDto.getEmail())
				.role(RoleType.USER)
				.build();
		
		userRepository.save(user);
	}
	
	@Transactional
	public void join(User user) {		
		userRepository.save(user);
	}
	
	@Transactional
	public void update(UserRequestDto userDto) {
		User persistance = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> {
			return new IllegalArgumentException("회원정보 수정 실패: 존재하지 않는 회원입니다.");
		});
		
		persistance.setPassword(encoder.encode(userDto.getPassword()));
		persistance.setNickname(userDto.getNickname());
	}
	
	@Transactional
	public void delete(Long user_id) {
		userRepository.deleteById(user_id);
	}
	
	@Transactional(readOnly = true)
	public Map<String, String> validateHandling(BindingResult bindingResult) {
		Map<String, String> validatorResult = new HashMap<>();
		
		for(FieldError error : bindingResult.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		
		return validatorResult;
	}
	
	@Transactional(readOnly = true)
	public Map<String, String> validateHandling(Errors errors){
		Map<String, String> validatorResult = new HashMap<>();
		
		for(FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		
		return validatorResult;
	}
	
	@Transactional
	public void kakaoLogin(String code) {
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "51f6c7b1350fbf2b147798b81b8649a7");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);
		
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauToken = null;
		try {
			oauToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch(JsonMappingException e) {
			e.printStackTrace();
		} catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		
		RestTemplate rt_ = new RestTemplate();
		HttpHeaders headers_ = new HttpHeaders();
		headers_.add("Authorization", "Bearer " + oauToken.getAccess_token());
		headers_.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
				new HttpEntity<>(headers_);
		
		ResponseEntity<String> response_ = rt_.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest,
				String.class
		);
		
		ObjectMapper objectMapper_ = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper_.readValue(response_.getBody(), KakaoProfile.class);
		} catch(JsonMappingException e) {
			e.printStackTrace();
		} catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
				.password(encoder.encode(secrectKey))
				.nickname(kakaoProfile.getProperties().getNickname())
				.email(kakaoProfile.getKakao_account().getEmail())
				.role(RoleType.USER)
				.oauth("kakao")
				.build();
		
		User originUser = userRepository.findByUsername(kakaoUser.getUsername()).orElseGet(() -> {
			return new User();
		});
		
		if(originUser.getUsername() == null)
			join(kakaoUser);
		
		Authentication authentication = 
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), secrectKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	@Transactional
	public void profileImageUpdate(Long user_id, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User user = userRepository.findById(user_id).orElseThrow(() -> {
			return new IllegalArgumentException("프로필 이미지 수정 실패: 존재하지 않는 회원입니다.");
		});
		
		user.setProfile_image_url(imageFileName);
	}
	
	@Transactional
	public void sendTmpPwd(SendTmpPwdDto dto) {
		
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String tmpPwd = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            tmpPwd += charSet[idx];
        }
		
		try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(dto.getEmail());
            message.setFrom(sendFrom);
            message.setSubject("baeg-won 임시 비밀번호 안내 이메일입니다.");
            message.setText("안녕하세요. baeg-won 임시비밀번호 안내 관련 이메일 입니다.\n"
            		+ "회원님의 임시 비밀번호는 " + tmpPwd + "입니다.\n" + "로그인 후에 비밀번호를 변경을 해주세요");
            javaMailSender.send(message);
        } catch (MailParseException e) {
            e.printStackTrace();
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
        } catch (MailSendException e) {
            e.printStackTrace();
        } catch (MailException e) {
            e.printStackTrace();
        }
		
		User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> {
			return new IllegalArgumentException("임시 비밀번호 변경 실패: 사용자 이름을 찾을 수 없습니다.");
		});
		
		user.setPassword(encoder.encode(tmpPwd));
	}
}