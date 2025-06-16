package com.bcu.movie.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Integer id;
    private Integer userId;
    private String orderNumber;
    private Integer vipType;
    private String vipTypeName;
    private BigDecimal amount;
    private Integer paymentMethod;
    private String paymentMethodName;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime expireTime;
    private String qrCodeUrl;
} 