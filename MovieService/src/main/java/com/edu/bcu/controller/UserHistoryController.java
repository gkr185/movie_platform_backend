package com.edu.bcu.controller;

import com.edu.bcu.entity.Movie;
import com.edu.bcu.entity.UserHistory;
import com.edu.bcu.service.UserHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class UserHistoryController {
    private final UserHistoryService historyService;

    public UserHistoryController(UserHistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/{userId}/movies/{movieId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserHistory addOrUpdateHistory(
            @PathVariable Long userId,
            @PathVariable Long movieId,
            @RequestParam(required = false) Integer progress,
            @RequestParam(required = false) Integer playTime
    ) {
        return historyService.addOrUpdateHistory(userId, movieId, progress, playTime);
    }

    @GetMapping("/{userId}/movies")
    public List<Map<String, Object>> getHistoryMovies(@PathVariable Long userId) {
        return historyService.getHistoryMovies(userId);
    }

    @GetMapping("/{userId}/movies/page")
    public Page<Map<String, Object>> getHistoryMovies(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return historyService.getHistoryMovies(userId, page, size);
    }

    @GetMapping("/{userId}/movies/time-range")
    public Page<Map<String, Object>> getHistoryMoviesByTimeRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return historyService.getHistoryMoviesByTimeRange(userId, startTime, endTime, page, size);
    }

    @DeleteMapping("/{userId}/movies/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistory(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        historyService.deleteHistory(userId, movieId);
    }

    @DeleteMapping("/{userId}/movies/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistories(
            @PathVariable Long userId,
            @RequestBody List<Long> movieIds
    ) {
        historyService.deleteHistories(userId, movieIds);
    }

    @DeleteMapping("/{userId}/movies")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearHistory(@PathVariable Long userId) {
        historyService.clearHistory(userId);
    }

    @GetMapping("/{userId}/movies/{movieId}")
    public UserHistory getHistory(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        return historyService.getHistory(userId, movieId);
    }
} 