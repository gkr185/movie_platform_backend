package com.bcu.movie.service.impl;

import com.bcu.movie.entity.News;
import com.bcu.movie.entity.NewsCategory;
import com.bcu.movie.entity.NewsSubscriber;
import com.bcu.movie.repository.NewsCategoryRepository;
import com.bcu.movie.repository.NewsRepository;
import com.bcu.movie.repository.NewsSubscriberRepository;
import com.bcu.movie.service.NewsService;
import jakarta.transaction.Transactional;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.commonmark.parser.Parser;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private NewsSubscriberRepository newsSubscriberRepository;

    @Override
    @Transactional
    public News createNews(News news) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(news.getContent());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);
        news.setContent(htmlContent);
        news.setPublishTime(new Date());

        News savedNews = newsRepository.save(news);
        pushNewsToSubscribers(savedNews);
        return savedNews;
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News getNewsById(Integer id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public List<News> getNewsByCategory(Integer categoryId) {
        NewsCategory category = newsCategoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            return newsRepository.findByCategory(category);
        }
        return null;
    }

    @Override
    public List<News> getNewsByKeyword(String keyword) {
        return newsRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    @Override
    @Transactional
    public News updateNews(News news) {
        News existingNews = newsRepository.findById(news.getId()).orElse(null);
        if (existingNews != null) {
            existingNews.setTitle(news.getTitle());
            existingNews.setContent(news.getContent());
            existingNews.setAuthor(news.getAuthor());
            existingNews.setSource(news.getSource());
            existingNews.setCover_image(news.getCover_image());
            existingNews.setIs_top(news.getIs_top());
            existingNews.setStatus(news.getStatus());
            return newsRepository.save(existingNews);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteNews(Integer id) {
        newsRepository.deleteById(id);
    }

    @Override
    public void pushNewsToSubscribers(News news) {
        List<NewsSubscriber> subscribers = newsSubscriberRepository.findAll();
        for (NewsSubscriber subscriber : subscribers) {
            // 模拟站内消息，使用日志记录
            logger.info("向订阅用户 {} 推送新资讯：{}", subscriber.getUserId(), news.getTitle());
        }
    }
}