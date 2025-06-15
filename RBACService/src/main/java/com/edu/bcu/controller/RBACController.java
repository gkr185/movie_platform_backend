package com.edu.bcu.controller;

import com.edu.bcu.common.Result;
import com.edu.bcu.entity.Permission;
import com.edu.bcu.entity.Role;
import com.edu.bcu.service.RBACService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "RBAC权限管理", description = "角色权限管理接口")
@RestController
@RequestMapping("/api/rbac")
@RequiredArgsConstructor
public class RBACController {

    private final RBACService rbacService;

    // ========== 权限检查接口 ==========

    @Operation(summary = "检查用户权限", description = "检查指定用户是否拥有某个权限")
    @GetMapping("/check/permission")
    public Result<Boolean> checkPermission(
            @Parameter(description = "用户ID") @RequestParam Integer userId,
            @Parameter(description = "权限代码") @RequestParam String permissionCode) {
        boolean hasPermission = rbacService.hasPermission(userId, permissionCode);
        return Result.success(hasPermission);
    }

    @Operation(summary = "检查用户角色", description = "检查指定用户是否拥有某个角色")
    @GetMapping("/check/role")
    public Result<Boolean> checkRole(
            @Parameter(description = "用户ID") @RequestParam Integer userId,
            @Parameter(description = "角色名称") @RequestParam String roleName) {
        boolean hasRole = rbacService.hasRole(userId, roleName);
        return Result.success(hasRole);
    }

    @Operation(summary = "获取用户权限", description = "获取指定用户的所有权限代码")
    @GetMapping("/users/{userId}/permissions")
    public Result<Set<String>> getUserPermissions(
            @Parameter(description = "用户ID") @PathVariable Integer userId) {
        Set<String> permissions = rbacService.getUserPermissions(userId);
        return Result.success(permissions);
    }

    @Operation(summary = "获取用户角色", description = "获取指定用户的所有角色")
    @GetMapping("/users/{userId}/roles")
    public Result<Set<Role>> getUserRoles(
            @Parameter(description = "用户ID") @PathVariable Integer userId) {
        Set<Role> roles = rbacService.getUserRoles(userId);
        return Result.success(roles);
    }

    // ========== 角色CRUD管理接口 ==========

    @Operation(summary = "创建角色", description = "创建新角色")
    @PostMapping("/roles")
    public Result<Role> createRole(
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        Role role = rbacService.createRole(name, description);
        return Result.success("角色创建成功", role);
    }

    @Operation(summary = "更新角色", description = "更新角色信息")
    @PutMapping("/roles/{roleId}")
    public Result<Role> updateRole(
            @Parameter(description = "角色ID") @PathVariable Integer roleId,
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        Role role = rbacService.updateRole(roleId, name, description);
        return Result.success("角色更新成功", role);
    }

    @Operation(summary = "删除角色", description = "删除指定角色")
    @DeleteMapping("/roles/{roleId}")
    public Result<Void> deleteRole(
            @Parameter(description = "角色ID") @PathVariable Integer roleId) {
        rbacService.deleteRole(roleId);
        return Result.success("角色删除成功", null);
    }

    @Operation(summary = "更新角色状态", description = "启用或禁用角色")
    @PutMapping("/roles/{roleId}/status")
    public Result<Void> updateRoleStatus(
            @Parameter(description = "角色ID") @PathVariable Integer roleId,
            @Parameter(description = "状态(1:启用 0:禁用)") @RequestParam Integer status) {
        rbacService.updateRoleStatus(roleId, status);
        return Result.success("角色状态更新成功", null);
    }

    @Operation(summary = "获取角色详情", description = "根据ID获取角色详细信息")
    @GetMapping("/roles/{roleId}")
    public Result<Role> getRoleById(
            @Parameter(description = "角色ID") @PathVariable Integer roleId) {
        Role role = rbacService.getRoleById(roleId);
        return Result.success(role);
    }

    // ========== 权限CRUD管理接口 ==========

    @Operation(summary = "更新权限", description = "更新权限信息")
    @PutMapping("/permissions/{permissionId}")
    public Result<Permission> updatePermission(
            @Parameter(description = "权限ID") @PathVariable Integer permissionId,
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        Permission permission = rbacService.updatePermission(permissionId, name, description);
        return Result.success("权限更新成功", permission);
    }

    @Operation(summary = "删除权限", description = "删除指定权限")
    @DeleteMapping("/permissions/{permissionId}")
    public Result<Void> deletePermission(
            @Parameter(description = "权限ID") @PathVariable Integer permissionId) {
        rbacService.deletePermission(permissionId);
        return Result.success("权限删除成功", null);
    }

    @Operation(summary = "获取权限详情", description = "根据ID获取权限详细信息")
    @GetMapping("/permissions/{permissionId}")
    public Result<Permission> getPermissionById(
            @Parameter(description = "权限ID") @PathVariable Integer permissionId) {
        Permission permission = rbacService.getPermissionById(permissionId);
        return Result.success(permission);
    }

    // ========== 用户角色管理接口 ==========

    @Operation(summary = "分配用户角色", description = "为指定用户分配角色")
    @PostMapping("/users/{userId}/roles")
    public Result<Void> assignRolesToUser(
            @Parameter(description = "用户ID") @PathVariable Integer userId,
            @RequestBody Set<Integer> roleIds) {
        rbacService.assignRolesToUser(userId, roleIds);
        return Result.success("角色分配成功", null);
    }

    @Operation(summary = "移除用户角色", description = "移除指定用户的角色")
    @DeleteMapping("/users/{userId}/roles")
    public Result<Void> removeRolesFromUser(
            @Parameter(description = "用户ID") @PathVariable Integer userId,
            @RequestBody Set<Integer> roleIds) {
        rbacService.removeRolesFromUser(userId, roleIds);
        return Result.success("角色移除成功", null);
    }

    // ========== 角色权限管理接口 ==========

    @Operation(summary = "分配角色权限", description = "为指定角色分配权限")
    @PostMapping("/roles/{roleId}/permissions")
    public Result<Void> assignPermissionsToRole(
            @Parameter(description = "角色ID") @PathVariable Integer roleId,
            @RequestBody Set<Integer> permissionIds) {
        rbacService.assignPermissionsToRole(roleId, permissionIds);
        return Result.success("权限分配成功", null);
    }

    @Operation(summary = "移除角色权限", description = "移除指定角色的权限")
    @DeleteMapping("/roles/{roleId}/permissions")
    public Result<Void> removePermissionsFromRole(
            @Parameter(description = "角色ID") @PathVariable Integer roleId,
            @RequestBody Set<Integer> permissionIds) {
        rbacService.removePermissionsFromRole(roleId, permissionIds);
        return Result.success("权限移除成功", null);
    }

    @Operation(summary = "获取角色权限", description = "获取指定角色的所有权限")
    @GetMapping("/roles/{roleId}/permissions")
    public Result<Set<Permission>> getRolePermissions(
            @Parameter(description = "角色ID") @PathVariable Integer roleId) {
        Set<Permission> permissions = rbacService.getRolePermissions(roleId);
        return Result.success(permissions);
    }

    // ========== 查询接口 ==========

    @Operation(summary = "获取所有角色", description = "获取系统中所有角色（包括启用和禁用状态）")
    @GetMapping("/roles")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = rbacService.getAllRoles();
        return Result.success(roles);
    }

    @Operation(summary = "获取所有权限", description = "获取系统中所有权限")
    @GetMapping("/permissions")
    public Result<List<Permission>> getAllPermissions() {
        List<Permission> permissions = rbacService.getAllPermissions();
        return Result.success(permissions);
    }

    // ========== 权限注册接口 (供其他微服务使用) ==========

    @Operation(summary = "注册权限", description = "供其他微服务动态注册权限使用")
    @PostMapping("/permissions/register")
    public Result<Void> registerPermission(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "0") Integer parentId) {
        rbacService.registerPermission(code, name, description, parentId);
        return Result.success("权限注册成功", null);
    }

    @Operation(summary = "批量注册权限", description = "批量注册权限列表")
    @PostMapping("/permissions/batch-register")
    public Result<Void> batchRegisterPermissions(@RequestBody List<Permission> permissions) {
        rbacService.batchRegisterPermissions(permissions);
        return Result.success("批量权限注册成功", null);
    }
} 