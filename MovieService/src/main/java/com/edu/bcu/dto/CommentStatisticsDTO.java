package com.edu.bcu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论统计数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentStatisticsDTO {
    /**
     * 总评论数
     */
    private Long total;
    
    /**
     * 待审核评论数
     */
    private Long pending;
    
    /**
     * 已通过评论数
     */
    private Long approved;
    
    /**
     * 已拒绝评论数
     */
    private Long rejected;
} 