package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.model.Author;
import com.example.demo.service.ArticleService;
import com.example.demo.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ArticleController.class)
public class ArticleControllerTest {

    private static final String FIRST_AUTHOR_ID = "1";
    private static final String FIRST_AUTHOR_NAME = "TestName";
    private static final String FIRST_AUTHOR_LAST_NAME = "TestLastName";

    private static final String SECOND_AUTHOR_ID = "2";
    private static final String SECOND_AUTHOR_NAME = "TestName2";
    private static final String SECOND_AUTHOR_LAST_NAME = "TestLastName2";

    private static final String FIRST_ARTICLE_ID = "1";
    private static final String FIRST_ARTICLE_TITLE = "First title";

    private static final String SECOND_ARTICLE_ID = "2";
    private static final String SECOND_ARTICLE_TITLE = "Second title";

    private static final LocalDate CURRENT_DATE = LocalDate.of(2019, 10, 28);

    @MockBean
    ArticleService articleService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetAllArticles() {
        Author firstAuthor = new Author();
        firstAuthor.setId(FIRST_AUTHOR_ID);
        firstAuthor.setName(FIRST_AUTHOR_NAME);
        firstAuthor.setLastName(FIRST_AUTHOR_LAST_NAME);

        Article firstArticle = new Article();
        firstArticle.setId(FIRST_ARTICLE_ID);
        firstArticle.setPublishedDate(CURRENT_DATE);
        firstArticle.setTitle(FIRST_ARTICLE_TITLE);
        firstArticle.setAuthor(firstAuthor);

        Author secondAuthor = new Author();
        secondAuthor.setId(SECOND_AUTHOR_ID);
        secondAuthor.setName(SECOND_AUTHOR_NAME);
        secondAuthor.setLastName(SECOND_AUTHOR_LAST_NAME);

        Article secondArticle = new Article();
        secondArticle.setId(SECOND_ARTICLE_ID);
        secondArticle.setPublishedDate(CURRENT_DATE);
        secondArticle.setTitle(SECOND_ARTICLE_TITLE);
        secondArticle.setAuthor(secondAuthor);

        Mockito.when(articleService.getAllArticles()).thenReturn(Flux.just(firstArticle, secondArticle));

        StepVerifier.create(webClient.get()
                .uri("/article")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle, secondArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).getAllArticles();
    }
}
