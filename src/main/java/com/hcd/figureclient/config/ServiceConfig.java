package com.hcd.figureclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcd.figureclient.service.operations.AuthInterceptor;
import com.hcd.figureclient.service.operations.CustomResponseErrorHandler;
import com.hcd.figureclient.service.operations.LoggingInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Configuration
public class ServiceConfig {

    @Bean
    public RestTemplateCustomizer templateCustomizer(LoggingInterceptor loggingInterceptor,
                                                     AuthInterceptor authInterceptor) {
        return restTemplate -> restTemplate.getInterceptors()
                .addAll(List.of(loggingInterceptor, authInterceptor));
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new CustomResponseErrorHandler(new ObjectMapper());
    }

    @Bean
    public RestOperations restTemplate(LoggingInterceptor loggingInterceptor,
                                       AuthInterceptor authInterceptor) {
        return new RestTemplateBuilder(templateCustomizer(loggingInterceptor, authInterceptor))
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .errorHandler(responseErrorHandler())
                .build();
    }
}
