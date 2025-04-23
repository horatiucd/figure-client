package com.hcd.figureclient.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CustomClientException extends RuntimeException {

    private final HttpStatusCode statusCode;
    private final String body;

    public CustomClientException(String message) {
        super(message);
        this.statusCode = HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        this.body = null;
    }

    public CustomClientException(String message, HttpStatusCode statusCode, String body) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
