package com.cos.blog.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.BoardModalDto;
import com.cos.blog.dto.BoardWriteDto;
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
	public void write(BoardWriteDto boardWriteDto, User user) {
		Board board = Board.builder()
				.title(boardWriteDto.getTitle())
				.content(boardWriteDto.getContent())
				.user(user)
				.count(0)
				.userNickname(user.getNickname())
				.category(boardWriteDto.getCategory())
				.build();
		
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true)
	public List<BoardModalDto> findByUser(Long user_id) {
		
		List<Board> boards = boardRepository.findByUserId(user_id);
		List<BoardModalDto> boardModalDtoList = new ArrayList<>();
		
		for(int i = 0; i < boards.size(); i++) {
			BoardModalDto boardModalDto = BoardModalDto.builder()
					.id(boards.get(i).getId())
					.title(boards.get(i).getTitle())
					.create_date(boards.get(i).getCreateDate())
					.views(boards.get(i).getCount())
					.recommends(boards.get(i).getRecommendCount())
					.build();
			
			boardModalDtoList.add(boardModalDto);
		}
		
		return boardModalDtoList;
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
		board.setPrev_board(boardRepository.findPrevBoard(id, board.getCategory()));
		board.setNext_board(boardRepository.findNextBoard(id, board.getCategory()));
		
		if(board.getSeen() == null) {
			board.setSeen("[" + principal_id.toString() + "]");
		}
		if(!board.getSeen().contains("[" + principal_id.toString() + "]")) {
			board.setSeen(board.getSeen() + "[" + principal_id.toString() + "]");
		}

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
		board.setCategory(requestBoard.getCategory());
	}

	@Transactional
	public void replyWrite(User user, Long board_id, Reply requestReply) {
		requestReply.setUser(user);
		
		Board board = boardRepository.findById(board_id).orElseThrow(() -> {
			return new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다.");
		});
		requestReply.setBoard(board);
		requestReply.setAlarm_confirm_state(false);

		replyRepository.save(requestReply);
	}

	@Transactional
	public void replyDelete(Long reply_id) {
		replyRepository.deleteById(reply_id);
	}
}
