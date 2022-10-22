package com.cos.blog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplyModalDto;
import com.cos.blog.model.Reply;
import com.cos.blog.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {
	
	private final ReplyRepository replyRepository;

	@Transactional(readOnly = true)
	public List<ReplyModalDto> findByUser(Long user_id) {
		
		List<Reply> replys = replyRepository.findByUserId(user_id);
		List<ReplyModalDto> replyModalDtoList = new ArrayList<>();
		
		for(int i = 0; i < replys.size(); i++) {
			ReplyModalDto replyModalDto = ReplyModalDto.builder()
					.id(replys.get(i).getId())
					.content(replys.get(i).getContent())
					.create_date(replys.get(i).getCreateDate())
					.build();
			
			replyModalDtoList.add(replyModalDto);
		}
		
		return replyModalDtoList;
	}
}
