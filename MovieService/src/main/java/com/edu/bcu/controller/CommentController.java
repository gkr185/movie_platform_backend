package com.edu.bcu.controller;

import com.edu.bcu.entity.Comment;
import com.edu.bcu.service.CommentService;
import com.edu.bcu.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService, MovieService movieService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @GetMapping("/movies/{movieId}")
    public Page<Comment> getCommentsByMovie(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "time") String sort
    ) {
        return commentService.getCommentsByMovieId(movieId, page, size, sort);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        comment.setId(commentId.intValue());
        return commentService.updateComment(comment);
    }

    // 点赞接口
    @PostMapping("/{commentId}/like")
    public Comment likeComment(@PathVariable Long commentId) {
        return commentService.likeComment(commentId);
    }

    // 取消点赞接口
    @PostMapping("/{commentId}/unlike")
    public Comment unlikeComment(@PathVariable Long commentId) {
        return commentService.unlikeComment(commentId);
    }

    // 点踩接口
    @PostMapping("/{commentId}/dislike")
    public Comment dislikeComment(@PathVariable Long commentId) {
        return commentService.dislikeComment(commentId);
    }

    // 取消点踩接口
    @PostMapping("/{commentId}/undislike")
    public Comment undislikeComment(@PathVariable Long commentId) {
        return commentService.undislikeComment(commentId);
    }
}