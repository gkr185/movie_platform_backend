package com.edu.bcu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI moviePlatformAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("电影平台分类服务 API")
                        .description("提供电影分类的管理功能，包括创建、更新、删除和查询分类信息")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BCU")
                                .email("support@bcu.edu.cn")
                                .url("https://www.bcu.edu.cn")));
    }
} 