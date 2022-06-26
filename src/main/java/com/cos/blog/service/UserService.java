package com.cos.blog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;

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
	
	@Transactional(readOnly = true)
	public User find(String username) {
		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		
		return user;
	}
}
