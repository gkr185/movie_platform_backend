package com.edu.bcu.service.impl;

import com.edu.bcu.dto.CategoryDTO;
import com.edu.bcu.entity.Category;
import com.edu.bcu.repository.CategoryRepository;
import com.edu.bcu.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("分类名称已存在");
        }
        
        Category category = new Category();
        // 手动设置字段，避免复制ID字段（创建时ID应该为null，由数据库自动生成）
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setParentId(categoryDTO.getParentId());
        category.setSortOrder(categoryDTO.getSortOrder());
        category.setStatus(categoryDTO.getStatus());
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Integer id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("分类不存在"));
                
        if (!category.getName().equals(categoryDTO.getName()) && 
            categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("分类名称已存在");
        }
        
        // 手动设置字段，避免复制ID字段导致的Hibernate错误
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setParentId(categoryDTO.getParentId());
        category.setSortOrder(categoryDTO.getSortOrder());
        category.setStatus(categoryDTO.getStatus());
        category.setUpdateTime(LocalDateTime.now());
        
        // 替代方案：使用BeanUtils但忽略ID字段
        // BeanUtils.copyProperties(categoryDTO, category, "id");
        // category.setUpdateTime(LocalDateTime.now());
        
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("分类不存在");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("分类不存在"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByStatus(1);
    }
} 