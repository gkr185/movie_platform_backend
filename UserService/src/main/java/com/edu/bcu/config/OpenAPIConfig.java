package com.edu.bcu.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    
    @Value("${server.port}")
    private String serverPort;

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户服务 API 文档")
                        .description("在线电影平台用户服务接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("BCU")
                                .email("support@bcu.edu.cn")
                                .url("https://www.bcu.edu.cn"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("用户服务 Wiki 文档")
                        .url("https://wiki.bcu.edu.cn"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("本地环境"),
                        new Server()
                                .url("https://api.bcu.edu.cn")
                                .description("生产环境")));
    }
} 