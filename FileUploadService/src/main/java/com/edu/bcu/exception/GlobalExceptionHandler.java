package com.edu.bcu.exception;

import com.edu.bcu.dto.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<FileUploadResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        log.error("文件大小超过限制：{}", e.getMessage());
        FileUploadResponse response = FileUploadResponse.failure("文件大小超过限制，最大支持100MB");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
    
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<FileUploadResponse> handleMultipartException(MultipartException e) {
        log.error("文件上传异常：{}", e.getMessage());
        FileUploadResponse response = FileUploadResponse.failure("文件上传异常：" + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FileUploadResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常：{}", e.getMessage());
        FileUploadResponse response = FileUploadResponse.failure("参数错误：" + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FileUploadResponse> handleGenericException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        FileUploadResponse response = FileUploadResponse.failure("系统异常，请稍后重试");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 