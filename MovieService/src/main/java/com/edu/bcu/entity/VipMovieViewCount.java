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
@Table(name = "vip_movie_view_count")
public class VipMovieViewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "int unsigned")
    private Integer id;

    @Column(name = "user_id", nullable = false, columnDefinition = "int unsigned")
    private Integer userId;

    @Column(name = "movie_id", nullable = false, columnDefinition = "int unsigned")
    private Integer movieId;

    @Column(name = "view_count", columnDefinition = "int unsigned default 0")
    private Integer viewCount;

    @Column(name = "create_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Column(name = "update_time", columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

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