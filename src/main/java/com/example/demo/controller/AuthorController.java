package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("author")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Author> getAllAuthors() {
        return service.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Mono<Author> getAuthorById(@PathVariable(name = "id") String id) {
        return service.getAuthor(id);
    }

    @PostMapping
    public Mono<Author> createAuthor(@RequestBody Author author) {
        return service.createAuthor(author);
    }

    // From this entry point certain author will be updated if can be found in database by provided id,
    // otherwise nothing will happen.
    @PatchMapping("/{id}")
    public Mono<Author> updateAuthorWithId(@PathVariable(name = "id") String id,
                                           @RequestBody Author author) {
        return service.updateAuthor(id, author);
    }

    // From this entry point author will be updated with field-to-field strategy.
    // it means, that existing author should be updated only if received author data contains id,
    // otherwise new author object with generated id will be created.
    @PatchMapping()
    public Mono<Author> updateAuthor(@RequestBody Author author) {
        return service.updateAuthor(author);
    }

    // From this entry point author will be deleted from database (if exist) and removed item
    // should be returned.
    @DeleteMapping("/{id}")
    public Mono<Author> deleteAuthor(@PathVariable(name = "id") String id) {
        return service.deleteAuthor(id);
    }

    // From this entry point author will be deleted from database (if exist) and empty response
    // should be returned.
    @DeleteMapping("/{id}/void")
    public Mono<Void> deleteAuthorAndReturnVoid(@PathVariable(name = "id") String id) {
        return service.deleteAuthorAndReturnVoid(id);
    }
}
