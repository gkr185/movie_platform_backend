package com.bcu.movie.Controller;

import com.bcu.movie.entity.Feedback;
import com.bcu.movie.entity.FeedbackProcessResponse;
import com.bcu.movie.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

// 反馈控制器
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<Feedback> createFeedback(@RequestParam Integer userId, @RequestParam String content, @RequestParam Integer type, @RequestParam String contact, @RequestParam Integer status) {
        try {
            logger.info("Received create feedback request: userId={}, content={}, type={}, contact={}, status={}", userId, content, type, contact, status);
            Feedback feedback = feedbackService.createFeedback(userId, content, type, contact, status);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            logger.error("Error creating feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/process")
    public ResponseEntity<FeedbackProcessResponse> Feedbackprocess(@RequestParam Integer feedbackId, @RequestParam Integer status, @RequestParam String reply) {
        try {
            feedbackService.FeedbackProcess(feedbackId, status, reply);
            FeedbackProcessResponse response = new FeedbackProcessResponse(feedbackId, status, reply);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 获取反馈列表（分页）
    @GetMapping("/list")
    public ResponseEntity<Page<Feedback>> getFeedbackList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Integer status) {
        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            
            Page<Feedback> feedbacks;
            if (status != null) {
                feedbacks = feedbackService.getFeedbacksByStatus(status, pageRequest);
            } else {
                feedbacks = feedbackService.getAllFeedbacks(pageRequest);
            }
            
            logger.info("Retrieved {} feedbacks, page {} of {}", feedbacks.getNumberOfElements(), page + 1, feedbacks.getTotalPages());
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error retrieving feedback list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 根据用户ID获取反馈列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(@PathVariable Integer userId) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByUserId(userId);
            logger.info("Retrieved {} feedbacks for user {}", feedbacks.size(), userId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error retrieving feedbacks for user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 根据反馈类型获取反馈列表
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Feedback>> getFeedbacksByType(@PathVariable Integer type) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByType(type);
            logger.info("Retrieved {} feedbacks for type {}", feedbacks.size(), type);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error retrieving feedbacks for type {}", type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 根据状态获取反馈列表
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Feedback>> getFeedbacksByStatusPath(@PathVariable Integer status) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByStatus(status, PageRequest.of(0, 100)).getContent();
            logger.info("Retrieved {} feedbacks for status {}", feedbacks.size(), status);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error retrieving feedbacks for status {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}