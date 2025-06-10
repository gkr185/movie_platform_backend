package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    List<UserFavorite> findByUserId(Long userId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}