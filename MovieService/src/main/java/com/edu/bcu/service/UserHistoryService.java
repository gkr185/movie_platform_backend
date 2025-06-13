package com.edu.bcu.service;

import com.edu.bcu.entity.Movie;
import com.edu.bcu.entity.UserHistory;
import com.edu.bcu.repository.jpa.MovieJpaRepository;
import com.edu.bcu.repository.jpa.UserHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserHistoryService {
    private final UserHistoryRepository historyRepository;
    private final MovieJpaRepository movieJpaRepository;

    public UserHistoryService(UserHistoryRepository historyRepository, MovieJpaRepository movieJpaRepository) {
        this.historyRepository = historyRepository;
        this.movieJpaRepository = movieJpaRepository;
    }

    @Transactional
    public UserHistory addOrUpdateHistory(Long userId, Long movieId, Integer progress, Integer playTime) {
        UserHistory history = historyRepository.findByUserIdAndMovieId(userId, movieId);
        
        if (history == null) {
            history = new UserHistory();
            history.setUserId(userId);
            history.setMovieId(movieId);
            history.setPlayTime(0);
            history.setProgress(0);
        }
        
        if (progress != null) {
            history.setProgress(progress);
        }
        if (playTime != null) {
            history.setPlayTime(playTime);
        }
        
        return historyRepository.save(history);
    }

    public List<Map<String, Object>> getHistoryMovies(Long userId) {
        List<UserHistory> histories = historyRepository.findByUserId(userId);
        List<Long> movieIds = histories.stream()
                .map(UserHistory::getMovieId)
                .collect(Collectors.toList());
        
        List<Movie> movies = movieJpaRepository.findAllById(movieIds);
        Map<Long, Movie> movieMap = movies.stream()
                .collect(Collectors.toMap(Movie::getId, movie -> movie));
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserHistory history : histories) {
            Movie movie = movieMap.get(history.getMovieId());
            if (movie != null) {
                Map<String, Object> historyWithMovie = Map.of(
                    "movie", movie,
                    "history", Map.of(
                        "playTime", history.getPlayTime(),
                        "progress", history.getProgress(),
                        "createTime", history.getCreateTime(),
                        "updateTime", history.getUpdateTime()
                    )
                );
                result.add(historyWithMovie);
            }
        }
        
        return result;
    }

    public Page<Map<String, Object>> getHistoryMovies(Long userId, int page, int size) {
        int zeroBasedPage = Math.max(0, page - 1);
        
        Page<UserHistory> histories = historyRepository.findByUserId(
                userId,
                PageRequest.of(zeroBasedPage, size, Sort.by("updateTime").descending())
        );
        
        List<Long> movieIds = histories.getContent().stream()
                .map(UserHistory::getMovieId)
                .collect(Collectors.toList());
        
        List<Movie> movies = movieJpaRepository.findAllById(movieIds);
        Map<Long, Movie> movieMap = movies.stream()
                .collect(Collectors.toMap(Movie::getId, movie -> movie));
        
        List<Map<String, Object>> content = histories.getContent().stream()
                .map(history -> {
                    Movie movie = movieMap.get(history.getMovieId());
                    return Map.of(
                        "movie", movie,
                        "history", Map.of(
                            "playTime", history.getPlayTime(),
                            "progress", history.getProgress(),
                            "createTime", history.getCreateTime(),
                            "updateTime", history.getUpdateTime()
                        )
                    );
                })
                .collect(Collectors.toList());
        
        return new PageImpl<>(
            content,
            histories.getPageable(),
            histories.getTotalElements()
        );
    }

    public Page<Map<String, Object>> getHistoryMoviesByTimeRange(
            Long userId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int page,
            int size
    ) {
        int zeroBasedPage = Math.max(0, page - 1);
        
        Page<UserHistory> histories = historyRepository.findByUserIdAndCreateTimeBetween(
                userId,
                startTime,
                endTime,
                PageRequest.of(zeroBasedPage, size, Sort.by("createTime").descending())
        );
        
        List<Long> movieIds = histories.getContent().stream()
                .map(UserHistory::getMovieId)
                .collect(Collectors.toList());
        
        List<Movie> movies = movieJpaRepository.findAllById(movieIds);
        Map<Long, Movie> movieMap = movies.stream()
                .collect(Collectors.toMap(Movie::getId, movie -> movie));
        
        List<Map<String, Object>> content = histories.getContent().stream()
                .map(history -> {
                    Movie movie = movieMap.get(history.getMovieId());
                    return Map.of(
                        "movie", movie,
                        "history", Map.of(
                            "playTime", history.getPlayTime(),
                            "progress", history.getProgress(),
                            "createTime", history.getCreateTime(),
                            "updateTime", history.getUpdateTime()
                        )
                    );
                })
                .collect(Collectors.toList());
        
        return new PageImpl<>(
            content,
            histories.getPageable(),
            histories.getTotalElements()
        );
    }

    @Transactional
    public void deleteHistory(Long userId, Long movieId) {
        historyRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    @Transactional
    public void deleteHistories(Long userId, List<Long> movieIds) {
        historyRepository.deleteByUserIdAndMovieIds(userId, movieIds);
    }

    @Transactional
    public void clearHistory(Long userId) {
        historyRepository.deleteByUserId(userId);
    }

    public UserHistory getHistory(Long userId, Long movieId) {
        return historyRepository.findByUserIdAndMovieId(userId, movieId);
    }
} 