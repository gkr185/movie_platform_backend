package com.bcu.movie.repository;

import com.bcu.movie.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 资讯分类仓库接口
@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Integer> {
    List<NewsCategory> findByStatusOrderBySortOrderAsc(Integer status);
}