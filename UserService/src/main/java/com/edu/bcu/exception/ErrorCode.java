package com.edu.bcu.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 通用错误码
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 用户相关错误码 (1000-1999)
    USERNAME_EXISTS(1001, "用户名已存在"),
    EMAIL_EXISTS(1002, "邮箱已被注册"),
    USER_NOT_FOUND(1003, "用户不存在"),
    PASSWORD_ERROR(1004, "密码错误"),
    ACCOUNT_DISABLED(1005, "账号已被禁用"),
    LOGIN_ERROR(1006, "用户名或密码错误"),
    OLD_PASSWORD_ERROR(1007, "原密码错误"),
    INVALID_TOKEN(1008, "无效的token"),
    TOKEN_EXPIRED(1009, "token已过期"),
    USERNAME_LENGTH_ERROR(1010, "用户名长度必须在3-20之间");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 