package com.edu.bcu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "category")
@Schema(description = "分类实体")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID")
    private Integer id;

    @Column(unique = true)
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "分类描述")
    private String description;
    
    @Column(name = "parent_id")
    @Schema(description = "父分类ID")
    private Long parentId;
    
    @Column(name = "sort_order")
    @Schema(description = "排序顺序")
    private Integer sortOrder;
    
    @Schema(description = "状态(0:禁用,1:启用)")
    private Integer status;
    
    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 