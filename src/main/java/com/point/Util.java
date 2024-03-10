package com.point;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import org.controlsfx.control.textfield.CustomTextField;

import com.point.controllers.MainController;
import com.point.models.SaleDetails;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	
	
	private static String centerText( String texto, int espacio ) {
		int tlength = texto.length();
		
		if(tlength > espacio) return "error";
		
		int margin = (espacio - tlength) / 2;
		
		String centeredText = 
				" ".repeat(margin) + 
				texto + 
				" ".repeat(margin) + 
				( margin % 2 == 0 ? "" : " " );
		
		return centeredText + "\n";
	}
	private static String formatProduct( SaleDetails details, int space) {
		String finalText = "";
		StringBuilder textoLeft = new StringBuilder(details.getProduct_name());
		int middle = space/2;
		
		if(textoLeft.length() > middle) {
			finalText = textoLeft.substring(0, middle) + "  ";
		}
		else {
//			int restante = middle - textoLeft.length();
			finalText = String.format( ("%-" + (middle + 2) + "s"), textoLeft.toString());
		}		
		
		finalText += String.format("%" + (middle-2) + "s", "$" + details.getSubtotal()) + "\n";

		
		return finalText;	
	}

	
	public static String generateTicket(ObservableList<SaleDetails> detailsList) {
		StringBuilder ticket = new StringBuilder("");
		ticket.append( centerText("Tienda", 35) );
		ticket.append( centerText("Tel: 55-5555-5555", 35) );
		ticket.append( centerText("Suc. CDMX", 35) );
		
		
		detailsList.forEach(detail -> ticket.append(formatProduct(detail, 35)));
		
		return String.valueOf(ticket);
	}
	
	public static Path getPath(String url){		
		try {
			return Path.of(new URI(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String pathToImage(String url){		
		return new File(url).toURI().toString();
	}
	
	
	public static void errorHighlight(Control ... nodes) {
		for(Control node : nodes) {			
			Border beforeBorder = node.getBorder();
			node.setBorder(redHighlight);
			Timer temporizador = new Timer();
			temporizador.schedule(new TimerTask() {
	            @Override
				public void run() {
	            	node.setBorder(beforeBorder);
	            	temporizador.cancel();
	            }   	
			}, 3000); 		
		
		}
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
	
	public static void summonChoosable(String msj, String ttl, Pane mainPane) {
		
		Stage stage = new Stage();
		BorderPane alertPane = new BorderPane();
		FlowPane options = new FlowPane();
		Label content = new Label( msj );
		Label title = new Label( ttl );
		Button confirmButton = new Button();
		Button denyButton = new Button();
		AtomicReference<Boolean> response = new AtomicReference<>(null);
	
		mainPane.setDisable(true);
		
		alertPane.setPadding(new Insets(10));
		alertPane.setStyle("-fx-background-color: rgb(205, 205, 205)");
		
		content.setMinHeight(50);
		content.setAlignment(Pos.CENTER_LEFT);
		content.setWrapText(true);
		
		confirmButton.setMaxWidth(100);
		confirmButton.setMaxHeight(40);
		confirmButton.setText("Confirmar");
		confirmButton.setOnAction(e -> {
			stage.close();
			response.set(Boolean.FALSE);
		});
		
		denyButton.setMaxWidth(100);
		denyButton.setMaxHeight(40);
		denyButton.setText("Cancelar");
		denyButton.requestFocus();
		
		options.setAlignment(Pos.CENTER_RIGHT);
		options.getChildren().addAll(confirmButton, denyButton);
		options.setHgap(10);
		
		alertPane.setTop(title);
		alertPane.setCenter(content);
		alertPane.setBottom(options);

		Scene scene = new Scene(alertPane);
		stage.setWidth(250);
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
        
        mainPane.setDisable(false);   
	}
	
	
	public static Button createButton(String caption, Double width, Double height, String ... styles) {
		Button btn = new Button();
		btn.getStyleClass().addAll(styles);
		btn.setText(caption);
		btn.setPrefWidth(width);
		btn.setMaxHeight(height);
		return btn;
	}
	

	public static void fixIntSpinner(Spinner<Integer> spinner, int defaultValue) {		
		spinner.getEditor().setTextFormatter(
		    new TextFormatter<Integer>(new IntegerStringConverter(), defaultValue, integerFilter));
	}
	public static void fixDoubleSpinner(Spinner<Double> spinner) {		
		spinner.getEditor().setTextFormatter(
				new TextFormatter<Double>(new DoubleStringConverter(), (double) 0, doubleFilter));
	}
	public static void fixTextArea(TextArea textArea) {
		textArea.setTextFormatter(
				new TextFormatter<String>(new DefaultStringConverter(), "Sin detalles", textFilter));
	}
	public static void fixTextField(CustomTextField textField) {
		textField.setTextFormatter(
				new TextFormatter<String>(new DefaultStringConverter(), "", textNumbersFilter));
	}
	
}
