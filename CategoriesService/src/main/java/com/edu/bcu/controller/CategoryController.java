package com.edu.bcu.controller;

import com.edu.bcu.common.Result;
import com.edu.bcu.dto.CategoryDTO;
import com.edu.bcu.entity.Category;
import com.edu.bcu.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分类管理", description = "提供分类的增删改查接口")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "创建分类", description = "创建一个新的电影分类")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping
    public Result<Category> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return Result.success(categoryService.createCategory(categoryDTO));
    }

    @Operation(summary = "更新分类", description = "根据ID更新分类信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/{id}")
    public Result<Category> updateCategory(
            @Parameter(description = "分类ID") @PathVariable Integer id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        return Result.success(categoryService.updateCategory(id, categoryDTO));
    }

    @Operation(summary = "删除分类", description = "根据ID删除分类")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return Result.success(null);
    }

    @Operation(summary = "获取分类详情", description = "根据ID获取分类详细信息")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping("/{id}")
    public Result<Category> getCategoryById(
            @Parameter(description = "分类ID") @PathVariable Integer id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    @Operation(summary = "获取所有分类", description = "获取所有分类列表")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @Operation(summary = "获取子分类", description = "根据父分类ID获取子分类列表")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping("/sub/{parentId}")
    public Result<List<Category>> getSubCategories(
            @Parameter(description = "父分类ID") @PathVariable Long parentId) {
        return Result.success(categoryService.getSubCategories(parentId));
    }

    @Operation(summary = "获取活动分类", description = "获取所有状态为活动的分类")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping("/active")
    public Result<List<Category>> getActiveCategories() {
        return Result.success(categoryService.getActiveCategories());
    }
} 