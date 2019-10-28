package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    @Query("{ id: { $exists: true }}")
    Flux<Author> findAllAuthorsPaged(Pageable pageable);
}
