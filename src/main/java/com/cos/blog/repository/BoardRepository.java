package com.cos.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Board;
import com.cos.blog.model.CategoryType;

// @Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

	@Modifying
	@Query("update Board b set b.count = b.count + 1 where b.id = :id")
	void updateCount(Long id);
	
	Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
	Page<Board> findByUserNicknameContaining(String searchKeyword, Pageable pageable);
	Page<Board> findAllByCategory(CategoryType category, Pageable pageable);
	
	@Query(value = "SELECT * FROM board "
			+ "WHERE id = (SELECT prev_no FROM (SELECT id, LAG(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board) B "
			+ "WHERE id = :id)", nativeQuery = true)
	Board findPrevBoard(Long id);
	
	@Query(value = "SELECT * FROM board "
			+ "WHERE id = (SELECT prev_no FROM (SELECT id, LEAD(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board) B "
			+ "WHERE id = :id)", nativeQuery = true)
	Board findNextBoard(Long id);
}
