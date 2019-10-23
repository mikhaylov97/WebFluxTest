package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public void getArticleById(@PathVariable(name = "id") String id) {

    }

    @PostMapping()
    public void createArticle(@RequestBody Article article) {
        articleService.createArticle(article);
    }

    @PutMapping
    public void updateArticle() {

    }

    @DeleteMapping
    public void deleteArticle() {

    }
}
