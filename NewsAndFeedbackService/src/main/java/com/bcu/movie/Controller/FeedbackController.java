package com.bcu.movie.Controller;

import com.bcu.movie.entity.Feedback;
import com.bcu.movie.entity.FeedbackProcessResponse;
import com.bcu.movie.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// 反馈控制器
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public Feedback createFeedback(@RequestParam Integer userId, @RequestParam String content, @RequestParam Integer type, @RequestParam String contact, @RequestParam Integer status) {
        try {
            logger.info("Received create feedback request: userId={}, content={}, type={}, contact={}, status={}", userId, content, type, contact, status);
            return feedbackService.createFeedback(userId, content, type, contact, status);
        } catch (Exception e) {
            logger.error("Error creating feedback", e);
            return null;
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
}