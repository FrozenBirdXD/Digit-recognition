package com.openjfx;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private Canvas canvas;
    @FXML
    private Button clearButton;

    public void drawDigit() {
        canvas.setOnMousePressed(event -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.setFill(Color.BLACK);
            gc.fillOval(event.getX(), event.getY(), 7, 7);
        });
        
        canvas.setOnMouseDragged(event -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.lineTo(event.getX(), event.getY());
            gc.fillOval(event.getX(), event.getY(), 7, 7);

        });
    }

    @FXML
    private void onClearButtonClick() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());     
    }
}
