package com.bcu.edu.service;

import com.bcu.edu.entity.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AdvertisementService {
    // 创建广告
    Advertisement createAdvertisement(Advertisement advertisement);
    
    // 更新广告
    Advertisement updateAdvertisement(Integer id, Advertisement advertisement);
    
    // 删除广告
    void deleteAdvertisement(Integer id);
    
    // 获取广告列表（分页）
    Page<Advertisement> getAllAdvertisements(Pageable pageable);
    
    // 获取所有图片广告
    List<Advertisement> getAllImageAdvertisements();
    
    // 随机获取一个视频广告
    Advertisement getRandomVideoAdvertisement();
} 