package com.edu.bcu.controller;

import com.edu.bcu.dto.FileUploadResponse;
import com.edu.bcu.entity.FileRecord;
import com.edu.bcu.service.FileUploadService;
import com.edu.bcu.service.UrlUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "文件上传管理", description = "文件上传、下载、删除等操作")
public class FileUploadController {
    
    private final FileUploadService fileUploadService;
    private final UrlUpdateService urlUpdateService;
    
    @PostMapping("/upload")
    @Operation(summary = "上传单个文件", description = "支持图片和视频文件上传")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "上传的文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件分类：avatar/poster/video/banner/news/ad", required = true)
            @RequestParam("category") String category,
            @Parameter(description = "关联的业务ID（可选）")
            @RequestParam(value = "relatedId", required = false) Long relatedId) {
        
        log.info("接收文件上传请求：文件名={}, 分类={}, 关联ID={}", 
                file.getOriginalFilename(), category, relatedId);
        
        FileUploadResponse response = fileUploadService.uploadFile(file, category, relatedId);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/upload/batch")
    @Operation(summary = "批量上传文件", description = "支持同时上传多个文件")
    public ResponseEntity<List<FileUploadResponse>> uploadFiles(
            @Parameter(description = "上传的文件数组", required = true)
            @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "文件分类：avatar/poster/video/banner/news/ad", required = true)
            @RequestParam("category") String category,
            @Parameter(description = "关联的业务ID（可选）")
            @RequestParam(value = "relatedId", required = false) Long relatedId) {
        
        log.info("接收批量文件上传请求：文件数量={}, 分类={}, 关联ID={}", 
                files.length, category, relatedId);
        
        List<FileUploadResponse> responses = fileUploadService.uploadFiles(files, category, relatedId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类获取文件列表", description = "获取指定分类下的所有文件")
    public ResponseEntity<List<FileRecord>> getFilesByCategory(
            @Parameter(description = "文件分类", required = true)
            @PathVariable String category) {
        
        List<FileRecord> files = fileUploadService.getFilesByCategory(category);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/related/{relatedId}")
    @Operation(summary = "根据关联ID获取文件列表", description = "获取指定业务ID关联的所有文件")
    public ResponseEntity<List<FileRecord>> getFilesByRelatedId(
            @Parameter(description = "关联的业务ID", required = true)
            @PathVariable Long relatedId) {
        
        List<FileRecord> files = fileUploadService.getFilesByRelatedId(relatedId);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/category/{category}/related/{relatedId}")
    @Operation(summary = "根据分类和关联ID获取文件列表", description = "获取指定分类和业务ID的文件")
    public ResponseEntity<List<FileRecord>> getFilesByCategoryAndRelatedId(
            @Parameter(description = "文件分类", required = true)
            @PathVariable String category,
            @Parameter(description = "关联的业务ID", required = true)
            @PathVariable Long relatedId) {
        
        List<FileRecord> files = fileUploadService.getFilesByCategoryAndRelatedId(category, relatedId);
        return ResponseEntity.ok(files);
    }
    
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件", description = "删除指定ID的文件（软删除）")
    public ResponseEntity<String> deleteFile(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long fileId) {
        
        boolean success = fileUploadService.deleteFile(fileId);
        if (success) {
            return ResponseEntity.ok("文件删除成功");
        } else {
            return ResponseEntity.badRequest().body("文件删除失败");
        }
    }
    
    @GetMapping("/filename/{fileName}")
    @Operation(summary = "根据文件名获取文件信息", description = "通过文件名查询文件记录")
    public ResponseEntity<FileRecord> getFileByFileName(
            @Parameter(description = "文件名", required = true)
            @PathVariable String fileName) {
        
        FileRecord file = fileUploadService.getFileByFileName(fileName);
        if (file != null) {
            return ResponseEntity.ok(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/all")
    @Operation(summary = "获取所有文件列表", description = "获取系统中所有的文件记录")
    public ResponseEntity<List<FileRecord>> getAllFiles() {
        List<FileRecord> files = fileUploadService.getAllFiles();
        return ResponseEntity.ok(files);
    }
    
    @PostMapping("/upload-and-update")
    @Operation(summary = "上传文件并更新数据库URL", description = "上传文件后自动更新相关表中的URL字段")
    public ResponseEntity<FileUploadResponse> uploadFileAndUpdateUrl(
            @Parameter(description = "上传的文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件分类：avatar/poster/video/banner/news/ad", required = true)
            @RequestParam("category") String category,
            @Parameter(description = "关联的业务ID", required = true)
            @RequestParam("relatedId") Long relatedId,
            @Parameter(description = "URL字段类型：avatar/poster/play/trailer/cover/image/video")
            @RequestParam(value = "urlType", required = false) String urlType) {
        
        log.info("接收文件上传并更新URL请求：文件名={}, 分类={}, 关联ID={}, URL类型={}", 
                file.getOriginalFilename(), category, relatedId, urlType);
        
        // 上传文件
        FileUploadResponse response = fileUploadService.uploadFile(file, category, relatedId);
        
        if (response.isSuccess()) {
            // 根据分类和URL类型更新相关表的URL字段
            boolean updateSuccess = updateRelatedUrl(category, urlType, relatedId, response.getFileUrl());
            if (updateSuccess) {
                response.setMessage(response.getMessage() + "，数据库URL更新成功");
            } else {
                response.setMessage(response.getMessage() + "，但数据库URL更新失败");
            }
        }
        
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查文件上传服务状态")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("文件上传服务运行正常");
    }
    
    /**
     * 根据分类和URL类型更新相关表的URL字段
     */
    private boolean updateRelatedUrl(String category, String urlType, Long relatedId, String fileUrl) {
        if (urlType == null || urlType.isEmpty()) {
            // 如果没有指定URL类型，根据分类自动判断
            urlType = getDefaultUrlType(category);
        }
        
        return switch (urlType.toLowerCase()) {
            case "avatar" -> urlUpdateService.updateUserAvatar(relatedId, fileUrl);
            case "poster" -> urlUpdateService.updateMoviePoster(relatedId, fileUrl);
            case "play" -> urlUpdateService.updateMoviePlayUrl(relatedId, fileUrl);
            case "trailer" -> urlUpdateService.updateMovieTrailerUrl(relatedId, fileUrl);
            case "cover" -> urlUpdateService.updateNewsCoverImage(relatedId, fileUrl);
            case "image" -> urlUpdateService.updateAdImageUrl(relatedId, fileUrl);
            case "video" -> urlUpdateService.updateAdVideoUrl(relatedId, fileUrl);
            default -> {
                log.warn("未知的URL类型：{}", urlType);
                yield false;
            }
        };
    }
    
    /**
     * 根据分类获取默认的URL类型
     */
    private String getDefaultUrlType(String category) {
        return switch (category.toLowerCase()) {
            case "avatar" -> "avatar";
            case "poster" -> "poster";
            case "video" -> "play";
            case "banner" -> "image";
            case "news" -> "cover";
            case "ad" -> "image";
            default -> "image";
        };
    }
} 