package com.bcu.movie.service.impl;

import com.bcu.movie.dto.PaymentRequest;
import com.bcu.movie.entity.Payment;
import com.bcu.movie.repository.PaymentRepository;
import com.bcu.movie.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment generatePayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setOrderNumber(generateOrderNumber());
        payment.setVipType(request.getPlanId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(0); // 0: 待支付
        payment.setCreateTime(LocalDateTime.now());
        payment.setExpireTime(LocalDateTime.now().plusMinutes(30)); // 30分钟过期
        
        return paymentRepository.save(payment);
    }

    @Override
    public String generateQRCode(Payment payment) {
        // 这里模拟生成二维码，实际项目中需要对接真实的支付平台
        return "https://example.com/pay/" + payment.getOrderNumber();
    }

    @Override
    public Payment checkPaymentStatus(String orderId) {
        return paymentRepository.findByOrderNumber(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    @Transactional
    public void handlePaymentCallback(String orderId, String status) {
        Payment payment = paymentRepository.findByOrderNumber(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if ("SUCCESS".equals(status)) {
            payment.setStatus(1); // 1: 支付成功
            payment.setPayTime(LocalDateTime.now());
        } else {
            payment.setStatus(2); // 2: 支付失败
        }
        
        paymentRepository.save(payment);
    }

    private String generateOrderNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
} 