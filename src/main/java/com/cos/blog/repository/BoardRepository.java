package com.cos.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Board;

// @Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

	@Modifying
	@Query("update Board b set b.count = b.count + 1 where b.id = :id")
	void updateCount(Long id);
	
	Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
