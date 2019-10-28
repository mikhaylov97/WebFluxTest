package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorMVCRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorMVCService {

    private final AuthorMVCRepository repository;

    public AuthorMVCService(AuthorMVCRepository repository) {
        this.repository = repository;
    }

    public Page<Author> findAllAuthors(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
