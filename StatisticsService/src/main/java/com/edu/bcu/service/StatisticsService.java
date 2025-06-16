package com.edu.bcu.service;

import com.edu.bcu.dto.DailyStatisticsDTO;
import com.edu.bcu.dto.TotalStatisticsDTO;
import com.edu.bcu.repository.StatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 统计服务层
 */
@Service
public class StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private StatisticsRepository statisticsRepository;

    /**
     * 获取指定日期的统计数据
     */
    public DailyStatisticsDTO getDailyStatistics(LocalDate date) {
        logger.info("获取日期 {} 的统计数据", date);
        
        try {
            DailyStatisticsDTO stats = new DailyStatisticsDTO();
            stats.setDate(date);
            
            // 并行获取各项统计数据
            stats.setNewUsers(statisticsRepository.getNewUsersCount(date));
            stats.setActiveUsers(statisticsRepository.getActiveUsersCount(date));
            stats.setTotalViews(statisticsRepository.getTotalViewsCount(date));
            stats.setUniqueViewers(statisticsRepository.getUniqueViewersCount(date));
            stats.setAvgViewTime(statisticsRepository.getAvgViewTime(date));
            stats.setNewFavorites(statisticsRepository.getNewFavoritesCount(date));
            stats.setNewComments(statisticsRepository.getNewCommentsCount(date));
            stats.setVipOrders(statisticsRepository.getVipOrdersCount(date));
            stats.setVipRevenue(statisticsRepository.getVipRevenue(date));
            stats.setFeedbackCount(statisticsRepository.getFeedbackCount(date));
            
            logger.info("成功获取日期 {} 的统计数据: 新增用户={}, 活跃用户={}, 观影次数={}", 
                       date, stats.getNewUsers(), stats.getActiveUsers(), stats.getTotalViews());
            
            return stats;
        } catch (Exception e) {
            logger.error("获取日期 {} 的统计数据失败", date, e);
            throw new RuntimeException("获取日统计数据失败", e);
        }
    }

    /**
     * 获取今日统计数据
     */
    public DailyStatisticsDTO getTodayStatistics() {
        return getDailyStatistics(LocalDate.now());
    }

    /**
     * 获取昨日统计数据
     */
    public DailyStatisticsDTO getYesterdayStatistics() {
        return getDailyStatistics(LocalDate.now().minusDays(1));
    }

    /**
     * 获取总统计数据
     */
    public TotalStatisticsDTO getTotalStatistics() {
        logger.info("获取总统计数据");
        
        try {
            TotalStatisticsDTO stats = new TotalStatisticsDTO();
            stats.setStatisticsTime(LocalDateTime.now());
            
            // 获取各项总统计数据
            stats.setTotalUsers(statisticsRepository.getTotalUsers());
            stats.setVipUsers(statisticsRepository.getVipUsers());
            stats.setTotalMovies(statisticsRepository.getTotalMovies());
            stats.setVipMovies(statisticsRepository.getVipMovies());
            stats.setTotalViews(statisticsRepository.getTotalViews());
            stats.setTotalViewHours(statisticsRepository.getTotalViewHours());
            stats.setTotalFavorites(statisticsRepository.getTotalFavorites());
            stats.setTotalComments(statisticsRepository.getTotalComments());
            stats.setTotalVipOrders(statisticsRepository.getTotalVipOrders());
            stats.setTotalVipRevenue(statisticsRepository.getTotalVipRevenue());
            stats.setTotalFeedback(statisticsRepository.getTotalFeedback());
            stats.setPlatformRating(statisticsRepository.getPlatformRating());
            stats.setAvgUserViewTime(statisticsRepository.getAvgUserViewTime());
            
            // 计算VIP转化率
            Long totalUsers = stats.getTotalUsers();
            Long vipUsers = stats.getVipUsers();
            if (totalUsers > 0) {
                stats.setVipConversionRate((vipUsers.doubleValue() / totalUsers.doubleValue()) * 100);
            } else {
                stats.setVipConversionRate(0.0);
            }
            
            logger.info("成功获取总统计数据: 总用户={}, VIP用户={}, 总电影={}, VIP转化率={}%", 
                       stats.getTotalUsers(), stats.getVipUsers(), stats.getTotalMovies(), 
                       String.format("%.2f", stats.getVipConversionRate()));
            
            return stats;
        } catch (Exception e) {
            logger.error("获取总统计数据失败", e);
            throw new RuntimeException("获取总统计数据失败", e);
        }
    }

    /**
     * 获取最近几天的统计数据
     */
    public List<Map<String, Object>> getRecentDailyStats(int days) {
        logger.info("获取最近 {} 天的统计数据", days);
        
        try {
            List<Map<String, Object>> stats = statisticsRepository.getRecentDailyStats(days);
            logger.info("成功获取最近 {} 天的统计数据，共 {} 条记录", days, stats.size());
            return stats;
        } catch (Exception e) {
            logger.error("获取最近 {} 天的统计数据失败", days, e);
            throw new RuntimeException("获取最近统计数据失败", e);
        }
    }

    /**
     * 获取热门电影排行
     */
    public List<Map<String, Object>> getPopularMovies(int limit) {
        logger.info("获取热门电影排行，限制 {} 条", limit);
        
        try {
            List<Map<String, Object>> movies = statisticsRepository.getPopularMovies(limit);
            logger.info("成功获取热门电影排行，共 {} 部电影", movies.size());
            return movies;
        } catch (Exception e) {
            logger.error("获取热门电影排行失败", e);
            throw new RuntimeException("获取热门电影排行失败", e);
        }
    }

    /**
     * 获取用户行为分布统计
     */
    public List<Map<String, Object>> getUserBehaviorStats() {
        logger.info("获取用户行为分布统计");
        
        try {
            List<Map<String, Object>> stats = statisticsRepository.getUserBehaviorStats();
            logger.info("成功获取用户行为分布统计，共 {} 种行为类型", stats.size());
            return stats;
        } catch (Exception e) {
            logger.error("获取用户行为分布统计失败", e);
            throw new RuntimeException("获取用户行为分布统计失败", e);
        }
    }

    /**
     * 刷新统计数据（手动触发）
     */
    public String refreshStatistics() {
        logger.info("开始手动刷新统计数据");
        
        try {
            // 获取今日和总统计数据以验证数据连接
            DailyStatisticsDTO todayStats = getTodayStatistics();
            TotalStatisticsDTO totalStats = getTotalStatistics();
            
            String result = String.format(
                "统计数据刷新成功！\n" +
                "今日统计: 新增用户=%d, 活跃用户=%d, 观影次数=%d\n" +
                "总统计: 总用户=%d, VIP用户=%d, 总电影=%d, VIP转化率=%.2f%%",
                todayStats.getNewUsers(), todayStats.getActiveUsers(), todayStats.getTotalViews(),
                totalStats.getTotalUsers(), totalStats.getVipUsers(), totalStats.getTotalMovies(),
                totalStats.getVipConversionRate()
            );
            
            logger.info("统计数据刷新成功: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("刷新统计数据失败", e);
            throw new RuntimeException("刷新统计数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取统计概览数据
     */
    public Map<String, Object> getStatisticsOverview() {
        logger.info("获取统计概览数据");
        
        try {
            DailyStatisticsDTO todayStats = getTodayStatistics();
            DailyStatisticsDTO yesterdayStats = getYesterdayStatistics();
            TotalStatisticsDTO totalStats = getTotalStatistics();
            
            return Map.of(
                "today", todayStats,
                "yesterday", yesterdayStats,
                "total", totalStats,
                "refreshTime", LocalDateTime.now()
            );
        } catch (Exception e) {
            logger.error("获取统计概览数据失败", e);
            throw new RuntimeException("获取统计概览数据失败", e);
        }
    }
} 