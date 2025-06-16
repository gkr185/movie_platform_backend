package com.bcu.movie.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderStatisticsResponse {
    private Long totalOrders;         // 总订单数
    private Long pendingOrders;       // 待支付订单数
    private Long paidOrders;          // 已支付订单数
    private Long cancelledOrders;     // 已取消订单数
    private BigDecimal totalAmount;   // 总金额
    private BigDecimal paidAmount;    // 已支付金额
    private Double paymentRate;       // 支付成功率
    
    // 各支付方式统计
    private Long wechatOrders;        // 微信支付订单数
    private Long alipayOrders;        // 支付宝订单数
    private Long bankCardOrders;      // 银行卡订单数
    
    // 各VIP类型统计
    private Long monthlyOrders;       // 月度会员订单数
    private Long quarterlyOrders;     // 季度会员订单数
    private Long yearlyOrders;        // 年度会员订单数
} 