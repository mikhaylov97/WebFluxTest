package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleMVCRepository extends PagingAndSortingRepository<Article, String> {
}
