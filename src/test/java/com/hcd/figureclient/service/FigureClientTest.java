package com.hcd.figureclient.service;

import com.hcd.figureclient.service.dto.Figure;
import com.hcd.figureclient.service.dto.FigureRequest;
import com.hcd.figureclient.service.error.CustomClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class FigureClientTest {

    @Autowired
    private FigureRestClient figureClient;

    @Test
    void allFigures() {
        List<Figure> figures = figureClient.allFigures();
        Assertions.assertFalse(figures.isEmpty());
    }

    @Test
    void oneFigure() {
        long id = figureClient.allFigures().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No figures found"))
                .id();

        Optional<Figure> figure = figureClient.oneFigure(id);
        Assertions.assertTrue(figure.isPresent());
    }

    @Test
    void createFigure() {
        var request  = new FigureRequest( "Fig " + UUID.randomUUID());

        Figure figure = figureClient.createFigure(request);
        Assertions.assertNotNull(figure);
        Assertions.assertTrue(figure.id() > 0L);
        Assertions.assertEquals(request.name(), figure.name());

        CustomClientException ex = Assertions.assertThrows(CustomClientException.class,
                () -> figureClient.createFigure(request));
        Assertions.assertEquals("A Figure with the same 'name' already exists.", ex.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), ex.getStatusCode().value());
        Assertions.assertEquals("""
                {"title":"Bad Request","status":400,"detail":"A Figure with the same 'name' already exists.","instance":"/api/v1/figures"}""", ex.getBody());
    }

    @Test
    void updateFigure() {
        List<Figure> figures = figureClient.allFigures();
        long id = figures.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No figures found"))
                .id();

        var updatedRequest = new FigureRequest("Updated Fig " + UUID.randomUUID());
        Figure updatedFigure = figureClient.updateFigure(id, updatedRequest);
        Assertions.assertNotNull(updatedFigure);
        Assertions.assertEquals(id, updatedFigure.id());
        Assertions.assertEquals(updatedRequest.name(), updatedFigure.name());

        Figure otherExistingFigure = figures.stream()
                .filter(f -> f.id() != id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not enough figures"));

        var updateExistingRequest = new FigureRequest(otherExistingFigure.name());
        CustomClientException ex = Assertions.assertThrows(CustomClientException.class,
                () -> figureClient.updateFigure(id, updateExistingRequest));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getStatusCode().value());
    }

    @Test
    void deleteFigure() {
        long id = figureClient.allFigures().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No figures found"))
                .id();

        figureClient.deleteFigure(id);

        CustomClientException ex = Assertions.assertThrows(CustomClientException.class,
                () -> figureClient.deleteFigure(id));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), ex.getStatusCode().value());
        Assertions.assertEquals("Figure not found.", ex.getMessage());
    }

    @Test
    void randomFigure() {
        CustomClientException ex = Assertions.assertThrows(CustomClientException.class,
                () -> figureClient.randomFigure());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getStatusCode().value());
        Assertions.assertEquals("Not implemented yet.", ex.getMessage());
    }
}
