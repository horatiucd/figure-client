package com.hcd.figureclient.service;

import com.hcd.figureclient.dto.Figure;
import com.hcd.figureclient.dto.FigureRequest;

import java.util.List;
import java.util.Optional;

public interface FigureClient {

    List<Figure> allFigures();

    Optional<Figure> oneFigure(long id);

    Figure createFigure(FigureRequest figure);

    Figure updateFigure(long id, FigureRequest figureRequest);

    void deleteFigure(long id);

    Figure randomFigure();
}
