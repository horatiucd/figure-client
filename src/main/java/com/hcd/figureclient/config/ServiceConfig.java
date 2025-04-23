package com.hcd.figureclient.config;

import com.hcd.figureclient.service.interceptor.AuthInterceptor;
import com.hcd.figureclient.service.interceptor.LoggingInterceptor;
import com.hcd.figureclient.service.error.CustomResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Configuration
public class ServiceConfig {

    @Bean
    public RestOperations restTemplate(LoggingInterceptor loggingInterceptor,
                                       AuthInterceptor authInterceptor,
                                       CustomResponseErrorHandler customResponseErrorHandler) {
        RestTemplateCustomizer customizer = restTemplate -> restTemplate.getInterceptors()
                .addAll(List.of(loggingInterceptor, authInterceptor));

        return new RestTemplateBuilder(customizer)
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .errorHandler(customResponseErrorHandler)
                .build();
    }

    @Bean
    public RestClient restClient(@Value("${figure.service.url}") String url,
                                 LoggingInterceptor loggingInterceptor,
                                 AuthInterceptor authInterceptor,
                                 CustomResponseErrorHandler customResponseErrorHandler) {
        return RestClient.builder()
                .baseUrl(url)
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .requestInterceptor(loggingInterceptor)
                .requestInterceptor(authInterceptor)
                .defaultStatusHandler(customResponseErrorHandler)
                .build();
    }
}
