package com.bcu.movie.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderQueryRequest {
    private Integer userId;              // 用户ID
    private String orderNumber;          // 订单号
    private Integer vipType;             // VIP类型
    private Integer paymentMethod;       // 支付方式
    private Integer status;              // 订单状态
    private LocalDateTime startTime;     // 开始时间
    private LocalDateTime endTime;       // 结束时间
    private Integer page = 0;            // 页码，默认从0开始
    private Integer size = 10;           // 每页大小，默认10条
    private String sortBy = "createTime"; // 排序字段
    private String sortDirection = "DESC"; // 排序方向
} 