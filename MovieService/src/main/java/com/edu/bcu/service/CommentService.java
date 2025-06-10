package com.edu.bcu.service;

import com.edu.bcu.entity.Comment;
import com.edu.bcu.entity.Movie;
import com.edu.bcu.repository.jpa.CommentRepository;
import com.edu.bcu.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MovieService movieService;

    public CommentService(CommentRepository commentRepository, MovieService movieService) {
        this.commentRepository = commentRepository;
        this.movieService = movieService;
    }

    public Comment saveComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);

        // 更新电影的 rating_count 和 rating
        Long movieId = (long) savedComment.getMovieId();
        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new RuntimeException("电影不存在"));

        if (movie.getRatingCount() == null) {
            movie.setRatingCount(1);
            movie.setRating((double) savedComment.getRating());
        } else {
            int newRatingCount = movie.getRatingCount() + 1;
            double newRating = (movie.getRating() * movie.getRatingCount() + savedComment.getRating()) / newRatingCount;
            movie.setRatingCount(newRatingCount);
            movie.setRating((double) Math.round(newRating * 10) / 10); // 保留1位小数
        }
        movieService.updateMovie(movieId, movie);

        return savedComment;
    }

    public Page<Comment> getCommentsByMovieId(Long movieId, int page, int size, String sort) {
        Sort sortBy = sort.equals("hot") ?
                Sort.by("likeCount").descending() :
                Sort.by("createTime").descending();
        return commentRepository.findByMovieId(movieId, PageRequest.of(page - 1, size, sortBy));
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));

        // 更新电影的 rating_count 和 rating
        Long movieId = (long) comment.getMovieId();
        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new RuntimeException("电影不存在"));

        int newRatingCount = movie.getRatingCount() - 1;
        if (newRatingCount > 0) {
            double newRating = (movie.getRating() * movie.getRatingCount() - comment.getRating()) / newRatingCount;
            movie.setRatingCount(newRatingCount);
            movie.setRating((double) Math.round(newRating * 10) / 10); // 保留1位小数
        } else {
            movie.setRatingCount(0);
            movie.setRating(0.0);
        }
        movieService.updateMovie(movieId, movie);

        commentRepository.deleteById(commentId);
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 点赞服务
    @Transactional
    public Comment likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));
        comment.like();
        return commentRepository.save(comment);
    }

    // 取消点赞服务
    @Transactional
    public Comment unlikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));
        comment.unlike();
        return commentRepository.save(comment);
    }

    // 点踩服务
    @Transactional
    public Comment dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));
        comment.dislike();
        return commentRepository.save(comment);
    }

    // 取消点踩服务
    @Transactional
    public Comment undislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));
        comment.undislike();
        return commentRepository.save(comment);
    }
}