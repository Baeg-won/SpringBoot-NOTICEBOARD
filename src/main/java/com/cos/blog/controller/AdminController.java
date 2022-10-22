package com.cos.blog.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import com.cos.blog.specification.AdminSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
	
	private final UserRepository userRepository;

	@GetMapping("/admin")
	public String admin(Model model,
			@PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "category", defaultValue = "user") String category,
			@RequestParam(value = "searchType", defaultValue = "") String searchType,
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
		
		Specification<User> spec = (root, query, criteriaBuilder) -> null;
		
		if(category.equals("user")) {
			if(!searchType.isEmpty()) {
				if(searchType.equals("username")) {
					spec = spec.and(AdminSpecification.searchTypeUsername(searchKeyword));
				} else {
					spec = spec.and(AdminSpecification.searchTypeNickname(searchKeyword));
				}
			}
			
			model.addAttribute("users", userRepository.findAll(spec, pageable));
		}
		
		model.addAttribute("category", category);
		
		return "admin";
	}
}
