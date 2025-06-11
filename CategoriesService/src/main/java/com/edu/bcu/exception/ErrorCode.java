package com.edu.bcu.exception;

/**
 * 错误码枚举类
 */
public enum ErrorCode {
    // 通用错误码
    SUCCESS(200, "操作成功"),
    PARAMS_ERROR(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统内部异常"),

    // 分类相关错误码 (2000-2999)
    CATEGORY_NOT_FOUND(2000, "分类不存在"),
    CATEGORY_NAME_EXIST(2001, "分类名称已存在"),
    PARENT_CATEGORY_NOT_EXIST(2002, "父分类不存在"),
    CATEGORY_HAS_CHILDREN(2003, "该分类下存在子分类，无法删除"),
    CATEGORY_LEVEL_EXCEED(2004, "分类层级超过限制"),
    CATEGORY_PARENT_ERROR(2005, "不能将分类设置为自己的子分类"),
    CATEGORY_UPDATE_ERROR(2006, "分类更新失败");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 