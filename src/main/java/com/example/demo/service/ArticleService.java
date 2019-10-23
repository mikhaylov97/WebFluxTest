package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AuthorRepository authorRepository;

    public ArticleService(ArticleRepository articleRepository, AuthorRepository authorRepository) {
        this.articleRepository = articleRepository;
        this.authorRepository = authorRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll().stream()
                .peek(a -> authorRepository.findById(a.getAuthorId()).ifPresent(a::setAuthor))
                .collect(Collectors.toList());
    }

    public void createArticle(Article article) {
        articleRepository.save(article);
    }
}
