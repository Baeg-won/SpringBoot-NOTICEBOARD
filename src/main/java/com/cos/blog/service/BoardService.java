package com.cos.blog.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@Transactional
	public Board detail(Long id, HttpServletRequest request, HttpServletResponse response, Long principal_id) {

		if(request != null) {
			Cookie oldCookie = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null)
				for (Cookie cookie : cookies)
					if (cookie.getName().equals("boardView"))
						oldCookie = cookie;

			if (oldCookie != null) {
				if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
					boardRepository.updateCount(id);
					oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
					oldCookie.setPath("/");
					oldCookie.setMaxAge(60 * 60 * 24);
					response.addCookie(oldCookie);
				}
			} else {
				boardRepository.updateCount(id);
				Cookie newCookie = new Cookie("boardView", "[" + id + "]");
				newCookie.setPath("/");
				newCookie.setMaxAge(60 * 60 * 24);
				response.addCookie(newCookie);
			}
		}

		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
		});

		board.getRecommends().forEach((recommend) -> {
			if (recommend.getUser().getId() == principal_id) {
				board.setRecommend_state(true);
			}
		});
		board.setRecommendCount(board.getRecommends().size());

		return board;
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
