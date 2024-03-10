package com.point.interfaces;

import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface DraggedScene {

    default void onDraggedScene(Pane panelFather) {
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        panelFather.setOnMousePressed(e -> {
            Stage stage = (Stage) panelFather.getScene().getWindow();
            xOffset.set(stage.getX() - e.getScreenX());
            yOffset.set(stage.getY() - e.getScreenY());

        });

        panelFather.setOnMouseDragged(e -> {
            Stage stage = (Stage) panelFather.getScene().getWindow();
            stage.setX(e.getScreenX() + xOffset.get());
            stage.setY(e.getScreenY() + yOffset.get());
            stage.setOpacity(0.8);
            panelFather.setCursor(Cursor.CLOSED_HAND);
            if(stage.isMaximized()) {
            	stage.setMaximized(false);
            }
        });

        panelFather.setOnMouseReleased(e -> {        	
        	Stage stage = (Stage) panelFather.getScene().getWindow();
        	panelFather.setCursor(Cursor.DEFAULT);
	        stage.setOpacity(1);
        });

    }
}
