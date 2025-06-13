package com.edu.bcu.controller;

import com.edu.bcu.entity.Movie;
import com.edu.bcu.entity.UserFavorite;
import com.edu.bcu.service.UserFavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites/{userId}/favorites")
public class UserFavoriteController {
    private final UserFavoriteService favoriteService;

    public UserFavoriteController(UserFavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{movieId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFavorite addFavorite(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        return favoriteService.addFavorite(userId, movieId);
    }

    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        favoriteService.removeFavorite(userId, movieId);
    }

    @GetMapping
    public List<Movie> getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoriteMoviesByUserId(userId);
    }

    @GetMapping("/{movieId}/exists")
    public boolean checkFavoriteExists(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        return favoriteService.exists(userId, movieId);
    }
}