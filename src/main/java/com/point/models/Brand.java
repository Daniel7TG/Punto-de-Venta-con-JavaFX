package com.point.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.point.database.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Brand {

	String brand;
	static Connection connect;
	static PreparedStatement statement;
	static ResultSet result;
	
	public Brand(String brand) {
		this.brand = brand;
	}
	public Brand() {
	}
	
	public static ObservableList<String> getAllBrands() {
		
		connect = Database.getConnect();
		ObservableList<String> list = FXCollections.observableArrayList();
		
		try {
			statement = connect.prepareStatement("SELECT * FROM brands");
			result = statement.executeQuery();			

			while(result.next()) {
						
				list.add(result.getString("brand_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list.isEmpty() ? null : list;
	}
	
	public static boolean existBrand(String brand) {
		connect = Database.getConnect();
		
		try {
			statement = connect.prepareStatement("SELECT * FROM brands WHERE brand_name = ?");
			statement.setString(1, brand);
			result = statement.executeQuery();
			if(result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void createBrand(String brand) {
		connect = Database.getConnect();
		
		try {
			statement = connect.prepareStatement("INSERT INTO brands(brand_name) VALUES(?)");
			statement.setString(1, brand);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ya existe");
		}
	}
	
}
