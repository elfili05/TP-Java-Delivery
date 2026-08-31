package main.java.data;

import java.sql.*;	

public class DbConnector {

	private static DbConnector instance;
	
	private String driver="com.mysql.cj.jdbc.Driver";
	private String host="localhost";
	private String port="3306";
	private String user="DBAdmin";
	private String password="admin";
	private String db="javadelivery";
	private int connected=0;
	private Connection conn=null;
	
	private DbConnector() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static DbConnector getInstance() {
		if (instance == null) {
			instance = new DbConnector();
		}
		return instance;
	}
	
	public Connection getConn() {
		try {
			if(conn==null || conn.isClosed()) {
				conn=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db+"?serverTimezone=UTC", user, password);
				connected=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connected++;
		return conn;
	}
	
	public void releaseConn() {
		connected--;
		try {
			if (connected<=0) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
