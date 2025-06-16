package com.edu.bcu.service;

import com.edu.bcu.entity.Movie;
import com.edu.bcu.entity.User;
import com.edu.bcu.entity.VipMovieViewCount;
import com.edu.bcu.repository.jpa.MovieJpaRepository;
import com.edu.bcu.repository.jpa.UserRepository;
import com.edu.bcu.repository.jpa.VipMovieViewCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MovieViewService {

    private final UserRepository userRepository;
    private final VipMovieViewCountRepository vipMovieViewCountRepository;
    private final MovieJpaRepository movieJpaRepository;

    public MovieViewService(UserRepository userRepository, VipMovieViewCountRepository vipMovieViewCountRepository, MovieJpaRepository movieJpaRepository) {
        this.userRepository = userRepository;
        this.vipMovieViewCountRepository = vipMovieViewCountRepository;
        this.movieJpaRepository = movieJpaRepository;
    }

    /**
     * 添加电影的观看次数
     * @param userId
     * @param movieId
     */
    @Transactional
    public void incrementMovieViewCount(Integer userId, Integer movieId) {
        // 原有的方法逻辑保持不变
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (user.getIsVip() != 1 ) {
            throw new IllegalStateException("非VIP用户或VIP已过期，无法增加电影观看次数");
        }

        Optional<VipMovieViewCount> optionalViewCount = vipMovieViewCountRepository.findByUserIdAndMovieId(userId, movieId);

        if (optionalViewCount.isPresent()) {
            VipMovieViewCount viewCount = optionalViewCount.get();
            viewCount.setViewCount(viewCount.getViewCount() + 1);
            vipMovieViewCountRepository.save(viewCount);
        } else {
            VipMovieViewCount newViewCount = new VipMovieViewCount();
            newViewCount.setUserId(userId);
            newViewCount.setMovieId(movieId);
            newViewCount.setViewCount(1);
            vipMovieViewCountRepository.save(newViewCount);
        }
    }

    /**
     * 获取按总观看次数降序排列的电影列表
     * @return
     */
    public List<Movie> getMoviesOrderByViewCountDesc() {
        List<Object[]> movieViewCounts = vipMovieViewCountRepository.getMovieViewCountsOrderByDesc();
        List<Long> movieIds = new ArrayList<>();
        Map<Long, Integer> movieViewCountMap = new HashMap<>();

        for (Object[] result : movieViewCounts) {
            Integer movieIdInt = (Integer) result[0];
            Long movieId = movieIdInt.longValue();
            Integer viewCount = ((Number) result[1]).intValue();
            movieIds.add(movieId);
            movieViewCountMap.put(movieId, viewCount);
        }

        List<Movie> movies = movieJpaRepository.findAllById(movieIds);

        // 按观看次数降序排序
        movies.sort((m1, m2) -> movieViewCountMap.get(m2.getId()).compareTo(movieViewCountMap.get(m1.getId())));

        return movies;
    }

    /**
     * 获取特定电影的总观看次数
      */

    public Integer getTotalViewCount(Integer movieId) {
        return vipMovieViewCountRepository.getTotalViewCountByMovieId(movieId);
    }
}