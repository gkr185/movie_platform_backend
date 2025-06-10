package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieJpaRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m WHERE m.genres LIKE %:category%")
    List<Movie> findByCategory(String category);

    List<Movie> findByIsHotOrderByRatingDesc(Integer isHot);

    List<Movie> findByIsRecommendedOrderByCreateTimeDesc(Integer isRecommended);

    @Query("SELECT m FROM Movie m ORDER BY m.createTime DESC")
    List<Movie> findByCreatTime();

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword% OR m.description LIKE %:keyword% OR m.director LIKE %:keyword% OR m.actors LIKE %:keyword%")
    List<Movie> searchByKeyword(String keyword);
}