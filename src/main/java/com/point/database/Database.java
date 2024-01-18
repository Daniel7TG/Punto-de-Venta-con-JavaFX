package com.point.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

public class Database {
	
	private static Connection connect;
	
    public static void connection() {
        if(connect != null) {
        	return;
        }
 
    	try{
        	Class.forName("com.mysql.cj.jdbc.Driver");    
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "root");
           
            System.out.println("Conectado");

        } catch(Exception e) {
        	e.printStackTrace();
        }
      
    }
    
    
	public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public static boolean verifyPassword(String inputPassword, String hashedPassword) {
		return BCrypt.checkpw(inputPassword, hashedPassword);
	}

	public static Connection getConnect() {
		return connect;
	}
		
}


