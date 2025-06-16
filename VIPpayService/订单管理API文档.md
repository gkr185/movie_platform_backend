# VIP订单管理API文档

## 概述
基于 `vip_order` 表的完整订单管理功能，包括订单查询、状态管理、统计分析等。

## 数据结构

### 1. 枚举定义

#### VIP类型 (VipType)
- `1` - 月度会员 (30天)
- `2` - 季度会员 (90天)  
- `3` - 年度会员 (365天)

#### 支付方式 (PaymentMethod)
- `0` - 微信支付
- `1` - 支付宝
- `2` - 银行卡

#### 订单状态 (OrderStatus)
- `0` - 待支付
- `1` - 已支付
- `2` - 已取消
- `3` - 已过期

## API接口

### 1. 分页查询订单列表
```
POST /api/payment/orders/search
```

**请求体 (OrderQueryRequest):**
```json
{
  "userId": 1,                    // 用户ID（可选）
  "orderNumber": "abc123",        // 订单号（模糊匹配，可选）
  "vipType": 1,                   // VIP类型（可选）
  "paymentMethod": 0,             // 支付方式（可选）
  "status": 1,                    // 订单状态（可选）
  "startTime": "2024-01-01T00:00:00",  // 开始时间（可选）
  "endTime": "2024-12-31T23:59:59",    // 结束时间（可选）
  "page": 0,                      // 页码，从0开始
  "size": 10,                     // 每页大小
  "sortBy": "createTime",         // 排序字段
  "sortDirection": "DESC"         // 排序方向 ASC/DESC
}
```

**响应:**
```json
{
  "content": [
    {
      "id": 1,
      "userId": 1,
      "orderNumber": "abc123456789",
      "vipType": 1,
      "vipTypeName": "月度会员",
      "amount": 29.99,
      "paymentMethod": 0,
      "paymentMethodName": "微信支付",
      "status": 1,
      "statusName": "已支付",
      "createTime": "2024-01-01T10:00:00",
      "payTime": "2024-01-01T10:05:00",
      "expireTime": "2024-01-01T10:30:00",
      "qrCodeUrl": "https://example.com/qr/abc123456789"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true
}
```

### 2. 获取订单详情
```
GET /api/payment/orders/{orderId}
```

**响应:**
```json
{
  "id": 1,
  "userId": 1,
  "orderNumber": "abc123456789",
  "vipType": 1,
  "vipTypeName": "月度会员",
  "amount": 29.99,
  "paymentMethod": 0,
  "paymentMethodName": "微信支付",
  "status": 1,
  "statusName": "已支付",
  "createTime": "2024-01-01T10:00:00",
  "payTime": "2024-01-01T10:05:00",
  "expireTime": "2024-01-01T10:30:00",
  "qrCodeUrl": "https://example.com/qr/abc123456789"
}
```

### 3. 根据订单号获取详情
```
GET /api/payment/orders/by-number/{orderNumber}
```

### 4. 取消订单
```
PUT /api/payment/orders/{orderId}/cancel
```

**限制:** 只能取消状态为"待支付"的订单

### 5. 获取订单统计
```
GET /api/payment/orders/statistics
```

**响应:**
```json
{
  "totalOrders": 1000,           // 总订单数
  "pendingOrders": 50,           // 待支付订单数
  "paidOrders": 800,             // 已支付订单数
  "cancelledOrders": 150,        // 已取消订单数
  "totalAmount": 50000.00,       // 总金额
  "paidAmount": 40000.00,        // 已支付金额
  "paymentRate": 0.8,            // 支付成功率
  "wechatOrders": 400,           // 微信支付订单数
  "alipayOrders": 350,           // 支付宝订单数
  "bankCardOrders": 50,          // 银行卡订单数
  "monthlyOrders": 300,          // 月度会员订单数
  "quarterlyOrders": 200,        // 季度会员订单数
  "yearlyOrders": 300            // 年度会员订单数
}
```

### 6. 获取用户订单列表
```
GET /api/payment/users/{userId}/orders?page=0&size=10
```

### 7. 手动取消过期订单
```
POST /api/payment/orders/cancel-expired
```

**说明:** 手动触发过期订单处理，系统也会每10分钟自动执行一次

## 自动化功能

### 定时任务
- **过期订单处理**: 每10分钟自动执行，将过期的待支付订单状态更改为"已取消"
- **执行时间**: 固定间隔600秒（10分钟）
- **日志记录**: 记录任务执行开始、完成和错误信息

## 错误处理

### 常见错误码
- `400` - 请求参数错误
- `404` - 订单不存在
- `409` - 订单状态不允许操作（如已支付订单无法取消）
- `500` - 服务器内部错误

### 错误示例
```json
{
  "error": "订单不存在",
  "timestamp": "2024-01-01T10:00:00",
  "status": 404
}
```

## 使用示例

### 1. 管理员查询所有订单
```bash
curl -X POST http://localhost:8066/api/payment/orders/search \
  -H "Content-Type: application/json" \
  -d '{
    "page": 0,
    "size": 20,
    "sortBy": "createTime",
    "sortDirection": "DESC"
  }'
```

### 2. 查询特定用户的订单
```bash
curl -X POST http://localhost:8066/api/payment/orders/search \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "page": 0,
    "size": 10
  }'
```

### 3. 按状态筛选订单
```bash
curl -X POST http://localhost:8066/api/payment/orders/search \
  -H "Content-Type: application/json" \
  -d '{
    "status": 0,
    "page": 0,
    "size": 10
  }'
```

### 4. 获取订单统计
```bash
curl -X GET http://localhost:8066/api/payment/orders/statistics
```

## 性能优化建议

1. **数据库索引**: 确保在 `user_id`, `status`, `create_time` 等字段上建立索引
2. **分页查询**: 使用合理的分页大小，避免一次性查询大量数据
3. **缓存统计**: 可以考虑对统计数据进行缓存，减少数据库查询
4. **异步处理**: 批量操作可以考虑异步处理，提高响应速度

## 扩展功能

未来可以扩展的功能包括：
- 订单导出功能
- 订单数据可视化
- 支付失败重试机制
- 订单状态变更通知
- 更详细的统计报表 