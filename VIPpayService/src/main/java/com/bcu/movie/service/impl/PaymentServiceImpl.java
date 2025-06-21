package com.bcu.movie.service.impl;

import com.bcu.movie.dto.*;
import com.bcu.movie.entity.Payment;
import com.bcu.movie.enums.OrderStatus;
import com.bcu.movie.enums.PaymentMethod;
import com.bcu.movie.enums.VipType;
import com.bcu.movie.repository.PaymentRepository;
import com.bcu.movie.repository.UserRepository;
import com.bcu.movie.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private UserRepository userRepository;

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
            
            // 保存订单状态
            paymentRepository.save(payment);
            
            // 更新用户VIP状态
            try {
                VipType vipType = VipType.getByCode(payment.getVipType());
                if (vipType != null) {
                    LocalDateTime vipExpireTime = LocalDateTime.now().plusDays(vipType.getDays());
                    LocalDateTime updateTime = LocalDateTime.now();
                    
                    // 直接更新数据库中的用户VIP状态
                    userRepository.updateVipStatus(payment.getUserId(), 1, vipExpireTime, updateTime);
                    System.out.println("用户VIP状态更新成功: 用户ID=" + payment.getUserId() + 
                                     ", VIP类型=" + vipType.getName() + 
                                     ", 过期时间=" + vipExpireTime);
                }
            } catch (Exception e) {
                // 记录日志，但不影响订单状态
                System.err.println("更新用户VIP状态失败: " + e.getMessage());
                // 在实际项目中应该使用日志框架
                // log.error("更新用户VIP状态失败, 订单号: {}, 用户ID: {}, 错误: {}", 
                //          orderId, payment.getUserId(), e.getMessage(), e);
            }
        } else {
            payment.setStatus(2); // 2: 支付失败
            paymentRepository.save(payment);
        }
    }

    private String generateOrderNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @Override
    public Page<OrderResponse> getOrders(OrderQueryRequest request) {
        Specification<Payment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (request.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), request.getUserId()));
            }
            if (request.getOrderNumber() != null) {
                predicates.add(criteriaBuilder.like(root.get("orderNumber"), "%" + request.getOrderNumber() + "%"));
            }
            if (request.getVipType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("vipType"), request.getVipType()));
            }
            if (request.getPaymentMethod() != null) {
                predicates.add(criteriaBuilder.equal(root.get("paymentMethod"), request.getPaymentMethod()));
            }
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getStartTime() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), request.getStartTime()));
            }
            if (request.getEndTime() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), request.getEndTime()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        Sort sort = Sort.by(request.getSortDirection().equalsIgnoreCase("ASC") ? 
                           Sort.Direction.ASC : Sort.Direction.DESC, request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        
        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);
        return paymentPage.map(this::convertToOrderResponse);
    }

    @Override
    public OrderResponse getOrderDetail(Integer orderId) {
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToOrderResponse(payment);
    }

    @Override
    public OrderResponse getOrderByNumber(String orderNumber) {
        Payment payment = paymentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToOrderResponse(payment);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Integer orderId) {
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if (payment.getStatus() != 0) {
            throw new RuntimeException("只能取消待支付订单");
        }
        
        payment.setStatus(2); // 2: 已取消
        Payment savedPayment = paymentRepository.save(payment);
        return convertToOrderResponse(savedPayment);
    }

    @Override
    public OrderStatisticsResponse getOrderStatistics() {
        OrderStatisticsResponse statistics = new OrderStatisticsResponse();
        
        // 基础统计
        statistics.setTotalOrders(paymentRepository.getTotalOrderCount());
        statistics.setPendingOrders(paymentRepository.getOrderCountByStatus(0));
        statistics.setPaidOrders(paymentRepository.getOrderCountByStatus(1));
        statistics.setCancelledOrders(paymentRepository.getOrderCountByStatus(2));
        
        // 金额统计
        BigDecimal totalAmount = paymentRepository.getTotalAmountByStatus(null);
        BigDecimal paidAmount = paymentRepository.getTotalAmountByStatus(1);
        statistics.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
        statistics.setPaidAmount(paidAmount != null ? paidAmount : BigDecimal.ZERO);
        
        // 支付成功率
        if (statistics.getTotalOrders() > 0) {
            statistics.setPaymentRate((double) statistics.getPaidOrders() / statistics.getTotalOrders());
        } else {
            statistics.setPaymentRate(0.0);
        }
        
        // 支付方式统计
        statistics.setWechatOrders(paymentRepository.getOrderCountByPaymentMethod(0));
        statistics.setAlipayOrders(paymentRepository.getOrderCountByPaymentMethod(1));
        statistics.setBankCardOrders(paymentRepository.getOrderCountByPaymentMethod(2));
        
        // VIP类型统计
        statistics.setMonthlyOrders(paymentRepository.getOrderCountByVipType(1));
        statistics.setQuarterlyOrders(paymentRepository.getOrderCountByVipType(2));
        statistics.setYearlyOrders(paymentRepository.getOrderCountByVipType(3));
        
        return statistics;
    }

    @Override
    @Transactional
    public void cancelExpiredOrders() {
        List<Payment> expiredOrders = paymentRepository.findExpiredOrders(LocalDateTime.now());
        for (Payment payment : expiredOrders) {
            payment.setStatus(2); // 2: 已取消
        }
        paymentRepository.saveAll(expiredOrders);
    }

    @Override
    public Page<OrderResponse> getUserOrders(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Payment> paymentPage = paymentRepository.findByUserId(userId, pageable);
        return paymentPage.map(this::convertToOrderResponse);
    }

    private OrderResponse convertToOrderResponse(Payment payment) {
        OrderResponse response = new OrderResponse();
        response.setId(payment.getId());
        response.setUserId(payment.getUserId());
        response.setOrderNumber(payment.getOrderNumber());
        response.setVipType(payment.getVipType());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setCreateTime(payment.getCreateTime());
        response.setPayTime(payment.getPayTime());
        response.setExpireTime(payment.getExpireTime());
        response.setQrCodeUrl(payment.getQrCodeUrl());
        
        // 设置枚举描述
        VipType vipType = VipType.getByCode(payment.getVipType());
        if (vipType != null) {
            response.setVipTypeName(vipType.getName());
        }
        
        PaymentMethod paymentMethod = PaymentMethod.getByCode(payment.getPaymentMethod());
        if (paymentMethod != null) {
            response.setPaymentMethodName(paymentMethod.getName());
        }
        
        OrderStatus orderStatus = OrderStatus.getByCode(payment.getStatus());
        if (orderStatus != null) {
            response.setStatusName(orderStatus.getName());
        }
        
        return response;
    }
} 