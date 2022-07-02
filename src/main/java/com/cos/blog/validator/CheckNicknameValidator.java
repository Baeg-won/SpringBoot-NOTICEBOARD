package com.cos.blog.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.cos.blog.dto.UserRequestDto;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CheckNicknameValidator extends AbstractValidator<UserRequestDto> {

	private final UserRepository userRepository;

	@Override
	protected void doValidate(UserRequestDto dto, Errors errors) {
		if(userRepository.existsByNickname(dto.getNickname())) {
			errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용중인 닉네임 입니다");
		}
	}
}
