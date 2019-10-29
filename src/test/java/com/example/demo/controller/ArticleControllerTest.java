package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebFluxTest(controllers = ArticleController.class)
public class ArticleControllerTest {

    @MockBean
    ArticleService articleService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetAllArticles() {
        Article firstArticle = createFirstArticle();

        Article secondArticle = createSecondArticle();

        Mockito.when(articleService.getAllArticles()).thenReturn(Flux.just(firstArticle, secondArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/article", HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle, secondArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).getAllArticles();
    }

    @Test
    void testGetArticleById() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleService.getArticle(FIRST_ARTICLE_ID)).thenReturn(Mono.just(firstArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/article/" + FIRST_ARTICLE_ID, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).getArticle(FIRST_ARTICLE_ID);
    }

    @Test
    void testCreateArticle() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleService.createArticle(firstArticle)).thenReturn(Mono.just(firstArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, POST, "/article", firstArticle, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).createArticle(firstArticle);
    }

    @Test
    void testUpdateArticle() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleService.updateArticle(FIRST_ARTICLE_ID, firstArticle)).thenReturn(Mono.just(firstArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + FIRST_ARTICLE_ID, firstArticle, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).updateArticle(FIRST_ARTICLE_ID, firstArticle);
    }

    @Test
    void testUpdateArticleAndReturnWithAuthor() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleService.updateArticleAndReturnWithAuthor(FIRST_ARTICLE_ID, firstArticle)).thenReturn(Mono.just(firstArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + FIRST_ARTICLE_ID + "/full", firstArticle, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).updateArticleAndReturnWithAuthor(FIRST_ARTICLE_ID, firstArticle);
    }

    @Test
    void testDeleteArticle() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleService.deleteArticle(FIRST_ARTICLE_ID)).thenReturn(Mono.just(firstArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/article/" + FIRST_ARTICLE_ID, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleService, times(1)).deleteArticle(FIRST_ARTICLE_ID);
    }
}
