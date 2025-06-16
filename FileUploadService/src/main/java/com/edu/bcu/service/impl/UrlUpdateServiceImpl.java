package com.edu.bcu.service.impl;

import com.edu.bcu.service.UrlUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UrlUpdateServiceImpl implements UrlUpdateService {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public boolean updateUserAvatar(Long userId, String avatarUrl) {
        try {
            String sql = "UPDATE user SET avatar = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, avatarUrl, userId);
            log.info("更新用户头像URL成功：userId={}, avatarUrl={}", userId, avatarUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新用户头像URL失败：userId={}, error={}", userId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateMoviePoster(Long movieId, String posterUrl) {
        try {
            String sql = "UPDATE movie SET poster_url = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, posterUrl, movieId);
            log.info("更新电影海报URL成功：movieId={}, posterUrl={}", movieId, posterUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新电影海报URL失败：movieId={}, error={}", movieId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateMoviePlayUrl(Long movieId, String playUrl) {
        try {
            String sql = "UPDATE movie SET play_url = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, playUrl, movieId);
            log.info("更新电影播放URL成功：movieId={}, playUrl={}", movieId, playUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新电影播放URL失败：movieId={}, error={}", movieId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateMovieTrailerUrl(Long movieId, String trailerUrl) {
        try {
            String sql = "UPDATE movie SET trailer_url = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, trailerUrl, movieId);
            log.info("更新电影预告片URL成功：movieId={}, trailerUrl={}", movieId, trailerUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新电影预告片URL失败：movieId={}, error={}", movieId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateNewsCoverImage(Long newsId, String coverImageUrl) {
        try {
            String sql = "UPDATE news SET cover_image = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, coverImageUrl, newsId);
            log.info("更新新闻封面图片URL成功：newsId={}, coverImageUrl={}", newsId, coverImageUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新新闻封面图片URL失败：newsId={}, error={}", newsId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateAdImageUrl(Long adId, String imageUrl) {
        try {
            String sql = "UPDATE advertisement SET image_url = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, imageUrl, adId);
            log.info("更新广告图片URL成功：adId={}, imageUrl={}", adId, imageUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新广告图片URL失败：adId={}, error={}", adId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateAdVideoUrl(Long adId, String videoUrl) {
        try {
            String sql = "UPDATE advertisement SET video_url = ?, update_time = NOW() WHERE id = ?";
            int rows = jdbcTemplate.update(sql, videoUrl, adId);
            log.info("更新广告视频URL成功：adId={}, videoUrl={}", adId, videoUrl);
            return rows > 0;
        } catch (Exception e) {
            log.error("更新广告视频URL失败：adId={}, error={}", adId, e.getMessage(), e);
            return false;
        }
    }
} 