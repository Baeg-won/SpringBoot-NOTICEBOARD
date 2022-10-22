package com.cos.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardModalDto {

	private Long id;
	private String title;
	private String create_date;
	private Integer views;
	private Integer recommends;
}
