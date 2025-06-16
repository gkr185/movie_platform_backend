package com.edu.bcu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "original_name", nullable = false)
    private String originalName;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_url", nullable = false)
    private String fileUrl;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(name = "file_type", nullable = false)
    private String fileType;
    
    @Column(name = "category", nullable = false)
    private String category; // avatar, poster, video, banner, news, ad
    
    @Column(name = "related_id")
    private Long relatedId; // 关联的业务ID，如用户ID、电影ID等
    
    @Column(name = "status")
    private Integer status = 1; // 1-有效 0-删除
    
    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 