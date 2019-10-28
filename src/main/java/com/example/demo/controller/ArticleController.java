package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public Flux<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Mono<Article> getArticleById(@PathVariable(name = "id") String id,
                                        ServerHttpResponse response) {
        return articleService.getArticle(id).onErrorResume(error -> {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return Mono.empty();
        });
    }

    @PostMapping()
    public Mono<Article> createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    // From this entry point updated and returned article won't contain author data.
    @PatchMapping("/{id}")
    public Mono<Article> updateArticle(@PathVariable(name = "id") String id,
                                       @RequestBody Article article) {
        return articleService.updateArticle(id, article);
    }

    // From this entry point updated and returned article will contain author data.
    @PatchMapping("/{id}/full")
    public Mono<Article> updateArticleAndReturnWithAuthor(@PathVariable(name = "id") String id,
                                                          @RequestBody Article article) {
        return articleService.updateArticleAndReturnWithAuthor(id, article);
    }

    @DeleteMapping("/{id}")
    public Mono<Article> deleteArticle(@PathVariable(name = "id") String id) {
        return articleService.deleteArticle(id);
    }
}
