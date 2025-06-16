# FileUploadService 文件上传服务 API 接口说明

## 服务概述
- **服务名称**: FileUploadService
- **端口**: 8068
- **基础路径**: http://localhost:8068/api/files
- **功能**: 提供文件上传、管理和URL更新服务

## 核心功能
1. 支持图片和视频文件上传
2. 按分类存储文件（头像、海报、视频、横幅、新闻、广告）
3. 自动生成唯一文件名和访问URL
4. 文件记录数据库管理
5. 自动更新相关业务表的URL字段
6. 文件删除和查询功能

## API 接口列表

### 1. 上传单个文件
**接口地址**: `POST /api/files/upload`

**请求参数**:
- `file` (MultipartFile, 必需): 上传的文件
- `category` (String, 必需): 文件分类 (avatar/poster/video/banner/news/ad)
- `relatedId` (Long, 可选): 关联的业务ID

**请求示例**:
```bash
curl -X POST "http://localhost:8068/api/files/upload" \
  -F "file=@/path/to/image.jpg" \
  -F "category=poster" \
  -F "relatedId=1"
```

**响应示例**:
```json
{
  "id": 1,
  "originalName": "movie_poster.jpg",
  "fileName": "a1b2c3d4_1703123456789.jpg",
  "fileUrl": "http://localhost:8068/uploads/posters/2024/12/16/a1b2c3d4_1703123456789.jpg",
  "fileSize": 1024000,
  "fileType": "image/jpeg",
  "category": "poster",
  "relatedId": 1,
  "message": "文件上传成功",
  "success": true
}
```

### 2. 批量上传文件
**接口地址**: `POST /api/files/upload/batch`

**请求参数**:
- `files` (MultipartFile[], 必需): 上传的文件数组
- `category` (String, 必需): 文件分类
- `relatedId` (Long, 可选): 关联的业务ID

**请求示例**:
```bash
curl -X POST "http://localhost:8068/api/files/upload/batch" \
  -F "files=@/path/to/image1.jpg" \
  -F "files=@/path/to/image2.jpg" \
  -F "category=banner" \
  -F "relatedId=2"
```

### 3. 上传文件并更新数据库URL
**接口地址**: `POST /api/files/upload-and-update`

**请求参数**:
- `file` (MultipartFile, 必需): 上传的文件
- `category` (String, 必需): 文件分类
- `relatedId` (Long, 必需): 关联的业务ID
- `urlType` (String, 可选): URL字段类型 (avatar/poster/play/trailer/cover/image/video)

**请求示例**:
```bash
# 上传用户头像
curl -X POST "http://localhost:8068/api/files/upload-and-update" \
  -F "file=@/path/to/avatar.jpg" \
  -F "category=avatar" \
  -F "relatedId=1" \
  -F "urlType=avatar"

# 上传电影海报
curl -X POST "http://localhost:8068/api/files/upload-and-update" \
  -F "file=@/path/to/poster.jpg" \
  -F "category=poster" \
  -F "relatedId=1" \
  -F "urlType=poster"

# 上传电影视频
curl -X POST "http://localhost:8068/api/files/upload-and-update" \
  -F "file=@/path/to/movie.mp4" \
  -F "category=video" \
  -F "relatedId=1" \
  -F "urlType=play"
```

### 4. 根据分类获取文件列表
**接口地址**: `GET /api/files/category/{category}`

**请求示例**:
```bash
curl "http://localhost:8068/api/files/category/poster"
```

**响应示例**:
```json
[
  {
    "id": 1,
    "originalName": "movie_poster.jpg",
    "fileName": "a1b2c3d4_1703123456789.jpg",
    "filePath": "/e:/E/movie_platform/public/uploads/posters/2024/12/16/a1b2c3d4_1703123456789.jpg",
    "fileUrl": "http://localhost:8068/uploads/posters/2024/12/16/a1b2c3d4_1703123456789.jpg",
    "fileSize": 1024000,
    "fileType": "image/jpeg",
    "category": "poster",
    "relatedId": 1,
    "status": 1,
    "createTime": "2024-12-16T10:30:00",
    "updateTime": "2024-12-16T10:30:00"
  }
]
```

### 5. 根据关联ID获取文件列表
**接口地址**: `GET /api/files/related/{relatedId}`

**请求示例**:
```bash
curl "http://localhost:8068/api/files/related/1"
```

### 6. 根据分类和关联ID获取文件列表
**接口地址**: `GET /api/files/category/{category}/related/{relatedId}`

**请求示例**:
```bash
curl "http://localhost:8068/api/files/category/poster/related/1"
```

### 7. 删除文件
**接口地址**: `DELETE /api/files/{fileId}`

**请求示例**:
```bash
curl -X DELETE "http://localhost:8068/api/files/1"
```

**响应示例**:
```json
"文件删除成功"
```

### 8. 根据文件名获取文件信息
**接口地址**: `GET /api/files/filename/{fileName}`

**请求示例**:
```bash
curl "http://localhost:8068/api/files/filename/a1b2c3d4_1703123456789.jpg"
```

### 9. 获取所有文件列表
**接口地址**: `GET /api/files/all`

**请求示例**:
```bash
curl "http://localhost:8068/api/files/all"
```

### 10. 健康检查
**接口地址**: `GET /api/files/health`

**请求示例**:
```bash
curl "http://localhost:8068/api/files/health"
```

## 文件分类说明

| 分类 | 说明 | 存储目录 | 支持格式 |
|------|------|----------|----------|
| avatar | 用户头像 | uploads/avatars/ | jpg, jpeg, png, gif, bmp, webp |
| poster | 电影海报 | uploads/posters/ | jpg, jpeg, png, gif, bmp, webp |
| video | 视频文件 | uploads/videos/ | mp4, avi, mov, wmv, flv, mkv, webm |
| banner | 横幅图片 | uploads/banners/ | jpg, jpeg, png, gif, bmp, webp |
| news | 新闻图片 | uploads/news/ | jpg, jpeg, png, gif, bmp, webp |
| ad | 广告素材 | uploads/ads/ | jpg, jpeg, png, gif, bmp, webp, mp4, avi, mov |

## URL字段类型说明

| URL类型 | 说明 | 更新的表和字段 |
|---------|------|----------------|
| avatar | 用户头像 | user.avatar |
| poster | 电影海报 | movie.poster_url |
| play | 电影播放地址 | movie.play_url |
| trailer | 电影预告片 | movie.trailer_url |
| cover | 新闻封面图片 | news.cover_image |
| image | 广告图片 | advertisement.image_url |
| video | 广告视频 | advertisement.video_url |

## 业务流程

### 1. 用户头像上传流程
```
1. 前端选择头像文件
2. 调用 /api/files/upload-and-update 接口
3. 参数：category=avatar, relatedId=用户ID, urlType=avatar
4. 系统上传文件到 uploads/avatars/yyyy/MM/dd/ 目录
5. 自动更新 user 表的 avatar 字段
6. 返回文件URL给前端
```

### 2. 电影资源上传流程
```
1. 管理员上传电影海报
   - category=poster, relatedId=电影ID, urlType=poster
   - 更新 movie.poster_url

2. 管理员上传电影视频
   - category=video, relatedId=电影ID, urlType=play
   - 更新 movie.play_url

3. 管理员上传预告片
   - category=video, relatedId=电影ID, urlType=trailer
   - 更新 movie.trailer_url
```

### 3. 新闻图片上传流程
```
1. 编辑上传新闻封面图片
2. 调用 /api/files/upload-and-update 接口
3. 参数：category=news, relatedId=新闻ID, urlType=cover
4. 系统上传文件到 uploads/news/yyyy/MM/dd/ 目录
5. 自动更新 news 表的 cover_image 字段
```

## 错误处理

### 常见错误码
- `400 Bad Request`: 参数错误或文件格式不支持
- `413 Payload Too Large`: 文件大小超过限制（100MB）
- `500 Internal Server Error`: 服务器内部错误

### 错误响应示例
```json
{
  "id": null,
  "originalName": null,
  "fileName": null,
  "fileUrl": null,
  "fileSize": null,
  "fileType": null,
  "category": null,
  "relatedId": null,
  "message": "文件大小超过限制，最大支持100MB",
  "success": false
}
```

## 配置说明

### 文件上传配置
```yaml
file:
  upload:
    base-path: /e:/E/movie_platform/public/uploads/  # 文件存储基础路径
    base-url: http://localhost:8068/uploads/          # 文件访问基础URL
    max-size: 104857600                               # 最大文件大小（100MB）
```

### Spring Boot 配置
```yaml
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 100MB      # 单个文件最大大小
      max-request-size: 500MB   # 请求最大大小
      file-size-threshold: 2KB  # 文件写入磁盘的阈值
```

## 数据库表结构

### file_record 表
```sql
CREATE TABLE `file_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
  `file_name` varchar(255) NOT NULL COMMENT '存储文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件存储路径',
  `file_url` varchar(500) NOT NULL COMMENT '文件访问URL',
  `file_size` bigint NOT NULL COMMENT '文件大小',
  `file_type` varchar(100) NOT NULL COMMENT '文件类型',
  `category` varchar(50) NOT NULL COMMENT '文件分类',
  `related_id` bigint DEFAULT NULL COMMENT '关联业务ID',
  `status` int DEFAULT 1 COMMENT '状态：1-有效，0-删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_related_id` (`related_id`),
  KEY `idx_file_name` (`file_name`)
);
```

## 注意事项

1. **文件安全**: 系统会验证文件类型和大小，防止恶意文件上传
2. **存储结构**: 文件按日期分目录存储，便于管理和备份
3. **唯一文件名**: 使用UUID+时间戳生成唯一文件名，避免冲突
4. **软删除**: 删除文件时采用软删除方式，保留记录便于恢复
5. **事务处理**: 文件上传和数据库操作在同一事务中，保证数据一致性
6. **错误处理**: 完善的异常处理机制，提供详细的错误信息

## Swagger 文档
访问地址: http://localhost:8068/swagger-ui.html 