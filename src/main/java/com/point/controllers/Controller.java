package com.point.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller implements Initializable {

	Stage stage;

	private double x, y;

	public Controller(Stage stage) {
		this.stage = stage;
		this.x = 0;
		this.y = 0;
	}

	public void setDraggable(Pane panel) {
		panel.setOnMousePressed((MouseEvent event) ->{
            x = event.getSceneX();
            y = event.getSceneY();
        });      
		panel.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);            
            stage.setOpacity(.8);
        });       
		panel.setOnMouseReleased((MouseEvent event) ->{
            stage.setOpacity(1);
        });
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
	}
	
}
