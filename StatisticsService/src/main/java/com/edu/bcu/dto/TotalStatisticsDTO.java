package com.edu.bcu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 总统计数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalStatisticsDTO {
    
    /**
     * 统计时间
     */
    private LocalDateTime statisticsTime;
    
    /**
     * 总用户数
     */
    private Long totalUsers;
    
    /**
     * VIP用户数
     */
    private Long vipUsers;
    
    /**
     * 总电影数
     */
    private Long totalMovies;
    
    /**
     * VIP电影数
     */
    private Long vipMovies;
    
    /**
     * 总观影次数
     */
    private Long totalViews;
    
    /**
     * 总观影时长（小时）
     */
    private Double totalViewHours;
    
    /**
     * 总收藏数
     */
    private Long totalFavorites;
    
    /**
     * 总评论数
     */
    private Long totalComments;
    
    /**
     * 总VIP订单数
     */
    private Long totalVipOrders;
    
    /**
     * 总VIP收入
     */
    private Double totalVipRevenue;
    
    /**
     * 总反馈数
     */
    private Long totalFeedback;
    
    /**
     * 平台评分（基于所有电影平均评分）
     */
    private Double platformRating;
    
    /**
     * 用户平均观影时长（分钟）
     */
    private Double avgUserViewTime;
    
    /**
     * VIP转化率
     */
    private Double vipConversionRate;
} 