package com.edu.bcu.service.impl;

import com.edu.bcu.config.FileUploadConfig;
import com.edu.bcu.dto.FileUploadResponse;
import com.edu.bcu.entity.FileRecord;
import com.edu.bcu.repository.FileRecordRepository;
import com.edu.bcu.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileUploadServiceImpl implements FileUploadService {
    
    private final FileRecordRepository fileRecordRepository;
    private final FileUploadConfig fileUploadConfig;
    
    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String category, Long relatedId) {
        try {
            // 验证文件
            if (file.isEmpty()) {
                return FileUploadResponse.failure("文件不能为空");
            }
            
            // 验证文件大小
            if (file.getSize() > fileUploadConfig.getMaxSize()) {
                return FileUploadResponse.failure("文件大小超过限制：" + (fileUploadConfig.getMaxSize() / 1024 / 1024) + "MB");
            }
            
            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return FileUploadResponse.failure("文件名不能为空");
            }
            
            String extension = getFileExtension(originalFilename);
            if (!fileUploadConfig.isAllowedFile(extension)) {
                return FileUploadResponse.failure("不支持的文件格式：" + extension);
            }
            
            // 生成唯一文件名
            String fileName = generateFileName(originalFilename);
            
            // 确定存储路径
            String categoryPath = fileUploadConfig.getCategoryPath(category);
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = categoryPath + datePath + "/";
            String fullPath = fileUploadConfig.getBasePath() + relativePath;
            
            // 创建目录
            Path directoryPath = Paths.get(fullPath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            
            // 保存文件
            String filePath = fullPath + fileName;
            File destFile = new File(filePath);
            file.transferTo(destFile);
            
            // 生成访问URL
            String fileUrl = fileUploadConfig.getBaseUrl() + relativePath + fileName;
            
            // 保存文件记录到数据库
            FileRecord fileRecord = new FileRecord();
            fileRecord.setOriginalName(originalFilename);
            fileRecord.setFileName(fileName);
            fileRecord.setFilePath(filePath);
            fileRecord.setFileUrl(fileUrl);
            fileRecord.setFileSize(file.getSize());
            fileRecord.setFileType(file.getContentType());
            fileRecord.setCategory(category);
            fileRecord.setRelatedId(relatedId);
            fileRecord.setStatus(1);
            
            FileRecord savedRecord = fileRecordRepository.save(fileRecord);
            
            log.info("文件上传成功：{} -> {}", originalFilename, fileUrl);
            
            return new FileUploadResponse(
                savedRecord.getId(),
                savedRecord.getOriginalName(),
                savedRecord.getFileName(),
                savedRecord.getFileUrl(),
                savedRecord.getFileSize(),
                savedRecord.getFileType(),
                savedRecord.getCategory(),
                savedRecord.getRelatedId()
            );
            
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage(), e);
            return FileUploadResponse.failure("文件上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("文件上传异常：{}", e.getMessage(), e);
            return FileUploadResponse.failure("文件上传异常：" + e.getMessage());
        }
    }
    
    @Override
    public List<FileUploadResponse> uploadFiles(MultipartFile[] files, String category, Long relatedId) {
        List<FileUploadResponse> responses = new ArrayList<>();
        
        if (files == null || files.length == 0) {
            responses.add(FileUploadResponse.failure("没有选择文件"));
            return responses;
        }
        
        for (MultipartFile file : files) {
            FileUploadResponse response = uploadFile(file, category, relatedId);
            responses.add(response);
        }
        
        return responses;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileRecord> getFilesByCategory(String category) {
        return fileRecordRepository.findActiveByCategoryQuery(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileRecord> getFilesByRelatedId(Long relatedId) {
        return fileRecordRepository.findActiveByRelatedIdQuery(relatedId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileRecord> getFilesByCategoryAndRelatedId(String category, Long relatedId) {
        return fileRecordRepository.findByCategoryAndRelatedId(category, relatedId);
    }
    
    @Override
    public boolean deleteFile(Long fileId) {
        try {
            Optional<FileRecord> fileRecordOpt = fileRecordRepository.findById(fileId);
            if (fileRecordOpt.isEmpty()) {
                log.warn("文件记录不存在：{}", fileId);
                return false;
            }
            
            FileRecord fileRecord = fileRecordOpt.get();
            
            // 删除物理文件
            try {
                File file = new File(fileRecord.getFilePath());
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        log.warn("物理文件删除失败：{}", fileRecord.getFilePath());
                    }
                }
            } catch (Exception e) {
                log.error("删除物理文件异常：{}", e.getMessage(), e);
            }
            
            // 软删除数据库记录
            fileRecord.setStatus(0);
            fileRecordRepository.save(fileRecord);
            
            log.info("文件删除成功：{}", fileRecord.getFileName());
            return true;
            
        } catch (Exception e) {
            log.error("文件删除异常：{}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public FileRecord getFileByFileName(String fileName) {
        return fileRecordRepository.findByFileName(fileName).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FileRecord> getAllFiles() {
        return fileRecordRepository.findAll();
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String timestamp = String.valueOf(System.currentTimeMillis());
        return uuid + "_" + timestamp + "." + extension;
    }
}