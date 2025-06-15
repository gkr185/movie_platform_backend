package com.edu.bcu.service;

import com.edu.bcu.entity.Permission;
import com.edu.bcu.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * RBAC核心服务接口 - 简化设计，便于其他微服务调用
 */
public interface RBACService {
    
    // ========== 核心权限检查方法 ==========
    
    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限代码
     * @return 是否拥有权限
     */
    boolean hasPermission(Integer userId, String permissionCode);
    
    /**
     * 检查用户是否拥有指定角色
     * @param userId 用户ID
     * @param roleName 角色名称
     * @return 是否拥有角色
     */
    boolean hasRole(Integer userId, String roleName);
    
    /**
     * 获取用户的所有权限代码
     * @param userId 用户ID
     * @return 权限代码集合
     */
    Set<String> getUserPermissions(Integer userId);
    
    /**
     * 获取用户的所有角色
     * @param userId 用户ID
     * @return 角色集合
     */
    Set<Role> getUserRoles(Integer userId);
    
    // ========== 用户角色管理 ==========
    
    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     */
    void assignRolesToUser(Integer userId, Set<Integer> roleIds);
    
    /**
     * 移除用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     */
    void removeRolesFromUser(Integer userId, Set<Integer> roleIds);
    
    // ========== 角色权限管理 ==========
    
    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID集合
     */
    void assignPermissionsToRole(Integer roleId, Set<Integer> permissionIds);
    
    /**
     * 移除角色权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID集合
     */
    void removePermissionsFromRole(Integer roleId, Set<Integer> permissionIds);
    
    // ========== 查询方法 ==========
    
    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<Role> getAllRoles();
    
    /**
     * 获取所有权限
     * @return 权限列表
     */
    List<Permission> getAllPermissions();
    
    /**
     * 根据角色ID获取角色权限
     * @param roleId 角色ID
     * @return 权限集合
     */
    Set<Permission> getRolePermissions(Integer roleId);
    
    // ========== 权限注册方法 (供其他微服务扩展使用) ==========
    
    /**
     * 注册新权限 - 供其他微服务动态添加权限使用
     * @param code 权限代码
     * @param name 权限名称
     * @param description 权限描述
     * @param parentId 父权限ID
     */
    void registerPermission(String code, String name, String description, Integer parentId);
    
    /**
     * 批量注册权限
     * @param permissions 权限列表
     */
    void batchRegisterPermissions(List<Permission> permissions);
} 