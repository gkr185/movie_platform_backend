package com.edu.bcu.controller;

import com.edu.bcu.common.Result;
import com.edu.bcu.dto.BatchMovieCategoryDTO;
import com.edu.bcu.dto.MovieCategoryDTO;
import com.edu.bcu.dto.MovieCategoryWithCategoryDTO;
import com.edu.bcu.entity.MovieCategory;
import com.edu.bcu.service.MovieCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "电影分类关联管理", description = "提供电影与分类关联的增删改查接口")
@RestController
@RequestMapping("/api/movie-categories")
public class MovieCategoryController {
    
    private final MovieCategoryService movieCategoryService;

    public MovieCategoryController(MovieCategoryService movieCategoryService) {
        this.movieCategoryService = movieCategoryService;
    }

    @Operation(summary = "添加电影分类关联", description = "为电影添加分类标签")
    @ApiResponse(responseCode = "200", description = "添加成功")
    @PostMapping
    public Result<MovieCategory> addMovieCategory(@Valid @RequestBody MovieCategoryDTO dto) {
        return Result.success(movieCategoryService.addMovieCategory(dto));
    }

    @Operation(summary = "批量添加电影分类关联", description = "为指定电影批量添加多个分类")
    @ApiResponse(responseCode = "200", description = "批量添加成功")
    @PostMapping("/batch")
    public Result<List<MovieCategory>> addMovieCategoriesBatch(
            @Parameter(description = "批量电影分类关联信息") @Valid @RequestBody BatchMovieCategoryDTO dto) {
        return Result.success(movieCategoryService.addMovieCategoriesBatch(dto.getMovieId(), dto.getCategoryIds()));
    }

    @Operation(summary = "删除电影分类关联", description = "删除指定电影的特定分类关联")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping
    public Result<Void> removeMovieCategory(
            @Parameter(description = "电影ID") @RequestParam Integer movieId,
            @Parameter(description = "分类ID") @RequestParam Integer categoryId) {
        movieCategoryService.removeMovieCategory(movieId, categoryId);
        return Result.success(null);
    }

    @Operation(summary = "批量删除电影分类关联", description = "批量删除指定电影的多个分类关联")
    @ApiResponse(responseCode = "200", description = "批量删除成功")
    @DeleteMapping("/batch")
    public Result<Void> removeMovieCategoriesBatch(
            @Parameter(description = "电影ID") @RequestParam Integer movieId,
            @Parameter(description = "分类ID列表") @RequestBody List<Integer> categoryIds) {
        movieCategoryService.removeMovieCategoriesBatch(movieId, categoryIds);
        return Result.success(null);
    }

    @Operation(summary = "删除电影所有分类关联", description = "删除指定电影的所有分类关联")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/movie/{movieId}")
    public Result<Void> removeAllMovieCategories(
            @Parameter(description = "电影ID") @PathVariable Integer movieId) {
        movieCategoryService.removeAllMovieCategories(movieId);
        return Result.success(null);
    }

    @Operation(summary = "更新电影分类关联", description = "替换电影的所有分类关联")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/movie/{movieId}")
    public Result<List<MovieCategory>> updateMovieCategories(
            @Parameter(description = "电影ID") @PathVariable Integer movieId,
            @Parameter(description = "新的分类ID列表") @RequestBody List<Integer> categoryIds) {
        return Result.success(movieCategoryService.updateMovieCategories(movieId, categoryIds));
    }

    @Operation(summary = "获取电影的所有分类", description = "查询指定电影关联的所有分类")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/movie/{movieId}")
    public Result<List<MovieCategory>> getMovieCategories(
            @Parameter(description = "电影ID") @PathVariable Integer movieId) {
        return Result.success(movieCategoryService.getMovieCategories(movieId));
    }

    @Operation(summary = "获取电影的所有分类（含详情）", description = "查询指定电影关联的所有分类，包含分类详细信息")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/movie/{movieId}/with-info")
    public Result<List<MovieCategoryWithCategoryDTO>> getMovieCategoriesWithInfo(
            @Parameter(description = "电影ID") @PathVariable Integer movieId) {
        return Result.success(movieCategoryService.getMovieCategoriesWithInfo(movieId));
    }

    @Operation(summary = "获取分类下的所有电影", description = "查询指定分类下关联的所有电影")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/category/{categoryId}")
    public Result<List<MovieCategory>> getCategoryMovies(
            @Parameter(description = "分类ID") @PathVariable Integer categoryId) {
        return Result.success(movieCategoryService.getCategoryMovies(categoryId));
    }

    @Operation(summary = "检查电影分类关联是否存在", description = "检查指定电影和分类的关联关系是否存在")
    @ApiResponse(responseCode = "200", description = "检查完成")
    @GetMapping("/exists")
    public Result<Boolean> existsMovieCategory(
            @Parameter(description = "电影ID") @RequestParam Integer movieId,
            @Parameter(description = "分类ID") @RequestParam Integer categoryId) {
        return Result.success(movieCategoryService.existsMovieCategory(movieId, categoryId));
    }

    @Operation(summary = "统计分类下的电影数量", description = "统计指定分类下关联的电影总数")
    @ApiResponse(responseCode = "200", description = "统计完成")
    @GetMapping("/count/category/{categoryId}")
    public Result<Long> countMoviesByCategory(
            @Parameter(description = "分类ID") @PathVariable Integer categoryId) {
        return Result.success(movieCategoryService.countMoviesByCategory(categoryId));
    }

    @Operation(summary = "统计电影的分类数量", description = "统计指定电影关联的分类总数")
    @ApiResponse(responseCode = "200", description = "统计完成")
    @GetMapping("/count/movie/{movieId}")
    public Result<Long> countCategoriesByMovie(
            @Parameter(description = "电影ID") @PathVariable Integer movieId) {
        return Result.success(movieCategoryService.countCategoriesByMovie(movieId));
    }
} 