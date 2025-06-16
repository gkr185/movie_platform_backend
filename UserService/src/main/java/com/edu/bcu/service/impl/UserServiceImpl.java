package com.edu.bcu.service.impl;

import com.edu.bcu.dto.UserLoginDTO;
import com.edu.bcu.dto.UserRegisterDTO;
import com.edu.bcu.entity.User;
import com.edu.bcu.repository.UserRepository;
import com.edu.bcu.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword()); // 实际应用中需要加密
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        if (registerDTO.getAvatar() != null) {
            user.setAvatar(registerDTO.getAvatar());
        }

        return userRepository.save(user);
    }

    @Override
    public User login(UserLoginDTO loginDTO) {
        return userRepository.findByUsername(loginDTO.getUsername())
                .filter(user -> user.getPassword().equals(loginDTO.getPassword())) // 实际应用中需要加密比较
                .map(user -> {
                    user.setLastLoginTime(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, User updateUser) {
        User user = getUserById(userId);
        
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPhone() != null) {
            user.setPhone(updateUser.getPhone());
        }
        if (updateUser.getAvatar() != null) {
            user.setAvatar(updateUser.getAvatar());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (!user.getPassword().equals(oldPassword)) { // 实际应用中需要加密比较
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(newPassword); // 实际应用中需要加密
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public boolean checkUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            return getAllUsers(pageable);
        }
        return userRepository.findByKeyword(keyword.trim(), pageable);
    }

    @Override
    @Transactional
    public void updateVipStatus(Integer userId, Integer vipType, LocalDateTime vipExpireTime) {
        User user = getUserById(userId);
        user.setIsVip(1); // 设置为VIP用户
        user.setVipExpireTime(vipExpireTime);
        userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void cancelVipStatus(Integer userId) {
        User user = getUserById(userId);
        user.setIsVip(0); // 取消VIP状态
        user.setVipExpireTime(null);
        userRepository.save(user);
    }
} 