package com.point.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.point.IndexApp;
import com.point.database.Database;
import com.point.interfaces.DraggedScene;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// TENGO QUE CAMBIAR QUE USERNAME SEA UNIQUE 

public class LoginController implements DraggedScene, Initializable {

	@FXML
	Button loginButton;
	@FXML
	AnchorPane loginPane;
	@FXML
	TextField loginUsername;
	@FXML
	PasswordField loginPassword;
	
	PreparedStatement statement; 
	Connection connect;
	ResultSet result;
	Alert alert;
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	onDraggedScene(loginPane);
	}
	
	public void login() {
		
		connect = Database.getConnect();
		
		try {
			statement = connect.prepareStatement("SELECT password FROM admin WHERE username = ?");
			statement.setString(1, loginUsername.getText());
			result = statement.executeQuery();			
			
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al Iniciar Sesion");

			if(!result.next()) {
		        alert.setContentText("Usuario no existe");
		        alert.showAndWait();
		        return;
			} 
			if(Database.verifyPassword(loginPassword.getText(), result.getString("password"))) {
				IndexApp.setRoot("main", "main");	
			} else {
				alert.setContentText("Contraseña Incorrecta");
				alert.showAndWait();
			}
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		
	}

}
