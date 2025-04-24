package com.hcd.figureclient.service.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseErrorHandler.class);

    private final ObjectMapper objectMapper;

    public CustomResponseErrorHandler() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        String body = new String(response.getBody().readAllBytes());

        if (statusCode.is4xxClientError()) {
            throw new CustomClientException("Client error.", statusCode, body);
        }

        String message = null;
        try {
            message = objectMapper.readValue(body, ErrorResponse.class).detail();
        } catch (JsonProcessingException e) {
            log.error("Failed to parse response body: {}", e.getMessage(), e);
        }

        throw new CustomClientException(message, statusCode, body);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ErrorResponse(String detail) {}
}
