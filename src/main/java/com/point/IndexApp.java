package com.point;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.point.controllers.LoginController;
import com.point.controllers.MainController;
import com.point.database.Database;
import com.point.models.Admin;
import com.point.models.Configuration;


public class IndexApp extends Application {

	public static final String GLOBAL = "styles/Global.css";
	private static final String THEME_BLUE = "styles/ThemeBlue.css";
	private static final String THEME_DARK = "styles/ThemeDark.css";
	private static final String THEME_RED = "styles/ThemeRed.css";
	private static final String THEME_SILVER = "styles/ThemeSilver.css";
	private static final List<String> STYLE_LIST = List.of(THEME_BLUE, THEME_RED, THEME_SILVER);
	private static Stage stage;
	
    @Override
    public void start(Stage s) throws IOException {
        stage=s;
        stage.setResizable(true);
        stage.initStyle(StageStyle.UNDECORATED); // Esto quita la barra de título y los botones de cerrar, minimizar y maximizar
        
        if(Configuration.getConfig().isSkipSession()) {
        	MainController controller = (MainController)IndexApp.setRoot("main", "main");
        	ResultSet result = Admin.get(Configuration.getConfig().getRememberUser());
        	try {
				controller.setActualAdmin(new Admin(
						result.getInt("id"),
						result.getString("first_name"),
						result.getString("last_name"),
						result.getString("username"),
						result.getString("password")
						));
			} catch (SQLException e) {}
        }
        else setRoot("login","");        	
		
    }

    
    public static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    
    public static Object setRoot(String fxml, String title) throws IOException {
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(IndexApp.class.getClassLoader().getResource("fxml/" + fxml + ".fxml"));
    	Parent root = fxmlLoader.load();
    	Scene scene = new Scene(root);
    	
    	if(Configuration.getConfig().getRememberUser() != ""  & fxml.equals("login")) 
    		((LoginController)fxmlLoader.getController()).preSetUsername(Configuration.getConfig().getRememberUser());
    	
    	setTheme(root, GLOBAL);
    	setStyles(root);
    	
    	try {
			root.getStylesheets().add(IndexApp.class.getClassLoader().getResource(GLOBAL).toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	
    	stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        
        return fxmlLoader.getController();
    }

    public static void updateStyle() {
    	setStyles((Parent)stage.getScene().getRoot());
    }
    public static void cleanStylesheets(Parent root) {
    	for(int i = 0; i < root.getStylesheets().size(); i++) {
    		String style = root.getStylesheets().get(i);
    		String[] array = style.split("/");
    		String stylePath = String.join("/", array[array.length-2], array[array.length-1]);
    		if(STYLE_LIST.contains(stylePath)) root.getStylesheets().remove(i);    		
    	}    		
    }
    
    public static void setStyles(Parent root) {
    	cleanStylesheets(root);
    	switch(Configuration.getConfig().getTheme()) {
    	case "Blue Theme":
    		setTheme(root, THEME_BLUE);
    		break;
    	case "Dark Theme":
    		setTheme(root, THEME_DARK);
    		break;
    	case "Red Theme":
    		setTheme(root, THEME_RED);
    		break;
    	case "Silver Theme":
    		setTheme(root, THEME_SILVER);
    		break;
    	}    
    }
    
    public static void main(String[] args) {
        launch(args);
    } 
    
    
    public static void setTheme(Parent root, String styles) {
    	try {
			root.getStylesheets().add(IndexApp.class.getClassLoader().getResource(styles).toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}    	
    }
    
    
    
    
    
    
    
}
