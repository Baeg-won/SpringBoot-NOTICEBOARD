package com.cos.blog.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
	
	private final BoardService boardService;

	@PostMapping("/api/board")
	public ResponseDto<Integer> write(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
		boardService.write(board, principal.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{board_id}")
	public ResponseDto<Integer> deleteById(@PathVariable("board_id") Long id) {
		boardService.delete(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{board_id}")
	public ResponseDto<Integer> update(@PathVariable("board_id") Long id, @RequestBody Board board) {
		boardService.update(id, board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/board/{board_id}/reply")
	public ResponseDto<Integer> replyWrite(@PathVariable("board_id") Long id, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal) {
		boardService.replyWrite(principal.getUser(), id, reply);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{board_id}/reply/{reply_id}")
	public ResponseDto<Integer> replyDelete(@PathVariable Long reply_id) {
		boardService.replyDelete(reply_id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
