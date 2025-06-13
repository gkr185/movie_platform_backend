package com.bcu.movie.Controller;


import com.bcu.movie.Filter.HtmlFilter;
import com.bcu.movie.entity.News;
import com.bcu.movie.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// 资讯控制器
@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @PostMapping("/create")
    public News createNews(@RequestBody News news) {
        news.setContent(HtmlFilter.filterHtml(news.getContent()));
        return newsService.createNews(news);
    }

    @GetMapping("/all")
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable Integer id) {
        return newsService.getNewsById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<News> getNewsByCategory(@PathVariable Integer categoryId) {
        return newsService.getNewsByCategory(categoryId);
    }

    @GetMapping("/keyword/{keyword}")
    public List<News> getNewsByKeyword(@PathVariable String keyword) {
        return newsService.getNewsByKeyword(keyword);
    }

    @PutMapping("/update")
    public News updateNews(@RequestBody News news) {
        return newsService.updateNews(news);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNews(@PathVariable Integer id) {
        newsService.deleteNews(id);
    }
}
