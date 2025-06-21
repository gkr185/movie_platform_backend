package com.edu.bcu.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private String sessionId;
    private UserVO user;
    private LocalDateTime loginTime;
    private Integer maxInactiveInterval; // 会话超时时间（秒）
    
    public LoginResponse(String sessionId, UserVO user, Integer maxInactiveInterval) {
        this.sessionId = sessionId;
        this.user = user;
        this.loginTime = LocalDateTime.now();
        this.maxInactiveInterval = maxInactiveInterval;
    }
} 