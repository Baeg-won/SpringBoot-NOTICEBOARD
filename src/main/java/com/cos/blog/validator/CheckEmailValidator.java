package com.cos.blog.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator<UserRequestDto> {

	private final UserRepository userRepository;

	@Override
	protected void doValidate(UserRequestDto dto, Errors errors) {
		if(userRepository.existsByEmail(dto.getEmail())) {
			errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다");
		}
	}
}
