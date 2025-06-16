package com.edu.bcu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {
    private String category; // avatar, poster, video, banner, news, ad
    private Long relatedId; // 关联的业务ID
    private String description; // 文件描述
}