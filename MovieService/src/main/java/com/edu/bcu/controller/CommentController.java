package com.edu.bcu.controller;

import com.edu.bcu.dto.CommentAuditDTO;
import com.edu.bcu.dto.CommentReportDTO;
import com.edu.bcu.dto.CommentStatisticsDTO;
import com.edu.bcu.entity.Comment;
import com.edu.bcu.service.CommentService;
import com.edu.bcu.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // ==================== 管理端接口 ====================
    
    /**
     * 获取所有评论列表（管理后台）
     */
    @GetMapping("/admin/list")
    public ResponseEntity<Page<Comment>> getAllCommentsForAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "time") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        
        Page<Comment> comments = commentService.getAllCommentsForAdmin(keyword, status, page, size, sort);
        return ResponseEntity.ok(comments);
    }
    
    /**
     * 获取评论统计信息
     */
    @GetMapping("/admin/statistics")
    public ResponseEntity<CommentStatisticsDTO> getCommentStatistics() {
        CommentStatisticsDTO statistics = commentService.getCommentStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 获取评论详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentDetail(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentDetail(id);
        return comment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 审核评论
     */
    @PutMapping("/{id}/audit")
    public ResponseEntity<Comment> auditComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentAuditDTO auditDTO) {
        
        try {
            Comment comment = commentService.auditComment(id, auditDTO.getStatus(), auditDTO.getRemark());
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 批量审核评论
     */
    @PutMapping("/batch/audit")
    public ResponseEntity<Void> batchAuditComments(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> idsObj = (List<Object>) request.get("ids");
            Object statusObj = request.get("status");
            
            if (idsObj == null || idsObj.isEmpty() || statusObj == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // 转换ID列表，支持Integer和Long类型
            List<Long> ids = idsObj.stream()
                    .map(id -> {
                        if (id instanceof Number) {
                            return ((Number) id).longValue();
                        } else {
                            return Long.valueOf(id.toString());
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
            
            // 处理状态参数，支持字符串和数字格式
            String status;
            if (statusObj instanceof String) {
                status = (String) statusObj;
            } else if (statusObj instanceof Number) {
                // 如果是数字，转换为对应的字符串
                int statusCode = ((Number) statusObj).intValue();
                switch (statusCode) {
                    case 0:
                        status = "pending";
                        break;
                    case 1:
                        status = "approved";
                        break;
                    case 2:
                        status = "rejected";
                        break;
                    default:
                        return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
            
            commentService.batchAuditComments(ids, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // 添加错误日志
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 批量删除评论
     */
    @DeleteMapping("/batch/delete")
    public ResponseEntity<Void> batchDeleteComments(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> idsObj = (List<Object>) request.get("ids");
            
            if (idsObj == null || idsObj.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // 转换ID列表，支持Integer和Long类型
            List<Long> ids = idsObj.stream()
                    .map(id -> {
                        if (id instanceof Number) {
                            return ((Number) id).longValue();
                        } else {
                            return Long.valueOf(id.toString());
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
            
            commentService.batchDeleteComments(ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // 添加错误日志
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 举报评论
     */
    @PostMapping("/{id}/report")
    public ResponseEntity<Void> reportComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentReportDTO reportDTO) {
        
        try {
            commentService.reportComment(id, reportDTO.getReason(), reportDTO.getType());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}