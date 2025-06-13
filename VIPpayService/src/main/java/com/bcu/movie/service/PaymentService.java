package com.bcu.movie.service;

import com.bcu.movie.dto.PaymentRequest;
import com.bcu.movie.entity.Payment;

public interface PaymentService {
    Payment generatePayment(PaymentRequest request);
    String generateQRCode(Payment payment);
    Payment checkPaymentStatus(String orderId);
    void handlePaymentCallback(String orderId, String status);
} 