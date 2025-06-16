package com.edu.bcu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 日统计数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatisticsDTO {
    
    /**
     * 统计日期
     */
    private LocalDate date;
    
    /**
     * 新增用户数
     */
    private Long newUsers;
    
    /**
     * 活跃用户数（当日有行为的用户）
     */
    private Long activeUsers;
    
    /**
     * 总观影次数
     */
    private Long totalViews;
    
    /**
     * 独立观影用户数
     */
    private Long uniqueViewers;
    
    /**
     * 平均观影时长（分钟）
     */
    private Double avgViewTime;
    
    /**
     * 新增收藏数
     */
    private Long newFavorites;
    
    /**
     * 新增评论数
     */
    private Long newComments;
    
    /**
     * VIP订单数
     */
    private Long vipOrders;
    
    /**
     * VIP订单收入
     */
    private Double vipRevenue;
    
    /**
     * 用户反馈数
     */
    private Long feedbackCount;
} 