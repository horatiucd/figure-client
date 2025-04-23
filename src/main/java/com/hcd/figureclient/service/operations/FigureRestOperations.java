package com.hcd.figureclient.service.operations;

import com.hcd.figureclient.service.CustomClientException;
import com.hcd.figureclient.service.FigureClient;
import com.hcd.figureclient.dto.Figure;
import com.hcd.figureclient.dto.FigureRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Optional;

@Service
public class FigureRestOperations implements FigureClient {

    private final String url;
    private final String apiKey;
    private final RestOperations restOperations;

    public FigureRestOperations(@Value("${figure.service.url}") String url,
                                @Value("${figure.service.api.key}") String apiKey,
                                RestOperations restOperations) {
        this.url = url;
        this.apiKey = apiKey;
        this.restOperations = restOperations;
    }

    @Override
    public List<Figure> allFigures() {
        HttpEntity<?> request = new HttpEntity<>(headers());
        ResponseEntity<Figure[]> response = restOperations.exchange(url,
                HttpMethod.GET, request, Figure[].class);

        Figure[] figures = response.getBody();
        if (figures == null) {
            throw new CustomClientException("Could not get the figures.");
        }
        return List.of(figures);
    }

    @Override
    public Optional<Figure> oneFigure(long id) {
        HttpEntity<?> request = new HttpEntity<>(headers());
        ResponseEntity<Figure> response = restOperations.exchange(url + "/{id}",
                HttpMethod.GET, request, Figure.class, id);

        Figure figure = response.getBody();
        if (figure == null) {
            return Optional.empty();
        }
        return Optional.of(figure);
    }

    @Override
    public Figure createFigure(FigureRequest figureRequest) {
        HttpEntity<FigureRequest> request = new HttpEntity<>(figureRequest, headers());
        ResponseEntity<Figure> response = restOperations.exchange(url,
                HttpMethod.POST, request, Figure.class);

        Figure figure = response.getBody();
        if (figure == null) {
            throw new CustomClientException("Could not create figure.");
        }
        return figure;
    }

    @Override
    public Figure updateFigure(long id, FigureRequest figureRequest) {
        HttpEntity<FigureRequest> request = new HttpEntity<>(figureRequest, headers());
        ResponseEntity<Figure> response = restOperations.exchange(url + "/{id}",
                HttpMethod.PUT, request, Figure.class, id);

        Figure figure = response.getBody();
        if (figure == null) {
            throw new CustomClientException("Could not update figure.");
        }
        return figure;
    }

    @Override
    public void deleteFigure(long id) {
        HttpEntity<?> request = new HttpEntity<>(headers());
        restOperations.exchange(url + "/{id}",
                HttpMethod.DELETE, request, Void.class, id);
    }

    @Override
    public Figure randomFigure() {
        HttpEntity<?> request = new HttpEntity<>(headers());
        ResponseEntity<Figure> response = restOperations.exchange(url + "/random",
                HttpMethod.GET, request, Figure.class);

        Figure figure = response.getBody();
        if (figure == null) {
            throw new CustomClientException("Could not get a random figure.");
        }
        return figure;
    }

    private HttpHeaders headers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        return headers;
    }
}
