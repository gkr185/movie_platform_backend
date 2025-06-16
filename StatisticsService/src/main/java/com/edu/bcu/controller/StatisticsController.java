package com.edu.bcu.controller;

import com.edu.bcu.dto.DailyStatisticsDTO;
import com.edu.bcu.dto.TotalStatisticsDTO;
import com.edu.bcu.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计数据控制器
 */
@RestController
@RequestMapping("/api/statistics")
@Tag(name = "统计管理", description = "平台统计数据管理接口")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取今日统计数据
     */
    @GetMapping("/daily/today")
    @Operation(summary = "获取今日统计数据", description = "获取当前日期的统计数据")
    public ResponseEntity<DailyStatisticsDTO> getTodayStatistics() {
        try {
            logger.info("请求获取今日统计数据");
            DailyStatisticsDTO stats = statisticsService.getTodayStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取今日统计数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取昨日统计数据
     */
    @GetMapping("/daily/yesterday")
    @Operation(summary = "获取昨日统计数据", description = "获取昨天的统计数据")
    public ResponseEntity<DailyStatisticsDTO> getYesterdayStatistics() {
        try {
            logger.info("请求获取昨日统计数据");
            DailyStatisticsDTO stats = statisticsService.getYesterdayStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取昨日统计数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取指定日期的统计数据
     */
    @GetMapping("/daily/{date}")
    @Operation(summary = "获取指定日期统计数据", description = "获取指定日期的统计数据")
    public ResponseEntity<DailyStatisticsDTO> getDailyStatistics(
            @Parameter(description = "日期，格式：yyyy-MM-dd", example = "2024-01-15")
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            logger.info("请求获取日期 {} 的统计数据", date);
            DailyStatisticsDTO stats = statisticsService.getDailyStatistics(date);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取日期 {} 的统计数据失败", date, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取总统计数据
     */
    @GetMapping("/total")
    @Operation(summary = "获取总统计数据", description = "获取平台的总体统计数据")
    public ResponseEntity<TotalStatisticsDTO> getTotalStatistics() {
        try {
            logger.info("请求获取总统计数据");
            TotalStatisticsDTO stats = statisticsService.getTotalStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取总统计数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取统计概览数据
     */
    @GetMapping("/overview")
    @Operation(summary = "获取统计概览", description = "获取包含今日、昨日和总体的统计概览数据")
    public ResponseEntity<Map<String, Object>> getStatisticsOverview() {
        try {
            logger.info("请求获取统计概览数据");
            Map<String, Object> overview = statisticsService.getStatisticsOverview();
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            logger.error("获取统计概览数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取最近几天的统计数据
     */
    @GetMapping("/daily/recent")
    @Operation(summary = "获取最近几天统计数据", description = "获取最近指定天数的统计数据")
    public ResponseEntity<List<Map<String, Object>>> getRecentDailyStats(
            @Parameter(description = "天数", example = "7")
            @RequestParam(defaultValue = "7") int days) {
        try {
            logger.info("请求获取最近 {} 天的统计数据", days);
            List<Map<String, Object>> stats = statisticsService.getRecentDailyStats(days);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取最近 {} 天的统计数据失败", days, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取热门电影排行
     */
    @GetMapping("/movies/popular")
    @Operation(summary = "获取热门电影排行", description = "获取基于观影次数的热门电影排行榜")
    public ResponseEntity<List<Map<String, Object>>> getPopularMovies(
            @Parameter(description = "返回数量限制", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        try {
            logger.info("请求获取热门电影排行，限制 {} 条", limit);
            List<Map<String, Object>> movies = statisticsService.getPopularMovies(limit);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            logger.error("获取热门电影排行失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取用户行为分布统计
     */
    @GetMapping("/behavior")
    @Operation(summary = "获取用户行为统计", description = "获取用户行为类型分布统计")
    public ResponseEntity<List<Map<String, Object>>> getUserBehaviorStats() {
        try {
            logger.info("请求获取用户行为分布统计");
            List<Map<String, Object>> stats = statisticsService.getUserBehaviorStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取用户行为分布统计失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 手动刷新统计数据
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新统计数据", description = "手动触发统计数据刷新")
    public ResponseEntity<Map<String, Object>> refreshStatistics() {
        try {
            logger.info("请求手动刷新统计数据");
            String result = statisticsService.refreshStatistics();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "统计数据刷新成功",
                "details", result,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            logger.error("手动刷新统计数据失败", e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "统计数据刷新失败: " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查统计服务是否正常运行")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            // 简单的健康检查：尝试获取总用户数
            TotalStatisticsDTO stats = statisticsService.getTotalStatistics();
            return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "StatisticsService",
                "timestamp", System.currentTimeMillis(),
                "totalUsers", stats.getTotalUsers()
            ));
        } catch (Exception e) {
            logger.error("健康检查失败", e);
            return ResponseEntity.ok(Map.of(
                "status", "DOWN",
                "service", "StatisticsService",
                "timestamp", System.currentTimeMillis(),
                "error", e.getMessage()
            ));
        }
    }
} 