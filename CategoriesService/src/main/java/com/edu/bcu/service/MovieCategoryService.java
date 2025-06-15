package com.edu.bcu.service;

import com.edu.bcu.dto.MovieCategoryDTO;
import com.edu.bcu.dto.MovieCategoryWithCategoryDTO;
import com.edu.bcu.entity.MovieCategory;
import java.util.List;

public interface MovieCategoryService {
    
    /**
     * 添加电影分类关联
     */
    MovieCategory addMovieCategory(MovieCategoryDTO dto);
    
    /**
     * 批量添加电影分类关联
     */
    List<MovieCategory> addMovieCategoriesBatch(Integer movieId, List<Integer> categoryIds);
    
    /**
     * 删除电影分类关联
     */
    void removeMovieCategory(Integer movieId, Integer categoryId);
    
    /**
     * 批量删除电影的分类关联
     */
    void removeMovieCategoriesBatch(Integer movieId, List<Integer> categoryIds);
    
    /**
     * 删除电影的所有分类关联
     */
    void removeAllMovieCategories(Integer movieId);
    
    /**
     * 更新电影的分类关联（先删除所有，再添加新的）
     */
    List<MovieCategory> updateMovieCategories(Integer movieId, List<Integer> categoryIds);
    
    /**
     * 获取电影的所有分类
     */
    List<MovieCategory> getMovieCategories(Integer movieId);
    
    /**
     * 获取电影的所有分类（包含分类详细信息）
     */
    List<MovieCategoryWithCategoryDTO> getMovieCategoriesWithInfo(Integer movieId);
    
    /**
     * 获取分类下的所有电影
     */
    List<MovieCategory> getCategoryMovies(Integer categoryId);
    
    /**
     * 检查电影分类关联是否存在
     */
    boolean existsMovieCategory(Integer movieId, Integer categoryId);
    
    /**
     * 统计分类下的电影数量
     */
    long countMoviesByCategory(Integer categoryId);
    
    /**
     * 统计电影的分类数量
     */
    long countCategoriesByMovie(Integer movieId);
} 