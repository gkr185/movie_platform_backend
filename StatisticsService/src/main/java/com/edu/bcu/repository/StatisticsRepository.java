package com.edu.bcu.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计数据访问层
 */
@Repository
public class StatisticsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取指定日期的新增用户数
     */
    public Long getNewUsersCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM user WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的活跃用户数（有行为的用户）
     */
    public Long getActiveUsersCount(LocalDate date) {
        String sql = "SELECT COUNT(DISTINCT user_id) FROM user_behavior WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的观影次数
     */
    public Long getTotalViewsCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM user_history WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的独立观影用户数
     */
    public Long getUniqueViewersCount(LocalDate date) {
        String sql = "SELECT COUNT(DISTINCT user_id) FROM user_history WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的平均观影时长（分钟）
     */
    public Double getAvgViewTime(LocalDate date) {
        String sql = "SELECT AVG(play_time/60.0) FROM user_history WHERE DATE(create_time) = ?";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, date);
        return result != null ? result : 0.0;
    }

    /**
     * 获取指定日期的新增收藏数
     */
    public Long getNewFavoritesCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM user_favorite WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的新增评论数
     */
    public Long getNewCommentsCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM comment WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的VIP订单数
     */
    public Long getVipOrdersCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM vip_order WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    /**
     * 获取指定日期的VIP订单收入
     */
    public Double getVipRevenue(LocalDate date) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM vip_order WHERE DATE(create_time) = ? AND status = 1";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, date);
        return result != null ? result.doubleValue() : 0.0;
    }

    /**
     * 获取指定日期的反馈数
     */
    public Long getFeedbackCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM feedback WHERE DATE(create_time) = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, date);
    }

    // ==================== 总统计方法 ====================

    /**
     * 获取总用户数
     */
    public Long getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM user";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取VIP用户数
     */
    public Long getVipUsers() {
        String sql = "SELECT COUNT(*) FROM user WHERE is_vip = 1";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取总电影数
     */
    public Long getTotalMovies() {
        String sql = "SELECT COUNT(*) FROM movie WHERE status = 1";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取VIP电影数
     */
    public Long getVipMovies() {
        String sql = "SELECT COUNT(*) FROM movie WHERE is_vip = 1 AND status = 1";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取总观影次数
     */
    public Long getTotalViews() {
        String sql = "SELECT COUNT(*) FROM user_history";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取总观影时长（小时）
     */
    public Double getTotalViewHours() {
        String sql = "SELECT COALESCE(SUM(play_time)/3600.0, 0) FROM user_history";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result != null ? result : 0.0;
    }

    /**
     * 获取总收藏数
     */
    public Long getTotalFavorites() {
        String sql = "SELECT COUNT(*) FROM user_favorite";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取总评论数
     */
    public Long getTotalComments() {
        String sql = "SELECT COUNT(*) FROM comment";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取总VIP订单数
     */
    public Long getTotalVipOrders() {
        String sql = "SELECT COUNT(*) FROM vip_order";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取总VIP收入
     */
    public Double getTotalVipRevenue() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM vip_order WHERE status = 1";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result.doubleValue() : 0.0;
    }

    /**
     * 获取总反馈数
     */
    public Long getTotalFeedback() {
        String sql = "SELECT COUNT(*) FROM feedback";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    /**
     * 获取平台平均评分
     */
    public Double getPlatformRating() {
        String sql = "SELECT AVG(rating) FROM movie WHERE rating IS NOT NULL AND rating > 0";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result != null ? result : 0.0;
    }

    /**
     * 获取用户平均观影时长（分钟）
     */
    public Double getAvgUserViewTime() {
        String sql = "SELECT AVG(play_time/60.0) FROM user_history WHERE play_time > 0";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result != null ? result : 0.0;
    }

    /**
     * 获取最近7天的日统计数据
     */
    public List<Map<String, Object>> getRecentDailyStats(int days) {
        String sql = """
            SELECT 
                DATE(create_time) as date,
                COUNT(DISTINCT user_id) as active_users,
                COUNT(*) as total_views
            FROM user_history 
            WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
            GROUP BY DATE(create_time)
            ORDER BY date DESC
            """;
        return jdbcTemplate.queryForList(sql, days);
    }

    /**
     * 获取热门电影排行（基于观影次数）
     */
    public List<Map<String, Object>> getPopularMovies(int limit) {
        String sql = """
            SELECT 
                m.id,
                m.title,
                COUNT(uh.id) as view_count,
                m.rating
            FROM movie m
            LEFT JOIN user_history uh ON m.id = uh.movie_id
            WHERE m.status = 1
            GROUP BY m.id, m.title, m.rating
            ORDER BY view_count DESC
            LIMIT ?
            """;
        return jdbcTemplate.queryForList(sql, limit);
    }

    /**
     * 获取用户行为分布统计
     */
    public List<Map<String, Object>> getUserBehaviorStats() {
        String sql = """
            SELECT 
                behavior_type,
                COUNT(*) as count,
                COUNT(DISTINCT user_id) as unique_users
            FROM user_behavior
            GROUP BY behavior_type
            ORDER BY behavior_type
            """;
        return jdbcTemplate.queryForList(sql);
    }
} 