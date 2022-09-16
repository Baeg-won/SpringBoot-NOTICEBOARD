package com.cos.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	private final BoardRepository boardRepository;
	private final BoardService boardService;
	
	@GetMapping("/")
	public String index(Model model, 
			@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "searchType", defaultValue = "title") String searchType,
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword) {
		
		if(searchKeyword == null || searchKeyword.isBlank()) {
			model.addAttribute("boards", boardRepository.findAll(pageable));
		}
		
		if(searchType.equals("nickname")) {
			model.addAttribute("boards", boardRepository.findByUserNicknameContaining(searchKeyword, pageable));
		} else {
			model.addAttribute("boards", boardRepository.findByTitleContaining(searchKeyword, pageable));
		}
		
		return "index";
	}
	
	@GetMapping("/board/writeForm")
	public String writeForm() {
		return "board/writeForm";
	}
	
	@GetMapping("/board/{id}")
	public String detail(@PathVariable Long id, Model model,
			HttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal PrincipalDetail principal,
			@RequestParam(value = "page", defaultValue = "0") String page,
			@RequestParam(value = "sort", defaultValue = "id") String sort,
			@RequestParam(value = "searchType", defaultValue = "title") String searchType,
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword) {
		
		model.addAttribute("board", boardService.detail(id, request, response, principal.getUser().getId()));
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchKeyword", searchKeyword);
		
		return "board/detail";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("board", boardService.detail(id, null, null, principal.getUser().getId()));
		return "board/updateForm";
	}
}
