package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthorMVCRepository extends PagingAndSortingRepository<Author, String> {
}
