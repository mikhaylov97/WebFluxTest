package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Configuration
public class WebFluxConfigurer implements org.springframework.web.reactive.config.WebFluxConfigurer {
    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {

        configurer.addCustomResolver(new ReactiveSortHandlerMethodArgumentResolver(),
                new ReactivePageableHandlerMethodArgumentResolver());
    }
}
