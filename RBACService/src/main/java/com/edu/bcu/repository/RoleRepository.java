package com.edu.bcu.repository;

import com.edu.bcu.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    /**
     * 根据角色名称查找角色
     */
    Optional<Role> findByName(String name);
    
    /**
     * 根据状态查找角色
     */
    List<Role> findByStatus(Integer status);
    
    /**
     * 查找启用的角色
     */
    default List<Role> findActiveRoles() {
        return findByStatus(1);
    }
    
    /**
     * 根据用户ID查找用户角色
     */
    @Query(value = """
        SELECT r.* FROM role r 
        JOIN user_role ur ON r.id = ur.role_id 
        WHERE ur.user_id = :userId AND r.status = 1
        """, nativeQuery = true)
    Set<Role> findRolesByUserId(@Param("userId") Integer userId);
    
    /**
     * 查找角色及其权限信息
     */
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
    Optional<Role> findByIdWithPermissions(@Param("id") Integer id);
    
    /**
     * 检查角色名称是否存在
     */
    boolean existsByName(String name);
} 