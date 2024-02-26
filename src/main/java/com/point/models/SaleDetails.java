package com.point.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.point.database.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SaleDetails {

	Long id_sale;
	String id_product;
	int quantity;
	String product_name;
	double unit_price;
	double subtotal;
	
	static Connection connect;
	static PreparedStatement statement;
	static ResultSet result;
	
	/**
	 * @param id
	 * @param id_sale
	 * @param id_product
	 * @param quantity
	 * @param unit_price
	 * @param subtotal
	 */
	public SaleDetails(Long id_sale, String id_product, int quantity, String product_name, double unit_price, double subtotal) {
		this.id_sale = id_sale;
		this.id_product = id_product;
		this.quantity = quantity;
		this.product_name = product_name;
		this.unit_price = unit_price;
		this.subtotal = subtotal;
		/* La db solo tiene 
			id_sale, id_product,
			quantity, subtotal
		*/
	}
	
	public SaleDetails() {
	}

	public static void save(SaleDetails details) {
		connect = Database.getConnect();
		
		try {			
			statement = connect.prepareStatement("INSERT INTO sale_details (id_sale, id_product, quantity, subtotal) VALUES (?, ?, ?, ?)");
			statement.setLong(1, details.id_sale);
			statement.setString(2, details.id_product);
			statement.setInt(3, details.quantity);
			statement.setDouble(4, details.subtotal);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ObservableList<SaleDetails> get(Long id) {
		ObservableList<SaleDetails> list = FXCollections.observableArrayList();
		
		connect = Database.getConnect();
		
		try {
//			String ins = "SELECT sale.id, sale.date, sale.total, product.id, sale_details.quantity, 
//			sale_details.subtotal, product.price, product.name 
//			FROM sale JOIN sale_details ON sale.id = sale_details.id_sale 
//			JOIN product ON sale_details.id_product = product.id WHERE sale.id = 3";

			statement = connect.prepareStatement("SELECT sale_details.id_sale, sale_details.id_product,"
					+ " sale_details.quantity, product.name, product.price, sale_details.subtotal "
					+ "FROM sale_details JOIN product ON sale_details.id_product = product.id WHERE sale_details.id_sale = ?");
			statement.setLong(1, id);
			result = statement.executeQuery();
			
			while(result.next()) {
				list.add(new SaleDetails(
						result.getLong(1),
						result.getString(2),
						result.getInt(3),
						result.getString(4),
						result.getDouble(5),
						result.getDouble(6)
						));
			}
		} catch(SQLException e) {
			System.out.println("error en getDetails");
		}
		
		return list;
	}
	
	@Override
	public String toString() {
		return "SaleDetails [id_sale=" + id_sale + ", id_product=" + id_product + ", quantity="
				+ quantity + ", unit_price=" + unit_price + ", subtotal=" + subtotal + "]";
	}
	
	public Long getId_sale() {
		return id_sale;
	}
	public void setId_sale(Long id_sale) {
		this.id_sale = id_sale;
	}
	public String getId_product() {
		return id_product;
	}
	public void setId_product(String id_product) {
		this.id_product = id_product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
}
