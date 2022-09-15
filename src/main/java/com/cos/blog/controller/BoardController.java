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
	
	private static String sort;

	private final BoardRepository boardRepository;
	private final BoardService boardService;
	
	public void find(Model model, Pageable pageable, String searchType, String searchKeyword) {
		if(searchKeyword == null || searchKeyword.isBlank()) {
			model.addAttribute("boards", boardRepository.findAll(pageable));
		}
		
		if(searchType.equals("nickname")) {
			model.addAttribute("boards", boardRepository.findByUserNicknameContaining(searchKeyword, pageable));
		} else {
			model.addAttribute("boards", boardRepository.findByTitleContaining(searchKeyword, pageable));
		}
	}
	
	@GetMapping("/")
	public String index(Model model, 
			@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "searchType", defaultValue = "title") String searchType,
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword) {
		
		find(model, pageable, searchType, searchKeyword);
		sort = "/";
		
		return "index";
	}
	
	@GetMapping("/sort/view")
	public String indexView(Model model, 
			@PageableDefault(size = 10, sort = "count", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "searchType", defaultValue = "title") String searchType,
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword) {
		
		find(model, pageable, searchType, searchKeyword);
		sort = "/sort/view";
		
		return "index";
	}
	
	@GetMapping("/sort/recommend")
	public String indexRecommend(Model model, 
			@PageableDefault(size = 10, sort = "recommendCount", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "searchType", defaultValue = "title") String searchType,
			@RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword) {
		
		find(model, pageable, searchType, searchKeyword);
		sort = "/sort/recommend";
		
		return "index";
	}
	
	@GetMapping("/board/writeForm")
	public String writeForm() {
		return "board/writeForm";
	}
	
	@GetMapping("/board/{id}")
	public String detail(@PathVariable Long id, Model model,
			HttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal PrincipalDetail principal) {
		
		model.addAttribute("board", boardService.detail(id, request, response, principal.getUser().getId()));
		model.addAttribute("sort", sort);
		
		return "board/detail";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("board", boardService.detail(id, null, null, principal.getUser().getId()));
		return "board/updateForm";
	}
}
