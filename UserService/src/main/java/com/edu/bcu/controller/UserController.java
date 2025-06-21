package com.edu.bcu.controller;

import com.edu.bcu.common.Result;
import com.edu.bcu.dto.LoginResponse;
import com.edu.bcu.dto.UserLoginDTO;
import com.edu.bcu.dto.UserRegisterDTO;
import com.edu.bcu.dto.UserVO;
import com.edu.bcu.dto.VipUpdateDTO;
import com.edu.bcu.entity.User;
import com.edu.bcu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册", description = "新用户注册接口")
    @ApiResponse(responseCode = "200", description = "注册成功", 
            content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Result.class)))
    @PostMapping("/register")
    public Result<User> register(@RequestBody @Valid UserRegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        return Result.success("注册成功", user);
    }

    @Operation(summary = "用户登录", description = "用户登录认证接口，登录成功返回Session信息")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid UserLoginDTO loginDTO, HttpServletRequest request) {
        LoginResponse loginResponse = userService.login(loginDTO, request);
        return Result.success("登录成功", loginResponse);
    }

    @Operation(summary = "用户登出", description = "用户登出接口，清除Session")
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        userService.logout(session);
        return Result.success("登出成功", null);
    }

    @Operation(summary = "获取当前登录用户信息", description = "获取当前Session中的用户信息")
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser(HttpSession session) {
        User user = userService.getCurrentUser(session);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(userVO);
    }

    @Operation(summary = "检查登录状态", description = "检查当前用户是否已登录")
    @GetMapping("/login-status")
    public Result<Boolean> checkLoginStatus(HttpSession session) {
        boolean isLoggedIn = userService.isLoggedIn(session);
        return Result.success(isLoggedIn);
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @Parameter(name = "userId", description = "用户ID", required = true)
    @GetMapping("/{userId}")
    public Result<User> getUserInfo(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    @Operation(summary = "更新用户信息", description = "更新当前登录用户的基本信息")
    @Parameter(name = "user", description = "用户信息", required = true)
    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestBody User user, HttpSession session) {
        // 验证登录状态并获取当前用户
        User currentUser = userService.getCurrentUser(session);
        User updatedUser = userService.updateUser(currentUser.getId(), user);
        return Result.success("更新成功", updatedUser);
    }

    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    @Parameters({
        @Parameter(name = "oldPassword", description = "原密码", required = true),
        @Parameter(name = "newPassword", description = "新密码", required = true)
    })
    @PutMapping("/password")
    public Result<Void> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session
    ) {
        // 验证登录状态并获取当前用户
        User currentUser = userService.getCurrentUser(session);
        userService.updatePassword(currentUser.getId(), oldPassword, newPassword);
        return Result.success("密码修改成功", null);
    }

    @Operation(summary = "删除用户", description = "删除指定用户账号（管理员功能）")
    @Parameter(name = "userId", description = "用户ID", required = true)
    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable Integer userId, HttpSession session) {
        // 验证登录状态（可以添加管理员权限验证）
        userService.getCurrentUser(session);
        userService.deleteUser(userId);
        return Result.success("账户删除成功", null);
    }

    @Operation(summary = "检查用户名可用性", description = "检查用户名是否可用")
    @Parameter(name = "username", description = "用户名", required = true)
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        boolean available = userService.checkUsernameAvailable(username);
        return Result.success(available);
    }

    @Operation(summary = "分页获取用户列表", description = "分页获取所有用户信息（管理员功能）")
    @Parameters({
        @Parameter(name = "page", description = "页码(从0开始)", required = false),
        @Parameter(name = "size", description = "每页大小", required = false),
        @Parameter(name = "sort", description = "排序字段", required = false)
    })
    @GetMapping("/list")
    public Result<Page<User>> getUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            HttpSession session
    ) {
        // 验证登录状态（可以添加管理员权限验证）
        userService.getCurrentUser(session);
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<User> userPage = userService.getAllUsers(pageable);
        return Result.success("获取成功", userPage);
    }

    @Operation(summary = "搜索用户", description = "根据关键词搜索用户(支持用户名、邮箱、手机号)")
    @Parameters({
        @Parameter(name = "keyword", description = "搜索关键词", required = true),
        @Parameter(name = "page", description = "页码(从0开始)", required = false),
        @Parameter(name = "size", description = "每页大小", required = false),
        @Parameter(name = "sort", description = "排序字段", required = false)
    })
    @GetMapping("/search")
    public Result<Page<User>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            HttpSession session
    ) {
        // 验证登录状态（可以添加管理员权限验证）
        userService.getCurrentUser(session);
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<User> userPage = userService.searchUsers(keyword, pageable);
        return Result.success("搜索成功", userPage);
    }

    @Operation(summary = "更新用户VIP状态", description = "支付成功后更新用户VIP状态")
    @Parameters({
        @Parameter(name = "userId", description = "用户ID", required = true),
        @Parameter(name = "vipUpdateDTO", description = "VIP更新信息", required = true)
    })
    @PutMapping("/{userId}/vip")
    public Result<Void> updateVipStatus(
            @PathVariable Integer userId,
            @RequestBody VipUpdateDTO vipUpdateDTO,
            HttpSession session
    ) {
        // 验证登录状态
        userService.getCurrentUser(session);
        userService.updateVipStatus(userId, vipUpdateDTO.getVipType(), vipUpdateDTO.getVipExpireTime());
        return Result.success("VIP状态更新成功", null);
    }
    
    @PutMapping("/{userId}/vip/params")
    public Result<Void> updateVipStatusByParams(
            @PathVariable Integer userId,
            @RequestParam Integer vipType,
            @RequestParam String vipExpireTime,
            HttpSession session
    ) {
        // 验证登录状态
        userService.getCurrentUser(session);
        LocalDateTime expireTime = LocalDateTime.parse(vipExpireTime);
        userService.updateVipStatus(userId, vipType, expireTime);
        return Result.success("VIP状态更新成功", null);
    }

    @Operation(summary = "取消用户VIP状态", description = "取消用户VIP状态")
    @Parameter(name = "userId", description = "用户ID", required = true)
    @DeleteMapping("/{userId}/vip")
    public Result<Void> cancelVipStatus(@PathVariable Integer userId, HttpSession session) {
        // 验证登录状态
        userService.getCurrentUser(session);
        userService.cancelVipStatus(userId);
        return Result.success("VIP状态已取消", null);
    }
}