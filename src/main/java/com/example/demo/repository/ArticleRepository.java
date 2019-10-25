package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {
}
