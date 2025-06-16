package com.edu.bcu.service;

import com.edu.bcu.dto.UserLoginDTO;
import com.edu.bcu.dto.UserRegisterDTO;
import com.edu.bcu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface UserService {
    User register(UserRegisterDTO registerDTO);
    User login(UserLoginDTO loginDTO);
    User getUserById(Integer userId);
    User updateUser(Integer userId, User user);
    void updatePassword(Integer userId, String oldPassword, String newPassword);
    void deleteUser(Integer userId);
    boolean checkUsernameAvailable(String username);
    Page<User> getAllUsers(Pageable pageable);
    Page<User> searchUsers(String keyword, Pageable pageable);
    
    // VIP相关方法
    void updateVipStatus(Integer userId, Integer vipType, LocalDateTime vipExpireTime);
    void cancelVipStatus(Integer userId);
} 