package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorMVCService;
import com.example.demo.service.AuthorService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("author")
public class AuthorController {

    private final AuthorService service;
    private final AuthorMVCService mvcService;

    public AuthorController(AuthorService service, AuthorMVCService mvcService) {
        this.service = service;
        this.mvcService = mvcService;
    }

    @GetMapping("/page/reactive")
    public Flux<Author> getAuthorsPageReactive(Pageable pageable) {
        return service.getAllAuthorsPaged(pageable);
    }

    @GetMapping("/page")
    public List<Author> getAuthorsPage(Pageable pageable) {
        return mvcService.findAllAuthors(pageable).stream().collect(Collectors.toList());
    }

    @GetMapping
    public Flux<Author> getAllAuthors() {
        return service.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Mono<Author> getAuthorById(@PathVariable(name = "id") String id,
                                      ServerHttpResponse response) {
        return service.getAuthor(id).onErrorResume(error -> {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return Mono.empty();
        });
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
