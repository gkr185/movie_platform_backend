package com.bcu.movie.repository;

import com.bcu.movie.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByOrderNumber(String orderNumber);
    
    // 根据用户ID查询订单
    Page<Payment> findByUserId(Integer userId, Pageable pageable);
    
    // 根据状态查询订单
    Page<Payment> findByStatus(Integer status, Pageable pageable);
    
    // 根据用户ID和状态查询订单
    List<Payment> findByUserIdAndStatus(Integer userId, Integer status);
    
    // 统计查询
    @Query("SELECT COUNT(p) FROM Payment p")
    long getTotalOrderCount();
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    long getOrderCountByStatus(@Param("status") Integer status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE (:status IS NULL OR p.status = :status)")
    BigDecimal getTotalAmountByStatus(@Param("status") Integer status);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.vipType = :vipType")
    long getOrderCountByVipType(@Param("vipType") Integer vipType);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :paymentMethod")
    long getOrderCountByPaymentMethod(@Param("paymentMethod") Integer paymentMethod);
    
    // 时间范围查询
    @Query("SELECT p FROM Payment p WHERE p.createTime BETWEEN :startTime AND :endTime")
    List<Payment> findByCreateTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    // 过期订单查询
    @Query("SELECT p FROM Payment p WHERE p.status = 0 AND p.expireTime < :currentTime")
    List<Payment> findExpiredOrders(@Param("currentTime") LocalDateTime currentTime);
} 