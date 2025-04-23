package com.hcd.figureclient.service.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public AuthInterceptor(@Value("${figure.service.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders()
                .add("x-api-key", apiKey);
        return execution.execute(request, body);
    }
}
