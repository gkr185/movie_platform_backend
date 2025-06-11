package com.edu.bcu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "分类数据传输对象")
public class CategoryDTO {
    @Schema(description = "分类ID")
    private Integer id;

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", required = true, example = "动作片")
    private String name;
    
    @Schema(description = "分类描述", example = "包含各类动作、打斗场景的电影")
    private String description;
    
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;
    
    @Schema(description = "排序顺序", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "状态(0:禁用,1:启用)", example = "1")
    private Integer status;
} 