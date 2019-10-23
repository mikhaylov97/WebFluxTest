package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.*;

@Document
public class Article {

    @Id
    private String id;
    private String title;
    private LocalDate publishedDate;

    private String authorId;
    @Transient
    private Author author;

    public Article(String id, String title, LocalDate publishedDate, Author author) {
        this.id = id;
        this.title = title;
        this.publishedDate = publishedDate;
        this.author = author;
        this.authorId = ofNullable(author).map(Author::getId).orElseThrow(RuntimeException::new);
    }

    public Article(Author author) {
        this.title = title;
        this.publishedDate = LocalDate.now();
        this.author = author;
        this.authorId = ofNullable(author).map(Author::getId).orElseThrow(RuntimeException::new);
    }

    public Article() {
        this.publishedDate = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @JsonIgnore
    public String getAuthorId() {
        return authorId;
    }

    @JsonSetter
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
