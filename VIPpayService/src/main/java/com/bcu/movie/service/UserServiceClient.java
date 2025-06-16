package com.bcu.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;
    
    /**
     * 更新用户VIP状态
     * @param userId 用户ID
     * @param vipType VIP类型
     * @param vipExpireTime VIP到期时间
     */
    public void updateVipStatus(Integer userId, Integer vipType, LocalDateTime vipExpireTime) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/vip";
            
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("vipType", vipType);
            requestBody.put("vipExpireTime", vipExpireTime);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("更新用户VIP状态失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 取消用户VIP状态
     * @param userId 用户ID
     */
    public void cancelVipStatus(Integer userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/vip";
            restTemplate.delete(url);
        } catch (Exception e) {
            throw new RuntimeException("取消用户VIP状态失败: " + e.getMessage(), e);
        }
    }
} 