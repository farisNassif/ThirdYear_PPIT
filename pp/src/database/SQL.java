package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	private static String dbName = "GAME_USERS";
	private static String charSet = "utf8";
	private static String uniCode = "utf8_unicode_ci";
	private final static String DATABASE_URL = "jdbc:mysql://localhost/GAME_USERS?autoReconnect=true&useSSL=false";
	private static Connection connection = null;
	private static Statement statement = null;

	public static void main() throws Exception {
		// Opens the connection
		openConnection();
		// Create database if not exists
		createDatabase();
		// Creates the tables if they don't already exist
		createTables();
	}

	private static void openConnection() {
		DriverManager.setLoginTimeout(15);
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "");
		} catch (SQLException e) {
			System.out.println("The following exception has occured: " + e.getMessage());
		}
	}

	private static void createTables() throws SQLException {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "users" + "  "
				+ "(user_id SMALLINT(6) NOT NULL AUTO_INCREMENT,"
				+ "name     VARCHAR(25),"
				+ "password VARCHAR(20),"
				+ "PRIMARY KEY(user_id)) Engine=InnoDB";

		statement = connection.createStatement();
		statement.execute(sqlCreate);
	}

	private static void createDatabase() throws SQLException {
		// Statement that creates the database if it doesn't exist
		String sql_stmt = "CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET " + charSet + " COLLATE "
				+ uniCode + ";";
		statement = connection.createStatement();
		// Executes the statement
		statement.executeUpdate(sql_stmt);
		// Just notifying that this method was executed, can remove later
		System.out.println("Required databse setup correctly");
	}

	public static void insertUser(String name, String password) throws SQLException {
		String insertUser = "INSERT INTO USERS VALUES (0,'" + name + "', '" + password + "')";
		statement = connection.createStatement();
		statement.execute(insertUser);
	}

	public static void closeConnection(String clientIP) {
		try {
			connection.close();
			System.out.println("Closing SQL Connection for client " + clientIP + " ...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void queryForUser() {
		//String sql_stmt = "SELECT * FROM " + dbName + ";";
		//statement = connection.createStatement();
		//statement.executeQuery(sql)
	}
}
