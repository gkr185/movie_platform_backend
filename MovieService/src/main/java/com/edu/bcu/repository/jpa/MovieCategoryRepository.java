package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
    List<MovieCategory> findByMovieId(Long movieId);
    List<MovieCategory> findByCategoryId(Integer categoryId);
}