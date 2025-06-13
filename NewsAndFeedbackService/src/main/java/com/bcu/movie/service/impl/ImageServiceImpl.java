package com.bcu.movie.service.impl;

import com.bcu.movie.entity.Advertisement;
import com.bcu.movie.entity.Movie;
import com.bcu.movie.entity.News;
import com.bcu.movie.repository.AdvertisementRepository;
import com.bcu.movie.repository.MovieRepository;
import com.bcu.movie.repository.NewsRepository;
import com.bcu.movie.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public List<String> getNewsImages() {
        // 实现获取新闻图片的逻辑
        return null;
    }

    @Override
    public List<String> getMovieImages() {
        // 实现获取电影图片的逻辑
        return null;
    }

    @Override
    public List<String> getAdvertisementImages() {
        // 实现获取广告图片的逻辑
        return null;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        // 实现通用的图片上传逻辑
        return null;
    }

    @Override
    @Transactional
    public String uploadImageToEntity(MultipartFile file, String entityType, Integer entityId) {
        String filePath = saveImage(file);
        if (filePath != null) {
            switch (entityType) {
                case "news":
                    News news = newsRepository.findById(entityId).orElse(null);
                    if (news != null) {
                        news.setCover_image(filePath);
                        newsRepository.save(news);
                    } else {
                        logger.error("未找到新闻实体，ID: {}", entityId);
                        return null;
                    }
                    break;
                case "movie":
                    Movie movie = movieRepository.findById(entityId).orElse(null);
                    if (movie != null) {
                        movie.setPoster_url(filePath);
                        movieRepository.save(movie);
                    } else {
                        logger.error("未找到电影实体，ID: {}", entityId);
                        return null;
                    }
                    break;
                case "advertisement":
                    Advertisement advertisement = advertisementRepository.findById(entityId).orElse(null);
                    if (advertisement != null) {
                        advertisement.setImageUrl(filePath);
                        advertisementRepository.save(advertisement);
                    } else {
                        logger.error("未找到广告实体，ID: {}", entityId);
                        return null;
                    }
                    break;
                default:
                    return null;
            }
            return filePath;
        }
        return null;
    }


    private String saveImage(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + extension;
                // 指定上传目录
                Path uploadDir = Paths.get("uploads");
                // 检查目录是否存在，不存在则创建
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path filePath = uploadDir.resolve(newFilename);
                file.transferTo(filePath);
                return filePath.toString();
            } catch (IOException e) {
                logger.error("保存图片时出错: ", e);
            }
        }
        return null;
    }
}