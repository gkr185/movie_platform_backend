# FileUploadService 文件上传服务

## 项目简介
FileUploadService 是电影平台的文件上传微服务，提供图片和视频文件的上传、存储、管理功能，并支持自动更新相关业务表的URL字段。

## 技术栈
- **Spring Boot**: 3.2.3
- **Spring Data JPA**: 数据持久化
- **MySQL**: 数据库
- **Consul**: 服务注册与发现
- **Swagger**: API文档
- **Lombok**: 简化代码

## 主要功能
1. **文件上传**: 支持单文件和批量文件上传
2. **分类存储**: 按业务分类存储文件（头像、海报、视频等）
3. **URL管理**: 自动更新相关业务表的URL字段
4. **文件管理**: 文件查询、删除等管理功能
5. **安全验证**: 文件类型和大小验证
6. **静态资源**: 提供文件访问服务

## 快速开始

### 1. 环境准备
- JDK 17+
- MySQL 8.0+
- Maven 3.6+
- Consul 1.15+

### 2. 数据库配置
执行 `file_record_table.sql` 创建文件记录表：
```sql
mysql -u root -p online_movie_db < file_record_table.sql
```

### 3. 配置文件
修改 `application.yml` 中的配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_movie_db
    username: your_username
    password: your_password

file:
  upload:
    base-path: /your/upload/path/
    base-url: http://localhost:8068/uploads/
```

### 4. 创建上传目录
确保以下目录存在：
```
public/uploads/
├── avatars/
├── posters/
├── videos/
├── banners/
├── news/
└── ads/
```

### 5. 启动服务
```bash
mvn spring-boot:run
```

服务启动后访问：
- API文档: http://localhost:8068/swagger-ui.html
- 健康检查: http://localhost:8068/api/files/health

## API 使用示例

### 上传用户头像
```bash
curl -X POST "http://localhost:8068/api/files/upload-and-update" \
  -F "file=@avatar.jpg" \
  -F "category=avatar" \
  -F "relatedId=1" \
  -F "urlType=avatar"
```

### 上传电影海报
```bash
curl -X POST "http://localhost:8068/api/files/upload-and-update" \
  -F "file=@poster.jpg" \
  -F "category=poster" \
  -F "relatedId=1" \
  -F "urlType=poster"
```

### 上传电影视频
```bash
curl -X POST "http://localhost:8068/api/files/upload-and-update" \
  -F "file=@movie.mp4" \
  -F "category=video" \
  -F "relatedId=1" \
  -F "urlType=play"
```

## 文件分类说明

| 分类 | 用途 | 存储目录 | 更新字段 |
|------|------|----------|----------|
| avatar | 用户头像 | uploads/avatars/ | user.avatar |
| poster | 电影海报 | uploads/posters/ | movie.poster_url |
| video | 视频文件 | uploads/videos/ | movie.play_url/trailer_url |
| banner | 横幅图片 | uploads/banners/ | - |
| news | 新闻图片 | uploads/news/ | news.cover_image |
| ad | 广告素材 | uploads/ads/ | advertisement.image_url/video_url |

## 配置说明

### 文件上传限制
- 最大文件大小: 100MB
- 支持图片格式: jpg, jpeg, png, gif, bmp, webp
- 支持视频格式: mp4, avi, mov, wmv, flv, mkv, webm

### 存储策略
- 文件按日期分目录存储: `category/yyyy/MM/dd/`
- 文件名使用UUID+时间戳确保唯一性
- 支持软删除，保留文件记录

## 开发指南

### 项目结构
```
src/main/java/com/edu/bcu/
├── config/          # 配置类
├── controller/      # 控制器
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── exception/      # 异常处理
├── repository/     # 数据访问层
└── service/        # 业务逻辑层
```

### 扩展开发
1. **添加新的文件分类**: 修改 `FileUploadConfig.getCategoryPath()`
2. **支持新的文件格式**: 更新配置中的 `allowedImageTypes` 或 `allowedVideoTypes`
3. **添加新的URL更新**: 在 `UrlUpdateService` 中添加新方法

## 部署说明

### Docker 部署
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/FileUploadService.jar app.jar
EXPOSE 8068
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 生产环境配置
1. 配置文件存储路径为绝对路径
2. 设置合适的文件大小限制
3. 配置文件备份策略
4. 监控磁盘空间使用

## 注意事项

1. **安全性**: 
   - 验证文件类型，防止恶意文件上传
   - 限制文件大小，防止磁盘空间耗尽
   - 使用唯一文件名，防止路径遍历攻击

2. **性能优化**:
   - 大文件上传建议使用分片上传
   - 定期清理无效文件记录
   - 考虑使用CDN加速文件访问

3. **数据一致性**:
   - 文件上传和数据库操作在同一事务中
   - 定期检查文件和数据库记录的一致性

## 故障排除

### 常见问题
1. **文件上传失败**: 检查目录权限和磁盘空间
2. **URL更新失败**: 检查数据库连接和表结构
3. **文件访问404**: 检查静态资源配置和文件路径

### 日志查看
```bash
# 查看上传日志
tail -f logs/file-upload.log

# 查看错误日志
tail -f logs/error.log
```

## 联系方式
如有问题请联系开发团队或提交Issue。 