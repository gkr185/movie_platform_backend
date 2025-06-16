package com.edu.bcu.service;

public interface UrlUpdateService {
    
    /**
     * 更新用户头像URL
     */
    boolean updateUserAvatar(Long userId, String avatarUrl);
    
    /**
     * 更新电影海报URL
     */
    boolean updateMoviePoster(Long movieId, String posterUrl);
    
    /**
     * 更新电影播放URL
     */
    boolean updateMoviePlayUrl(Long movieId, String playUrl);
    
    /**
     * 更新电影预告片URL
     */
    boolean updateMovieTrailerUrl(Long movieId, String trailerUrl);
    
    /**
     * 更新新闻封面图片URL
     */
    boolean updateNewsCoverImage(Long newsId, String coverImageUrl);
    
    /**
     * 更新广告图片URL
     */
    boolean updateAdImageUrl(Long adId, String imageUrl);
    
    /**
     * 更新广告视频URL
     */
    boolean updateAdVideoUrl(Long adId, String videoUrl);
} 