package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findByUserId(Long userId);
    
    Page<UserHistory> findByUserId(Long userId, Pageable pageable);
    
    UserHistory findByUserIdAndMovieId(Long userId, Long movieId);
    
    @Modifying
    @Query("DELETE FROM UserHistory h WHERE h.userId = ?1")
    void deleteByUserId(Long userId);
    
    @Modifying
    @Query("DELETE FROM UserHistory h WHERE h.userId = ?1 AND h.movieId = ?2")
    void deleteByUserIdAndMovieId(Long userId, Long movieId);

    @Modifying
    @Query("DELETE FROM UserHistory h WHERE h.userId = ?1 AND h.movieId IN ?2")
    void deleteByUserIdAndMovieIds(Long userId, List<Long> movieIds);

    Page<UserHistory> findByUserIdAndCreateTimeBetween(
            Long userId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    );
} 