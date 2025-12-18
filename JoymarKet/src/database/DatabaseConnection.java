package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton class to manage database connection
public class DatabaseConnection {

	private static DatabaseConnection instance;
	private Connection connection;
	
    // Database configuration
	private final String HOST = "localhost";
	private final String PORT = "3306";
	private final String DATABASE = "joymarket";
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	
    // Private constructor to prevent multiple instances
	private DatabaseConnection() {
		try {
			String url = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DATABASE);
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
			System.out.println("Database connected successfully to: " + DATABASE + ".");
		} catch (SQLException e) {
			System.err.println("Database connection failed.");
			System.err.println("Error: " + e.getMessage() + ".");
			e.printStackTrace();
		}
	}
	
    // Get single instance of DatabaseConnection
	public static DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}
	
    // Get active database connection
	public Connection getConnection() {
		return connection;
	}

}
