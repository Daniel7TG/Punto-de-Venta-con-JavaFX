package com.point.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Consumer;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.tableview2.TableView2;
import org.controlsfx.control.textfield.CustomTextField;

import com.point.Util;
import com.point.interfaces.DraggedScene;
import com.point.models.Brand;
import com.point.models.Product;
import com.point.models.SaleDetails;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class MainController implements DraggedScene, Initializable{

	// New Sale 
	@FXML
	BorderPane newSalePane;
	@FXML
	Label ticketPane;
	@FXML
	Spinner<Double> paymentSale;
	@FXML
	Spinner<Integer> quantitySale;
	@FXML
	CustomTextField totalCostSale, idSale;
	@FXML
	TableView2<SaleDetails> tableSale;
	@FXML
	TableColumn<SaleDetails, Long> columnIdSale;
	@FXML
	TableColumn<SaleDetails, Integer> columnQuantitySale;
	@FXML
	TableColumn<SaleDetails, String> columnNameSale;
	@FXML
	TableColumn<SaleDetails, Double> columnPriceSale, columnTotalSale;
	
	
	@FXML
	VBox menuPane,
	selectedProduct;
	@FXML
	FlowPane topPanel,
	searchProductPane, createProductPane;
	@FXML
	AnchorPane newAdminPane, historyPane, productDetailsPane,
	mainContainer, menuPaneAnchor, contentMain;
	@FXML 
	BorderPane appPane,
	newProductPane;
	@FXML 
	Tab filterProdTab, createProdTab;

	@FXML 
	TableView2<Product> tableProducts;
	@FXML 
	TableColumn<Product, Integer> existenciasProductColumn;
	@FXML 
	TableColumn<Product, String> nombreProductColumn, marcaProductColumn, detallesProductColumn, deleteColumn, idProductColumn;
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
	CustomTextField nameProductDetails, newProdName, newProdBrandTxt, brandProductDetailsTxt, filterProdName,
	idProductDetails, newProdId, filterProdId;
	@FXML 
	CheckComboBox<String> filterProdBrand;
	@FXML 
	RangeSlider filterRangeBar;


	private Stage stage;
	private boolean oppened = false;
	
	private final float 
	WIDTH_MAIN_PANEL = 300,
	WIDTH_VISIBLE_BUTTONS_PANEL = 70,
	WIDTH_PANEL_MOVEMENT = WIDTH_MAIN_PANEL - WIDTH_VISIBLE_BUTTONS_PANEL;
	private final Color[]
	SUCCESS = {Color.web("a2e0c4", 0.9), Color.web("#006134")},
	ALERT = {Color.web("fee6c6", 0.9), Color.web("#fca300")},
	ERROR = {Color.web("d46e73", 0.9), Color.web("#a1131a")};
	
	// Products Panel
	private final String DEFAULT_IMAGE = MainController.class.getResource("/img/default.jpg").toString(); 

	private final Path IMAGE_PATH = Paths.get("img");

	
	private SpinnerValueFactory<Double> priceSpinner, newPriceSpinner, leftFilterSpinner, rightFilterSpinner, salePaymentSpinner;
	private SpinnerValueFactory<Integer> quantitySpinner, newQuantitySpinner, saleQuantitySpinner;
	private final FileChooser fileChooser = new FileChooser();	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Util.initialize(contentMain, menuPaneAnchor);
		
		
		onDraggedScene(topPanel);		

		initializeProducts();	
		initializeSale();
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

	//Panel Sale
	public void initializeSale() {
		
		columnIdSale.setCellValueFactory(new PropertyValueFactory<SaleDetails, Long>("id_product"));
		columnNameSale.setCellValueFactory(new PropertyValueFactory<SaleDetails, String>("product_name"));
		columnPriceSale.setCellValueFactory(new PropertyValueFactory<SaleDetails, Double>("unit_price"));
		columnQuantitySale.setCellValueFactory(new PropertyValueFactory<SaleDetails, Integer>("quantity"));
		columnTotalSale.setCellValueFactory(new PropertyValueFactory<SaleDetails, Double>("subtotal"));	
		
		salePaymentSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000);
		saleQuantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000);
		
		paymentSale.setValueFactory(salePaymentSpinner);
		quantitySale.setValueFactory(saleQuantitySpinner);
		
		Util.fixDoubleSpinner(paymentSale);
		Util.fixIntSpinner(quantitySale);
		
		
	}
	public void addProdSale() {
		Product product = Product.getProduct(idSale.getText());
		
		if(product == null) {
			Util.summonAlert("No existe el product", ERROR, 0);
			return;
		}

		Long id = product.getId();
		
		ObservableList<SaleDetails> list = tableSale.getItems();
		for(int i = 0; i < list.size(); i++) {
			SaleDetails details = list.get(i);
			System.out.println( details.getId_product().equals(id)+" "+ details.getId_product() + " " + id );
			if(details.getId_product().equals(id)) {
				details.setQuantity( details.getQuantity() + quantitySale.getValue() );
				details.setSubtotal( details.getUnit_price() * details.getQuantity() );
				tableSale.refresh();
				return;
			}
		}
		
		String name = product.getName();
		Double price = product.getPrice();
		Integer quantity = quantitySale.getValue();
		Double subtotal = price * quantity;
		
		// Guardamos sale, asignamos el id de retorno a todos los details y despues los guardamos
		// id de details no se usa 
		SaleDetails details = new SaleDetails( 0L, 0L, id, quantity, name, price, subtotal);
		tableSale.getItems().add(details);
		
	}
	
	public void finishSale() {
		
	}
	
	// Panel Productos 
	// Inicializa todos los recursos para el panel de Productos
	public void initializeProducts() {

		if (!Files.exists(IMAGE_PATH)) {
			try {
				Files.createDirectories(IMAGE_PATH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		fileChooser.getExtensionFilters().add(new ExtensionFilter("IMG files (*.jpg)", "*.jpg", "*.png"));

		
		// Columnas de la tabla Productos
		idProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("id"));
		existenciasProductColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer> ("quantity"));
		nombreProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("name"));
		marcaProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("brand"));
		detallesProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String> ("details"));
		precioProductColumn.setCellValueFactory(new PropertyValueFactory<Product, Double> ("price"));

	
        deleteColumn.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(TableColumn<Product, String> tableColumn) {
            	return new TableCell<Product, String>() {
            	
            		@Override
            		protected void updateItem(String item, boolean empty) {
            			super.updateItem(item, empty);
            			
            			if(empty) {
            				this.setGraphic(null);
            			} else {            		
            				Button btn = new Button();
            				Product product = this.getTableView().getItems().get(getIndex());
            				
            				btn.getStyleClass().add("deleteButton");
	            			btn.setText("Eliminar");
	            			btn.setPrefWidth(Double.MAX_VALUE);
	            			btn.setMaxHeight(tableProducts.getFixedCellSize());
	            			btn.setOnAction(e->{
	            				deleteImage(product.getImagen());
	            				Product.deleteProduct(product.getId());
	            				Util.summonAlert("Producto Eliminado Correctamente", SUCCESS, 0);
	            				
	            				searchProducts();
	            				updateFilterRange();
	            					
	            				if(Long.valueOf(idProductDetails.getText()) == product.getId()) {
	            					tableProducts.getSelectionModel().clearSelection();
	            					clearDetailsPane();	            					
	            				}
	            				if(tableProducts.getItems().isEmpty()) {
	            					clearFilters();
	            				}
	            				
	            			});
	            			
	            			
	            			this.setPadding(Insets.EMPTY);
	            			this.setGraphic(btn);
            			}
            		}
            	};
            }
		});


		// Actualiza panel de detalles segun lo seleccionado en la tabla
		tableProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, product) -> {
			if(notSelectedProduct.isVisible()) {
				notSelectedProduct.setVisible(false);
				selectedProduct.setVisible(true);
				selectedProduct.setDisable(false);
			}	
			if (product != null) {
				Image image = new Image(product.getImagen());	
				idProductDetails.setText(String.valueOf(product.getId()));
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
		newPriceSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 10);
		newQuantitySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 10);
		
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
		
		
		Util.fixIntSpinner(newProdQuantity);
		Util.fixDoubleSpinner(newProdPrice);
		Util.fixIntSpinner(quantityProductDetails);
		Util.fixDoubleSpinner(priceProductDetails);
		
		Util.fixDoubleSpinner(filterRangeLeft);
		Util.fixDoubleSpinner(filterRangeRight);
		
		Util.fixTextArea(detailsProductDetails);
		Util.fixTextArea(newProdDetails);
		
		Util.fixTextField(idProductDetails);
		Util.fixTextField(filterProdId);
		Util.fixTextField(newProdId);
		
		// spinner de filtro		
		updateFilterRange();
		
		updateTableProducts();
		updateComboBoxes();		
	}
	
	private boolean validateNewProd(CustomTextField id, CustomTextField name, SpinnerValueFactory<Double> price, CustomTextField brand, ImageView image, TextArea details, SpinnerValueFactory<Integer> quantity) {
		
		ArrayList<String> errores = new ArrayList<String>();
		ArrayList<String> alertas = new ArrayList<String>();
		
		if(id.getText().isBlank()) {
			errores.add("El id no debe estar vacio");
			Util.errorHighlight(id);
		} else if(id.getText().length() != 13) {
			errores.add("El id debe tener 13 caracteres");
			Util.errorHighlight(id);			
		} else if( Product.exist(Long.valueOf(id.getText())) & !id.equals(idProductDetails) ) {
			errores.add("ID existente");
			Util.errorHighlight(id);						
		}
		
		if(name.getText().isBlank()) {
			errores.add("El nombre no debe estar vacio");
			Util.errorHighlight(name);
		} else if(name.getText().length() >= 30) {
			errores.add("El nombre debe ser menor a 30 caracteres");
			Util.errorHighlight(name);			
		}
		
		if(brand.getText().isBlank()) {
			errores.add("El nombre de la marca no debe estar vacio");
			Util.errorHighlight(brand);
		} else if(brand.getText().length() >= 20) {
			errores.add("La marca debe ser menor a 20 caracteres");
			Util.errorHighlight(brand);			
		}
	
		if(price.getValue() == 0) {
			alertas.add("Guardado con precio de 0");
		}
		if(quantity.getValue() == 0) {
			alertas.add("Guardado con cantidad de 0");
		}
		if(image.getImage().getUrl().equals(DEFAULT_IMAGE)) {
			alertas.add("Guardado sin imagen");
		}
		if(details.getText().isBlank()) {
			alertas.add("Guardado sin detalles");			
		}
		
		for(int i = 0; i < errores.size(); i++) {
			Util.summonAlert(errores.get(i), ERROR, i);
			}
		if(!errores.isEmpty()) {
			return errores.isEmpty();
		}
		
		for(int i = 0; i < alertas.size(); i++) {
			Util.summonAlert(alertas.get(i), ALERT, i);
		}
		if(alertas.isEmpty()) {
			Util.summonAlert("Producto guardado Correctamente", SUCCESS, 0);
		}
		
		return errores.isEmpty();
	}
	
	private void clearCreateNew() {
		newProdName.setText("");
		newPriceSpinner.setValue(0.0);
		newProdBrandTxt.setText("");
		newProdView.setImage(new Image(DEFAULT_IMAGE)); 
		newProdDetails.setText("");
		newQuantitySpinner.setValue(0);
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
	private void clearDetailsPane() {
		tableProducts.getSelectionModel().clearSelection();
		notSelectedProduct.setVisible(true);
		notSelectedProduct.setDisable(false);
		selectedProduct.setVisible(false);
		selectedProduct.setDisable(true);
	}
	
	// Actualiza los datos de la tabla desde la db
	private void updateTableProducts() {
		ObservableList<Product> loadedProducts = Product.getAllProducts();
		tableProducts.setItems(loadedProducts);	
	}
	// Actualiza los datos de las marcas desde la db
	private void updateComboBoxes() {
		ObservableList<String> list = Brand.getAllBrands();
		filterProdBrand.getItems().clear();
		filterProdBrand.getItems().addAll(list);
		newProdBrandCb.setItems(list);
		brandProductDetails.setItems(list);
	}
	private void updateFilterRange() {
		Double[] prices = Product.getPricesRange();
		Double min = prices[0];
		Double max = prices[1];
		leftFilterSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, min, 1);
		rightFilterSpinner = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, max, 1);
		filterRangeLeft.setValueFactory(leftFilterSpinner);
		filterRangeRight.setValueFactory(rightFilterSpinner);
		filterRangeBar.setMax(max);
		filterRangeBar.setMin(min);
		filterRangeBar.setHighValue(max);
		filterRangeBar.setLowValue(min);
		
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
	// Guarda un producto con los datos registrados
	public void confirmProduct() {	

		if(filterProdTab.isSelected()) {			
			tableProducts.getSelectionModel().clearSelection();
			searchProducts();
		}
		else { 
			
			if(validateNewProd(newProdId, newProdName, newPriceSpinner, newProdBrandTxt, newProdView, newProdDetails, newQuantitySpinner)) {
				String image = saveImage(newProdView);
				
				saveProduct(newProdId, newProdName, newPriceSpinner, newProdBrandTxt, image, newProdDetails, newQuantitySpinner);
				clearCreateNew();				
			}
			
		}
	}
	
	// Filtra los productos tomando los datos seleccionados
	public void searchProducts(){
		notSelectedProduct.setVisible(true);
		selectedProduct.setVisible(false);
		selectedProduct.setDisable(true);
		String id = filterProdId.getText();
		String name = filterProdName.getText();
		Double min = filterRangeBar.getLowValue();
		Double max = filterRangeBar.getHighValue();
		ObservableList<String> listBrand = filterProdBrand.checkModelProperty().get().getCheckedItems();
		ObservableList<Product> loadedProducts = Product.getFilteredProducts(id, name, min, max, listBrand);
		tableProducts.setItems(loadedProducts);			
	}
	
	

	
	// actualiza el producto, borra la imagen
	public void saveChangeButton() {
		Product product = tableProducts.getSelectionModel().getSelectedItem();
		String image = product.getImagen();
		if(validateNewProd(idProductDetails, nameProductDetails, priceSpinner, brandProductDetailsTxt, imgProductDetails, detailsProductDetails, quantitySpinner)) {
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
			saveProduct(idProductDetails, nameProductDetails, priceSpinner, brandProductDetailsTxt, image, detailsProductDetails, quantitySpinner);
		}
	}

	
	// guarda el producto, crea la marca, actualiza los nodos
	private void saveProduct(CustomTextField id, CustomTextField name, SpinnerValueFactory<Double> price, CustomTextField brand, String image, TextArea details, SpinnerValueFactory<Integer> quantity) {
		if(!Brand.existBrand(brand.getText())) Brand.createBrand(brand.getText());

		Product product = new Product(
				Long.valueOf(id.getText()),
				name.getText(),
				price.getValue(),
				brand.getText(),
				image,
				details.getText(),
				quantity.getValue()
				);

//		product.setId(Product.saveProduct(product));
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
	
	
	private void deleteImage(String path) {
		Path image = Util.getPath(path);
	
		if(!path.equals(DEFAULT_IMAGE))
			try {
				Files.deleteIfExists(image);
			} catch (IOException e) {
				e.printStackTrace();
			}	
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



	
}














