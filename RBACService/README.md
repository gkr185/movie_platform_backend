# RBACService - 权限管理微服务

## 📋 项目概述

RBACService是一个独立的权限管理微服务，采用标准的RBAC(基于角色的访问控制)模型，为电影平台系统提供统一的权限管理功能。

### 🎯 设计目标

- **简化设计** - 只保留必要的权限管理功能
- **易于集成** - 其他微服务可以方便地调用权限接口
- **高可扩展** - 支持动态添加新的权限和角色
- **标准化** - 采用统一的权限代码格式

## 🏗️ 系统架构

### 角色体系（简化版）

| 角色 | 角色名称 | 描述 | 权限级别 |
|------|----------|------|----------|
| SUPER_ADMIN | 超级管理员 | 系统最高权限，可管理所有功能 | 4 |
| VIP_USER | VIP用户 | 付费用户，享受高级功能 | 3 |
| REGULAR_USER | 普通用户 | 注册用户，基础功能权限 | 2 |
| GUEST | 游客 | 未注册用户，只能浏览 | 1 |

### 权限模块设计

```
用户管理模块: user:*
- user:view - 查看用户信息
- user:create - 创建新用户
- user:update - 更新用户信息
- user:delete - 删除用户账号

电影模块: movie:*
- movie:view - 浏览电影信息
- movie:comment - 评论电影
- movie:favorite - 收藏电影
- movie:history - 观影历史

VIP功能模块: vip:*
- vip:play - 观看VIP专享内容
- vip:download - 下载电影资源
- vip:no_ad - 免广告体验

管理功能模块: admin:*
- admin:user_manage - 用户管理
- admin:role_manage - 角色管理
- admin:permission_manage - 权限管理
- admin:system_config - 系统配置

扩展模块: (预留给其他微服务)
- content:* - 内容管理权限
- order:* - 订单管理权限
```

## 🚀 快速开始

### 1. 启动服务

```bash
# 确保数据库可用
# 启动RBACService
mvn spring-boot:run
```

服务默认运行在端口 **8067**

### 2. 访问API文档

启动后访问: `http://localhost:8067/swagger-ui.html`

### 3. 核心API接口

#### 权限检查
```bash
# 检查用户权限
GET /api/rbac/check/permission?userId=1&permissionCode=movie:view

# 检查用户角色
GET /api/rbac/check/role?userId=1&roleName=VIP_USER
```

#### 获取用户权限信息
```bash
# 获取用户所有权限
GET /api/rbac/users/1/permissions

# 获取用户所有角色
GET /api/rbac/users/1/roles
```

#### 权限管理
```bash
# 为用户分配角色
POST /api/rbac/users/1/roles
Content-Type: application/json
[2, 3]

# 为角色分配权限
POST /api/rbac/roles/2/permissions  
Content-Type: application/json
[1, 2, 3, 4]
```

## 🔌 其他微服务集成

### 1. 在其他微服务中调用权限检查

```java
@RestController
public class MovieController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/movies/{id}/play")
    public Result<String> playMovie(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = getCurrentUserId(request);
        
        // 调用RBAC服务检查权限
        String url = "http://RBACService/api/rbac/check/permission?userId=" + userId + "&permissionCode=movie:view";
        Result<Boolean> response = restTemplate.getForObject(url, Result.class);
        
        if (response.getData()) {
            // 用户有权限，继续处理
            return handleMoviePlay(id, userId);
        } else {
            return Result.error("没有观看权限");
        }
    }
}
```

### 2. 使用Feign客户端（推荐）

```java
@FeignClient("RBACService")
public interface RBACClient {
    
    @GetMapping("/api/rbac/check/permission")
    Result<Boolean> checkPermission(@RequestParam Integer userId, 
                                   @RequestParam String permissionCode);
    
    @GetMapping("/api/rbac/check/role")
    Result<Boolean> checkRole(@RequestParam Integer userId, 
                             @RequestParam String roleName);
    
    @GetMapping("/api/rbac/users/{userId}/permissions")
    Result<Set<String>> getUserPermissions(@PathVariable Integer userId);
}

@Service
public class MovieService {
    
    @Autowired
    private RBACClient rbacClient;
    
    public String getPlayUrl(Integer movieId, Integer userId) {
        // 检查VIP权限
        Result<Boolean> vipCheck = rbacClient.checkPermission(userId, "vip:play");
        
        if (vipCheck.getData()) {
            return getVipPlayUrl(movieId);  // 返回高清地址
        } else {
            return getRegularPlayUrl(movieId);  // 返回标清地址
        }
    }
}
```

### 3. 新微服务注册权限

```java
@Service
public class NewsService {
    
    @Autowired
    private RBACClient rbacClient;
    
    @PostConstruct
    public void registerPermissions() {
        // 注册新闻服务相关权限
        rbacClient.registerPermission("news:view", "查看新闻", "浏览新闻资讯", 0);
        rbacClient.registerPermission("news:create", "创建新闻", "发布新闻", 0);
        rbacClient.registerPermission("news:update", "更新新闻", "修改新闻", 0);
        rbacClient.registerPermission("news:delete", "删除新闻", "删除新闻", 0);
    }
}
```

## 🛠️ 开发指南

### 权限代码规范

所有权限代码采用 `模块:操作` 的格式：

- **模块名**: 小写，表示功能模块，如 user、movie、vip
- **操作名**: 小写，表示具体操作，如 view、create、update、delete
- **示例**: `user:delete`、`movie:view`、`vip:play`

### 角色权限配置

在 `DataInitializer` 中配置默认的角色权限关系：

```java
// 为角色分配权限
assignPermissionsToRole(RoleConstants.VIP_USER, Arrays.asList(
    PermissionConstants.USER_VIEW, 
    PermissionConstants.USER_UPDATE,
    PermissionConstants.MOVIE_VIEW, 
    PermissionConstants.MOVIE_COMMENT,
    PermissionConstants.VIP_PLAY, 
    PermissionConstants.VIP_DOWNLOAD
));
```

### 添加新权限

1. **在 PermissionConstants 中定义权限常量**
```java
public static final String NEWS_CREATE = "news:create";
```

2. **在数据初始化器中添加权限**
```java
createPermission(PermissionConstants.NEWS_CREATE, "创建新闻", "发布新闻资讯", 0)
```

3. **为相应角色分配权限**
```java
assignPermissionsToRole(RoleConstants.SUPER_ADMIN, Arrays.asList(
    PermissionConstants.NEWS_CREATE
));
```

## 📊 数据库设计

### 核心表结构

- **role** - 角色表
- **permission** - 权限表  
- **role_permission** - 角色权限关联表
- **user_role** - 用户角色关联表（在user表中维护）

### 权限查询SQL示例

```sql
-- 查询用户的所有权限
SELECT DISTINCT p.code 
FROM permission p 
JOIN role_permission rp ON p.id = rp.permission_id
JOIN user_role ur ON rp.role_id = ur.role_id  
WHERE ur.user_id = ?

-- 查询角色的所有权限
SELECT p.code 
FROM permission p 
JOIN role_permission rp ON p.id = rp.permission_id
WHERE rp.role_id = ?
```

## 🚦 最佳实践

### 1. 权限检查粒度

- **粗粒度**: 在Controller层检查模块级权限
- **细粒度**: 在Service层检查具体操作权限
- **数据权限**: 在方法内部检查数据所有权

### 2. 性能优化

- 使用缓存存储用户权限信息
- 批量权限检查减少网络调用
- 权限变更时及时清理缓存

### 3. 安全考虑

- 权限检查失败时记录日志
- 敏感操作需要二次验证
- 定期审计权限分配情况

## 🔧 扩展功能

### 1. 缓存集成

```java
@Cacheable("userPermissions")
public Set<String> getUserPermissions(Integer userId) {
    // 权限查询逻辑
}
```

### 2. 权限变更通知

```java
@EventListener
public void onPermissionChanged(PermissionChangeEvent event) {
    // 清理相关缓存
    // 通知其他服务
}
```

### 3. 权限审计

```java
@Component
public class PermissionAuditLogger {
    
    public void logPermissionCheck(Integer userId, String permission, boolean result) {
        // 记录权限检查日志
    }
}
```

## 📞 技术支持

- **服务端口**: 8067
- **健康检查**: `/actuator/health`
- **API文档**: `/swagger-ui.html`
- **监控端点**: `/actuator/*`

## 🤝 贡献指南

1. 新增权限时遵循命名规范
2. 更新权限配置后同步更新文档
3. 重要变更需要编写单元测试
4. 提交前确保代码格式化

---

**注意**: 这是一个简化的权限管理系统，专注于核心功能和易用性。如需更复杂的权限控制（如数据权限、动态权限等），可以基于此架构进行扩展。 