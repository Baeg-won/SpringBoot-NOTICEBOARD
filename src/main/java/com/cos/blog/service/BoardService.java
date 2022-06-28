package com.cos.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;

	@Transactional
	public void write(Board board, User user) {
		board.setUser(user);
		board.setCount(0);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true)
	public Board detail(Long id) {
		return boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
		});
	}
	
	@Transactional
	public void delete(Long id) {
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void update(Long id, Board requestBoard) {
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 수정 실패: 아이디를 찾을 수 없습니다");
		});
		
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
	}
	
	@Transactional
	public void replyWrite(User user, Long board_id, Reply requestReply) {
		requestReply.setUser(user);
		requestReply.setBoard(boardRepository.findById(board_id).orElseThrow(() -> {
			return new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다.");
		}));
		
		replyRepository.save(requestReply);
	}
	
	@Transactional
	public void replyDelete(Long reply_id) {
		replyRepository.deleteById(reply_id);
	}
}
