package com.edu.bcu.service.impl;

import com.edu.bcu.entity.Permission;
import com.edu.bcu.entity.Role;
import com.edu.bcu.repository.PermissionRepository;
import com.edu.bcu.repository.RoleRepository;
import com.edu.bcu.service.RBACService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RBACServiceImpl implements RBACService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean hasPermission(Integer userId, String permissionCode) {
        log.debug("检查用户 {} 是否拥有权限: {}", userId, permissionCode);
        
        Set<Permission> userPermissions = permissionRepository.findPermissionsByUserId(userId);
        boolean hasPermission = userPermissions.stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));
        
        log.debug("用户 {} 权限检查结果: {}", userId, hasPermission);
        return hasPermission;
    }

    @Override
    public boolean hasRole(Integer userId, String roleName) {
        log.debug("检查用户 {} 是否拥有角色: {}", userId, roleName);
        
        Set<Role> userRoles = roleRepository.findRolesByUserId(userId);
        boolean hasRole = userRoles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
        
        log.debug("用户 {} 角色检查结果: {}", userId, hasRole);
        return hasRole;
    }

    @Override
    public Set<String> getUserPermissions(Integer userId) {
        log.debug("获取用户 {} 的所有权限", userId);
        
        Set<Permission> permissions = permissionRepository.findPermissionsByUserId(userId);
        return permissions.stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Role> getUserRoles(Integer userId) {
        log.debug("获取用户 {} 的所有角色", userId);
        return roleRepository.findRolesByUserId(userId);
    }

    // ========== 角色CRUD管理 ==========

    @Override
    @Transactional
    public Role createRole(String name, String description) {
        log.info("创建角色: name={}, description={}", name, description);
        
        if (roleRepository.existsByName(name)) {
            throw new RuntimeException("角色名称已存在: " + name);
        }
        
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setStatus(1); // 默认启用
        
        Role savedRole = roleRepository.save(role);
        log.info("角色创建成功，ID: {}", savedRole.getId());
        return savedRole;
    }

    @Override
    @Transactional
    public Role updateRole(Integer roleId, String name, String description) {
        log.info("更新角色: roleId={}, name={}, description={}", roleId, name, description);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
        
        // 检查名称是否重复（排除自己）
        if (!role.getName().equals(name) && roleRepository.existsByName(name)) {
            throw new RuntimeException("角色名称已存在: " + name);
        }
        
        role.setName(name);
        role.setDescription(description);
        
        Role updatedRole = roleRepository.save(role);
        log.info("角色更新成功: {}", roleId);
        return updatedRole;
    }

    @Override
    @Transactional
    public void deleteRole(Integer roleId) {
        log.info("删除角色: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
        
        // 检查是否有用户使用此角色
        long userCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_role WHERE role_id = ?", 
                Long.class, roleId);
        
        if (userCount > 0) {
            throw new RuntimeException("无法删除角色，还有 " + userCount + " 个用户使用此角色");
        }
        
        // 删除角色权限关联
        jdbcTemplate.update("DELETE FROM role_permission WHERE role_id = ?", roleId);
        
        // 删除角色
        roleRepository.delete(role);
        log.info("角色删除成功: {}", roleId);
    }

    @Override
    @Transactional
    public void updateRoleStatus(Integer roleId, Integer status) {
        log.info("更新角色状态: roleId={}, status={}", roleId, status);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
        
        role.setStatus(status);
        roleRepository.save(role);
        log.info("角色状态更新成功: {}", roleId);
    }

    // ========== 权限CRUD管理 ==========

    @Override
    @Transactional
    public Permission updatePermission(Integer permissionId, String name, String description) {
        log.info("更新权限: permissionId={}, name={}, description={}", permissionId, name, description);
        
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("权限不存在: " + permissionId));
        
        permission.setName(name);
        permission.setDescription(description);
        
        Permission updatedPermission = permissionRepository.save(permission);
        log.info("权限更新成功: {}", permissionId);
        return updatedPermission;
    }

    @Override
    @Transactional
    public void deletePermission(Integer permissionId) {
        log.info("删除权限: {}", permissionId);
        
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("权限不存在: " + permissionId));
        
        // 检查是否有角色使用此权限
        long roleCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM role_permission WHERE permission_id = ?", 
                Long.class, permissionId);
        
        if (roleCount > 0) {
            throw new RuntimeException("无法删除权限，还有 " + roleCount + " 个角色使用此权限");
        }
        
        // 删除权限
        permissionRepository.delete(permission);
        log.info("权限删除成功: {}", permissionId);
    }

    // ========== 用户角色管理 ==========

    @Override
    @Transactional
    public void assignRolesToUser(Integer userId, Set<Integer> roleIds) {
        log.info("为用户 {} 分配角色: {}", userId, roleIds);
        
        // 验证角色是否存在
        for (Integer roleId : roleIds) {
            if (!roleRepository.existsById(roleId)) {
                throw new RuntimeException("角色不存在: " + roleId);
            }
        }
        
        // 先删除用户现有角色
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", userId);
        
        // 批量插入新角色
        String sql = "INSERT INTO user_role (user_id, role_id, create_time) VALUES (?, ?, NOW())";
        for (Integer roleId : roleIds) {
            jdbcTemplate.update(sql, userId, roleId);
        }
        
        log.info("用户 {} 角色分配完成，分配了 {} 个角色", userId, roleIds.size());
    }

    @Override
    @Transactional
    public void removeRolesFromUser(Integer userId, Set<Integer> roleIds) {
        log.info("移除用户 {} 的角色: {}", userId, roleIds);
        
        if (roleIds.isEmpty()) {
            return;
        }
        
        String sql = "DELETE FROM user_role WHERE user_id = ? AND role_id = ?";
        for (Integer roleId : roleIds) {
            jdbcTemplate.update(sql, userId, roleId);
        }
        
        log.info("用户 {} 角色移除完成，移除了 {} 个角色", userId, roleIds.size());
    }

    @Override
    @Transactional
    public void assignPermissionsToRole(Integer roleId, Set<Integer> permissionIds) {
        log.info("为角色 {} 分配权限: {}", roleId, permissionIds);
        
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new RuntimeException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        Set<Permission> permissions = permissionIds.stream()
                .map(permissionId -> permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new RuntimeException("权限不存在: " + permissionId)))
                .collect(Collectors.toSet());
        
        role.setPermissions(permissions);
        roleRepository.save(role);
        
        log.info("角色 {} 权限分配完成", roleId);
    }

    @Override
    @Transactional
    public void removePermissionsFromRole(Integer roleId, Set<Integer> permissionIds) {
        log.info("移除角色 {} 的权限: {}", roleId, permissionIds);
        
        Optional<Role> roleOpt = roleRepository.findByIdWithPermissions(roleId);
        if (roleOpt.isEmpty()) {
            throw new RuntimeException("角色不存在: " + roleId);
        }
        
        Role role = roleOpt.get();
        Set<Permission> currentPermissions = role.getPermissions();
        
        // 移除指定权限
        currentPermissions.removeIf(permission -> permissionIds.contains(permission.getId()));
        
        roleRepository.save(role);
        
        log.info("角色 {} 权限移除完成", roleId);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Set<Permission> getRolePermissions(Integer roleId) {
        return permissionRepository.findPermissionsByRoleId(roleId);
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
    }

    @Override
    public Permission getPermissionById(Integer permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("权限不存在: " + permissionId));
    }

    @Override
    @Transactional
    public void registerPermission(String code, String name, String description, Integer parentId) {
        log.info("注册新权限: code={}, name={}", code, name);
        
        if (permissionRepository.existsByCode(code)) {
            log.warn("权限代码已存在: {}", code);
            return;
        }
        
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        permission.setParentId(parentId != null ? parentId : 0);
        
        permissionRepository.save(permission);
        log.info("权限注册成功: {}", code);
    }

    @Override
    @Transactional
    public void batchRegisterPermissions(List<Permission> permissions) {
        log.info("批量注册权限，数量: {}", permissions.size());
        
        List<Permission> newPermissions = permissions.stream()
                .filter(permission -> !permissionRepository.existsByCode(permission.getCode()))
                .collect(Collectors.toList());
        
        if (!newPermissions.isEmpty()) {
            permissionRepository.saveAll(newPermissions);
            log.info("批量注册权限完成，新增数量: {}", newPermissions.size());
        } else {
            log.info("没有新权限需要注册");
        }
    }
} 