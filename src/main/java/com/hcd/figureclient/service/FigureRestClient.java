package com.hcd.figureclient.service;

import com.hcd.figureclient.service.dto.Figure;
import com.hcd.figureclient.service.dto.FigureRequest;
import com.hcd.figureclient.service.error.CustomClientException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class FigureRestClient implements FigureClient {

    private final RestClient restClient;

    public FigureRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<Figure> allFigures() {
        var figures = restClient.get()
                .retrieve()
                .body(Figure[].class);

        if (figures == null) {
            throw new CustomClientException("Could not get the figures.");
        }
        return List.of(figures);
    }

    @Override
    public Optional<Figure> oneFigure(long id) {
        var figure = restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(Figure.class);

        return Optional.ofNullable(figure);
    }

    @Override
    public Figure createFigure(FigureRequest figureRequest) {
        var figure = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(figureRequest)
                .retrieve()
                .body(Figure.class);

        if (figure == null) {
            throw new CustomClientException("Could not create figure.");
        }
        return figure;
    }

    @Override
    public Figure updateFigure(long id, FigureRequest figureRequest) {
        var figure = restClient.put()
                .uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(figureRequest)
                .retrieve()
                .body(Figure.class);

        if (figure == null) {
            throw new CustomClientException("Could not update figure.");
        }
        return figure;
    }

    @Override
    public void deleteFigure(long id) {
        restClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Figure randomFigure() {
        var figure = restClient.get()
                .uri("/random")
                .retrieve()
                .body(Figure.class);

        if (figure == null) {
            throw new CustomClientException("Could not get a random figure.");
        }
        return figure;
    }
}
