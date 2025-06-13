package com.bcu.movie.repository;

import com.bcu.movie.entity.NewsSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

// 资讯订阅用户仓库接口
public interface NewsSubscriberRepository extends JpaRepository<NewsSubscriber, Integer> {
}