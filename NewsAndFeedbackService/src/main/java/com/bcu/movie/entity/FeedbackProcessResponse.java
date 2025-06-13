package com.bcu.movie.entity;

public class FeedbackProcessResponse {
    private Integer feedbackId;
    private Integer status;
    private String reply;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public FeedbackProcessResponse(Integer feedbackId, Integer status, String reply) {
        this.feedbackId = feedbackId;
        this.status = status;
        this.reply = reply;
    }
}
