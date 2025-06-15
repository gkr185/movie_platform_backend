package com.edu.bcu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "电影分类关联及分类信息DTO")
public class MovieCategoryWithCategoryDTO {
    
    @Schema(description = "关联ID")
    private Long id;
    
    @Schema(description = "电影ID")
    private Integer movieId;
    
    @Schema(description = "分类ID")
    private Integer categoryId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "分类信息")
    private CategoryInfo category;
    
    @Data
    @Schema(description = "分类基本信息")
    public static class CategoryInfo {
        @Schema(description = "分类ID")
        private Integer id;
        
        @Schema(description = "分类名称")
        private String name;
        
        @Schema(description = "分类描述")
        private String description;
        
        @Schema(description = "状态")
        private Integer status;
    }
} 