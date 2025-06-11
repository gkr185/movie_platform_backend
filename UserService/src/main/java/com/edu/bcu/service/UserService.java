package com.edu.bcu.service;

import com.edu.bcu.dto.UserLoginDTO;
import com.edu.bcu.dto.UserRegisterDTO;
import com.edu.bcu.entity.User;

public interface UserService {
    User register(UserRegisterDTO registerDTO);
    User login(UserLoginDTO loginDTO);
    User getUserById(Integer userId);
    User updateUser(Integer userId, User user);
    void updatePassword(Integer userId, String oldPassword, String newPassword);
    void deleteUser(Integer userId);
    boolean checkUsernameAvailable(String username);
} 