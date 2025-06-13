package com.bcu.movie.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Integer userId;
    private Integer planId;
    private Integer paymentMethod;
    private BigDecimal amount;
} 