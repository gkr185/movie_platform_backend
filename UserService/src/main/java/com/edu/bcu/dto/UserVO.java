package com.edu.bcu.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
    private Integer userStatus;
    private Integer isVip;
    private LocalDateTime vipExpireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 