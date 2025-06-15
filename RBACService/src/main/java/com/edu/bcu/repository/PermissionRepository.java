package com.edu.bcu.repository;

import com.edu.bcu.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    
    /**
     * 根据权限代码查找权限
     */
    Optional<Permission> findByCode(String code);
    
    /**
     * 根据父权限ID查找子权限
     */
    List<Permission> findByParentId(Integer parentId);
    
    /**
     * 查找根权限（父权限ID为0）
     */
    @Query("SELECT p FROM Permission p WHERE p.parentId = 0 ORDER BY p.id")
    List<Permission> findRootPermissions();
    
    /**
     * 根据用户ID查找用户的所有权限
     */
    @Query(value = """
        SELECT DISTINCT p.* FROM permission p 
        JOIN role_permission rp ON p.id = rp.permission_id
        JOIN role r ON rp.role_id = r.id
        JOIN user_role ur ON r.id = ur.role_id 
        WHERE ur.user_id = :userId AND r.status = 1
        """, nativeQuery = true)
    Set<Permission> findPermissionsByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据角色ID查找角色权限
     */
    @Query(value = """
        SELECT p.* FROM permission p 
        JOIN role_permission rp ON p.id = rp.permission_id
        JOIN role r ON rp.role_id = r.id
        WHERE r.id = :roleId AND r.status = 1
        """, nativeQuery = true)
    Set<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);
    
    /**
     * 检查权限代码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 根据权限代码批量查找权限
     */
    @Query("SELECT p FROM Permission p WHERE p.code IN :codes")
    List<Permission> findByCodeIn(@Param("codes") List<String> codes);
} 