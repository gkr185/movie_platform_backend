package com.edu.bcu.service.impl;

import com.edu.bcu.dto.MovieCategoryDTO;
import com.edu.bcu.dto.MovieCategoryWithCategoryDTO;
import com.edu.bcu.entity.MovieCategory;
import com.edu.bcu.exception.BusinessException;
import com.edu.bcu.exception.ErrorCode;
import com.edu.bcu.repository.CategoryRepository;
import com.edu.bcu.repository.MovieCategoryRepository;
import com.edu.bcu.service.MovieCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieCategoryServiceImpl implements MovieCategoryService {
    
    private final MovieCategoryRepository movieCategoryRepository;
    private final CategoryRepository categoryRepository;

    public MovieCategoryServiceImpl(MovieCategoryRepository movieCategoryRepository, 
                                   CategoryRepository categoryRepository) {
        this.movieCategoryRepository = movieCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public MovieCategory addMovieCategory(MovieCategoryDTO dto) {
        // 验证分类是否存在
        if (!categoryRepository.existsById(dto.getCategoryId())) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        
        // 检查关联是否已存在
        if (movieCategoryRepository.existsByMovieIdAndCategoryId(dto.getMovieId(), dto.getCategoryId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "电影分类关联已存在");
        }
        
        MovieCategory movieCategory = new MovieCategory();
        movieCategory.setMovieId(dto.getMovieId());
        movieCategory.setCategoryId(dto.getCategoryId());
        movieCategory.setCreateTime(LocalDateTime.now());
        
        return movieCategoryRepository.save(movieCategory);
    }

    @Override
    @Transactional
    public List<MovieCategory> addMovieCategoriesBatch(Integer movieId, List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "分类ID列表不能为空");
        }
        
        // 验证所有分类是否存在
        for (Integer categoryId : categoryIds) {
            if (!categoryRepository.existsById(categoryId)) {
                throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND.getCode(), "分类ID " + categoryId + " 不存在");
            }
        }
        
        List<MovieCategory> movieCategories = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (Integer categoryId : categoryIds) {
            // 跳过已存在的关联
            if (!movieCategoryRepository.existsByMovieIdAndCategoryId(movieId, categoryId)) {
                MovieCategory movieCategory = new MovieCategory();
                movieCategory.setMovieId(movieId);
                movieCategory.setCategoryId(categoryId);
                movieCategory.setCreateTime(now);
                movieCategories.add(movieCategory);
            }
        }
        
        if (movieCategories.isEmpty()) {
            log.info("所有电影分类关联已存在，无需添加");
            return new ArrayList<>();
        }
        
        return movieCategoryRepository.saveAll(movieCategories);
    }

    @Override
    @Transactional
    public void removeMovieCategory(Integer movieId, Integer categoryId) {
        if (!movieCategoryRepository.existsByMovieIdAndCategoryId(movieId, categoryId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "电影分类关联不存在");
        }
        
        movieCategoryRepository.deleteByMovieIdAndCategoryId(movieId, categoryId);
    }

    @Override
    @Transactional
    public void removeMovieCategoriesBatch(Integer movieId, List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "分类ID列表不能为空");
        }
        
        movieCategoryRepository.deleteByMovieIdAndCategoryIdIn(movieId, categoryIds);
    }

    @Override
    @Transactional
    public void removeAllMovieCategories(Integer movieId) {
        movieCategoryRepository.deleteByMovieId(movieId);
    }

    @Override
    @Transactional
    public List<MovieCategory> updateMovieCategories(Integer movieId, List<Integer> categoryIds) {
        // 先删除所有现有关联
        movieCategoryRepository.deleteByMovieId(movieId);
        
        // 添加新的关联
        if (categoryIds != null && !categoryIds.isEmpty()) {
            return addMovieCategoriesBatch(movieId, categoryIds);
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<MovieCategory> getMovieCategories(Integer movieId) {
        return movieCategoryRepository.findByMovieId(movieId);
    }

    @Override
    public List<MovieCategoryWithCategoryDTO> getMovieCategoriesWithInfo(Integer movieId) {
        List<MovieCategory> movieCategories = movieCategoryRepository.findByMovieIdWithCategory(movieId);
        return movieCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private MovieCategoryWithCategoryDTO convertToDTO(MovieCategory movieCategory) {
        MovieCategoryWithCategoryDTO dto = new MovieCategoryWithCategoryDTO();
        dto.setId(movieCategory.getId());
        dto.setMovieId(movieCategory.getMovieId());
        dto.setCategoryId(movieCategory.getCategoryId());
        dto.setCreateTime(movieCategory.getCreateTime());
        
        if (movieCategory.getCategory() != null) {
            MovieCategoryWithCategoryDTO.CategoryInfo categoryInfo = new MovieCategoryWithCategoryDTO.CategoryInfo();
            categoryInfo.setId(movieCategory.getCategory().getId());
            categoryInfo.setName(movieCategory.getCategory().getName());
            categoryInfo.setDescription(movieCategory.getCategory().getDescription());
            categoryInfo.setStatus(movieCategory.getCategory().getStatus());
            dto.setCategory(categoryInfo);
        }
        
        return dto;
    }

    @Override
    public List<MovieCategory> getCategoryMovies(Integer categoryId) {
        // 验证分类是否存在
        if (!categoryRepository.existsById(categoryId)) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        
        return movieCategoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public boolean existsMovieCategory(Integer movieId, Integer categoryId) {
        return movieCategoryRepository.existsByMovieIdAndCategoryId(movieId, categoryId);
    }

    @Override
    public long countMoviesByCategory(Integer categoryId) {
        return movieCategoryRepository.countByCategoryId(categoryId);
    }

    @Override
    public long countCategoriesByMovie(Integer movieId) {
        return movieCategoryRepository.countByMovieId(movieId);
    }
} 