package com.point.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.point.IndexApp;
import com.point.Util;
import com.point.controllers.MainController;
import com.point.database.Database;


public class Configuration {

	private String theme;
	private boolean skipSession;
	private String rememberUser;
	private boolean autoProducts;
	private boolean activateAlerts;
	private int saleDefAmount;
	private int adminId;

	private static Configuration config;


	private static final String DEFAULT_CONFIG = "/config/configuration.json"; 
	private static String user_config = Configuration.class.getResource("/config/configuration.json").toString();

	/**
	 * @param theme
	 * @param skipSession
	 * @param rememberUser
	 * @param autoProducts
	 * @param activateAlerts
	 * @param saleDefAmount
	 */
	public Configuration(String theme, boolean skipSession, String rememberUser, boolean autoProducts,
		boolean activateAlerts, int saleDefAmount) {
		this.theme = theme;
		this.skipSession = skipSession;
		this.rememberUser = rememberUser;
		this.autoProducts = autoProducts;
		this.activateAlerts = activateAlerts;
		this.saleDefAmount = saleDefAmount;
		
	}
	
	
	public static Configuration loadConfig() {
		
		File jsonFile = new File("resources/config/configuration.json");
		
		if(!jsonFile.exists()) {
			if (!jsonFile.exists()) {	
				try {
					Files.createDirectories(Paths.get(jsonFile.getParent()));
					Files.copy( Configuration.class.getResourceAsStream("/config/configuration.json"), Paths.get("resources/config/configuration.json"));      
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
			
		StringBuilder string = new StringBuilder();
        Scanner scanner;
		try {
			scanner = new Scanner(jsonFile);
			while(scanner.hasNext()) string.append(scanner.nextLine());
			scanner.close();

			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject) parser.parse(string.toString());	
			config = new Configuration(
					(String) json.get("theme"),
					(Boolean) json.get("skipSession"),
					(String) json.get("rememberUser"),
					(Boolean) json.get("autoProducts"),
					(Boolean) json.get("activateAlerts"),
					((Long) json.get("saleDefAmount")).intValue()
					);
			return config;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	
	public static void saveConfig(Configuration config) {
		System.out.println(config.toString());
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("theme", config.getTheme());
		map.put("skipSession", config.isSkipSession());
		map.put("rememberUser", config.getRememberUser());
		map.put("autoProducts", config.isAutoProducts());
		map.put("activateAlerts", config.isActivateAlerts());
		map.put("saleDefAmount", config.getSaleDefAmount());		
		JSONObject jo = new JSONObject(map);

       	File file = new File("resources/config/configuration.json");

		if(file.exists()) {
			try {
				FileWriter fw = new FileWriter(file);
				fw.write(jo.toJSONString());
				fw.flush();
				fw.close();
			}catch(FileNotFoundException e) {
			}catch(IOException e) {
			}
		}else {
		}
        
	}
	
	
//	public static void saveConfig(Configuration config) {
//		Connection connection = Database.getConnect();
//		try {
//			PreparedStatement statement = connection.prepareStatement("INSERT INTO "
//					+ "config(theme, skip_session, remember_user, auto_products, activate_alerts, sale_def_amount, admin_id) "
//					+ "VALUES(?, ?, ?, ?, ?, ?, ?)");
//			statement.setString(1, config.getTheme());
//			statement.setBoolean(2, config.isSkipSession());
//			statement.setBoolean(3, config.isRememberUser());
//			statement.setBoolean(4, config.isAutoProducts());
//			statement.setBoolean(5, config.isActivateAlerts());
//			statement.setInt(6, config.getSaleDefAmount());
//			statement.setInt(7, config.adminId);
//			statement.executeUpdate();	
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	public static void updateConfig(Configuration config) {
//		Connection connection = Database.getConnect();
//		try {
//			PreparedStatement statement = connection.prepareStatement("UPDATE config "
//					+ "SET theme = ?, skip_session = ?, remember_user = ?, auto_products = ?, "
//					+ "activate_alerts = ?, sale_def_amount = ? "
//					+ "WHERE admin_id = ?");
//			statement.setString(1, config.getTheme());
//			statement.setBoolean(2, config.isSkipSession());
//			statement.setBoolean(3, config.isRememberUser());
//			statement.setBoolean(4, config.isAutoProducts());
//			statement.setBoolean(5, config.isActivateAlerts());
//			statement.setInt(6, config.getSaleDefAmount());
//			statement.setInt(7, config.adminId);
//			System.out.println(
//			statement.toString());
//			statement.executeUpdate();	
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static Configuration getConfig(int id) {
//		Connection connection = Database.getConnect();
//		try {
//			PreparedStatement statement = connection.prepareStatement("SELECT * FROM config WHERE admin_id = ?");
//			statement.setInt(1, id);
//			ResultSet result = statement.executeQuery();
//			if(result.next()) {
//				config = new Configuration(
//						result.getString("theme"), 
//						result.getBoolean("skip_session"), 
//						result.getBoolean("remember_user"), 
//						result.getBoolean("auto_products"), 
//						result.getBoolean("activate_alerts"), 
//						result.getInt("sale_def_amount"),
//						result.getInt("admin_id")
//						);
//				return config;
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}	
//	
	public static Configuration getConfig() {
		return config;
	}
	
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public boolean isSkipSession() {
		return skipSession;
	}

	public void setSkipSession(boolean skipSession) {
		this.skipSession = skipSession;
	}

	public String getRememberUser() {
		return rememberUser;
	}

	public void setRememberUser(String rememberUser) {
		this.rememberUser = rememberUser;
	}

	public boolean isAutoProducts() {
		return autoProducts;
	}

	public void setAutoProducts(boolean autoProducts) {
		this.autoProducts = autoProducts;
	}

	public boolean isActivateAlerts() {
		return activateAlerts;
	}

	public void setActivateAlerts(boolean activateAlerts) {
		this.activateAlerts = activateAlerts;
	}

	public int getSaleDefAmount() {
		return saleDefAmount;
	}

	public void setSaleDefAmount(int saleDefAmount) {
		this.saleDefAmount = saleDefAmount;
	}


	@Override
	public String toString() {
		return "Configuration [theme=" + theme + ", skipSession=" + skipSession + ", rememberUser=" + rememberUser
				+ ", autoProducts=" + autoProducts + ", activateAlerts=" + activateAlerts + ", saleDefAmount="
				+ saleDefAmount + ", adminId=" + adminId + "]";
	}
	
	
	
}
