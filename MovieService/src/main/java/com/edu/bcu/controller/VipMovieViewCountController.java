package com.edu.bcu.controller;
import com.edu.bcu.entity.Movie;
import com.edu.bcu.service.MovieViewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moviesView")
public class VipMovieViewCountController {
    private final MovieViewService MovieViewService;

    public VipMovieViewCountController(MovieViewService MovieViewService) {
        this.MovieViewService = MovieViewService;
    }

    /**
     * 增加VIP用户观看电影次数
     *
     * @param userId  用户ID
     * @param movieId 电影ID
     */
    @PostMapping("/{movieId}/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void incrementViewCount(@PathVariable Integer userId, @PathVariable Integer movieId) {
        MovieViewService.incrementMovieViewCount(userId, movieId);
    }

    /**
     * 返回按总观看次数降序排列的电影列表
     * @return
     */
    @GetMapping("/movies")
    public List<Movie> getMoviesOrderByViewCountDesc() {
        return MovieViewService.getMoviesOrderByViewCountDesc();
    }

    /**
     * 获取特定电影的总观看次数
     * @param movieId
     * @return
     */
    @GetMapping("/{movieId}/count")
    public Integer getMovieViewCount(@PathVariable Integer movieId) {
        return MovieViewService.getTotalViewCount(movieId);
    }
}