package com.edu.bcu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movie_category", 
       uniqueConstraints = @UniqueConstraint(name = "idx_movie_category", 
                                           columnNames = {"movie_id", "category_id"}))
@Schema(description = "电影分类关联实体")
public class MovieCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "关联ID")
    private Long id;

    @Column(name = "movie_id", nullable = false)
    @Schema(description = "电影ID", required = true)
    private Integer movieId;
    
    @Column(name = "category_id", nullable = false)
    @Schema(description = "分类ID", required = true)
    private Integer categoryId;
    
    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    // 可选：与Category实体的关联关系（仅用于查询便利）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @Schema(description = "关联的分类信息")
    @JsonIgnore
    private Category category;
} 