package com.edu.bcu.service;

import com.edu.bcu.dto.CategoryDTO;
import com.edu.bcu.entity.Category;
import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Integer id, CategoryDTO categoryDTO);
    void deleteCategory(Integer id);
    Category getCategoryById(Integer id);
    List<Category> getAllCategories();
    List<Category> getSubCategories(Long parentId);
    List<Category> getActiveCategories();
} 