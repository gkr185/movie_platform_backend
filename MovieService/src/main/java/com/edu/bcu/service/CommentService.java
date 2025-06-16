package com.edu.bcu.service;

import com.edu.bcu.dto.CommentStatisticsDTO;
import com.edu.bcu.entity.Comment;
import com.edu.bcu.entity.Movie;
import com.edu.bcu.repository.jpa.CommentRepository;
import com.edu.bcu.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        // 确保页码不小于1
        page = Math.max(1, page);
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

    // ==================== 管理端功能 ====================
    
    /**
     * 获取管理端评论列表
     */
    public Page<Comment> getAllCommentsForAdmin(String keyword, String status, int page, int size, String sort) {
        // 确保页码不小于1
        page = Math.max(1, page);
        
        // 构建排序
        Sort sortBy;
        switch (sort) {
            case "hot":
                sortBy = Sort.by("likeCount").descending();
                break;
            case "rating":
                sortBy = Sort.by("rating").descending();
                break;
            default:
                sortBy = Sort.by("createTime").descending();
        }
        
        PageRequest pageRequest = PageRequest.of(page - 1, size, sortBy);
        
        // 转换状态字符串为数字
        Integer statusCode = convertStatusToCode(status);
        
        // 根据条件查询
        if (StringUtils.hasText(keyword) || statusCode != null) {
            return commentRepository.findByStatusAndKeyword(statusCode, keyword, pageRequest);
        } else {
            return commentRepository.findAll(pageRequest);
        }
    }
    
    /**
     * 获取评论统计信息
     */
    public CommentStatisticsDTO getCommentStatistics() {
        Long total = commentRepository.countTotal();
        Long pending = commentRepository.countByStatus(0);  // 待审核
        Long approved = commentRepository.countByStatus(1); // 已通过
        Long rejected = commentRepository.countByStatus(2); // 已拒绝
        
        return new CommentStatisticsDTO(total, pending, approved, rejected);
    }
    
    /**
     * 审核评论
     */
    @Transactional
    public Comment auditComment(Long commentId, String status, String remark) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));
        
        Integer statusCode = convertStatusToCode(status);
        if (statusCode == null) {
            throw new IllegalArgumentException("无效的审核状态");
        }
        
        comment.setStatus(statusCode);
        comment.setUpdateTime(LocalDateTime.now());
        
        return commentRepository.save(comment);
    }
    
    /**
     * 批量审核评论
     */
    @Transactional
    public void batchAuditComments(List<Long> ids, String status) {
        Integer statusCode = convertStatusToCode(status);
        if (statusCode == null) {
            throw new IllegalArgumentException("无效的审核状态");
        }
        
        for (Long id : ids) {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null) {
                comment.setStatus(statusCode);
                comment.setUpdateTime(LocalDateTime.now());
                commentRepository.save(comment);
            }
        }
    }
    
    /**
     * 批量删除评论
     */
    @Transactional
    public void batchDeleteComments(List<Long> ids) {
        for (Long id : ids) {
            try {
                deleteComment(id);
            } catch (Exception e) {
                // 记录日志，但不中断批量操作
                System.err.println("删除评论失败，ID: " + id + ", 错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 获取评论详情
     */
    public Optional<Comment> getCommentDetail(Long commentId) {
        return commentRepository.findByIdWithDetails(commentId);
    }
    
    /**
     * 举报评论
     */
    @Transactional
    public void reportComment(Long commentId, String reason, String type) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("评论不存在"));
        
        // 这里可以创建举报记录表，暂时简化处理
        // 可以将举报信息记录到日志或单独的举报表中
        System.out.println("评论举报 - ID: " + commentId + ", 原因: " + reason + ", 类型: " + type);
        
        // 如果举报次数过多，可以自动将评论状态设为待审核
        // comment.setStatus(0);
        // commentRepository.save(comment);
    }
    
    /**
     * 转换状态字符串为状态码
     */
    private Integer convertStatusToCode(String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }
        
        switch (status.toLowerCase()) {
            case "pending":
                return 0;
            case "approved":
                return 1;
            case "rejected":
                return 2;
            default:
                return null;
        }
    }
}