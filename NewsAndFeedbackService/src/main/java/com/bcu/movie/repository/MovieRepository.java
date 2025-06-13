package com.bcu.movie.repository;

import com.bcu.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

// 电影仓库接口
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    // 可以添加自定义的查询方法
}