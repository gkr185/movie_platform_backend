package com.bcu.movie.Controller;


import com.bcu.movie.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @GetMapping("/news")
    public List<String> getNewsImages() {
        List<String> newsImages = imageService.getNewsImages();
        logger.info("Retrieved {} news images.", newsImages.size());
        return newsImages;
    }

    @GetMapping("/movie")
    public List<String> getMovieImages() {
        List<String> movieImages = imageService.getMovieImages();
        logger.info("Retrieved {} movie images.", movieImages.size());
        return movieImages;
    }

    @GetMapping("/advertisement")
    public List<String> getAdvertisementImages() {
        List<String> advertisementImages = imageService.getAdvertisementImages();
        logger.info("Retrieved {} advertisement images.", advertisementImages.size());
        return advertisementImages;
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        String filePath = imageService.uploadImage(file);
        if (filePath != null) {
            logger.info("文件上传成功，保存路径: {}", filePath);
            return filePath;
        } else {
            logger.error("文件上传失败");
            return "文件上传失败";
        }
    }

    @PostMapping("/uploadToEntity")
    public String uploadImageToEntity(@RequestParam("file") MultipartFile file,
                                      @RequestParam("entityType") String entityType,
                                      @RequestParam("entityId") Integer entityId) {
        logger.info("Received request to upload image to entity: type={}, id={}", entityType, entityId);
        String filePath = imageService.uploadImageToEntity(file, entityType, entityId);
        if (filePath != null) {
            logger.info("图片上传到 {} 实体（ID: {}）成功，保存路径: {}", entityType, entityId, filePath);
            return filePath;
        } else {
            logger.error("图片上传到 {} 实体（ID: {}）失败", entityType, entityId);
            return "图片上传失败";
        }
    }
}
