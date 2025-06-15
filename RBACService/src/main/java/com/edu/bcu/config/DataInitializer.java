package com.edu.bcu.config;

import com.edu.bcu.constant.PermissionConstants;
import com.edu.bcu.constant.RoleConstants;
import com.edu.bcu.entity.Permission;
import com.edu.bcu.entity.Role;
import com.edu.bcu.repository.PermissionRepository;
import com.edu.bcu.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 数据初始化器 - 在应用启动时初始化基础角色和权限
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("开始初始化RBAC基础数据...");
        
        initializePermissions();
        initializeRoles();
        initializeRolePermissions();
        
        log.info("RBAC基础数据初始化完成");
    }

    /**
     * 初始化权限数据
     */
    private void initializePermissions() {
        log.info("初始化权限数据...");
        
        // 检查是否已有权限数据
        long permissionCount = permissionRepository.count();
        if (permissionCount > 0) {
            log.info("权限数据已存在，跳过初始化。当前权限数量: {}", permissionCount);
            return;
        }
        
        List<Permission> permissions = Arrays.asList(
            // 用户管理模块
            createPermission(PermissionConstants.USER_VIEW, "查看用户", "查看用户信息", 0),
            createPermission(PermissionConstants.USER_CREATE, "创建用户", "创建新用户", 0),
            createPermission(PermissionConstants.USER_UPDATE, "更新用户", "更新用户信息", 0),
            createPermission(PermissionConstants.USER_DELETE, "删除用户", "删除用户账号", 0),
            
            // 电影模块
            createPermission(PermissionConstants.MOVIE_VIEW, "查看电影", "浏览电影信息", 0),
            createPermission(PermissionConstants.MOVIE_COMMENT, "评论电影", "对电影进行评论", 0),
            createPermission(PermissionConstants.MOVIE_FAVORITE, "收藏电影", "收藏喜欢的电影", 0),
            createPermission(PermissionConstants.MOVIE_HISTORY, "观影历史", "查看观影历史记录", 0),
            
            // VIP功能模块
            createPermission(PermissionConstants.VIP_PLAY, "VIP播放", "观看VIP专享内容", 0),
            createPermission(PermissionConstants.VIP_DOWNLOAD, "VIP下载", "下载电影资源", 0),
            createPermission(PermissionConstants.VIP_NO_AD, "免广告", "享受无广告观影体验", 0),
            
            // 管理功能模块
            createPermission(PermissionConstants.ADMIN_USER_MANAGE, "管理用户", "管理系统用户", 0),
            createPermission(PermissionConstants.ADMIN_ROLE_MANAGE, "管理角色", "管理系统角色", 0),
            createPermission(PermissionConstants.ADMIN_PERMISSION_MANAGE, "管理权限", "管理系统权限", 0),
            createPermission(PermissionConstants.ADMIN_SYSTEM_CONFIG, "系统配置", "配置系统参数", 0),
            
            // 内容管理模块 (预留)
            createPermission(PermissionConstants.CONTENT_CREATE, "创建内容", "创建新内容", 0),
            createPermission(PermissionConstants.CONTENT_UPDATE, "更新内容", "更新内容信息", 0),
            createPermission(PermissionConstants.CONTENT_DELETE, "删除内容", "删除内容", 0),
            createPermission(PermissionConstants.CONTENT_AUDIT, "内容审核", "审核内容合规性", 0),
            
            // 订单支付模块 (预留)
            createPermission(PermissionConstants.ORDER_VIEW, "查看订单", "查看订单信息", 0),
            createPermission(PermissionConstants.ORDER_CREATE, "创建订单", "创建新订单", 0),
            createPermission(PermissionConstants.ORDER_CANCEL, "取消订单", "取消未支付订单", 0)
        );
        
        for (Permission permission : permissions) {
            if (!permissionRepository.existsByCode(permission.getCode())) {
                permissionRepository.save(permission);
                log.debug("创建权限: {}", permission.getCode());
            }
        }
        
        log.info("权限数据初始化完成，共 {} 个权限", permissions.size());
    }

    /**
     * 初始化角色数据
     */
    private void initializeRoles() {
        log.info("初始化角色数据...");
        
        // 检查是否已有角色数据
        long roleCount = roleRepository.count();
        if (roleCount > 0) {
            log.info("角色数据已存在，跳过初始化。当前角色数量: {}", roleCount);
            return;
        }
        
        List<Role> roles = Arrays.asList(
            createRole(RoleConstants.SUPER_ADMIN, RoleConstants.SUPER_ADMIN_DESC),
            createRole(RoleConstants.VIP_USER, RoleConstants.VIP_USER_DESC),
            createRole(RoleConstants.REGULAR_USER, RoleConstants.REGULAR_USER_DESC),
            createRole(RoleConstants.GUEST, RoleConstants.GUEST_DESC)
        );
        
        for (Role role : roles) {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
                log.debug("创建角色: {}", role.getName());
            }
        }
        
        log.info("角色数据初始化完成，共 {} 个角色", roles.size());
    }

    /**
     * 初始化角色权限关联
     */
    private void initializeRolePermissions() {
        log.info("初始化角色权限关联...");
        
        // 超级管理员 - 拥有所有权限
        assignPermissionsToRole(RoleConstants.SUPER_ADMIN, Arrays.asList(
            PermissionConstants.USER_VIEW, PermissionConstants.USER_CREATE, 
            PermissionConstants.USER_UPDATE, PermissionConstants.USER_DELETE,
            PermissionConstants.MOVIE_VIEW, PermissionConstants.MOVIE_COMMENT,
            PermissionConstants.MOVIE_FAVORITE, PermissionConstants.MOVIE_HISTORY,
            PermissionConstants.VIP_PLAY, PermissionConstants.VIP_DOWNLOAD, PermissionConstants.VIP_NO_AD,
            PermissionConstants.ADMIN_USER_MANAGE, PermissionConstants.ADMIN_ROLE_MANAGE,
            PermissionConstants.ADMIN_PERMISSION_MANAGE, PermissionConstants.ADMIN_SYSTEM_CONFIG,
            PermissionConstants.CONTENT_CREATE, PermissionConstants.CONTENT_UPDATE,
            PermissionConstants.CONTENT_DELETE, PermissionConstants.CONTENT_AUDIT,
            PermissionConstants.ORDER_VIEW, PermissionConstants.ORDER_CREATE, PermissionConstants.ORDER_CANCEL
        ));
        
        // VIP用户 - 拥有VIP功能 + 基础功能
        assignPermissionsToRole(RoleConstants.VIP_USER, Arrays.asList(
            PermissionConstants.USER_VIEW, PermissionConstants.USER_UPDATE,
            PermissionConstants.MOVIE_VIEW, PermissionConstants.MOVIE_COMMENT,
            PermissionConstants.MOVIE_FAVORITE, PermissionConstants.MOVIE_HISTORY,
            PermissionConstants.VIP_PLAY, PermissionConstants.VIP_DOWNLOAD, PermissionConstants.VIP_NO_AD,
            PermissionConstants.ORDER_VIEW, PermissionConstants.ORDER_CREATE, PermissionConstants.ORDER_CANCEL
        ));
        
        // 普通用户 - 基础功能
        assignPermissionsToRole(RoleConstants.REGULAR_USER, Arrays.asList(
            PermissionConstants.USER_VIEW, PermissionConstants.USER_UPDATE,
            PermissionConstants.MOVIE_VIEW, PermissionConstants.MOVIE_COMMENT,
            PermissionConstants.MOVIE_FAVORITE, PermissionConstants.MOVIE_HISTORY,
            PermissionConstants.ORDER_VIEW, PermissionConstants.ORDER_CREATE, PermissionConstants.ORDER_CANCEL
        ));
        
        // 游客 - 只能浏览
        assignPermissionsToRole(RoleConstants.GUEST, Arrays.asList(
            PermissionConstants.MOVIE_VIEW
        ));
        
        log.info("角色权限关联初始化完成");
    }

    private Permission createPermission(String code, String name, String description, Integer parentId) {
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        permission.setParentId(parentId);
        return permission;
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setStatus(1);
        return role;
    }

    private void assignPermissionsToRole(String roleName, List<String> permissionCodes) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            log.warn("角色不存在: {}", roleName);
            return;
        }
        
        Role role = roleOpt.get();
        List<Permission> permissions = permissionRepository.findByCodeIn(permissionCodes);
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        
        log.debug("为角色 {} 分配了 {} 个权限", roleName, permissions.size());
    }
} 