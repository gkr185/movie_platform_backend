package com.edu.bcu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "批量电影分类关联数据传输对象")
public class BatchMovieCategoryDTO {
    
    @NotNull(message = "电影ID不能为空")
    @Schema(description = "电影ID", required = true, example = "1")
    private Integer movieId;
    
    @NotNull(message = "分类ID列表不能为空")
    @Schema(description = "分类ID列表", required = true, example = "[1, 2, 3]")
    private List<Integer> categoryIds;
} 