package com.point.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;

import com.point.database.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Sale {

	Long id;
	Date date;
	double total;
	
	int productAmount;
//	ArrayList<?> productList;

	static Connection connect;
	static PreparedStatement statement;
	static ResultSet result;
	
	public Sale() {
	}
	public Sale(Date date, double total) {
		this.date = date;
		this.total = total;
	}
	public Sale(Date date, double total, int productAmount) {
		this.date = date;
		this.total = total;
		this.productAmount = productAmount;
	}
	public Sale(Long id, Date date, double total, int productAmount) {
		this.id = id;
		this.date = date;
		this.total = total;
		this.productAmount = productAmount;
	}

	
	public static ObservableList<Sale> get(LocalDate date, Double min, Double max) {
		
		connect = Database.getConnect();
		ObservableList<Sale> list = FXCollections.observableArrayList();
		
		String instruction = "SELECT sale.id, sale.date, sale.total, sale_details.quantity "
				+ "FROM sale JOIN sale_details ON sale.id = sale_details.id_sale WHERE total BETWEEN ? AND ?";
		try {
			
			if(date != null) instruction += "AND date = ?";
			
			statement = connect.prepareStatement(instruction);
			statement.setDouble(1, min);
			statement.setDouble(2, max);
			
			if(date != null) statement.setDate(3, Date.valueOf(date));

			result = statement.executeQuery();			
			
			while(result.next()) {
				if(list.stream().filter(sale -> {
					try {
						if(sale.getId() == result.getLong("id")) {
							sale.setProductAmount(sale.getProductAmount() + result.getInt("quantity"));
							return true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return false;
				}).findAny().isEmpty() ) {					
					Sale sale = new Sale(					
							result.getLong("id"),
							result.getDate("date"),
							result.getDouble("total"),
							result.getInt("quantity")
							);
					list.add(sale);
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public static ObservableList<Sale> getAll(){
		connect = Database.getConnect();
		ObservableList<Sale> list = FXCollections.observableArrayList();
		try {	
			String ins = "SELECT sale.id, sale.date, sale.total, sale_details.quantity "
					+ "FROM sale JOIN sale_details ON sale.id = sale_details.id_sale JOIN product ON sale_details.id_product = product.id";

			statement = connect.prepareStatement(ins);
			result = statement.executeQuery();
			
			while(result.next()) {
				if(list.stream().filter(sale -> {
					try {
						if(sale.getId() == result.getLong("id")) {
							sale.setProductAmount(sale.getProductAmount() + result.getInt("quantity"));
							return true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return false;
				}).findAny().isEmpty() ) {					
					Sale sale = new Sale(
							
							result.getLong("id"),
							result.getDate("date"),
							result.getDouble("total"),
							result.getInt("quantity")
							);
					list.add(sale);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static Long save(Sale sale) {
		connect = Database.getConnect();
		Long id_sale = 0L;	
		try {	
			statement = connect.prepareStatement("INSERT INTO sale (date, total) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			statement.setDate(1, sale.date);
			statement.setDouble(2, sale.total);
			statement.executeUpdate();
			try (ResultSet keys = statement.getGeneratedKeys()) {
			    if(keys.next()) id_sale = keys.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id_sale;
	}
	public static void delete(Long id) {
		connect = Database.getConnect();
		try {
			statement = connect.prepareStatement("DELETE FROM sale WHERE id = ?");
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Override
	public String toString() {
		return "Sale [id=" + id + ", date=" + date + ", total=" + total + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	public int getProductAmount() {
		return productAmount;
	}
	public void setProductAmount(int productAmount) {
		this.productAmount = productAmount;
	}
	public static Double[] getPricesRange() {
		connect = Database.getConnect();
		Double prices[] = new Double[2];
		
		try {
		    statement = connect.prepareStatement("SELECT MAX(total) FROM sale");
		    result = statement.executeQuery();
		    if (result.next()) {
		        prices[1] = result.getDouble(1); 
		    }
		    statement = connect.prepareStatement("SELECT MIN(total) FROM sale");
		    result = statement.executeQuery();
		    if (result.next()) {
		        prices[0] = result.getDouble(1); 
		    }			
		}catch(SQLException e) {
			prices[0] = (double) 0;
			prices[1] = (double) 0;
		}
		return prices;
	}

	
//	public ArrayList<?> getProductList() {
//		return productList;
//	}
//
//	public void setProductList(ArrayList<?> productList) {
//		this.productList = productList;
//	}

	
}
