package com.edu.bcu.repository;

import com.edu.bcu.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
    
    /**
     * 根据电影ID查找所有分类关联
     */
    List<MovieCategory> findByMovieId(Integer movieId);
    
    /**
     * 根据电影ID查找所有分类关联（包含分类信息）
     */
    @Query("SELECT mc FROM MovieCategory mc JOIN FETCH mc.category WHERE mc.movieId = :movieId")
    List<MovieCategory> findByMovieIdWithCategory(@Param("movieId") Integer movieId);
    
    /**
     * 根据分类ID查找所有电影关联
     */
    List<MovieCategory> findByCategoryId(Integer categoryId);
    
    /**
     * 查找指定电影和分类的关联关系
     */
    Optional<MovieCategory> findByMovieIdAndCategoryId(Integer movieId, Integer categoryId);
    
    /**
     * 检查电影和分类关联是否存在
     */
    boolean existsByMovieIdAndCategoryId(Integer movieId, Integer categoryId);
    
    /**
     * 删除指定电影的所有分类关联
     */
    @Modifying
    @Query("DELETE FROM MovieCategory mc WHERE mc.movieId = :movieId")
    void deleteByMovieId(@Param("movieId") Integer movieId);
    
    /**
     * 删除指定分类的所有电影关联
     */
    @Modifying
    @Query("DELETE FROM MovieCategory mc WHERE mc.categoryId = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 删除指定电影和分类的关联
     */
    void deleteByMovieIdAndCategoryId(Integer movieId, Integer categoryId);
    
    /**
     * 批量删除指定电影的多个分类关联
     */
    @Modifying
    @Query("DELETE FROM MovieCategory mc WHERE mc.movieId = :movieId AND mc.categoryId IN :categoryIds")
    void deleteByMovieIdAndCategoryIdIn(@Param("movieId") Integer movieId, @Param("categoryIds") List<Integer> categoryIds);
    
    /**
     * 统计指定分类下的电影数量
     */
    @Query("SELECT COUNT(mc) FROM MovieCategory mc WHERE mc.categoryId = :categoryId")
    long countByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 统计指定电影的分类数量
     */
    @Query("SELECT COUNT(mc) FROM MovieCategory mc WHERE mc.movieId = :movieId")
    long countByMovieId(@Param("movieId") Integer movieId);
} 