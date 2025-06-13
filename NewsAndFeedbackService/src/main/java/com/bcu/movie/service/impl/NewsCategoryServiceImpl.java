package com.bcu.movie.service.impl;

import com.bcu.movie.entity.NewsCategory;
import com.bcu.movie.repository.NewsCategoryRepository;
import com.bcu.movie.service.NewsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {
    
    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Override
    public List<NewsCategory> getAllCategories() {
        return newsCategoryRepository.findAll();
    }

    @Override
    public List<NewsCategory> getActiveCategories() {
        return newsCategoryRepository.findByStatusOrderBySortOrderAsc(1);
    }

    @Override
    public NewsCategory getCategoryById(Integer id) {
        return newsCategoryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public NewsCategory createCategory(NewsCategory category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        if (category.getStatus() == null) {
            category.setStatus(1); // 默认启用
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0); // 默认排序值
        }
        return newsCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public NewsCategory updateCategory(Integer id, NewsCategory category) {
        NewsCategory existingCategory = newsCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setSortOrder(category.getSortOrder());
        existingCategory.setStatus(category.getStatus());
        existingCategory.setUpdateTime(LocalDateTime.now());
        
        return newsCategoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        NewsCategory category = newsCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        newsCategoryRepository.delete(category);
    }

    @Override
    @Transactional
    public NewsCategory updateStatus(Integer id, Integer status) {
        NewsCategory category = newsCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        return newsCategoryRepository.save(category);
    }
} 