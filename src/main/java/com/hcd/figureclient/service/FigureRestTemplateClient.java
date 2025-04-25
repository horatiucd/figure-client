package com.hcd.figureclient.service;

import com.hcd.figureclient.service.dto.Figure;
import com.hcd.figureclient.service.dto.FigureRequest;
import com.hcd.figureclient.service.error.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Optional;

@Service
public class FigureRestTemplateClient implements FigureClient {

    private final String url;
    private final RestOperations restOperations;

    public FigureRestTemplateClient(@Value("${figure.service.url}") String url,
                                    RestOperations restOperations) {
        this.url = url;
        this.restOperations = restOperations;
    }

    @Override
    public List<Figure> allFigures() {
        ResponseEntity<Figure[]> response = restOperations.exchange(url,
                HttpMethod.GET, null, Figure[].class);

        Figure[] figures = response.getBody();
        if (figures == null) {
            throw new CustomException("Could not get the figures.");
        }
        return List.of(figures);
    }

    @Override
    public Optional<Figure> oneFigure(long id) {
        ResponseEntity<Figure> response = restOperations.exchange(url + "/{id}",
                HttpMethod.GET, null, Figure.class, id);

        Figure figure = response.getBody();
        if (figure == null) {
            return Optional.empty();
        }
        return Optional.of(figure);
    }

    @Override
    public Figure createFigure(FigureRequest figureRequest) {
        HttpEntity<FigureRequest> request = new HttpEntity<>(figureRequest);
        ResponseEntity<Figure> response = restOperations.exchange(url,
                HttpMethod.POST, request, Figure.class);

        Figure figure = response.getBody();
        if (figure == null) {
            throw new CustomException("Could not create figure.");
        }
        return figure;
    }

    @Override
    public Figure updateFigure(long id, FigureRequest figureRequest) {
        HttpEntity<FigureRequest> request = new HttpEntity<>(figureRequest);
        ResponseEntity<Figure> response = restOperations.exchange(url + "/{id}",
                HttpMethod.PUT, request, Figure.class, id);

        Figure figure = response.getBody();
        if (figure == null) {
            throw new CustomException("Could not update figure.");
        }
        return figure;
    }

    @Override
    public void deleteFigure(long id) {
        restOperations.exchange(url + "/{id}",
                HttpMethod.DELETE, null, Void.class, id);
    }

    @Override
    public Figure randomFigure() {
        ResponseEntity<Figure> response = restOperations.exchange(url + "/random",
                HttpMethod.GET, null, Figure.class);

        Figure figure = response.getBody();
        if (figure == null) {
            throw new CustomException("Could not get a random figure.");
        }
        return figure;
    }
}
