package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.model.Author;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class ControllerTestUtil {

    static final String FIRST_AUTHOR_ID = "1";
    static final String FIRST_AUTHOR_NAME = "TestName";
    static final String FIRST_AUTHOR_LAST_NAME = "TestLastName";

    static final String SECOND_AUTHOR_ID = "2";
    static final String SECOND_AUTHOR_NAME = "TestName2";
    static final String SECOND_AUTHOR_LAST_NAME = "TestLastName2";

    static final String FIRST_ARTICLE_ID = "1";
    static final String FIRST_ARTICLE_TITLE = "First title";

    static final String SECOND_ARTICLE_ID = "2";
    static final String SECOND_ARTICLE_TITLE = "Second title";

    static final int AUTHOR_DEFAULT_PAGE_NUMBER = 0;
    static final int AUTHOR_DEFAULT_PAGE_SIZE = 5;

    static Article createFirstArticle() {
        Article article = new Article();
        article.setId(FIRST_ARTICLE_ID);
        article.setTitle(FIRST_ARTICLE_TITLE);
        article.setAuthor(createFirstAuthor());

        return article;
    }

    static Article createSecondArticle() {
        Article article = new Article();
        article.setId(SECOND_ARTICLE_ID);
        article.setTitle(SECOND_ARTICLE_TITLE);
        article.setAuthor(createSecondAuthor());

        return article;
    }

    static Author createFirstAuthor() {
        Author author = new Author();
        author.setId(FIRST_AUTHOR_ID);
        author.setName(FIRST_AUTHOR_NAME);
        author.setLastName(FIRST_AUTHOR_LAST_NAME);

        return author;
    }

    static Author createSecondAuthor() {
        Author author = new Author();
        author.setId(SECOND_AUTHOR_ID);
        author.setName(SECOND_AUTHOR_NAME);
        author.setLastName(SECOND_AUTHOR_LAST_NAME);

        return author;
    }

    static WebTestClient.ResponseSpec prepareWebClient(WebTestClient client, HttpMethod httpMethod, String uri, HttpStatus expectedStatus) {
        return prepareWebClient(client, httpMethod, uri, null, expectedStatus);
    }


    static <T> WebTestClient.ResponseSpec prepareWebClient(WebTestClient client, HttpMethod httpMethod, String uri, T body, HttpStatus expectedStatus) {
        switch (httpMethod) {
            case GET:
                return client.get()
                        .uri(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isEqualTo(expectedStatus);
            case POST:
                return client.post()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(body), body.getClass())
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isEqualTo(expectedStatus);
            case PATCH:
                return client.patch()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(body), body.getClass())
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isEqualTo(expectedStatus);
            case DELETE:
                return client.delete()
                        .uri(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isEqualTo(expectedStatus);
        }

        throw new IllegalArgumentException("Passed illegal http method. Should be GET or POST or PATCH or DELETE.");
    }

    enum HttpMethod {
        GET,
        POST,
        PATCH,
        DELETE
    }
}
