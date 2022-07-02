package com.cos.blog.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CheckUsernameValidator extends AbstractValidator<UserRequestDto> {

	private final UserRepository userRepository;

	@Override
	protected void doValidate(UserRequestDto dto, Errors errors) {
		if(userRepository.existsByUsername(dto.getUsername())) {
			errors.rejectValue("username", "아이디 중복 오류", "이미 사용중인 아이디 입니다");
		}
	}
}
