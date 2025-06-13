package com.bcu.movie.service;

import com.bcu.movie.entity.Feedback;
import com.bcu.movie.entity.FeedbackProcess;
import org.springframework.transaction.annotation.Transactional;

public interface FeedbackService {
    @Transactional
    Feedback createFeedback(Integer userId, String content, Integer type, String contact, Integer status);

    @Transactional
    Feedback createFeedback(Integer userId, String content, Integer type);

    void Feedbackprocess(Integer feedbackId, Integer status, String reply);

    @Transactional
    void FeedbackProcess(Integer feedbackId, Integer status, String reply);


}
