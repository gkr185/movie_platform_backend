package com.edu.bcu.service;

import com.edu.bcu.dto.FileUploadResponse;
import com.edu.bcu.entity.FileRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    
    /**
     * 上传单个文件
     */
    FileUploadResponse uploadFile(MultipartFile file, String category, Long relatedId);
    
    /**
     * 上传多个文件
     */
    List<FileUploadResponse> uploadFiles(MultipartFile[] files, String category, Long relatedId);
    
    /**
     * 根据分类获取文件列表
     */
    List<FileRecord> getFilesByCategory(String category);
    
    /**
     * 根据关联ID获取文件列表
     */
    List<FileRecord> getFilesByRelatedId(Long relatedId);
    
    /**
     * 根据分类和关联ID获取文件列表
     */
    List<FileRecord> getFilesByCategoryAndRelatedId(String category, Long relatedId);
    
    /**
     * 删除文件
     */
    boolean deleteFile(Long fileId);
    
    /**
     * 根据文件名获取文件记录
     */
    FileRecord getFileByFileName(String fileName);
    
    /**
     * 获取所有文件记录
     */
    List<FileRecord> getAllFiles();
} 