package com.edu.bcu.service.impl;

import com.edu.bcu.entity.Permission;
import com.edu.bcu.entity.Role;
import com.edu.bcu.repository.PermissionRepository;
import com.edu.bcu.repository.RoleRepository;
import com.edu.bcu.service.RBACService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @Transactional
    public void assignRolesToUser(Integer userId, Set<Integer> roleIds) {
        log.info("为用户 {} 分配角色: {}", userId, roleIds);
        
        // 由于我们没有UserRole实体类，这个功能暂时使用空实现
        // 在实际项目中，这应该通过UserService来处理，或者创建UserRole实体类
        log.warn("用户角色分配功能需要通过UserService来实现，当前为空实现");
        
        log.info("用户 {} 角色分配完成（空实现）", userId);
    }

    @Override
    @Transactional
    public void removeRolesFromUser(Integer userId, Set<Integer> roleIds) {
        log.info("移除用户 {} 的角色: {}", userId, roleIds);
        
        // 由于我们没有UserRole实体类，这个功能暂时使用空实现
        log.warn("用户角色移除功能需要通过UserService来实现，当前为空实现");
        
        log.info("用户 {} 角色移除完成（空实现）", userId);
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
        return roleRepository.findActiveRoles();
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

    // ========== 私有辅助方法 ==========

} 