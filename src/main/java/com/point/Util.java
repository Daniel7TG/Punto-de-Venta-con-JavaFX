package com.point;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

import org.controlsfx.control.textfield.CustomTextField;

import com.point.controllers.MainController;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Util {
	
	
//	private static String redHighlight = "-fx-border-color: red";
	private static AnchorPane menuPaneAnchor;
	private static AnchorPane contentMain;
	
	private static Border redHighlight = new Border(
			new BorderStroke( 
				Color.RED,
	            BorderStrokeStyle.SOLID,
	            new CornerRadii(4),
	            BorderStroke.DEFAULT_WIDTHS){}
			);
	
	static UnaryOperator<Change> integerFilter = change -> {
		String newText = change.getControlNewText();
		if (newText.matches("([0-9]*)?")) { 
			try{ 
				if(Integer.parseInt(newText) > 100000 | newText.length() > 6) return null;
				else return change;
			} catch(NumberFormatException e) {
				change.setText("0");
				return change;
			}
		}
		return null;
	};
	static UnaryOperator<Change> doubleFilter = change -> {
		String newText = change.getControlNewText();
		
		if(newText.length() == 0) {
			change.setText("0");
			return change;					
		}
		if (newText.matches("^[0-9]+(\\.[0-9]*)?$")) { 
			if(newText.length() <= 8) return change;
		}
		return null;
	};
	static UnaryOperator<Change> textFilter = change -> {
		change.setText(change.getText().replace('\n', ' '));
		return change;
	};
	static UnaryOperator<Change> textNumbersFilter = change -> {
		if(change.getControlNewText().matches("([0-9]*)?")) return change;
		return null;
	};
	
	
	public static void initialize(AnchorPane contentPane, AnchorPane leftPane) {
		contentMain = contentPane;
		menuPaneAnchor = leftPane;
	}
	
	
	public static Path getPath(String url){		
		try {
			return Path.of(new URI(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.out.println("error en GetPath");
		}
		return null;
	}
	
	
	public static String pathToImage(String url){		
		return new File(url).toURI().toString();
	}
	
	
	public static void errorHighlight(Control node) {
		Timer temporizador = new Timer();		
		node.setBorder(redHighlight);
		
		temporizador.schedule(new TimerTask() {
            @Override
			public void run() {
            	node.setBorder(new Border(
            			new BorderStroke( 
            					Color.TRANSPARENT,
            		            BorderStrokeStyle.SOLID,
            		            new CornerRadii(4),
            		            BorderStroke.THIN){}
            				)
            			);
            	temporizador.cancel();
            }   	
		}, 3000); 		
		
	}
	
	
	public static void summonAlert(String msj, Color[] colors, int amount) {

		Label alert = new Label(msj);
		alert.setBorder(new Border(
			new BorderStroke( 
				Color.TRANSPARENT,
	            BorderStrokeStyle.SOLID,
	            new CornerRadii(10),
	            BorderStroke.DEFAULT_WIDTHS)
			));
		alert.setBackground(new Background(new BackgroundFill( colors[0], new CornerRadii(10), Insets.EMPTY))); 
		alert.setTextFill(colors[1]);
		alert.setFont(new Font("Montserrat", 14));
		alert.setLayoutX(85 - 300);
		alert.setLayoutY(30 + (60 * amount));
		alert.setPadding(new Insets(10));
		alert.setPrefWidth(330);
		alert.setPrefHeight(50);
		contentMain.getChildren().add(alert);
		menuPaneAnchor.toFront();
		
		FadeTransition ft = new FadeTransition(Duration.millis(2000), alert);
		ft.setDelay(Duration.millis(5000));
		ft.setFromValue(1.0);
		ft.setToValue(0);
		ft.setOnFinished(e -> {
			contentMain.getChildren().remove(alert);
		});
		ft.play();
	}
	

	public static void fixIntSpinner(Spinner<Integer> spinner) {		
		spinner.getEditor().setTextFormatter(
		    new TextFormatter<Integer>(new IntegerStringConverter(), 10, integerFilter));
	}
	public static void fixDoubleSpinner(Spinner<Double> spinner) {		
		spinner.getEditor().setTextFormatter(
				new TextFormatter<Double>(new DoubleStringConverter(), (double) 10, doubleFilter));
	}
	public static void fixTextArea(TextArea textArea) {
		textArea.setTextFormatter(
				new TextFormatter<String>(new DefaultStringConverter(), "sin detalles", textFilter));
	}
	public static void fixTextField(CustomTextField textField) {
		textField.setTextFormatter(
				new TextFormatter<String>(new DefaultStringConverter(), "", textNumbersFilter));
	}
	
}
