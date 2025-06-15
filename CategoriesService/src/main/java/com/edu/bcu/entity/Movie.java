package com.edu.bcu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movie")
@Schema(description = "电影实体")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "电影ID")
    private Integer id;

    @Schema(description = "电影标题")
    private String title;
    
    @Column(name = "original_title")
    @Schema(description = "原始标题")
    private String originalTitle;
    
    @Schema(description = "导演")
    private String director;
    
    @Schema(description = "编剧")
    private String writers;
    
    @Schema(description = "演员")
    private String actors;
    
    @Schema(description = "类型")
    private String genres;
    
    @Schema(description = "国家")
    private String country;
    
    @Schema(description = "语言")
    private String language;
    
    @Column(name = "release_date")
    @Schema(description = "上映日期")
    private LocalDate releaseDate;
    
    @Schema(description = "片长(分钟)")
    private Integer runtime;
    
    @Column(name = "imdb_id")
    @Schema(description = "IMDB ID")
    private String imdbId;
    
    @Schema(description = "评分")
    private Double rating;
    
    @Column(name = "rating_count")
    @Schema(description = "评分人数")
    private Integer ratingCount;
    
    @Schema(description = "电影描述")
    private String description;
    
    @Column(name = "poster_url")
    @Schema(description = "海报URL")
    private String posterUrl;
    
    @Column(name = "trailer_url")
    @Schema(description = "预告片URL")
    private String trailerUrl;
    
    @Column(name = "play_url")
    @Schema(description = "播放地址")
    private String playUrl;
    
    @Column(name = "is_recommended")
    @Schema(description = "是否推荐")
    private Integer isRecommended;
    
    @Column(name = "is_hot")
    @Schema(description = "是否热门")
    private Integer isHot;
    
    @Column(name = "is_vip")
    @Schema(description = "是否VIP")
    private Integer isVip;
    
    @Schema(description = "状态")
    private Integer status;
    
    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 