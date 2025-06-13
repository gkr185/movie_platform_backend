package com.bcu.movie.service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<String> getNewsImages();
    List<String> getMovieImages();
    List<String> getAdvertisementImages();
    String uploadImage(MultipartFile file);
    String uploadImageToEntity(MultipartFile file, String entityType, Integer entityId);
}
