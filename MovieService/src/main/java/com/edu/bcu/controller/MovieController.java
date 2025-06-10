package com.edu.bcu.controller;

import com.edu.bcu.entity.Movie;
import com.edu.bcu.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping("/{movieId}")
    public Movie getMovie(@PathVariable Long movieId) {
        return movieService.getMovieById(movieId)
                .orElseThrow(() -> new RuntimeException("电影不存在"));
    }

    @PutMapping("/{movieId}")
    public Movie updateMovie(@PathVariable Long movieId, @RequestBody Movie movie) {
        return movieService.updateMovie(movieId, movie);
    }

    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
    }

    @GetMapping("/categories/{categoryId}")
    public List<Movie> getMoviesByCategory(@PathVariable String categoryId) {
        return movieService.searchByCategory(categoryId);
    }

    @GetMapping("/rankings/hot")
    public List<Movie> getHotMovies() {
        return movieService.getHotMovies();
    }

    @GetMapping("/rankings/recommended")
    public List<Movie> getRecommendedMovies() {
        return movieService.getRecommendedMovies();
    }
    @GetMapping("/rankings/new")
    public List<Movie> getNewMovies() {
        return movieService.getNewMovice();
    }

    @GetMapping
    public Page<Movie> getAllMovies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return movieService.getAllMovies(page, size);
    }

    @GetMapping("/search")
    public List<Movie> searchMovies(@RequestParam String keyword) {
        return movieService.searchByKeyword(keyword);
    }
}