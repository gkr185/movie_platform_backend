package com.edu.bcu.service;

import com.edu.bcu.dto.LoginResponse;
import com.edu.bcu.dto.UserLoginDTO;
import com.edu.bcu.dto.UserRegisterDTO;
import com.edu.bcu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

public interface UserService {
    User register(UserRegisterDTO registerDTO);
    LoginResponse login(UserLoginDTO loginDTO, HttpServletRequest request);
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
    
    // Session相关方法
    User getCurrentUser(HttpSession session);
    void logout(HttpSession session);
    boolean isLoggedIn(HttpSession session);
} 