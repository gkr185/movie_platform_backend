# 统计服务 (StatisticsService)

## 📊 服务概述

StatisticsService是电影平台的统计数据管理服务，提供日统计、总统计和手动刷新统计功能。

### 🎯 核心功能

- **日统计**：获取指定日期的统计数据
- **总统计**：获取平台的总体统计数据  
- **手动刷新**：手动触发统计数据刷新

### 🚀 服务信息

- **端口**: 8067
- **服务名**: statistics-service
- **数据库**: online_movie_db
- **注册中心**: Consul (localhost:8500)

## 📋 API 接口

### 日统计接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/statistics/daily/today` | GET | 获取今日统计数据 |
| `/api/statistics/daily/yesterday` | GET | 获取昨日统计数据 |
| `/api/statistics/daily/{date}` | GET | 获取指定日期统计数据 |
| `/api/statistics/daily/recent` | GET | 获取最近几天统计数据 |

### 总统计接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/statistics/total` | GET | 获取总统计数据 |
| `/api/statistics/overview` | GET | 获取统计概览数据 |

### 扩展接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/statistics/movies/popular` | GET | 获取热门电影排行 |
| `/api/statistics/behavior` | GET | 获取用户行为统计 |
| `/api/statistics/refresh` | POST | 手动刷新统计数据 |
| `/api/statistics/health` | GET | 健康检查 |

## 📊 统计指标

### 日统计指标

- 新增用户数
- 活跃用户数
- 总观影次数
- 独立观影用户数
- 平均观影时长
- 新增收藏数
- 新增评论数
- VIP订单数
- VIP订单收入
- 用户反馈数

### 总统计指标

- 总用户数
- VIP用户数
- 总电影数
- VIP电影数
- 总观影次数
- 总观影时长
- 总收藏数
- 总评论数
- 总VIP订单数
- 总VIP收入
- 总反馈数
- 平台评分
- 用户平均观影时长
- VIP转化率

## 🛠️ 技术架构

### 技术栈

- **Spring Boot 3.2.3**
- **Spring Cloud 2023.0.0**
- **MySQL 8.0**
- **Consul服务发现**
- **SpringDoc OpenAPI 3**
- **Lombok**

### 架构设计

```
Controller层 (REST API)
    ↓
Service层 (业务逻辑)
    ↓
Repository层 (数据访问)
    ↓
MySQL数据库
```

### 数据源表

基于现有数据库表进行统计：

- `user` - 用户基础数据
- `user_history` - 观影历史
- `user_favorite` - 用户收藏
- `user_behavior` - 用户行为
- `comment` - 评论数据
- `movie` - 电影数据
- `vip_order` - VIP订单
- `feedback` - 用户反馈

## 🚀 启动方式

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Consul 1.15+

### 2. 配置数据库

确保MySQL中存在`online_movie_db`数据库，并且包含所需的表结构。

### 3. 启动Consul

```bash
consul agent -dev
```

### 4. 启动服务

```bash
cd StatisticsService
mvn spring-boot:run
```

### 5. 访问接口

- **API文档**: http://localhost:8067/swagger-ui.html
- **健康检查**: http://localhost:8067/api/statistics/health
- **统计概览**: http://localhost:8067/api/statistics/overview

## 📝 使用示例

### 获取今日统计

```bash
curl http://localhost:8067/api/statistics/daily/today
```

### 获取总统计

```bash
curl http://localhost:8067/api/statistics/total
```

### 手动刷新统计

```bash
curl -X POST http://localhost:8067/api/statistics/refresh
```

### 获取热门电影排行

```bash
curl http://localhost:8067/api/statistics/movies/popular?limit=10
```

## 🔧 配置说明

### application.yml

```yaml
server:
  port: 8067

spring:
  application:
    name: statistics-service
  datasource:
    url: jdbc:mysql://localhost:3306/online_movie_db
    username: root
    password: 123456
  cloud:
    consul:
      host: localhost
      port: 8500
```

## 📈 监控与日志

### 健康检查

服务提供健康检查接口，可以监控服务状态：

```bash
curl http://localhost:8067/api/statistics/health
```

### 日志配置

日志级别设置为DEBUG，可以查看详细的统计数据获取过程。

## 🎯 设计特点

1. **简化设计**：只实现日统计、总统计和手动刷新
2. **无新增表**：基于现有数据库表进行统计
3. **实时计算**：每次请求时实时计算统计数据
4. **灵活扩展**：支持添加新的统计维度和指标
5. **性能优化**：使用JdbcTemplate进行高效的数据库查询

## 🔄 与其他服务的集成

StatisticsService作为独立的统计服务，可以为以下场景提供数据支持：

- 管理后台的数据大屏
- 运营分析报表
- 用户个人统计页面
- 实时监控告警

通过Consul服务发现，其他服务可以方便地调用统计接口获取所需数据。 