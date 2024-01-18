package com.point;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.point.database.Database;


public class IndexApp extends Application {

	private static Stage stage;
	
    @Override
    public void start(Stage s) throws IOException {
        stage=s;
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED); // Esto quita la barra de título y los botones de cerrar, minimizar y maximizar
        setRoot("login","");
    }

    
    public static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    
    public static void setRoot(String fxml, String title) throws IOException {
        Parent root = loadFXML(fxml);
    	Scene scene = new Scene(root);
    	
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IndexApp.class.getResource("../../fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    
    public static void main(String[] args) {
    	Database.connection(); 	
        launch(args);
    } 
    
}
