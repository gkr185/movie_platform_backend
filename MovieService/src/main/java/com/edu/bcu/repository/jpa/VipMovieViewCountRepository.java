package com.edu.bcu.repository.jpa;

import com.edu.bcu.entity.VipMovieViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VipMovieViewCountRepository extends JpaRepository<VipMovieViewCount, Integer> {
    Optional<VipMovieViewCount> findByUserIdAndMovieId(Integer userId, Integer movieId);

    @Query("SELECT mvc.movieId, SUM(mvc.viewCount) as totalViewCount " +
            "FROM VipMovieViewCount mvc " +
            "GROUP BY mvc.movieId " +
            "ORDER BY totalViewCount DESC")
    List<Object[]> getMovieViewCountsOrderByDesc();

    @Query("SELECT SUM(mvc.viewCount) FROM VipMovieViewCount mvc WHERE mvc.movieId = :movieId")
    Integer getTotalViewCountByMovieId(Integer movieId);
}