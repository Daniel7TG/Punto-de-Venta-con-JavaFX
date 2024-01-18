package com.point.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;

import org.controlsfx.control.tableview2.TableView2;
import org.controlsfx.control.textfield.CustomTextField;

import com.point.interfaces.DraggedScene;
import com.point.models.Brand;
import com.point.models.Product;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController implements DraggedScene, Initializable{

	@FXML
	Button menuLeftMove,
	newSaleButton, newAdminButton, newProductButton, historyButton,
	changeProductButton,
	newProdImage,
	confirmProdButton,
	newProdImgButton, detailsProdImgButton;
	@FXML
	VBox menuPane, appPanel,
	selectedProduct;
	@FXML
	FlowPane topPanel,
	searchProductPane, createProductPane;
	@FXML
	AnchorPane newAdminPane, newSalePane, historyPane, productDetailsPane;
//	@FXML
//	ToggleGroup productActions;
//	@FXML 
//	ToggleButton filterProdsButton, createProdsButton;
	@FXML 
	BorderPane newProductPane;

	@FXML 
	TableView2<Product> tableProducts;
	@FXML 
	TableColumn<Product, Integer> idProductColumn, existenciasProductColumn;
	@FXML 
	TableColumn<Product, String> nombreProductColumn, marcaProductColumn, detallesProductColumn;
	@FXML 
	TableColumn<Product, Double> precioProductColumn;
	
	@FXML 
	Label notSelectedProduct;
	@FXML 
	ImageView imgProductDetails,
	newProdView;
	@FXML 
	TextField nameProductDetails;
	@FXML 
	Spinner<Integer> quantityProductDetails,
	newProdQuantity;
	@FXML 
	Spinner<Double> priceProductDetails,
	newProdPrice;
	@FXML 
	ComboBox<String> brandProductDetails,
	newProdBrandCb;
	@FXML 
	TextArea detailsProductDetails,
	newProdDetails;
	@FXML 
	CustomTextField newProdName, newProdBrandTxt;
	
	SpinnerValueFactory<Double> priceSpinner;
	SpinnerValueFactory<Integer> quantitySpinner;
	SpinnerValueFactory<Double> newPriceSpinner;
	SpinnerValueFactory<Integer> newQuantitySpinner;
	
	private Stage stage;
	boolean oppened = false;
	
	private final float 
	WIDTH_MAIN_PANEL = 300,
	WIDTH_VISIBLE_BUTTONS_PANEL = 70,
	WIDTH_PANEL_MOVEMENT = WIDTH_MAIN_PANEL - WIDTH_VISIBLE_BUTTONS_PANEL;
	
	private final Image DEFAULT_IMAGE = new Image("img/default.jpg");
	private final String IMAGE_PATH = System.getProperty("user.dir") + "/src/main/resources/img";
	private final FileChooser fileChooser = new FileChooser();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		onDraggedScene(topPanel);

		fileChooser.getExtensionFilters().add(new ExtensionFilter("IMG files (*.jpg)", "*.jpg", "*.png"));
		
		initializeProducts();	
		

	}
	
		
	public void saveChangeButton() {
		Product product = tableProducts.getSelectionModel().getSelectedItem();
		Long id = product.getId();
		
		Product updatedProduct = new Product(
			id,
			nameProductDetails.getText(),
			priceSpinner.getValue(),
			brandProductDetails.getSelectionModel().getSelectedItem(),
			imgProductDetails.getImage().getUrl(),
			detailsProductDetails.getText(),
			quantitySpinner.getValue()
				);
		
		Product.updateProduct(updatedProduct, id);
		updateTableProducts();
		tableProducts.getSelectionModel().select(updatedProduct);
	}
	
	
	// Cambia la imagen 
	public void selectProdImage(ActionEvent event) {
		String uniqueID;
		Path destinationPath;
		Image image;		
		ImageView view = event.getSource().equals(detailsProdImgButton) ? imgProductDetails : newProdView;
		
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			try {
				// Crea la ruta con el nuevo nombre de imagen
				uniqueID = UUID.randomUUID().toString();
				destinationPath = Path.of( IMAGE_PATH, uniqueID + ".jpg");

				// Si no es la imagen por default
				if (!view.getImage().getUrl().equals(DEFAULT_IMAGE.getUrl())) {
					String[] array = view.getImage().getUrl().split("/");
					String name = array[array.length-1];
					Files.deleteIfExists( Path.of(IMAGE_PATH, name) );
				}
				
				Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.COPY_ATTRIBUTES);
				image = new Image(destinationPath.toUri().toString());
				view.setImage(image);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	
	// Guarda un producto con los datos registrados
	public void confirmProduct() {
		if(!Brand.existBrand(newProdBrandTxt.getText())) {
			Brand.createBrand(newProdBrandTxt.getText());
		}
		Product product = new Product(
				0L,
				newProdName.getText(),
				newPriceSpinner.getValue(),
				newProdBrandTxt.getText(),
				newProdView.getImage().getUrl() != null ? newProdView.getImage().getUrl() : "",
				newProdDetails.getText(),
				newQuantitySpinner.getValue()
				);
		Product.saveProduct(product);
		updateTableProducts();
		updateComboBoxes();
		tableProducts.getSelectionModel().select(product);
	}
	
	// Acciones de la barra superior
	public void maximizeApp() {
		checkStage();
		if(stage.isMaximized()) {
			stage.setMaximized(false);
		} else {
			stage.setMaximized(true);			
		}		
	}
	public void minimizeApp() {
		checkStage();
		stage.setIconified(true);			
	}
	public void closeApp() {
		checkStage();
		stage.close();		
	}
	
	// Sí no se ha definido el stage, lo define
	public void checkStage() {
		if(stage == null) {
			stage = (Stage) topPanel.getScene().getWindow();
		}
	}
	// Desactiva todos los paneles principales 
	private void setAllDisable() {
		newAdminPane.setVisible(false);
		newAdminPane.setDisable(true);

		newProductPane.setVisible(false); 
		newProductPane.setDisable(true);
		
		historyPane.setVisible(false);
		historyPane.setDisable(true);
		
		newSalePane.setVisible(false);
		newSalePane.setDisable(true);
	}
	// Desplaza las opciones laterales del menu 
	public void contract(){	
		TranslateTransition transition = new TranslateTransition();
		menuLeftMove.setDisable(true);
		if(oppened) {
			oppened = false;
			transition.setByX(-WIDTH_PANEL_MOVEMENT);
		}
		else {
			oppened = true;
			transition.setByX(WIDTH_PANEL_MOVEMENT);
		}
		transition.setNode(menuPane);
		transition.setOnFinished(e -> {
			menuLeftMove.setDisable(false);
		});		
		
		transition.play();		
	}
	// Cambia el panel principal despues de seleccionar una opcion 
	public void changeView(ActionEvent event) {
		Button source = (Button) event.getSource();
		setAllDisable();
		if(newSaleButton.equals(source)) {
			newSalePane.setVisible(true);
			newSalePane.setDisable(false);			
		} else if(newAdminButton.equals(source)) {
			newAdminPane.setVisible(true);
			newAdminPane.setDisable(false);
		} else if(historyButton.equals(source)) {
			historyPane.setVisible(true);
			historyPane.setDisable(false);			
		} else {			
			newProductPane.setVisible(true); 
			newProductPane.setDisable(false);
		}
	}

	
	// Panel Productos 
	// Inicializa todos los recursos para el panel de Productos
	public void initializeProducts() {
		// Columnas de la tabla Productos
		idProductColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer> ("id"));
		existenciasProductColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer> ("quantity"));
		nombreProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("name"));
		marcaProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("brand"));
		detallesProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("details"));
		precioProductColumn.setCellValueFactory(new PropertyValueFactory<Product, Double> ("price"));
		
		// Spinners de productos
		priceSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000);
		quantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		newPriceSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000);
		newQuantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		quantityProductDetails.setValueFactory(quantitySpinner);
		priceProductDetails.setValueFactory(priceSpinner);
		newProdQuantity.setValueFactory(newQuantitySpinner);
		newProdPrice.setValueFactory(newPriceSpinner);
		
		// Actualiza panel de detalles segun lo seleccionado en la tabla
		tableProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, product) -> {
		    if(notSelectedProduct.isVisible()) {
		    	notSelectedProduct.setVisible(false);
		    	selectedProduct.setVisible(true);
		    	selectedProduct.setDisable(false);
		    }	
			if (product != null) {
				Image image = new Image(product.getImagen());	
		    	imgProductDetails.setImage(image);
		    	nameProductDetails.setText(product.getName());
		    	quantitySpinner.setValue(product.getQuantity());
		    	priceSpinner.setValue(product.getPrice());
		    	brandProductDetails.setValue(product.getBrand());
		    	detailsProductDetails.setText(product.getDetails());
		    }
		});
		
		// Sincroniza el input de texto y combobox
		newProdBrandCb.getSelectionModel().selectedItemProperty().addListener( (obs, oldSelection, newSelection) ->{
			newProdBrandTxt.setText(newSelection);
		});
		newProdBrandTxt.textProperty().addListener( (event)->{
			newProdBrandCb.valueProperty().set(newProdBrandTxt.getText());
		});
		
		updateTableProducts();
		updateComboBoxes();		
	}
	// Actualiza los datos de la tabla desde la db
	public void updateTableProducts() {
		ObservableList<Product> loadedProducts = Product.getAllProducts();
		tableProducts.setItems(loadedProducts);
	}
	// Actualiza los datos de las marcas desde la db
	public void updateComboBoxes() {
		newProdBrandCb.setItems(Brand.getAllBrands());
		brandProductDetails.setItems(Brand.getAllBrands());
	}
	
}














