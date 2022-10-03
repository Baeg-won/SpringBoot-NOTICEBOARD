package com.cos.blog.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용량 데이터를 저장하기 위함
	private String content;

	private int count;
	
	@JsonIgnoreProperties({"boards", "replys"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@JsonIgnoreProperties({"board", "user"})
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	@OrderBy("id desc")
	private List<Reply> replys = new ArrayList<>();
	
	@JsonIgnoreProperties({"board", "user"})
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Recommend> recommends;
	
	private int recommendCount;
	
	private String userNickname;
	
	private String seen;
	
	private String category;
	
	@Transient
	private Board prev_board;
	
	@Transient
	private Board next_board;
	
	@Transient
	private boolean recommend_state;
	
	@CreationTimestamp
	private Timestamp create_date;
	
	public String getCreateDate() {
		return new SimpleDateFormat("yyyy.MM.dd").format(create_date);
	}
}
