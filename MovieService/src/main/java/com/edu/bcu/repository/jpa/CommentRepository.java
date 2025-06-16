package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 原有方法
    Page<Comment> findByMovieId(Long movieId, Pageable pageable);
    
    // 管理端查询方法
    Page<Comment> findByStatus(Integer status, Pageable pageable);
    
    Page<Comment> findByContentContaining(String keyword, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:keyword IS NULL OR :keyword = '' OR c.content LIKE %:keyword%)")
    Page<Comment> findByStatusAndKeyword(@Param("status") Integer status, 
                                       @Param("keyword") String keyword, 
                                       Pageable pageable);
    
    // 统计查询方法
    Long countByStatus(Integer status);
    
    @Query("SELECT COUNT(c) FROM Comment c")
    Long countTotal();
    
    // 用户评论查询
    Page<Comment> findByUserId(Integer userId, Pageable pageable);
    
    // 父评论查询
    List<Comment> findByParentId(Long parentId);
    
    // 详情查询（包含关联信息）
    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    Optional<Comment> findByIdWithDetails(@Param("id") Long id);
}