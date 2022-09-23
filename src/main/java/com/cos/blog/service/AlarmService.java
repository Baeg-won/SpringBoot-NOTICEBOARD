package com.cos.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.Reply;
import com.cos.blog.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {
	
	private final ReplyRepository replyRepository;

	@Transactional
	public void alarmConfirm(Long reply_id) {
		
		Reply reply = replyRepository.findById(reply_id).orElseThrow(() -> {
			return new IllegalArgumentException("알람 확인 실패: 댓글 id를 찾을 수 없습니다.");
		});
		
		reply.setAlarm_confirm_state(true);
	}
}
