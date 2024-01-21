package com.point.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.tableview2.TableView2;
import org.controlsfx.control.textfield.CustomTextField;

import com.point.IndexApp;
import com.point.Util;
import com.point.interfaces.DraggedScene;
import com.point.models.Brand;
import com.point.models.Product;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
	VBox menuPane, appPanel,
	selectedProduct;
	@FXML
	FlowPane topPanel,
	searchProductPane, createProductPane;
	@FXML
	AnchorPane newAdminPane, newSalePane, historyPane, productDetailsPane;
	@FXML 
	BorderPane newProductPane;
	@FXML 
	Tab filterProdTab, createProdTab;

	@FXML 
	TableView2<Product> tableProducts;
	@FXML 
	TableColumn<Product, Integer> idProductColumn, existenciasProductColumn;
	@FXML 
	TableColumn<Product, String> nombreProductColumn, marcaProductColumn, detallesProductColumn;
	@FXML 
	TableColumn<Product, Double> precioProductColumn;

	@FXML
	Button menuLeftMove,
	newSaleButton, newAdminButton, newProductButton, historyButton,
	changeProductButton,
	newProdImage,
	confirmProdButton,
	newProdImgButton, detailsProdImgButton;	
	@FXML 
	Label notSelectedProduct;
	@FXML 
	ImageView imgProductDetails,
	newProdView;
	@FXML 
	Spinner<Integer> quantityProductDetails,
	newProdQuantity;
	@FXML 
	Spinner<Double> priceProductDetails,
	newProdPrice, filterRangeLeft, filterRangeRight;
	@FXML 
	ComboBox<String> brandProductDetails,
	newProdBrandCb;
	@FXML 
	TextArea detailsProductDetails,
	newProdDetails;
	@FXML 
	CustomTextField nameProductDetails, newProdName, newProdBrandTxt, brandProductDetailsTxt, filterProdName;
	@FXML 
	CheckComboBox<String> filterProdBrand;
	@FXML 
	RangeSlider filterRangeBar;


	private Stage stage;
	boolean oppened = false;

	private final float 
	WIDTH_MAIN_PANEL = 300,
	WIDTH_VISIBLE_BUTTONS_PANEL = 70,
	WIDTH_PANEL_MOVEMENT = WIDTH_MAIN_PANEL - WIDTH_VISIBLE_BUTTONS_PANEL;


	// path para dev
	//	private final Path IMAGE_PATH = Paths.get(System.getProperty("user.dir") + "/src/main/resources/img");


	// Products Panel
	private final String DEFAULT_IMAGE = MainController.class.getResource("/img/default.jpg").toString(); 

	private final Path IMAGE_PATH = Paths.get("img");

	private SpinnerValueFactory<Double> priceSpinner, newPriceSpinner, leftFilterSpinner, rightFilterSpinner ;
	private SpinnerValueFactory<Integer> quantitySpinner, newQuantitySpinner;
	private final FileChooser fileChooser = new FileChooser();	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		onDraggedScene(topPanel);

		if (!Files.exists(IMAGE_PATH)) {
			try {
				Files.createDirectories(IMAGE_PATH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		fileChooser.getExtensionFilters().add(new ExtensionFilter("IMG files (*.jpg)", "*.jpg", "*.png"));

		initializeProducts();	

	}




	// Guarda un producto con los datos registrados
	public void confirmProduct() {	

		if(filterProdTab.isSelected()) {

			searchProducts();

		} else { 
			String image = saveImage(newProdView);
			saveProduct(0L, newProdName, newPriceSpinner, newProdBrandTxt, image, newProdDetails, newQuantitySpinner);
		}
	}
	
	public void searchProducts(){
			
		// name puede ser ""
		// price puede ser MIN a MAX
		// brand puede ser todas
		tableProducts.getSelectionModel().clearSelection();
		notSelectedProduct.setVisible(true);
		selectedProduct.setVisible(false);
		selectedProduct.setDisable(true);
		String name = filterProdName.getText();
		Double min = filterRangeBar.getLowValue();
		Double max = filterRangeBar.getHighValue();
		ObservableList<String> listBrand = filterProdBrand.checkModelProperty().get().getCheckedItems();
		
		ObservableList<Product> loadedProducts = Product.getFilteredProducts(name, min, max, listBrand);

		tableProducts.setItems(loadedProducts);			
		
	}
	public void clearFilters() {
		filterProdName.setText("");
		leftFilterSpinner.setValue(filterRangeBar.getMin());
		rightFilterSpinner.setValue(filterRangeBar.getMax());
		
		filterRangeBar.setHighValue(filterRangeBar.getMax());
		filterRangeBar.setLowValue(filterRangeBar.getMin());
		filterProdBrand.getCheckModel().clearChecks();
		updateTableProducts();
	}
	
	// actualiza el producto, borra la imagen
	public void saveChangeButton() {
		Product product = tableProducts.getSelectionModel().getSelectedItem();
		String image = product.getImagen();
		try {
			// si cambia la imagen
			if( !imgProductDetails.getImage().getUrl().equals(product.getImagen()) ) {
				if(!product.getImagen().equals(DEFAULT_IMAGE)) {				
					Files.deleteIfExists( Util.getPath(image));
				}
				image = saveImage(imgProductDetails);							
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		saveProduct(product.getId(), nameProductDetails, priceSpinner, brandProductDetailsTxt, image, detailsProductDetails, quantitySpinner);
	}

	
	// guarda el producto, crea la marca, actualiza los nodos
	private void saveProduct(Long id, CustomTextField name, SpinnerValueFactory<Double> price, CustomTextField brand, String image, TextArea details, SpinnerValueFactory<Integer> quantity) {
		if(!Brand.existBrand(brand.getText())) Brand.createBrand(brand.getText());

		Product product = new Product(
				id,
				name.getText(),
				price.getValue(),
				brand.getText(),
				image,
				details.getText(),
				quantity.getValue()
				);

		Product.saveProduct(product);
		clearFilters(); // incluye actualizar tabla
		updateComboBoxes();
		updateFilterRange();
		tableProducts.getSelectionModel().select(product);
	}

	
	// copia la imagen hasta una la carpeta /img, si no es la default genera uniqueID 
	private String saveImage(ImageView view) {

		String uniqueID;
		Path destinationPath;

		if(view.getImage().getUrl().equals(DEFAULT_IMAGE)) return DEFAULT_IMAGE;

		uniqueID = UUID.randomUUID().toString();
		destinationPath = IMAGE_PATH.resolve(uniqueID + ".jpg");
		try {
			Files.copy( Util.getPath(view.getImage().getUrl()) , destinationPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Util.pathToImage(destinationPath.toString());
	}


	// solo cambia la imagen del view 
	public void selectProdImage(ActionEvent event) {
		Image image;		
		ImageView view = event.getSource().equals(detailsProdImgButton) ? imgProductDetails : newProdView;
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			image = new Image(selectedFile.toURI().toString());
			view.setImage(image);
		}
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
		
		// Spinners de productos
		priceSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000);
		quantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		newPriceSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000);
		newQuantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		
		quantityProductDetails.setValueFactory(quantitySpinner);
		priceProductDetails.setValueFactory(priceSpinner);
		newProdQuantity.setValueFactory(newQuantitySpinner);
		newProdPrice.setValueFactory(newPriceSpinner);

		// Sincroniza el input de texto y combobox
		newProdBrandCb.getSelectionModel().selectedItemProperty().addListener( (obs, oldSelection, newSelection) ->{
			newProdBrandTxt.setText(newSelection);
		});
		newProdBrandTxt.textProperty().addListener( (event)->{
			newProdBrandCb.valueProperty().set(newProdBrandTxt.getText());
		});		
		brandProductDetails.getSelectionModel().selectedItemProperty().addListener( (obs, oldSelection, newSelection) ->{
			brandProductDetailsTxt.setText(newSelection);
		});
		brandProductDetailsTxt.textProperty().addListener( (event)->{
			brandProductDetails.valueProperty().set(brandProductDetailsTxt.getText());
		});
		
		
		// spinner de filtro
		updateFilterRange();
		

		
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
		ObservableList<String> list = Brand.getAllBrands();
		filterProdBrand.getItems().clear();
		filterProdBrand.getItems().addAll(list);
		newProdBrandCb.setItems(list);
		brandProductDetails.setItems(list);
	}
	public void updateFilterRange() {
		Double[] prices = Product.getPricesRange();
		Double min = prices[0];
		Double max = prices[1];
		leftFilterSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, min, 1);
		rightFilterSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, max, 1);
		filterRangeLeft.setValueFactory(leftFilterSpinner);
		filterRangeRight.setValueFactory(rightFilterSpinner);
		filterRangeBar.setMax(max);
		filterRangeBar.setMin(min);
		
		leftFilterSpinner.valueProperty().addListener((Observable o) -> {
			filterRangeBar.setLowValue((double) leftFilterSpinner.getValue());
		});
		rightFilterSpinner.valueProperty().addListener((Observable o) -> {
			filterRangeBar.setHighValue((double) rightFilterSpinner.getValue());
		});
		
		filterRangeBar.highValueProperty().addListener((Observable o) -> {
			rightFilterSpinner.setValue(filterRangeBar.getHighValue());
		});
		filterRangeBar.lowValueProperty().addListener((Observable o) -> {
			leftFilterSpinner.setValue(filterRangeBar.getLowValue());
		});
	}

}














