package com.point.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.point.database.Database;

public class Configuration {

	private String theme;
	private boolean skipSession;
	private boolean rememberUser;
	private boolean autoProducts;
	private boolean activateAlerts;
	private int saleDefAmount;
	private int adminId;

	private static Configuration config;
	/**
	 * @param theme
	 * @param skipSession
	 * @param rememberUser
	 * @param autoProducts
	 * @param activateAlerts
	 * @param saleDefAmount
	 */
	public Configuration(String theme, boolean skipSession, boolean rememberUser, boolean autoProducts,
			boolean activateAlerts, int saleDefAmount, int id) {
		this.theme = theme;
		this.skipSession = skipSession;
		this.rememberUser = rememberUser;
		this.autoProducts = autoProducts;
		this.activateAlerts = activateAlerts;
		this.saleDefAmount = saleDefAmount;
		this.adminId = id;
	}
	
	public static void saveConfig(Configuration config) {
		Connection connection = Database.getConnect();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO "
					+ "config(theme, skip_session, remember_user, auto_products, activate_alerts, sale_def_amount, admin_id) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)");
			statement.setString(1, config.getTheme());
			statement.setBoolean(2, config.isSkipSession());
			statement.setBoolean(3, config.isRememberUser());
			statement.setBoolean(4, config.isAutoProducts());
			statement.setBoolean(5, config.isActivateAlerts());
			statement.setInt(6, config.getSaleDefAmount());
			statement.setInt(7, config.adminId);
			statement.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void updateConfig(Configuration config) {
		Connection connection = Database.getConnect();
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE config "
					+ "SET theme = ?, skip_session = ?, remember_user = ?, auto_products = ?, "
					+ "activate_alerts = ?, sale_def_amount = ? "
					+ "WHERE admin_id = ?");
			statement.setString(1, config.getTheme());
			statement.setBoolean(2, config.isSkipSession());
			statement.setBoolean(3, config.isRememberUser());
			statement.setBoolean(4, config.isAutoProducts());
			statement.setBoolean(5, config.isActivateAlerts());
			statement.setInt(6, config.getSaleDefAmount());
			statement.setInt(7, config.adminId);
			System.out.println(
			statement.toString());
			statement.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Configuration getConfig(int id) {
		Connection connection = Database.getConnect();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM config WHERE admin_id = ?");
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				config = new Configuration(
						result.getString("theme"), 
						result.getBoolean("skip_session"), 
						result.getBoolean("remember_user"), 
						result.getBoolean("auto_products"), 
						result.getBoolean("activate_alerts"), 
						result.getInt("sale_def_amount"),
						result.getInt("admin_id")
						);
				return config;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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

	public boolean isRememberUser() {
		return rememberUser;
	}

	public void setRememberUser(boolean rememberUser) {
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
	
	
	
}
