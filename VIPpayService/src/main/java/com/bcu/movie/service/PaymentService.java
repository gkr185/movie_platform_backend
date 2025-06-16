package com.bcu.movie.service;

import com.bcu.movie.dto.*;
import com.bcu.movie.entity.Payment;
import org.springframework.data.domain.Page;

public interface PaymentService {
    Payment generatePayment(PaymentRequest request);
    String generateQRCode(Payment payment);
    Payment checkPaymentStatus(String orderId);
    void handlePaymentCallback(String orderId, String status);
    
    // 订单管理功能
    Page<OrderResponse> getOrders(OrderQueryRequest request);
    OrderResponse getOrderDetail(Integer orderId);
    OrderResponse getOrderByNumber(String orderNumber);
    OrderResponse cancelOrder(Integer orderId);
    OrderStatisticsResponse getOrderStatistics();
    
    // 批量操作
    void cancelExpiredOrders();
    
    // 用户订单查询
    Page<OrderResponse> getUserOrders(Integer userId, int page, int size);
} 