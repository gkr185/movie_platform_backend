package com.bcu.movie.repository;

import com.bcu.movie.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

// 广告仓库接口
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    // 可以添加自定义的查询方法
}
