package com.point.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.point.database.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

	private Long id;
	private String name;
	private double price;
	private String brand;
	private String imagen;
	private String details;
	private int quantity;
	
	static Connection connect;
	static PreparedStatement statement;
	static ResultSet result;
	
	/**
	 * @param id
	 * @param name
	 * @param price
	 * @param brand
	 * @param imagen
	 * @param details
	 */
	public Product(Long id, String name, double price, String brand, String imagen, String details, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.brand = brand.isBlank() ? "" : brand;
		this.imagen = imagen.isBlank() ? "img/default.jpg" : imagen;
		this.details = details.isBlank() ? "sin detalles" : details;
		this.quantity = quantity;
	}
	
	
	public Product() {
	}


	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", brand=" + brand + ", imagen=" + imagen
				+ ", details=" + details + "]";
	}
	
	public static ObservableList<Product> getAllProducts() {
		
		connect = Database.getConnect();
		ObservableList<Product> list = FXCollections.observableArrayList();
		
		try {
			statement = connect.prepareStatement("SELECT * FROM product");
			result = statement.executeQuery();			

			while(result.next()) {
				Product producto = new Product(
						result.getLong("id"),
						result.getString("name"),
						result.getDouble("price"),
						result.getString("brand"),
						result.getString("image"),
						result.getString("details"),
						result.getInt("quantity")
						);
				list.add(producto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list.isEmpty() ? null : list;
	}
	
	
	public static void saveProduct(Product product) {		
		try {
			if(product.id == 0) {
				saveProduct(product, "INSERT INTO product(name, price, brand, image, details, quantity) VALUES (?, ?, ?, ?, ?, ?)");			
			} else { 			
				saveProduct(product, "UPDATE product SET name = ?, price = ?, brand = ?, image = ?, details = ?, quantity = ? WHERE id = ?");						
				statement.setLong(7, product.id);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveProduct(Product product, String instruction) throws SQLException {
		connect = Database.getConnect();
		statement = connect.prepareStatement(instruction);
		statement.setString(1, product.name);
		statement.setDouble(2, product.price);
		statement.setString(3, product.brand.isBlank() ? "" : product.brand);
		statement.setString(4, product.imagen.isBlank() ? "img/default.jpg" : product.imagen);
		statement.setString(5, product.details.isBlank() ? "sin detalles" : product.details);
		statement.setInt(6, product.quantity);
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getImagen() {
		return imagen;
	}


	public void setImagen(String imagen) {
		this.imagen = imagen;
	}


	public String getDetails() {
		return details;
	}


	public void setDetails(String details) {
		this.details = details;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
