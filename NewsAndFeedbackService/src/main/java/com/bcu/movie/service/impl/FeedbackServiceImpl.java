package com.bcu.movie.service.impl;
import com.bcu.movie.entity.Feedback;
import com.bcu.movie.entity.FeedbackProcess;
import com.bcu.movie.entity.User;
import com.bcu.movie.repository.FeedbackProcessRepository;
import com.bcu.movie.repository.FeedbackRepository;
import com.bcu.movie.repository.UserRepository;
import com.bcu.movie.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackProcessRepository feedbackProcessRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Feedback createFeedback(Integer userId, String content, Integer type, String contact, Integer status) {
        Feedback feedback = new Feedback();
        // 根据 userId 获取 User 对象
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            feedback.setUser(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
        feedback.setContent(content);
        feedback.setType(type);
        feedback.setContact(contact); // 设置联系人信息
        feedback.setStatus(status); // 设置处理状态
        Date utilDate = new Date();
        feedback.setCreateTime(new java.sql.Timestamp(utilDate.getTime()));
        feedback.setUpdateTime(new java.sql.Timestamp(utilDate.getTime()));
        return feedbackRepository.save(feedback);
    }

    @Override
    @Transactional
    public Feedback createFeedback(Integer userId, String content, Integer type) {
        return createFeedback(userId, content, type, null, 0); // 复用五个参数的方法，默认状态为 0
    }

    @Override
    public void Feedbackprocess(Integer feedbackId, Integer status, String reply) {
        // 可以添加具体的实现逻辑
    }

    @Override
    @Transactional
    public void FeedbackProcess(Integer feedbackId, Integer status, String reply) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new RuntimeException("Feedback not found with id: " + feedbackId));
        FeedbackProcess process = new FeedbackProcess();
        process.setFeedback(feedback);
        process.setStatus(status);
        process.setReply(reply);
        Date utilDate = new Date();
        process.setCreateTime(new java.sql.Date(utilDate.getTime()));
        feedbackProcessRepository.save(process);

        feedback.setStatus(status);
        feedback.setReply(reply);
        feedback.setReplyTime(new java.sql.Timestamp(utilDate.getTime()));
        feedback.setUpdateTime(new java.sql.Timestamp(utilDate.getTime()));
        feedbackRepository.save(feedback);
    }

    @Override
    public Page<Feedback> getAllFeedbacks(Pageable pageable) {
        return feedbackRepository.findAllWithUser(pageable);
    }

    @Override
    public Page<Feedback> getFeedbacksByStatus(Integer status, Pageable pageable) {
        return feedbackRepository.findByStatus(status, pageable);
    }

    @Override
    public List<Feedback> getFeedbacksByUserId(Integer userId) {
        return feedbackRepository.findByUser_Id(userId);
    }

    @Override
    public List<Feedback> getFeedbacksByType(Integer type) {
        return feedbackRepository.findByType(type);
    }
}