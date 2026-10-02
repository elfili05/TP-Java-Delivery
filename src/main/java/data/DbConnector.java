package main.java.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.Arrays;
import java.util.TimeZone;

public class DbConnector {

	private static DbConnector instance;

	private String driver="com.mysql.cj.jdbc.Driver";
	// los valores por defecto son los de desarrollo; en un despliegue se pueden pisar con variables de entorno (DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME).
	private String host=valueOf("DB_HOST", "localhost");
	private String port=valueOf("DB_PORT", "3306");
	private String user=valueOf("DB_USER", "DBAdmin");
	private String password=valueOf("DB_PASSWORD", "admin");
	private String db=valueOf("DB_NAME", "javadelivery");

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

	// si no se puede conectar lanza SQLException (los repositorios ya la capturan): antes devolvía null y explotaba con NullPointerException.
	public Connection getConn() throws SQLException {
		Connection current = conn.get();
		if(current==null || current.isClosed()) {
			current=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db+"?serverTimezone="+serverTimeZone()+"&useUnicode=true&characterEncoding=UTF-8", user, password);
			conn.set(current);
			connected.set(0);
		}
		connected.set(connected.get()+1);
		return current;
	}

	public void releaseConn() {
		int remaining = connected.get()-1;
		connected.set(remaining);
		if (remaining<=0) {
			forceRelease();
		}
	}

	// cierra y olvida la conexión de este hilo pase lo que pase; la llama el filtro al terminar cada request
	// como red de seguridad por si algún repositorio no llegó a hacer releaseConn.
	public void forceRelease() {
		Connection current = conn.get();
		try {
			if (current!=null) {
				if (!current.isClosed() && !current.getAutoCommit()) {
					current.rollback();
				}
				current.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.remove();
			connected.remove();
		}
	}

	// serverTimezone = la zona horaria de la JVM (así el driver no desplaza las horas TIME al guardar ni al leer).
	// el "+" de ids como "GMT+01:00" hay que codificarlo porque el driver lo leería como un espacio; si la JVM tiene una zona
	// que el driver no reconoce, se usa UTC (que es lo mismo que usan la JVM y MySQL en un servidor típico).
	private String serverTimeZone() {
		String zoneId = TimeZone.getDefault().getID();
		// el driver solo acepta ids de la base de zonas (America/..., UTC, etc.): otros como "GMT+01:00" harían fallar la conexión.
		if (!Arrays.asList(TimeZone.getAvailableIDs()).contains(zoneId)) {
			zoneId = "UTC";
		}
		try {
			return URLEncoder.encode(zoneId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "UTC";
		}
	}

	private static String valueOf(String variableName, String defaultValue) {
		String value = System.getenv(variableName);
		return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
	}

}
