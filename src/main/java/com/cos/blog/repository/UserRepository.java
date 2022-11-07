package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;

// @Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
	
	//SELECT * FROM user WHERE username = ?1;
	Optional<User> findByUsername(String username);
	
	boolean existsByUsername(String username);
	boolean existsByNickname(String nickname);
	boolean existsByEmail(String email);
}
