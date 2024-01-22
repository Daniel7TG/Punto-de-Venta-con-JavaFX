package com.point;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Util {
	
//	private static String redHighlight = "-fx-border-color: red";
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
//	static UnaryOperator<Change> doubleFilter = change -> {
//		String newText = change.getControlNewText();
//		
//		if(newText.length() == 0) {
//			change.setText("0");
//			return change;					
//		}
//		if (newText.matches("^[0-9]+(\\.[0-9]*)?$")) { 
//			try{ 
//				if(Double.parseDouble(newText) > 100000 | newText.length() > 6) return null;
//				else return change;
//			} catch(NumberFormatException e) {
//				change.setText("0");
//				return change;
//			}
//		}
//		return null;
//	};
	
	
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
	

	public static void fixIntSpinner(Spinner<Integer> spinner) {		
		spinner.getEditor().setTextFormatter(
		    new TextFormatter<Integer>(new IntegerStringConverter(), 10, integerFilter));
	}
	public static void fixDoubleSpinner(Spinner<Double> spinner) {		
		spinner.getEditor().setTextFormatter(
				new TextFormatter<Double>(new DoubleStringConverter(), (double) 10, doubleFilter));
	}
	
}
