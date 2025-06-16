package com.edu.bcu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 评论审核数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentAuditDTO {
    /**
     * 审核状态
     */
    @NotBlank(message = "审核状态不能为空")
    @Pattern(regexp = "approved|rejected", message = "审核状态只能是approved或rejected")
    private String status;
    
    /**
     * 审核备注
     */
    private String remark;
    
    /**
     * 审核人ID（从token中获取）
     */
    private Integer auditorId;
} 