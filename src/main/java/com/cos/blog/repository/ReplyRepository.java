package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.Reply;

// @Repository
public interface ReplyRepository extends JpaRepository<Reply, Long>{

}
