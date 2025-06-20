package com.edu.bcu.service.impl;

import com.edu.bcu.dto.LoginResponse;
import com.edu.bcu.dto.UserLoginDTO;
import com.edu.bcu.dto.UserRegisterDTO;
import com.edu.bcu.dto.UserVO;
import com.edu.bcu.entity.User;
import com.edu.bcu.repository.UserRepository;
import com.edu.bcu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    
    // Session中存储用户信息的key
    private static final String USER_SESSION_KEY = "currentUser";

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
    @Transactional
    public LoginResponse login(UserLoginDTO loginDTO, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .filter(u -> u.getPassword().equals(loginDTO.getPassword())) // 实际应用中需要加密比较
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 创建Session并存储用户信息
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(1800); // 30分钟超时
        session.setAttribute(USER_SESSION_KEY, user);

        // 转换为UserVO（不包含敏感信息如密码）
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return new LoginResponse(session.getId(), userVO, session.getMaxInactiveInterval());
    }

    @Override
    public User getCurrentUser(HttpSession session) {
        if (session == null) {
            throw new RuntimeException("未登录");
        }
        
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        if (user == null) {
            throw new RuntimeException("登录已过期，请重新登录");
        }
        
        return user;
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
            session.invalidate();
        }
    }

    @Override
    public boolean isLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }
        return session.getAttribute(USER_SESSION_KEY) != null;
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