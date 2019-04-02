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
		try {
			// Max amount of time driver will wait is 15s
			DriverManager.setLoginTimeout(15);
			connection = DriverManager.getConnection(DATABASE_URL, "root", "");
			// Create database if not exists
			createDatabase();
			createTables();
		} catch (SQLException ex) {
			System.out.println("The following exception has occured: " + ex.getMessage());
		}
	}

	// private static void

	private static void createTables() throws SQLException {
		String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "users" + "  (name     VARCHAR(25),"
				+ "   password VARCHAR(20))";

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
		//DriverManager.setLoginTimeout(15);
		//try {
		//	connection = DriverManager.getConnection(DATABASE_URL, "root", "");
		//} catch (SQLException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	    String insertUser = "INSERT INTO USERS VALUES ('" + name + "', '" + password + "')";
		statement = connection.createStatement();
		statement.execute(insertUser);
	}
}
