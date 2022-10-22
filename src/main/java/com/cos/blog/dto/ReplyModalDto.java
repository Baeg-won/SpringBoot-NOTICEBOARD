package com.cos.blog.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReplyModalDto {

	private Long id;
	private String content;
	private String create_date;
}
