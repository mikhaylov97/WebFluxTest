package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleMVCRepository;
import com.example.demo.repository.AuthorMVCRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticleMVCService {

    private final ArticleMVCRepository repository;
    private final AuthorMVCRepository authorRepository;

    public ArticleMVCService(ArticleMVCRepository repository, AuthorMVCRepository authorRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
    }

    public Page<Article> findAllArticles(Pageable pageable) {
        return repository.findAll(pageable).map(p -> {
            authorRepository.findById(p.getAuthorId()).ifPresent(p::setAuthor);
            return p;
        });
    }
}
