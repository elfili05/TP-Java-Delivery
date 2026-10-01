package main.java.data;

import java.sql.*;
import java.util.TimeZone;

public class DbConnector {

	private static DbConnector instance;

	private String driver="com.mysql.cj.jdbc.Driver";
	private String host="localhost";
	private String port="3306";
	private String user="DBAdmin";
	private String password="admin";
	private String db="javadelivery";

	// cada request corre en su propio hilo de Tomcat: la conexión y el contador viven por hilo,
	// así dos requests simultáneos no comparten statements ni se cierran la conexión entre sí.
	private ThreadLocal<Connection> conn = new ThreadLocal<Connection>();
	private ThreadLocal<Integer> connected = ThreadLocal.withInitial(() -> 0);

	private DbConnector() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static synchronized DbConnector getInstance() {
		if (instance == null) {
			instance = new DbConnector();
		}
		return instance;
	}

	public Connection getConn() {
		try {
			Connection current = conn.get();
			if(current==null || current.isClosed()) {
				// serverTimezone = la zona horaria de la JVM (MySQL corre en la misma máquina): con "UTC" fijo, las horas
				// de los horarios (TIME) se corrían según la diferencia entre UTC y la zona real.
				current=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db+"?serverTimezone="+TimeZone.getDefault().getID(), user, password);
				conn.set(current);
				connected.set(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connected.set(connected.get()+1);
		return conn.get();
	}

	public void releaseConn() {
		int remaining = connected.get()-1;
		connected.set(remaining);
		if (remaining<=0) {
			try {
				Connection current = conn.get();
				if (current!=null) {
					current.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn.remove();
				connected.remove();
			}
		}
	}

}
