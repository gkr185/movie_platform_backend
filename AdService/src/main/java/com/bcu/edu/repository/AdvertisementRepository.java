package com.bcu.edu.repository;

import com.bcu.edu.entity.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    
    // 获取所有图片广告
    @Query("SELECT a FROM Advertisement a WHERE a.imageUrl IS NOT NULL AND a.imageUrl != ''")
    List<Advertisement> findAllImageAds();
    
    // 获取所有视频广告
    @Query("SELECT a FROM Advertisement a WHERE a.videoUrl IS NOT NULL AND a.videoUrl != ''")
    List<Advertisement> findAllVideoAds();
    
    // 分页查询所有广告
    Page<Advertisement> findAll(Pageable pageable);
} 