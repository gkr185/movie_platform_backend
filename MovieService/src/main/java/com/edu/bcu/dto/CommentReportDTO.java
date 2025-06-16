package com.edu.bcu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 评论举报数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportDTO {
    /**
     * 举报原因
     */
    @NotBlank(message = "举报原因不能为空")
    @Size(max = 500, message = "举报原因不能超过500字符")
    private String reason;
    
    /**
     * 举报类型
     */
    private String type;
    
    /**
     * 举报人ID（可选，从token中获取）
     */
    private Integer reporterId;
} 