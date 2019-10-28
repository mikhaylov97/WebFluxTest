package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AuthorRepository authorRepository;

    public ArticleService(ArticleRepository articleRepository, AuthorRepository authorRepository) {
        this.articleRepository = articleRepository;
        this.authorRepository = authorRepository;
    }

    public Flux<Article> getAllArticlesPaged(final Pageable pageable) {
        return articleRepository.findAllArticlesPaged(pageable)
                .flatMap(article -> authorRepository.findById(article.getAuthorId()).map(author -> {
                    article.setAuthor(author);
                    return article;
                }).switchIfEmpty(Mono.just(article)));
    }

    public Flux<Article> getAllArticles() {
        return articleRepository
                .findAll()
                .flatMap(article -> authorRepository.findById(article.getAuthorId()).map(author -> {
                    article.setAuthor(author);
                    return article;
                }).switchIfEmpty(Mono.just(article)));
    }

    public Mono<Article> getArticle(String id) {
        Mono<Article> foundArticle = articleRepository.findById(id);
        return foundArticle
                .flatMap(article -> authorRepository.findById(article.getAuthorId()).map(author -> {
                    article.setAuthor(author);
                    return article;
                }).switchIfEmpty(foundArticle))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Incorrect id value!")));
    }

    public Mono<Article> createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Mono<Article> updateArticle(String id, Article item) {
        return articleRepository
                .findById(id)
                .map(article -> {
                    article.setTitle(item.getTitle());
                    article.setPublishedDate(item.getPublishedDate());
                    article.setAuthorId(item.getAuthorId());
                    return article;
                }).flatMap(articleRepository::save);
    }

    public Mono<Article> updateArticleAndReturnWithAuthor(String id, Article item) {
        return articleRepository
                .findById(id)
                .map(article -> {
                    article.setTitle(item.getTitle());
                    article.setPublishedDate(item.getPublishedDate());
                    article.setAuthorId(item.getAuthorId());
                    return article;
                })
                .flatMap(articleRepository::save)
                .flatMap(article -> authorRepository.findById(article.getAuthorId()).map(author -> {
                    article.setAuthor(author);
                    return article;
                }));
    }

    public Mono<Article> deleteArticle(String id) {
        return articleRepository
                .findById(id)
                .flatMap(article -> articleRepository.deleteById(id).thenReturn(article));
    }
}
