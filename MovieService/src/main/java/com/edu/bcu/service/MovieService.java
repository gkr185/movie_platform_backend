package com.edu.bcu.service;

import com.edu.bcu.entity.Category;
import com.edu.bcu.entity.Movie;
import com.edu.bcu.entity.MovieCategory;
import com.edu.bcu.repository.jpa.CategoryRepository;
import com.edu.bcu.repository.jpa.MovieCategoryRepository;
import com.edu.bcu.repository.jpa.MovieJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieJpaRepository jpaRepository;
    private final MovieCategoryRepository movieCategoryRepository;
    private final CategoryRepository categoryRepository;

    public MovieService(MovieJpaRepository jpaRepository, MovieCategoryRepository movieCategoryRepository, CategoryRepository categoryRepository) {
        this.jpaRepository = jpaRepository;
        this.movieCategoryRepository = movieCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public Movie createMovie(Movie movie) {
        Movie savedMovie = jpaRepository.save(movie);

        // 假设 genres 是用逗号分隔的类别名称
        String[] genres = movie.getGenres().split(",");
        List<MovieCategory> movieCategories = new ArrayList<>();
        for (String genre : genres) {
            System.out.println(genre);
            // 根据类别名称查询类别 ID
            Long categoryId = findCategoryIdByName(genre);
            if (categoryId != null) {
                MovieCategory movieCategory = new MovieCategory();
                movieCategory.setMovieId(Math.toIntExact(savedMovie.getId()));
                movieCategory.setCategoryId(Math.toIntExact(categoryId));
                movieCategories.add(movieCategory);
            }
        }
        movieCategoryRepository.saveAll(movieCategories);

        return savedMovie;
    }

    // 根据类别名称查询类别 ID
    private Long findCategoryIdByName(String genre) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getName().equals(genre)) {
                return category.getId();
            }
        }
        return null;
    }

    public Optional<Movie> getMovieById(Long movieId) {
        return jpaRepository.findById(movieId);
    }

    @Transactional
    public Movie updateMovie(Long movieId, Movie updatedMovie) {
        Movie existingMovie = jpaRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("电影不存在"));
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setActors(updatedMovie.getActors());
        existingMovie.setGenres(updatedMovie.getGenres());
        return jpaRepository.save(existingMovie);
    }

    public void deleteMovie(Long movieId) {
        jpaRepository.deleteById(movieId);
    }

    public List<Movie> searchByCategory(String categoryId) {
        // 先到 movie_category 表中根据 categoryId 查询出所有相关的 movieId
        List<MovieCategory> movieCategories = movieCategoryRepository.findByCategoryId(Integer.parseInt(categoryId));
        List<Long> movieIds = new ArrayList<>();
        for (MovieCategory movieCategory : movieCategories) {
            movieIds.add((long) movieCategory.getMovieId());
        }

        // 再到 movie 表中根据这些 movieId 找出对应的电影
        return jpaRepository.findAllById(movieIds);
    }

    public List<Movie> getHotMovies() {
        return jpaRepository.findByIsHotOrderByRatingDesc(1);
    }

    public List<Movie> getRecommendedMovies() {
        return jpaRepository.findByIsRecommendedOrderByCreateTimeDesc(1);
    }

    public Page<Movie> getAllMovies(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page - 1, size, Sort.by("createTime").descending()));
    }

    public List<Movie> searchByKeyword(String keyword) {
        // 简单的模糊查询示例，根据实际需求调整
        return jpaRepository.searchByKeyword(keyword);
    }

    public List<Movie> getNewMovice() {
        return jpaRepository.findByCreatTime();
    }
}