package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

// Is needed for initialize Pageable object within Controller.
@Configuration
@EnableSpringDataWebSupport
public class PaginationConfiguration {
}
