package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
	
	@Value("${secrect.key}")
	private String secrectKey;

	@Transactional
	public void join(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	
	@Transactional
	public void update(User user) {
		User persistance = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> {
			return new IllegalArgumentException("회원정보 수정 실패: 존재하지 않는 회원입니다.");
		});
		
		persistance.setPassword(encoder.encode(user.getPassword()));
		persistance.setEmail(user.getEmail());
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
				.password(secrectKey)
				.email(kakaoProfile.getKakao_account().getEmail())
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
}