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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100, unique = true)
	private String username;
	
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column(nullable = false, length = 10, unique = true)
	private String nickname;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	private String oauth;
	
	private String profile_image_url;
	
	@JsonIgnoreProperties({"user"})
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Board> boards = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Reply> replys = new ArrayList<>();
	
	@CreationTimestamp
	private Timestamp create_date;
	
	public String getCreateDate() {
		return new SimpleDateFormat("yyyy.MM.dd").format(create_date);
	}
	
	public int getBoardCount() {
		return boards.size();
	}
	
	public int getReplyCount() {
		return replys.size();
	}
}
