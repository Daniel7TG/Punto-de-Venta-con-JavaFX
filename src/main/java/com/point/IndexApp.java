package com.point;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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

    
    public static Object setRoot(String fxml, String title) throws IOException {
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(IndexApp.class.getClassLoader().getResource("fxml/" + fxml + ".fxml"));

    	Parent root = fxmlLoader.load();
    	Scene scene = new Scene(root);
    
    	setTheme(root);
    	
    	stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        
        return fxmlLoader.getController();
    }

    
//    private static Parent loadFXML(String fxml) throws IOException  {
//		
//    }

    
    public static void main(String[] args) {
    	Database.connection(); 	
        launch(args);
    } 
    
    
    public static void setTheme(Parent root) {
    	Theme theme = Theme.BLUE;
    	try {
			root.getStylesheets().add(IndexApp.class.getClassLoader().getResource("styles/ThemeBlue.css").toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	
//    	root.setStyle(
//    			String.format("* Button { \n"
//    			+ "-fx-background-color: %s;\n"
//    			+ "-fx-border-color: %s;\n"
//    			+ "-fx-text-fill: %s;\n"
//    			+ "}", 
//    			theme.getButtonBg(), theme.getButtonBorder(), theme.getButtonLabel() ) 
//    			);
    	
    }
    
    
    
    
    
    
    
}
