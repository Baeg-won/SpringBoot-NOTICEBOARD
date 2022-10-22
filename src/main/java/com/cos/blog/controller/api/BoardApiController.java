package com.cos.blog.controller.api;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.BoardModalDto;
import com.cos.blog.dto.BoardWriteDto;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.service.BoardService;
import com.cos.blog.service.RecommendService;
import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
	
	private final BoardService boardService;
	private final RecommendService recommendService;

	@PostMapping("/api/board")
	public ResponseDto<Integer> write(@RequestBody BoardWriteDto boardWriteDto, @AuthenticationPrincipal PrincipalDetail principal) {
		boardService.write(boardWriteDto, principal.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@GetMapping("/api/board/{user_id}")
	public ResponseEntity<?> findByUser(@PathVariable("user_id") Long id) {
		
		List<BoardModalDto> boardModalDtoList = boardService.findByUser(id);
		
		return new ResponseEntity<>(boardModalDtoList, HttpStatus.OK);
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
	public ResponseDto<Integer> replyWrite(@PathVariable("board_id") Long id, 
			@RequestBody Reply reply, 
			@AuthenticationPrincipal PrincipalDetail principal) {
		
		boardService.replyWrite(principal.getUser(), id, reply);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{board_id}/reply/{reply_id}")
	public ResponseDto<Integer> replyDelete(@PathVariable Long reply_id) {
		boardService.replyDelete(reply_id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/board/{board_id}/recommend")
	public ResponseDto<Integer> recommend(@PathVariable("board_id") Long board_id, @AuthenticationPrincipal PrincipalDetail principal) {
		recommendService.recommend(board_id, principal.getUser().getId());
		return new ResponseDto<Integer>(HttpStatus.CREATED.value(), 1);
	}
	
	@DeleteMapping("/api/board/{board_id}/recommend")
	public ResponseDto<Integer> cancelRecommend(@PathVariable("board_id") Long board_id, @AuthenticationPrincipal PrincipalDetail principal) {
		recommendService.cancelRecommend(board_id, principal.getUser().getId());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping(value="/uploadSummernoteImageFile", produces = "application/json")
	@ResponseBody
	public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
		
		JsonObject jsonObject = new JsonObject();
		
		String fileRoot = "D:\\workspace\\springboot\\upload\\";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
				
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		
		File targetFile = new File(fileRoot + savedFileName);	
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			jsonObject.addProperty("url", "/upload/" + savedFileName);
			jsonObject.addProperty("filename", savedFileName);
			jsonObject.addProperty("responseCode", "success");
			
			File thumbnailFile = new File(fileRoot, "s_" + savedFileName);
			
			BufferedImage bo_image = ImageIO.read(targetFile);
			
			double ratio = 3;
			int width = (int) (bo_image.getWidth() / ratio);
			int height = (int) (bo_image.getHeight() / ratio);
			
			BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
							
			Graphics2D graphic = bt_image.createGraphics();
			
			graphic.drawImage(bo_image, 0, 0, width, height, null);
				
			ImageIO.write(bt_image, "jpg", thumbnailFile);
		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
			jsonObject.addProperty("responseCode", "error");
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
