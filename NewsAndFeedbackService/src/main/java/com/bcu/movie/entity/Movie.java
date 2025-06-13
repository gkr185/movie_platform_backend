package com.bcu.movie.entity;

import jakarta.persistence.*;

// 电影实体类
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String poster_url; // 修改为 poster_url

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_url() { // 修改为 getPoster_url
        return poster_url;
    }

    public void setPoster_url(String poster_url) { // 修改为 setPoster_url
        this.poster_url = poster_url;
    }
}