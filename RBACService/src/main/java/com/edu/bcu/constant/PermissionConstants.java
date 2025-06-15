package com.edu.bcu.constant;

/**
 * 权限常量 - 模块化设计，方便扩展
 * 格式: 模块:操作
 */
public class PermissionConstants {
    
    // ========== 用户管理模块 ==========
    public static final String USER_VIEW = "user:view";
    public static final String USER_CREATE = "user:create";
    public static final String USER_UPDATE = "user:update";
    public static final String USER_DELETE = "user:delete";
    
    // ========== 电影模块 ==========
    public static final String MOVIE_VIEW = "movie:view";
    public static final String MOVIE_COMMENT = "movie:comment";
    public static final String MOVIE_FAVORITE = "movie:favorite";
    public static final String MOVIE_HISTORY = "movie:history";
    
    // ========== VIP功能模块 ==========
    public static final String VIP_PLAY = "vip:play";
    public static final String VIP_DOWNLOAD = "vip:download";
    public static final String VIP_NO_AD = "vip:no_ad";
    
    // ========== 管理功能模块 ==========
    public static final String ADMIN_USER_MANAGE = "admin:user_manage";
    public static final String ADMIN_ROLE_MANAGE = "admin:role_manage";
    public static final String ADMIN_PERMISSION_MANAGE = "admin:permission_manage";
    public static final String ADMIN_SYSTEM_CONFIG = "admin:system_config";
    
    // ========== 内容管理模块 (预留给其他服务扩展) ==========
    public static final String CONTENT_CREATE = "content:create";
    public static final String CONTENT_UPDATE = "content:update";
    public static final String CONTENT_DELETE = "content:delete";
    public static final String CONTENT_AUDIT = "content:audit";
    
    // ========== 订单支付模块 (预留给VIP服务扩展) ==========
    public static final String ORDER_VIEW = "order:view";
    public static final String ORDER_CREATE = "order:create";
    public static final String ORDER_CANCEL = "order:cancel";
} 