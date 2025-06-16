package com.edu.bcu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
@Data
public class FileUploadConfig implements WebMvcConfigurer {
    
    private String basePath = "E:/E/movie_platform/public/uploads/";
    private String baseUrl = "http://localhost:8068/uploads/";
    private long maxSize = 100 * 1024 * 1024; // 100MB
    
    // 允许的图片格式
    private String[] allowedImageTypes = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
    
    // 允许的视频格式
    private String[] allowedVideoTypes = {"mp4", "avi", "mov", "wmv", "flv", "mkv", "webm"};
    
    // 文件分类对应的目录
    private String avatarPath = "avatars/";
    private String posterPath = "posters/";
    private String videoPath = "videos/";
    private String bannerPath = "banners/";
    private String newsPath = "news/";
    private String adPath = "ads/";
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + basePath);
    }
    
    public String getCategoryPath(String category) {
        return switch (category.toLowerCase()) {
            case "avatar" -> avatarPath;
            case "poster" -> posterPath;
            case "video" -> videoPath;
            case "banner" -> bannerPath;
            case "news" -> newsPath;
            case "ad" -> adPath;
            default -> "others/";
        };
    }
    
    public boolean isImageFile(String extension) {
        for (String type : allowedImageTypes) {
            if (type.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isVideoFile(String extension) {
        for (String type : allowedVideoTypes) {
            if (type.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAllowedFile(String extension) {
        return isImageFile(extension) || isVideoFile(extension);
    }
} 