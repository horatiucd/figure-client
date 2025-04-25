package com.hcd.figureclient.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CustomException extends RuntimeException {

    private final HttpStatusCode statusCode;
    private final String detail;

    public CustomException(String message) {
        super(message);
        this.statusCode = HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        this.detail = null;
    }

    public CustomException(String message, HttpStatusCode statusCode, String detail) {
        super(message);
        this.statusCode = statusCode;
        this.detail = detail;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getDetail() {
        return detail;
    }
}
