package com.bcu.movie.controller;

import com.bcu.movie.dto.*;
import com.bcu.movie.entity.Payment;
import com.bcu.movie.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/generate-qrcode")
    public ResponseEntity<?> generateQRCode(@RequestBody PaymentRequest request) {
        // 参数验证
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest().body("用户ID不能为空");
        }
        if (request.getPlanId() == null) {
            return ResponseEntity.badRequest().body("套餐ID不能为空");
        }
        if (request.getAmount() == null) {
            return ResponseEntity.badRequest().body("支付金额不能为空");
        }
        if (request.getPaymentMethod() == null) {
            return ResponseEntity.badRequest().body("支付方式不能为空");
        }

        Payment payment = paymentService.generatePayment(request);
        String qrCodeUrl = paymentService.generateQRCode(payment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", payment.getOrderNumber());
        response.put("qrCodeUrl", qrCodeUrl);
        response.put("amount", payment.getAmount());
        response.put("expireTime", payment.getExpireTime());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<?> checkPaymentStatus(@PathVariable String orderId) {
        Payment payment = paymentService.checkPaymentStatus(orderId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", payment.getOrderNumber());
        response.put("status", payment.getStatus());
        response.put("amount", payment.getAmount());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<?> handlePaymentCallback(@RequestBody Map<String, String> request) {
        String orderId = request.get("orderId");
        String status = request.get("status");
        
        if (orderId == null || status == null) {
            return ResponseEntity.badRequest().body("订单号和支付状态不能为空");
        }
        
        paymentService.handlePaymentCallback(orderId, status);
        
        return ResponseEntity.ok().build();
    }

    // ==================== 订单管理相关API ====================

    /**
     * 分页查询订单列表
     */
    @PostMapping("/orders/search")
    public ResponseEntity<Page<OrderResponse>> getOrders(@RequestBody OrderQueryRequest request) {
        Page<OrderResponse> orders = paymentService.getOrders(request);
        return ResponseEntity.ok(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable Integer orderId) {
        OrderResponse order = paymentService.getOrderDetail(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * 根据订单号获取订单详情
     */
    @GetMapping("/orders/by-number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
        OrderResponse order = paymentService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }

    /**
     * 取消订单
     */
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Integer orderId) {
        OrderResponse order = paymentService.cancelOrder(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * 获取订单统计数据
     */
    @GetMapping("/orders/statistics")
    public ResponseEntity<OrderStatisticsResponse> getOrderStatistics() {
        OrderStatisticsResponse statistics = paymentService.getOrderStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取用户订单列表
     */
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> orders = paymentService.getUserOrders(userId, page, size);
        return ResponseEntity.ok(orders);
    }

    /**
     * 手动触发过期订单取消
     */
    @PostMapping("/orders/cancel-expired")
    public ResponseEntity<String> cancelExpiredOrders() {
        paymentService.cancelExpiredOrders();
        return ResponseEntity.ok("过期订单处理完成");
    }
} 