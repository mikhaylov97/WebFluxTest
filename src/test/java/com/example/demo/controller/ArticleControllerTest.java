package com.example.demo.controller;

import com.example.demo.config.WebFluxConfigurer;
import com.example.demo.model.Article;
import com.example.demo.model.Author;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.example.demo.controller.ControllerTestUtil.HttpMethod.*;
import static com.example.demo.controller.ControllerTestUtil.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ArticleController.class)
@Import({ArticleService.class, WebFluxConfigurer.class})
public class ArticleControllerTest {

    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    AuthorRepository authorRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetAllArticles() {
        Article firstArticleWithoutAuthor = createFirstArticleWithoutAuthor();
        Article firstArticleWithAuthor = createFirstArticle();
        Author firstAuthor = firstArticleWithAuthor.getAuthor();

        Article secondArticleWithoutAuthor = createSecondArticleWithoutAuthor();
        Article secondArticleWithAuthor = createSecondArticle();
        Author secondAuthor = secondArticleWithAuthor.getAuthor();

        Mockito.when(articleRepository.findAll()).thenReturn(Flux.just(firstArticleWithoutAuthor, secondArticleWithoutAuthor));
        Mockito.when(authorRepository.findById(firstArticleWithoutAuthor.getAuthorId())).thenReturn(Mono.just(firstAuthor));
        Mockito.when(authorRepository.findById(secondArticleWithoutAuthor.getAuthorId())).thenReturn(Mono.just(secondAuthor));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/article", HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticleWithAuthor)
                .expectNext(secondArticleWithAuthor)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findAll();
        Mockito.verify(authorRepository, times(1)).findById(firstArticleWithoutAuthor.getAuthorId());
        Mockito.verify(authorRepository, times(1)).findById(secondArticleWithoutAuthor.getAuthorId());
    }

    @Test
    void testGetArticleById() {
        Article articleWithoutAuthor = createFirstArticleWithoutAuthor();
        Article articleWithAuthor = createFirstArticle();
        Author author = articleWithAuthor.getAuthor();

        Mockito.when(articleRepository.findById(articleWithoutAuthor.getId())).thenReturn(Mono.just(articleWithoutAuthor));
        Mockito.when(authorRepository.findById(articleWithoutAuthor.getAuthorId())).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/article/" + articleWithoutAuthor.getId(), HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(articleWithAuthor)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(articleWithoutAuthor.getId());
        Mockito.verify(authorRepository, times(1)).findById(articleWithoutAuthor.getAuthorId());
    }

    @Test
    void testGetArticleByIdWhenAuthorNotFound() {
        Article articleWithoutAuthor = createFirstArticleWithoutAuthor();
        Article articleToBeChecked = createFirstArticleWithoutAuthor();
        articleToBeChecked.setAuthorId(null);

        Mockito.when(articleRepository.findById(articleWithoutAuthor.getId())).thenReturn(Mono.just(articleWithoutAuthor));
        Mockito.when(authorRepository.findById(articleWithoutAuthor.getAuthorId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/article/" + articleWithoutAuthor.getId(), HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(articleToBeChecked)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(articleWithoutAuthor.getId());
        Mockito.verify(authorRepository, times(1)).findById(articleWithoutAuthor.getAuthorId());
    }

    @Test
    void testGetArticleByIdWhenArticleNotFound() {
        Article articleWithoutAuthor = createFirstArticleWithoutAuthor();

        Mockito.when(articleRepository.findById(articleWithoutAuthor.getId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, GET, "/article/" + articleWithoutAuthor.getId(), HttpStatus.NOT_FOUND)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(articleWithoutAuthor.getId());
    }

    @Test
    void testCreateArticle() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleRepository.save(firstArticle)).thenReturn(Mono.just(firstArticle));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, POST, "/article", firstArticle, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).save(firstArticle);
    }

    @Test
    @Disabled
    void testUpdateArticle() {
        Article storedArticleWithAuthor = createFirstArticle();
        Article storedArticleWithoutAuthor = createFirstArticleWithoutAuthor();

        Article updatedArticle = createSecondArticle();
        updatedArticle.setId(storedArticleWithAuthor.getId());

        Article updatedArticleWithoutAuthor = createSecondArticleWithoutAuthor();
        updatedArticle.setId(storedArticleWithAuthor.getId());
        updatedArticle.setAuthorId(null);

        Mockito.when(articleRepository.findById(storedArticleWithAuthor.getId())).thenReturn(Mono.just(storedArticleWithoutAuthor));
        Mockito.when(articleRepository.save(updatedArticleWithoutAuthor)).thenReturn(Mono.just(updatedArticleWithoutAuthor));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + storedArticleWithAuthor.getId(), storedArticleWithoutAuthor, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(updatedArticle)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(storedArticleWithAuthor.getId());
        Mockito.verify(articleRepository, times(1)).save(updatedArticleWithoutAuthor);
    }

    @Test
    void testUpdateArticleWhenArticleNotFound() {
        Article storedArticleWithAuthor = createFirstArticle();

        Article updatedArticle = createSecondArticleWithoutAuthor();
        updatedArticle.setId(storedArticleWithAuthor.getId());

        Mockito.when(articleRepository.findById(storedArticleWithAuthor.getId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + storedArticleWithAuthor.getId(), updatedArticle, HttpStatus.NOT_FOUND)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(storedArticleWithAuthor.getId());
    }

    @Test
    @Disabled
    void testUpdateArticleAndReturnWithAuthor() {
        Article storedArticleWithAuthor = createFirstArticle();
        Article storedArticleWithoutAuthor = createFirstArticleWithoutAuthor();

        Article updatedArticle = createSecondArticle();
        updatedArticle.setId(storedArticleWithAuthor.getId());
        Author author = updatedArticle.getAuthor();

        Mockito.when(articleRepository.findById(storedArticleWithAuthor.getId())).thenReturn(Mono.just(storedArticleWithoutAuthor));
        Mockito.when(articleRepository.save(updatedArticle)).thenReturn(Mono.just(updatedArticle));
        Mockito.when(authorRepository.findById(updatedArticle.getAuthorId())).thenReturn(Mono.just(author));

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + storedArticleWithAuthor.getId() + "/full", updatedArticle, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(updatedArticle)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(storedArticleWithAuthor.getId());
        Mockito.verify(articleRepository, times(1)).save(updatedArticle);
        Mockito.verify(authorRepository, times(1)).findById(updatedArticle.getAuthorId());
    }

    @Test
    @Disabled
    void testUpdateArticleAndReturnWithAuthorWhenAuthorNotFound() {
        Article storedArticleWithAuthor = createFirstArticle();
        Article storedArticleWithoutAuthor = createFirstArticleWithoutAuthor();

        Article updatedArticle = createSecondArticle();
        updatedArticle.setId(storedArticleWithAuthor.getId());

        Article updatedArticleWithoutAuthor = createSecondArticleWithoutAuthor();
        updatedArticle.setId(storedArticleWithAuthor.getId());

        Mockito.when(articleRepository.findById(storedArticleWithAuthor.getId())).thenReturn(Mono.just(storedArticleWithoutAuthor));
        Mockito.when(articleRepository.save(updatedArticle)).thenReturn(Mono.just(updatedArticle));
        Mockito.when(authorRepository.findById(updatedArticle.getAuthorId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + storedArticleWithAuthor.getId() + "/full", updatedArticle, HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(updatedArticleWithoutAuthor)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(storedArticleWithAuthor.getId());
        Mockito.verify(articleRepository, times(1)).save(updatedArticle);
        Mockito.verify(authorRepository, times(1)).findById(updatedArticle.getAuthorId());
    }

    @Test
    void testUpdateArticleAndReturnWithAuthorWhenArticleNotFound() {
        Article articleWithoutAuthor = createFirstArticleWithoutAuthor();

        Mockito.when(articleRepository.findById(articleWithoutAuthor.getId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, PATCH, "/article/" + articleWithoutAuthor.getId() + "/full", articleWithoutAuthor, HttpStatus.NOT_FOUND)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(articleWithoutAuthor.getId());
    }

    @Test
    void testDeleteArticle() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleRepository.findById(firstArticle.getId())).thenReturn(Mono.just(firstArticle));
        Mockito.when(articleRepository.deleteById(firstArticle.getId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/article/" + firstArticle.getId(), HttpStatus.OK)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .expectNext(firstArticle)
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(firstArticle.getId());
        Mockito.verify(articleRepository, times(1)).deleteById(firstArticle.getId());
    }

    @Test
    void testDeleteArticleWhenArticleNotFound() {
        Article firstArticle = createFirstArticle();

        Mockito.when(articleRepository.findById(firstArticle.getId())).thenReturn(Mono.empty());

        StepVerifier.create(ControllerTestUtil
                .prepareWebClient(webClient, DELETE, "/article/" + firstArticle.getId(), HttpStatus.NOT_FOUND)
                .returnResult(Article.class)
                .getResponseBody()
        )
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(articleRepository, times(1)).findById(firstArticle.getId());
    }
}
