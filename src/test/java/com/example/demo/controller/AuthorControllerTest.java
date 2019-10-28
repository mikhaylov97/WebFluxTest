package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class AuthorControllerTest {

    @MockBean
    AuthorService authorService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetAllAuthors() {
        Author firstAuthor = createFirstAuthor();

        Author secondAuthor = createSecondAuthor();

        Mockito.when(authorService.getAllAuthors()).thenReturn(Flux.just(firstAuthor, secondAuthor));

        ControllerTestUtil.
                prepareWebClient(webClient, GET, "/author")
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(FIRST_AUTHOR_ID)
                .jsonPath("$.[0].name").isEqualTo(FIRST_AUTHOR_NAME)
                .jsonPath("$.[0].lastName").isEqualTo(FIRST_AUTHOR_LAST_NAME)
                .jsonPath("$.[1].id").isEqualTo(SECOND_AUTHOR_ID)
                .jsonPath("$.[1].name").isEqualTo(SECOND_AUTHOR_NAME)
                .jsonPath("$.[1].lastName").isEqualTo(SECOND_AUTHOR_LAST_NAME);

        Mockito.verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void testGetAuthorById() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.getAuthor(FIRST_AUTHOR_ID)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/author/" + FIRST_AUTHOR_ID)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNextMatches(a -> a.getId().equals(FIRST_AUTHOR_ID)
                        && a.getName().equals(FIRST_AUTHOR_NAME)
                        && a.getLastName().equals(FIRST_AUTHOR_LAST_NAME))
                .verifyComplete();

        Mockito.verify(authorService, times(1)).getAuthor(FIRST_AUTHOR_ID);
    }

    @Test
    void testGetAuthorByIncorrectId() {
        Mockito.when(authorService.getAuthor(FIRST_AUTHOR_ID)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/author/" + FIRST_AUTHOR_ID)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(authorService, times(1)).getAuthor(FIRST_AUTHOR_ID);
    }

    @Test
    void testCreateAuthor() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.createAuthor(author)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, POST, "/author", author)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorService, times(1)).createAuthor(author);
    }

    @Test
    void testUpdateAuthorWithId() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.updateAuthor(FIRST_AUTHOR_ID, author)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/author/" + FIRST_AUTHOR_ID, author)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorService, times(1)).updateAuthor(FIRST_AUTHOR_ID, author);
    }

    @Test
    void testUpdateAuthorWithIncorrectId() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.updateAuthor(FIRST_AUTHOR_ID, author)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/author/" + FIRST_AUTHOR_ID, author)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(authorService, times(1)).updateAuthor(FIRST_AUTHOR_ID, author);
    }

    @Test
    void testUpdateAuthor() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.updateAuthor(author)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/author", author)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorService, times(1)).updateAuthor(author);
    }

    @Test
    void testDeleteAuthor() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.deleteAuthor(FIRST_AUTHOR_ID)).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/author/" + FIRST_AUTHOR_ID)
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        Mockito.verify(authorService, times(1)).deleteAuthor(FIRST_AUTHOR_ID);
    }

    @Test
    void testDeleteAuthorWithReturnedVoid() {
        Author author = createFirstAuthor();

        Mockito.when(authorService.deleteAuthorAndReturnVoid(FIRST_AUTHOR_ID)).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/author/" + FIRST_AUTHOR_ID + "/void")
                .returnResult(Author.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(authorService, times(1)).deleteAuthorAndReturnVoid(FIRST_AUTHOR_ID);
    }
}
