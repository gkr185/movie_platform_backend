package com.bcu.movie.service;

import com.bcu.movie.entity.News;

import java.util.List;

public interface NewsService {
    News createNews(News news);
    List<News> getAllNews();
    News getNewsById(Integer id);
    List<News> getNewsByCategory(Integer categoryId);
    List<News> getNewsByKeyword(String keyword);
    News updateNews(News news);
    void deleteNews(Integer id);
    void pushNewsToSubscribers(News news);
}
