package com.cos.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Board;

// @Repository
public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board>{

	@Modifying
	@Query("update Board b set b.count = b.count + 1 where b.id = :id")
	void updateCount(Long id);
	
	@Query(value = "SELECT * FROM board "
			+ "WHERE id = (SELECT prev_no FROM (SELECT id, LAG(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board WHERE category = :category) B "
			+ "WHERE id = :id)", nativeQuery = true)
	Board findPrevBoard(Long id, String category);
	
	@Query(value = "SELECT * FROM board "
			+ "WHERE id = (SELECT prev_no FROM (SELECT id, LEAD(id, 1, -1) OVER(ORDER BY id) AS prev_no FROM board WHERE category = :category) B "
			+ "WHERE id = :id)", nativeQuery = true)
	Board findNextBoard(Long id, String category);
	
	@Query(value = "SELECT * FROM board WHERE user_id = :user_id ORDER BY id DESC", nativeQuery = true)
	List<Board> findByUserId(Long user_id);
	
	@Query(value = "select count(*) from board where date(create_date) = date(now())", nativeQuery = true)
	int countTodayBoard();
	
	@Query(value = "select count(*) from board where date(create_date) = date(date_add(now(), interval - 1 day))", nativeQuery = true)
	int countYesterdayBoard();
	
	@Query(value = "select count(*) from board", nativeQuery = true)
	int countTotalBoard();
	
	@Query(value = "select count(*) from board where category = :category", nativeQuery = true)
	int countByCategory(String category);
}
