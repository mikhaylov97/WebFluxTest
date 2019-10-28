package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Flux<Author> getAllAuthors() {
        return repository.findAll();
    }

    public Mono<Author> getAuthor(String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new IllegalArgumentException("Incorrect id value!")));
    }

    public Mono<Author> createAuthor(Author author) {
        return repository.save(author);
    }

    public Mono<Author> updateAuthor(Author author) {
        return repository.save(author);
    }

    public Mono<Author> updateAuthor(String id, Author item) {
        return repository
                .findById(id)
                .map(author -> {
                    author.setName(item.getName());
                    author.setLastName(item.getLastName());
                    return author;
                })
                .flatMap(repository::save);
    }

    public Mono<Author> deleteAuthor(String id) {
        return repository
                .findById(id)
                .flatMap(author -> repository.deleteById(id).thenReturn(author));
    }

    public Mono<Void> deleteAuthorAndReturnVoid(String id) {
        return repository
                .findById(id)
                .flatMap(repository::delete);
    }
}
