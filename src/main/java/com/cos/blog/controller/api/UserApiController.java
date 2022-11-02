package com.cos.blog.controller.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.dto.SendTmpPwdDto;
import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.repository.UserRepository;
import com.cos.blog.service.UserService;
import com.cos.blog.validator.CheckEmailValidator;
import com.cos.blog.validator.CheckNicknameValidator;
import com.cos.blog.validator.CheckUsernameValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	private final UserRepository userRepository;
	
	private final AuthenticationManager authenticationManager;
	
	private final CheckUsernameValidator checkUsernameValidator;
	private final CheckNicknameValidator checkNicknameValidator;
	private final CheckEmailValidator checkEmailValidator;
	
	@InitBinder
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(checkUsernameValidator);
		binder.addValidators(checkNicknameValidator);
		binder.addValidators(checkEmailValidator);
	}

	@PostMapping("/auth/joinProc")
	public ResponseDto<?> join(@Valid @RequestBody UserRequestDto userDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			Map<String, String> validatorResult = userService.validateHandling(bindingResult);
			
			return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validatorResult);
		}
		
		userService.join(userDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@PutMapping("/user")
	public ResponseDto<?> update(@RequestBody UserRequestDto userDto) {
		userService.update(userDto);

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/user/{user_id}/profileImageUrl")
	public ResponseDto<?> profileImageUpdate(@PathVariable Long user_id, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetail principalDetail) {
		userService.profileImageUpdate(user_id, profileImageFile);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
	}
	
	@DeleteMapping("/api/user/delete/{user_id}")
	public ResponseDto<?> delete(@PathVariable Long user_id) {
		userService.delete(user_id);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
	}
	
	@PostMapping("/auth/find")
	public ResponseDto<?> find(@RequestBody SendTmpPwdDto dto) {
				
		if(!userRepository.existsByUsername(dto.getUsername()) || !Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", dto.getEmail())) {
			Map<String, String> validResult = new HashMap<>();
			
			if(!userRepository.existsByUsername(dto.getUsername())) {
				validResult.put("valid_username", "존재하지 않는 사용자 이름입니다.");
			}
			if(!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", dto.getEmail())) {
				validResult.put("valid_email", "올바르지 않은 이메일 형식입니다.");
			}
			
			return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), validResult); 
		}
		
		userService.sendTmpPwd(dto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
	}
}
