package com.edu.bcu.service;

import com.edu.bcu.entity.Movie;
import com.edu.bcu.entity.UserFavorite;
import com.edu.bcu.repository.jpa.MovieJpaRepository;
import com.edu.bcu.repository.jpa.UserFavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFavoriteService {
    private final UserFavoriteRepository favoriteRepository;
    private final MovieJpaRepository movieJpaRepository;

    public UserFavoriteService(UserFavoriteRepository favoriteRepository, MovieJpaRepository movieJpaRepository) {
        this.favoriteRepository = favoriteRepository;
        this.movieJpaRepository = movieJpaRepository;
    }

    public UserFavorite addFavorite(Long userId, Long movieId) {
        if (exists(userId, movieId)) {
            throw new RuntimeException("已收藏该电影");
        }
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setMovieId(movieId);
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long movieId) {
        favoriteRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public boolean exists(Long userId, Long movieId) {
        return favoriteRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public List<Movie> getFavoriteMoviesByUserId(Long userId) {
        List<UserFavorite> favorites = favoriteRepository.findByUserId(userId);
        List<Long> movieIds = new ArrayList<>();
        for (UserFavorite favorite : favorites) {
            movieIds.add(favorite.getMovieId());
        }
        return movieJpaRepository.findAllById(movieIds);
    }
}