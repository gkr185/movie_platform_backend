package com.edu.bcu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "original_title")
    private String originalTitle;
    private String director;
    private String writers;
    private String actors;
    @Column(name = "genres")
    private String genres;
    private String country;
    private String language;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    private Integer runtime;
    @Column(name = "imdb_id")
    private String imdbId;
    private Double rating;
    @Column(name = "rating_count")
    private Integer ratingCount;
    private String description;
    @Column(name = "poster_url")
    private String posterUrl;
    @Column(name = "trailer_url")
    private String trailerUrl;
    @Column(name = "play_url")
    private String playUrl;
    @Column(name = "is_recommended")
    private Integer isRecommended;
    @Column(name = "is_hot")
    private Integer isHot;
    @Column(name = "is_vip")
    private Integer isVip;
    private Integer status;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
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