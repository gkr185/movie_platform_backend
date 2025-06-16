package com.bcu.movie.config;

import com.bcu.movie.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private PaymentService paymentService;

    /**
     * 每10分钟执行一次，自动取消过期订单
     */
    @Scheduled(fixedRate = 600000) // 10分钟 = 600000毫秒
    public void cancelExpiredOrders() {
        try {
            log.info("开始执行过期订单取消任务");
            paymentService.cancelExpiredOrders();
            log.info("过期订单取消任务执行完成");
        } catch (Exception e) {
            log.error("执行过期订单取消任务时发生错误", e);
        }
    }
} 