package com.edu.bcu.example;

import com.edu.bcu.constant.PermissionConstants;
import com.edu.bcu.constant.RoleConstants;
import com.edu.bcu.entity.Permission;
import com.edu.bcu.service.RBACService;
import com.edu.bcu.util.PermissionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 其他微服务集成RBAC示例
 * 演示如何在其他微服务中使用权限管理功能
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IntegrationExample {

    private final RBACService rbacService;

    /**
     * 示例1: UserService如何集成权限检查
     */
    public void userServiceExample() {
        log.info("=== UserService 集成示例 ===");
        
        Integer currentUserId = 1; // 当前操作用户ID
        Integer targetUserId = 2;  // 目标用户ID
        
        // 检查当前用户是否有删除用户的权限
        if (rbacService.hasPermission(currentUserId, PermissionConstants.USER_DELETE)) {
            log.info("用户 {} 可以删除用户 {}", currentUserId, targetUserId);
            // 执行删除逻辑
        } else {
            log.warn("用户 {} 没有删除用户权限", currentUserId);
            // 抛出权限不足异常
        }
        
        // 检查是否为管理员
        if (rbacService.hasRole(currentUserId, RoleConstants.SUPER_ADMIN)) {
            log.info("管理员操作：可以查看所有用户信息");
        } else {
            log.info("普通用户操作：只能查看自己的信息");
        }
    }

    /**
     * 示例2: MovieService如何集成权限检查
     */
    public void movieServiceExample() {
        log.info("=== MovieService 集成示例 ===");
        
        Integer userId = 1;
        Integer movieId = 1001;
        
        // 检查用户是否可以观看VIP电影
        if (rbacService.hasPermission(userId, PermissionConstants.VIP_PLAY)) {
            log.info("VIP用户 {} 可以观看VIP电影 {}", userId, movieId);
            // 返回高清播放地址
        } else {
            log.info("普通用户 {} 观看标清版本", userId);
            // 返回标清播放地址
        }
        
        // 检查用户是否可以下载电影
        if (rbacService.hasPermission(userId, PermissionConstants.VIP_DOWNLOAD)) {
            log.info("用户 {} 可以下载电影", userId);
        } else {
            log.info("用户 {} 不能下载电影", userId);
        }
        
        // 检查用户是否需要看广告
        if (rbacService.hasPermission(userId, PermissionConstants.VIP_NO_AD)) {
            log.info("VIP用户 {} 无需观看广告", userId);
        } else {
            log.info("普通用户 {} 需要观看广告", userId);
        }
    }

    /**
     * 示例3: VIPService如何集成权限检查
     */
    public void vipServiceExample() {
        log.info("=== VIPService 集成示例 ===");
        
        Integer userId = 1;
        
        // 检查用户是否可以创建VIP订单
        if (rbacService.hasPermission(userId, PermissionConstants.ORDER_CREATE)) {
            log.info("用户 {} 可以创建VIP订单", userId);
        } else {
            log.info("游客不能创建订单，需要先注册");
        }
        
        // 检查用户角色级别
        if (rbacService.hasRole(userId, RoleConstants.VIP_USER)) {
            log.info("已是VIP用户，可以续费");
        } else if (rbacService.hasRole(userId, RoleConstants.REGULAR_USER)) {
            log.info("普通用户，可以升级为VIP");
        } else {
            log.info("游客，需要先注册");
        }
    }

    /**
     * 示例4: 如何在新微服务中注册权限
     */
    public void registerNewServicePermissions() {
        log.info("=== 新微服务权限注册示例 ===");
        
        // 方式1: 单个注册权限
        rbacService.registerPermission(
            "news:create", 
            "创建新闻", 
            "发布新闻资讯", 
            0
        );
        
        // 方式2: 批量注册权限
        List<Permission> newsPermissions = List.of(
            createPermission("news:view", "查看新闻", "浏览新闻资讯"),
            createPermission("news:update", "更新新闻", "修改新闻内容"),
            createPermission("news:delete", "删除新闻", "删除新闻"),
            createPermission("news:audit", "审核新闻", "审核新闻内容")
        );
        
        rbacService.batchRegisterPermissions(newsPermissions);
        log.info("新闻服务权限注册完成");
    }

    /**
     * 示例5: 如何使用权限辅助工具
     */
    public void permissionHelperExample() {
        log.info("=== 权限辅助工具示例 ===");
        
        String roleName = RoleConstants.VIP_USER;
        
        // 使用辅助方法检查角色
        if (PermissionHelper.isVipUser(roleName)) {
            log.info("这是VIP用户");
        }
        
        // 检查角色级别
        int roleLevel = PermissionHelper.getRoleLevel(roleName);
        log.info("角色 {} 的级别是: {}", roleName, roleLevel);
        
        // 检查是否有足够权限
        if (PermissionHelper.hasEnoughPrivilege(roleName, RoleConstants.REGULAR_USER)) {
            log.info("VIP用户权限足够");
        }
        
        // 解析权限代码
        String module = PermissionHelper.getModuleFromPermissionCode("movie:view");
        String action = PermissionHelper.getActionFromPermissionCode("movie:view");
        log.info("权限模块: {}, 操作: {}", module, action);
    }

    /**
     * 示例6: 在Controller中使用权限检查
     */
    public void controllerExample() {
        log.info("=== Controller 权限检查示例 ===");
        
        // 伪代码演示Controller中的权限检查
        /*
        @RestController
        public class MovieController {
            
            @Autowired
            private RBACService rbacService;
            
            @GetMapping("/movies/{id}/play")
            public Result<String> playMovie(@PathVariable Integer id, HttpServletRequest request) {
                Integer userId = getCurrentUserId(request);
                
                // 检查基础观看权限
                if (!rbacService.hasPermission(userId, PermissionConstants.MOVIE_VIEW)) {
                    return Result.error("没有观看权限");
                }
                
                // 检查VIP权限决定播放质量
                if (rbacService.hasPermission(userId, PermissionConstants.VIP_PLAY)) {
                    return Result.success("VIP高清播放地址");
                } else {
                    return Result.success("标清播放地址");
                }
            }
            
            @PostMapping("/movies/{id}/download")
            public Result<String> downloadMovie(@PathVariable Integer id, HttpServletRequest request) {
                Integer userId = getCurrentUserId(request);
                
                if (!rbacService.hasPermission(userId, PermissionConstants.VIP_DOWNLOAD)) {
                    return Result.error("VIP用户才能下载");
                }
                
                return Result.success("下载地址");
            }
        }
        */
    }

    private Permission createPermission(String code, String name, String description) {
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        permission.setParentId(0);
        return permission;
    }
} 