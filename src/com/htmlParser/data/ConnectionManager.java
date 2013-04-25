package com.htmlParser.data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionManager {

	private static final String DATABASE_PROPERTIES = "database.properties";

	private static String driver = null;
	private static String url = null;
	private static String userName = null;
	private static String password = null;
	private static String host = null;
	private static String port = null;
	private static String datebaseName = null;

	static {
		try {
			loadPropertiesFile();
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadPropertiesFile() throws Exception {
		InputStream input = new FileInputStream(DATABASE_PROPERTIES);
//
//		InputStream in = ConnectionManager.class
//				.getResourceAsStream(DATABASE_PROPERTIES);
//		if (in == null)
//			System.out.println("Null");

		Properties p = new Properties();
		p.load(input);


		driver = p.getProperty("mysql.driver");
		host = p.getProperty("mysql.host");
		port = p.getProperty("mysql.port");
		datebaseName = p.getProperty("mysql.importDatabaseName");
		userName = p.getProperty("mysql.username");
		password = p.getProperty("mysql.password");
		url = "jdbc:mysql://" + host + ":" + port + "/" + datebaseName
				+ "?useUnicode=true&characterEncoding=utf-8";
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection con) {
		if (con != null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void closeStateMent(Statement st) {
		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

}
