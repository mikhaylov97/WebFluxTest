package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Article> findAllArticlesPaged(Pageable pageable);
}
