package com.cos.blog.controller.api;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.cos.blog.validator.CheckEmailValidator;
import com.cos.blog.validator.CheckNicknameValidator;
import com.cos.blog.validator.CheckUsernameValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
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
}
