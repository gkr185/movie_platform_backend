package com.bcu.movie.repository;

import com.bcu.movie.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

// 反馈仓库接口
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}