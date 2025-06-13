package com.bcu.movie.repository;

import com.bcu.movie.entity.News;
import com.bcu.movie.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 资讯仓库接口
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByCategory(NewsCategory category);
    List<News> findByTitleContainingOrContentContaining(String title, String content);
}
