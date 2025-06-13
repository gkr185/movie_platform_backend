package com.bcu.movie.controller;

import com.bcu.movie.dto.PaymentRequest;
import com.bcu.movie.entity.Payment;
import com.bcu.movie.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
} 