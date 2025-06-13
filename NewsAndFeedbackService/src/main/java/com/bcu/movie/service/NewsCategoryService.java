package com.bcu.movie.service;

import com.bcu.movie.entity.NewsCategory;
import java.util.List;

public interface NewsCategoryService {
    List<NewsCategory> getAllCategories();
    List<NewsCategory> getActiveCategories();
    NewsCategory getCategoryById(Integer id);
    
    NewsCategory createCategory(NewsCategory category);
    
    NewsCategory updateCategory(Integer id, NewsCategory category);
    
    void deleteCategory(Integer id);
    
    NewsCategory updateStatus(Integer id, Integer status);
} 