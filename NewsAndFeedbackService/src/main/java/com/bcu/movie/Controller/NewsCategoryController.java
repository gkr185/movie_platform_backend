package com.bcu.movie.Controller;

import com.bcu.movie.entity.NewsCategory;
import com.bcu.movie.service.NewsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news/categories")
public class NewsCategoryController {

    @Autowired
    private NewsCategoryService newsCategoryService;

    @GetMapping
    public ResponseEntity<List<NewsCategory>> getAllCategories() {
        return ResponseEntity.ok(newsCategoryService.getAllCategories());
    }

    @GetMapping("/active")
    public ResponseEntity<List<NewsCategory>> getActiveCategories() {
        return ResponseEntity.ok(newsCategoryService.getActiveCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsCategory> getCategoryById(@PathVariable Integer id) {
        NewsCategory category = newsCategoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<NewsCategory> createCategory(@RequestBody NewsCategory category) {
        try {
            NewsCategory createdCategory = newsCategoryService.createCategory(category);
            return ResponseEntity.ok(createdCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsCategory> updateCategory(@PathVariable Integer id, @RequestBody NewsCategory category) {
        try {
            NewsCategory updatedCategory = newsCategoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        try {
            newsCategoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<NewsCategory> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        try {
            NewsCategory updatedCategory = newsCategoryService.updateStatus(id, status);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 