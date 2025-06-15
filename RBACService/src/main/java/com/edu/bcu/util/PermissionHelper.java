package com.edu.bcu.util;

import com.edu.bcu.constant.PermissionConstants;
import com.edu.bcu.constant.RoleConstants;
import com.edu.bcu.entity.Permission;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 权限助手工具类 - 为其他微服务提供便利方法
 */
@Slf4j
public class PermissionHelper {
    
    /**
     * 创建权限列表 - 供其他微服务快速创建权限数据
     */
    public static List<Permission> createMoviePermissions() {
        return Arrays.asList(
            createPermission(PermissionConstants.MOVIE_VIEW, "查看电影", "浏览电影信息"),
            createPermission(PermissionConstants.MOVIE_COMMENT, "评论电影", "对电影进行评论"),
            createPermission(PermissionConstants.MOVIE_FAVORITE, "收藏电影", "收藏喜欢的电影"),
            createPermission(PermissionConstants.MOVIE_HISTORY, "观影历史", "查看观影历史记录")
        );
    }
    
    /**
     * 创建VIP权限列表
     */
    public static List<Permission> createVipPermissions() {
        return Arrays.asList(
            createPermission(PermissionConstants.VIP_PLAY, "VIP播放", "观看VIP专享内容"),
            createPermission(PermissionConstants.VIP_DOWNLOAD, "VIP下载", "下载电影资源"),
            createPermission(PermissionConstants.VIP_NO_AD, "免广告", "享受无广告观影体验"),
            createPermission(PermissionConstants.ORDER_VIEW, "查看订单", "查看VIP订单信息"),
            createPermission(PermissionConstants.ORDER_CREATE, "创建订单", "创建VIP订单"),
            createPermission(PermissionConstants.ORDER_CANCEL, "取消订单", "取消未支付订单")
        );
    }
    
    /**
     * 创建用户管理权限列表
     */
    public static List<Permission> createUserPermissions() {
        return Arrays.asList(
            createPermission(PermissionConstants.USER_VIEW, "查看用户", "查看用户信息"),
            createPermission(PermissionConstants.USER_CREATE, "创建用户", "创建新用户"),
            createPermission(PermissionConstants.USER_UPDATE, "更新用户", "更新用户信息"),
            createPermission(PermissionConstants.USER_DELETE, "删除用户", "删除用户账号")
        );
    }
    
    /**
     * 创建内容管理权限列表 (预留)
     */
    public static List<Permission> createContentPermissions() {
        return Arrays.asList(
            createPermission(PermissionConstants.CONTENT_CREATE, "创建内容", "创建新内容"),
            createPermission(PermissionConstants.CONTENT_UPDATE, "更新内容", "更新内容信息"),
            createPermission(PermissionConstants.CONTENT_DELETE, "删除内容", "删除内容"),
            createPermission(PermissionConstants.CONTENT_AUDIT, "内容审核", "审核内容合规性")
        );
    }
    
    /**
     * 检查是否为管理员角色
     */
    public static boolean isSuperAdmin(String roleName) {
        return RoleConstants.SUPER_ADMIN.equals(roleName);
    }
    
    /**
     * 检查是否为VIP用户
     */
    public static boolean isVipUser(String roleName) {
        return RoleConstants.VIP_USER.equals(roleName);
    }
    
    /**
     * 检查是否为普通用户
     */
    public static boolean isRegularUser(String roleName) {
        return RoleConstants.REGULAR_USER.equals(roleName);
    }
    
    /**
     * 检查是否为游客
     */
    public static boolean isGuest(String roleName) {
        return RoleConstants.GUEST.equals(roleName);
    }
    
    /**
     * 获取角色级别 - 数字越大权限越高
     */
    public static int getRoleLevel(String roleName) {
        return switch (roleName) {
            case RoleConstants.SUPER_ADMIN -> 4;
            case RoleConstants.VIP_USER -> 3;
            case RoleConstants.REGULAR_USER -> 2;
            case RoleConstants.GUEST -> 1;
            default -> 0;
        };
    }
    
    /**
     * 检查角色是否有足够权限
     */
    public static boolean hasEnoughPrivilege(String currentRole, String requiredRole) {
        return getRoleLevel(currentRole) >= getRoleLevel(requiredRole);
    }
    
    /**
     * 快速创建权限对象
     */
    private static Permission createPermission(String code, String name, String description) {
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        permission.setParentId(0);
        return permission;
    }
    
    /**
     * 构建权限代码 - 标准格式：模块:操作
     */
    public static String buildPermissionCode(String module, String action) {
        return module + ":" + action;
    }
    
    /**
     * 解析权限代码，获取模块名
     */
    public static String getModuleFromPermissionCode(String permissionCode) {
        if (permissionCode == null || !permissionCode.contains(":")) {
            return permissionCode;
        }
        return permissionCode.split(":")[0];
    }
    
    /**
     * 解析权限代码，获取操作名
     */
    public static String getActionFromPermissionCode(String permissionCode) {
        if (permissionCode == null || !permissionCode.contains(":")) {
            return "";
        }
        String[] parts = permissionCode.split(":");
        return parts.length > 1 ? parts[1] : "";
    }
    
    /**
     * 记录权限检查日志
     */
    public static void logPermissionCheck(Integer userId, String permission, boolean hasPermission) {
        if (hasPermission) {
            log.debug("用户 {} 拥有权限 {}", userId, permission);
        } else {
            log.warn("用户 {} 缺少权限 {} - 访问被拒绝", userId, permission);
        }
    }
    
    /**
     * 记录角色检查日志
     */
    public static void logRoleCheck(Integer userId, String role, boolean hasRole) {
        if (hasRole) {
            log.debug("用户 {} 拥有角色 {}", userId, role);
        } else {
            log.warn("用户 {} 不是 {} 角色 - 访问被拒绝", userId, role);
        }
    }
} 