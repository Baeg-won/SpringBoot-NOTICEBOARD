package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpControllerTest {

	@GetMapping("/test/get")
	public String getTest(Member member) {
		return "GET 요청 : " + member.getId();
	}
	
	@PostMapping("/test/post")
	public String postTest(@RequestParam int id) {
		return "POST 요청 : " + id;
	}
	
	@PutMapping("/test/put")
	public String putTest() {
		return "PUT 요청";
	}
	
	@DeleteMapping("/test/delete")
	public String deleteTest() {
		return "DELETE 요청";
	}
}
