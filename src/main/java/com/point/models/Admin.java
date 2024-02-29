package com.point.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.point.database.Database;

public class Admin {

	int id;
	String firstName;
	String lastName;
	String userName;
	String password;
	
	static Connection connect;
	static PreparedStatement statement;
	static ResultSet result;
		
	/**
	 * @param firstName
	 * @param lastName
	 * @param userName
	 * @param password
	 */
	public Admin(int id, String firstName, String lastName, String userName, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
	}

	
	/**
	 * 
	 * @param username
	 * @return a {@code ResultSet} with the cursor at the unique position,
	 * {@code null} if the username isn't found on the database 
	 */
	public static ResultSet get(String username) {
		connect = Database.getConnect();

		try {
			statement = connect.prepareStatement("SELECT * FROM admin WHERE username = ?");
			statement.setString(1, username);
			result = statement.executeQuery(); 
			if(result.next()) return result;			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static void save(String firstName, String lastName, String username, String password) {
		connect = Database.getConnect();
		 
		try {
			statement = connect.prepareStatement("INSERT INTO admin(first_name, last_name, username, password) VALUES (?, ?, ?, ?)");
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, username);
			statement.setString(4, Database.hashPassword(password));
			statement.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Admin [firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + ", password="
				+ password + "]";
	}


		
}
