package com.example.demo.controller;

import com.example.demo.config.WebFluxConfigurer;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.example.demo.controller.ControllerTestUtil.*;
import static com.example.demo.controller.ControllerTestUtil.HttpMethod.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AuthorController.class)
@Import({AuthorService.class, WebFluxConfigurer.class})
public class AuthorControllerTest {

    @MockBean
    AuthorRepository authorRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetAllAuthorsPagedWithDefaultParams() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable pageable = PageRequest.of(AUTHOR_DEFAULT_PAGE_NUMBER,AUTHOR_DEFAULT_PAGE_SIZE);

        Mockito.when(authorRepository.findAllAuthorsPaged(pageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(pageable);
    }

    @Test
    void testGetAllAuthorsPagedWithDefaultSizeAndSort() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable pageable = PageRequest.of(1,5);

        Mockito.when(authorRepository.findAllAuthorsPaged(pageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=1", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(pageable);
    }

    @Test
    void testGetAllAuthorsPagedWithDefaultSort() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable pageable = PageRequest.of(2,3);

        Mockito.when(authorRepository.findAllAuthorsPaged(pageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=2&size=3", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(pageable);
    }

    @Test
    void testGetAllAuthorsPagedWithCustomParams() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable pageable = PageRequest.of(3,2, Sort.by("name").descending());

        Mockito.when(authorRepository.findAllAuthorsPaged(pageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=3&size=2&sort=name,desc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(pageable);
    }

    @Test
    void testGetAllAuthorsPagedWithCustomParamsAndMultipleSortAsMultipleParameters() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable pageable = PageRequest.of(3,2, Sort.by("name").descending().and(Sort.by("id").ascending()));

        Mockito.when(authorRepository.findAllAuthorsPaged(pageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=3&size=2&sort=name,desc&sort=id,asc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(pageable);
    }

    @Test
    void testGetAllAuthorsPagedWithMultipleSortAsOneParameterFirstCase() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(4,2, Sort.by("name").descending().and(Sort.by("id").descending()));

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=4&size=2&sort=name,id,desc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthorsPagedWithMultipleSortAsOneParameterSecondCase() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(4,2, Sort.by("name").descending()
                .and(Sort.by("asc").descending())
                .and(Sort.by("id").descending()));

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=4&size=2&sort=name,asc,id,desc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthorsPagedWithUnusualParamsOrder() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable pageable = PageRequest.of(1,2, Sort.by("name").descending().and(Sort.by("id").ascending()));

        Mockito.when(authorRepository.findAllAuthorsPaged(pageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?size=2&sort=name,desc&sort=id,asc&page=1", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(pageable);
    }

    @Test
    void testGetAllAuthorsPagedWithIncorrectPageNumber() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(AUTHOR_DEFAULT_PAGE_NUMBER,2, Sort.by("name").descending());

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=-1&size=2&sort=name,desc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthorsPagedWithIncorrectPageNumberAndSizeNumber() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(AUTHOR_DEFAULT_PAGE_NUMBER,AUTHOR_DEFAULT_PAGE_SIZE, Sort.by("name").descending());

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=-1&size=0&sort=name,desc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthorsPagedWithIncorrectPageNumberAndSizeNumberAndSortName() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(AUTHOR_DEFAULT_PAGE_NUMBER,AUTHOR_DEFAULT_PAGE_SIZE, Sort.by("nme").descending());

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=-5&size=-5&sort=nme,desc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthorsPagedWithIncorrectPageNumberAndSizeNumberAndSortOrder() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(AUTHOR_DEFAULT_PAGE_NUMBER,AUTHOR_DEFAULT_PAGE_SIZE, Sort.by("name").ascending().and(Sort.by("dessc").ascending()));

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?page=-5&size=-5&sort=name,dessc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthorsPagedWithIncorrectParameterNames() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Pageable realPageable = PageRequest.of(AUTHOR_DEFAULT_PAGE_NUMBER,AUTHOR_DEFAULT_PAGE_SIZE);

        Mockito.when(authorRepository.findAllAuthorsPaged(realPageable)).thenReturn(Flux.just(firstAuthor, secondAuthor));

        StepVerifier.create(ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author/page/reactive?pge=-5&sze=-5&sorrt=name,dessc", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstAuthor, secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findAllAuthorsPaged(realPageable);
    }

    @Test
    void testGetAllAuthors() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Mockito.when(authorRepository.findAll()).thenReturn(Flux.just(firstAuthor, secondAuthor));

        ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author", HttpStatus.OK)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(FIRST_AUTHOR_ID)
                .jsonPath("$.[0].name").isEqualTo(FIRST_AUTHOR_NAME)
                .jsonPath("$.[0].lastName").isEqualTo(FIRST_AUTHOR_LAST_NAME)
                .jsonPath("$.[1].id").isEqualTo(SECOND_AUTHOR_ID)
                .jsonPath("$.[1].name").isEqualTo(SECOND_AUTHOR_NAME)
                .jsonPath("$.[1].lastName").isEqualTo(SECOND_AUTHOR_LAST_NAME);

        Mockito.verify(authorRepository, times(1)).findAll();
    }

    @Test
    void testGetAuthorById() {
        Author author = createFirstAuthor();

        Mockito.when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/author/" + author.getId(), HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNextMatches(a -> a.getId().equals(FIRST_AUTHOR_ID)
                        && a.getName().equals(FIRST_AUTHOR_NAME)
                        && a.getLastName().equals(FIRST_AUTHOR_LAST_NAME))
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findById(author.getId());
    }

    @Test
    void testGetAuthorByIncorrectId() {
        Mockito.when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/author/" + FIRST_AUTHOR_ID, HttpStatus.NOT_FOUND)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
    }

    @Test
    void testCreateAuthor() {
        Author author = createFirstAuthor();

        Mockito.when(authorRepository.save(author)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, POST, "/author", author, HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).save(author);
    }

    @Test
    void testUpdateAuthorWithId() {
        Author firstAuthor = createFirstAuthor();
        Author secondAuthor = createSecondAuthor();
        secondAuthor.setId(FIRST_AUTHOR_ID);

        Mockito.when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Mono.just(firstAuthor));
        Mockito.when(authorRepository.save(secondAuthor)).thenReturn(Mono.just(secondAuthor));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/author/" + FIRST_AUTHOR_ID, secondAuthor, HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(secondAuthor)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
        Mockito.verify(authorRepository, times(1)).save(secondAuthor);
    }

    @Test
    void testUpdateAuthorWithIncorrectId() {
        Author secondAuthor = createSecondAuthor();
        secondAuthor.setId(FIRST_AUTHOR_ID);

        Mockito.when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/author/" + FIRST_AUTHOR_ID, secondAuthor, HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
    }

    @Test
    void testUpdateAuthor() {
        Author author = createFirstAuthor();

        Mockito.when(authorRepository.save(author)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/author", author, HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).save(author);
    }

    @Test
    void testDeleteAuthor() {
        Author author = createFirstAuthor();

        Mockito.when(authorRepository.findById(FIRST_AUTHOR_ID)).thenReturn(Mono.just(author));
        Mockito.when(authorRepository.deleteById(FIRST_AUTHOR_ID)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/author/" + FIRST_AUTHOR_ID, HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findById(FIRST_AUTHOR_ID);
        Mockito.verify(authorRepository, times(1)).deleteById(FIRST_AUTHOR_ID);
    }

    @Test
    void testDeleteAuthorWithReturnedVoid() {
        Author author = createFirstAuthor();

        Mockito.when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));
        Mockito.when(authorRepository.delete(author)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/author/" + author.getId() + "/void", HttpStatus.OK)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(authorRepository, times(1)).findById(author.getId());
        Mockito.verify(authorRepository, times(1)).delete(author);
    }
}
