package com.cos.blog.specification;

import org.springframework.data.jpa.domain.Specification;

import com.cos.blog.model.User;

public class AdminSpecification {
	
	public static Specification<User> searchTypeUsername(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"), "%" + searchKeyword + "%");
    }
	
	public static Specification<User> searchTypeNickname(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nickname"), "%" + searchKeyword + "%");
    }
}
