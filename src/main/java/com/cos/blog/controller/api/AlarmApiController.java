package com.cos.blog.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.service.AlarmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AlarmApiController {
	
	private final AlarmService alarmService;
	
	@PutMapping("/api/confirm/{reply_id}")
	public ResponseDto<Integer> alarmConfirm(@PathVariable Long reply_id) {
		
		alarmService.alarmConfirm(reply_id);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
