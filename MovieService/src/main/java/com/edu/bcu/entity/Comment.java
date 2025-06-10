package com.edu.bcu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "movie_id")
    private Integer movieId;
    private String content;
    private Integer rating;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "like_count")
    private Integer likeCount;
    @Column(name = "dislike_count")
    private Integer dislikeCount;
    private Integer status;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 点赞方法
    public void like() {
        this.likeCount = (this.likeCount == null ? 0 : this.likeCount) + 1;
    }

    // 取消点赞
    public void unlike() {
        if (this.likeCount != null && this.likeCount > 0) {
            this.likeCount--;
        }
    }

    // 点踩方法
    public void dislike() {
        this.dislikeCount = (this.dislikeCount == null ? 0 : this.dislikeCount) + 1;
    }

    // 取消点踩
    public void undislike() {
        if (this.dislikeCount != null && this.dislikeCount > 0) {
            this.dislikeCount--;
        }
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}