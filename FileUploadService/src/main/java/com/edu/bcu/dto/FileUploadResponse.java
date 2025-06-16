package com.edu.bcu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private Long id;
    private String originalName;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private String category;
    private Long relatedId;
    private String message;
    private boolean success;
    
    // 构造器用于成功响应
    public FileUploadResponse(Long id, String originalName, String fileName, String fileUrl, 
                             Long fileSize, String fileType, String category, Long relatedId) {
        this.id = id;
        this.originalName = originalName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.category = category;
        this.relatedId = relatedId;
        this.success = true;
        this.message = "文件上传成功";
    }
    
    // 构造器用于失败响应
    public FileUploadResponse(String message) {
        this.success = false;
        this.message = message;
    }
    
    // 构造器用于成功响应（简化）
    public static FileUploadResponse success(String fileUrl, String fileName, String message) {
        FileUploadResponse response = new FileUploadResponse();
        response.setFileUrl(fileUrl);
        response.setFileName(fileName);
        response.setMessage(message);
        response.setSuccess(true);
        return response;
    }
    
    // 构造器用于失败响应（简化）
    public static FileUploadResponse failure(String message) {
        FileUploadResponse response = new FileUploadResponse();
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }
} 