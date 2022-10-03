package com.cos.blog.specification;

import org.springframework.data.jpa.domain.Specification;

import com.cos.blog.model.Board;

public class BoardSpecification {
	
	public static Specification<Board> searchTypeTitle(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + searchKeyword + "%");
    }
	
	public static Specification<Board> searchTypeWriter(String searchKeyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("nickname"), searchKeyword);
    }
	
	public static Specification<Board> equalCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }
	
	public static Specification<Board> recGreaterThan(int recommendCount) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("recommendCount"), recommendCount);
    }
}
