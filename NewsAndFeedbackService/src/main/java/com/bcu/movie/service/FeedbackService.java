package com.bcu.movie.service;

import com.bcu.movie.entity.Feedback;
import com.bcu.movie.entity.FeedbackProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface FeedbackService {
    @Transactional
    Feedback createFeedback(Integer userId, String content, Integer type, String contact, Integer status);

    @Transactional
    Feedback createFeedback(Integer userId, String content, Integer type);

    void Feedbackprocess(Integer feedbackId, Integer status, String reply);

    @Transactional
    void FeedbackProcess(Integer feedbackId, Integer status, String reply);

    // 获取反馈列表（分页）
    Page<Feedback> getAllFeedbacks(Pageable pageable);
    
    // 根据状态获取反馈列表（分页）
    Page<Feedback> getFeedbacksByStatus(Integer status, Pageable pageable);
    
    // 根据用户ID获取反馈列表
    List<Feedback> getFeedbacksByUserId(Integer userId);
    
    // 根据反馈类型获取反馈列表
    List<Feedback> getFeedbacksByType(Integer type);
}
