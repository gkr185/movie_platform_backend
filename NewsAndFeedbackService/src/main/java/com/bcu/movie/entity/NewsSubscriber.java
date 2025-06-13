package com.bcu.movie.entity;

import jakarta.persistence.*;
import lombok.Data;

// 资讯订阅用户实体类

@Entity
@Table(name = "news_subscriber")
public class NewsSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}