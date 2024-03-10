package com.point.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.point.interfaces.DraggedScene;
import com.point.models.Configuration;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigurationController implements Initializable, DraggedScene {

	@FXML
	GridPane configPane;
	@FXML
	CheckBox confSkipSession, confRemUser, confAutoProd, confAlerts;
	@FXML
	ComboBox<String> confTheme;
	@FXML
	Spinner<Integer> confSellAmount;
	
	public int adminId;
	SpinnerValueFactory<Integer> spinnerValue;
	private String username;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		onDraggedScene(configPane);
		
		spinnerValue = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
		confSellAmount.setValueFactory(spinnerValue);
		
		confTheme.setItems(FXCollections.observableArrayList("BlueTheme", "nothing"));
		loadConfig();
	}
	
	public void loadConfig() {
	
		Configuration config = Configuration.getConfig();
		confTheme.getSelectionModel().select(config.getTheme());
		confSkipSession.setSelected(config.isSkipSession());
		confRemUser.setSelected(!config.getRememberUser().equals(""));
		confAutoProd.setSelected(config.isAutoProducts());
		confAlerts.setSelected(config.isActivateAlerts());
		confSellAmount.getValueFactory().setValue(config.getSaleDefAmount());

	}
	

	public void closeConfig() {
		Stage stage = (Stage) configPane.getScene().getWindow();
		stage.close();
	}
	
	public void saveConfig() {
		
		Configuration config = new Configuration(
				confTheme.getSelectionModel().getSelectedItem(),
				confSkipSession.isSelected(),
				confRemUser.isSelected() ? username : "",
				confAutoProd.isSelected(),
				confAlerts.isSelected(),
				confSellAmount.getValue()
				);
		Configuration.saveConfig(config);
		
		closeConfig();
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
