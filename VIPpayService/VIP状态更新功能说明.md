# VIP状态更新功能说明

## 功能概述

在用户支付VIP订单成功后，系统会自动更新用户的VIP状态，包括：
- 将用户标记为VIP用户 (`is_vip = 1`)
- 根据购买的VIP套餐类型设置到期时间

## 实现架构

### 1. 微服务调用流程
```
支付服务 (VIPpayService:8066) 
    ↓ 支付成功回调
    ↓ HTTP调用
用户服务 (UserService:8082)
    ↓ 更新数据库
用户表 (user.is_vip, user.vip_expire_time)
```

### 2. 关键组件

#### 支付服务 (VIPpayService)
- `PaymentServiceImpl.handlePaymentCallback()` - 支付回调处理
- `UserServiceClient` - 用户服务HTTP客户端
- `RestTemplateConfig` - HTTP客户端配置

#### 用户服务 (UserService)
- `UserService.updateVipStatus()` - 更新VIP状态接口
- `UserController.updateVipStatus()` - VIP状态更新API
- `VipUpdateDTO` - VIP更新数据传输对象

## API接口

### 用户服务 - 更新VIP状态
```http
PUT /api/users/{userId}/vip
Content-Type: application/json

{
  "vipType": 1,
  "vipExpireTime": "2024-12-31T23:59:59"
}
```

### 用户服务 - 取消VIP状态
```http
DELETE /api/users/{userId}/vip
```

## VIP套餐类型与有效期

| VIP类型 | 名称 | 有效期 |
|---------|------|--------|
| 1 | 月度会员 | 30天 |
| 2 | 季度会员 | 90天 |
| 3 | 年度会员 | 365天 |

## 支付成功处理流程

1. **接收支付回调** - `handlePaymentCallback(orderId, "SUCCESS")`
2. **更新订单状态** - 设置订单为已支付状态
3. **计算VIP到期时间** - 当前时间 + VIP套餐天数
4. **调用用户服务** - 通过HTTP调用更新用户VIP状态
5. **异常处理** - 如果用户服务调用失败，记录日志但不影响订单状态

## 测试方法

### 1. 完整支付流程测试

1. **启动服务**
   ```bash
   # 启动用户服务
   cd movie_platform_backend/UserService
   mvn spring-boot:run
   
   # 启动支付服务
   cd movie_platform_backend/VIPpayService
   mvn spring-boot:run
   ```

2. **创建支付订单**
   ```http
   POST http://localhost:8066/api/payment/generate-qrcode
   Content-Type: application/json
   
   {
     "userId": 1,
     "planId": 1,
     "amount": 29.90,
     "paymentMethod": 0
   }
   ```

3. **模拟支付成功**
   ```http
   POST http://localhost:8066/api/payment/callback
   Content-Type: application/json
   
   {
     "orderId": "返回的订单号",
     "status": "SUCCESS"
   }
   ```

4. **验证用户VIP状态**
   ```http
   GET http://localhost:8082/api/users/1
   ```
   
   检查返回的用户信息中：
   - `isVip` 应该为 `1`
   - `vipExpireTime` 应该是当前时间 + 30天

### 2. 单独测试用户服务API

```http
PUT http://localhost:8082/api/users/1/vip
Content-Type: application/json

{
  "vipType": 2,
  "vipExpireTime": "2024-12-31T23:59:59"
}
```

### 3. 前端集成测试

在前端VIP页面进行支付测试：
1. 选择VIP套餐
2. 生成支付二维码
3. 点击"模拟支付成功"
4. 观察页面是否显示"支付成功！VIP权限已开通"
5. 检查用户信息是否更新

## 配置说明

### 支付服务配置 (application.yml)
```yaml
user:
  service:
    url: http://localhost:8082
```

### 数据库表结构

#### 用户表 (user)
```sql
ALTER TABLE user ADD COLUMN is_vip INT DEFAULT 0 COMMENT 'VIP状态 0:普通用户 1:VIP用户';
ALTER TABLE user ADD COLUMN vip_expire_time DATETIME COMMENT 'VIP到期时间';
```

#### 订单表 (vip_order)
```sql
CREATE TABLE vip_order (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  order_number VARCHAR(32) UNIQUE NOT NULL,
  vip_type INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  payment_method INT,
  status INT DEFAULT 0,
  create_time DATETIME,
  pay_time DATETIME,
  expire_time DATETIME,
  qr_code_url VARCHAR(255)
);
```

## 异常处理

1. **用户服务不可用** - 记录错误日志，订单状态仍为已支付
2. **网络超时** - 可以考虑实现重试机制
3. **用户不存在** - 用户服务会返回错误，支付服务记录日志
4. **数据库异常** - 事务回滚，保证数据一致性

## 后续优化建议

1. **消息队列** - 使用MQ实现异步处理，提高性能
2. **重试机制** - 实现失败重试和补偿机制
3. **监控告警** - 添加VIP状态更新失败的监控
4. **日志完善** - 使用专业日志框架替代System.err.println
5. **缓存优化** - 缓存用户VIP状态，减少数据库查询 