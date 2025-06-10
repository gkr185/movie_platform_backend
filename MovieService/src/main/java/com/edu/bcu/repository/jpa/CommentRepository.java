package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByMovieId(Long movieId, Pageable pageable);
}