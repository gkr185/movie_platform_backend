package com.bcu.movie.repository;

import com.bcu.movie.entity.FeedbackProcess;
import org.springframework.data.jpa.repository.JpaRepository;

// 反馈处理记录仓库接口
public interface FeedbackProcessRepository extends JpaRepository<FeedbackProcess, Integer> {
}