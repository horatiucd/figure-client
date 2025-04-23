package com.hcd.figureclient.service.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        logRequest(body);

        ClientHttpResponse response = execution.execute(request, body);

        logResponse(response);
        return response;
    }

    private void logRequest(byte[] body) {
        var bodyContent = new String(body);
        log.debug("Request body : {}", bodyContent);
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        var bodyContent = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
        log.debug("Response body: {}", bodyContent);
    }
}
