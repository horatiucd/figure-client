package com.hcd.figureclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcd.figureclient.service.operations.CustomResponseErrorHandler;
import com.hcd.figureclient.service.operations.LoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;

@Configuration
public class RestConfig {

    public static final Logger log = LoggerFactory.getLogger(RestConfig.class);

    @Bean
    public RestTemplateCustomizer templateCustomizer() {
        ClientHttpRequestInterceptor interceptor = new LoggingInterceptor();
        return restTemplate -> restTemplate.getInterceptors().add(interceptor);
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new CustomResponseErrorHandler(new ObjectMapper());
    }

    @Bean
    public RestOperations restTemplate() {
        return new RestTemplateBuilder(templateCustomizer())
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .errorHandler(responseErrorHandler())
                .build();
    }
}
