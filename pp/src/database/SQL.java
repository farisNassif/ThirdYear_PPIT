package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import services.Services;

/**
 * This class contains all relveant Scripts for Inserting/Querying the SQL
 * database.
 * 
 * @author Faris Nassif
 * @version 1.3
 * @since 1.0
 */
public class SQL {
	private static String dbName = "GAME_USERS";
	private static String charSet = "utf8";
	private static String uniCode = "utf8_unicode_ci";
	private final static String DATABASE_URL = "jdbc:mysql://localhost/GAME_USERS?autoReconnect=true&useSSL=false";
	private static Connection connection = null;
	private static Statement statement = null;
	private static String sqlCreate = " ";

	/**
	 * Always called when the Server is put online, opens the connection, creates
	 * the database/tables if they don't already exist.
	 * 
	 * @author Faris Nassif
	 */
	public static void onStartup() throws SQLException {
		// Opens the connection
		openConnection();
		// Create database if not exists
		createDatabase();
		// Creates the tables if they don't already exist
		createTables();
	}

	/**
	 * Opens the connection to the Database, this is the first method ran when
	 * {@link SQL#onStartup()} is executed.
	 * 
	 * @author Faris Nassif
	 */
	private static void openConnection() {
		DriverManager.setLoginTimeout(15);
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "");
		} catch (SQLException e) {
			System.out.println("The following exception has occured: " + e.getMessage());
		}
	}

	/**
	 * Creates the Database if it doesn't already exist, this is the second method
	 * ran when {@link SQL#onStartup()} is executed.
	 * 
	 * @author Faris Nassif
	 */
	private static void createDatabase() throws SQLException {
		// Statement that creates the database if it doesn't exist
		String sql_stmt = "CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET " + charSet + " COLLATE "
				+ uniCode + ";";
		statement = connection.createStatement();
		// Executes the statement
		statement.executeUpdate(sql_stmt);
		// Just notifying that this method was executed, can remove later
		System.out.println("<Required databse setup correctly>");
	}

	/**
	 * Creates all the Tables within the Database, this is the thrid method ran when
	 * {@link SQL#onStartup()} is executed.
	 * 
	 * @author Faris Nassif
	 */
	private static void createTables() throws SQLException {

		sqlCreate = "CREATE TABLE IF NOT EXISTS " + "users" + "  " + "(user_id SMALLINT(6) NOT NULL AUTO_INCREMENT,"
				+ "name    VARCHAR(25)," + "password VARCHAR(64)," + "PRIMARY KEY(user_id)) Engine=InnoDB";

		statement = connection.createStatement();
		statement.execute(sqlCreate);

		sqlCreate = "CREATE TABLE IF NOT EXISTS " + "war_saves" + "  " + "(save_id SMALLINT(6) NOT NULL AUTO_INCREMENT,"
				+ "player_name    VARCHAR(25)," + "file_name VARCHAR(20)," + "time_stamp    VARCHAR(40),"
				+ "PRIMARY KEY(save_id)) Engine=InnoDB";

		statement = connection.createStatement();
		statement.execute(sqlCreate);
		
		sqlCreate = "CREATE TABLE IF NOT EXISTS " + "lives_saves" + "  " + "(save_id SMALLINT(6) NOT NULL AUTO_INCREMENT,"
				+ "player_name    VARCHAR(25)," + "file_name VARCHAR(20)," + "time_stamp    VARCHAR(40),"
				+ "PRIMARY KEY(save_id)) Engine=InnoDB";

		statement = connection.createStatement();
		statement.execute(sqlCreate);
	}

	/**
	 * Inserts a New user into the [Users] table and performs a SHA on their
	 * password
	 * 
	 * @param name     - Name the user has entered
	 * @param password - Password the user has entered
	 * @throws SQLException
	 */
	public static void insertUser(String name, String password) throws SQLException {
		String insertUser = "INSERT INTO USERS VALUES (0,'" + name + "', SHA1('" + password + "'))";
		statement = connection.createStatement();
		statement.execute(insertUser);
	}

	/**
	 * Executed from {@link server.Server#run Server.run()} at the end when the
	 * Server Connection is terminated
	 * 
	 * @param name     - Name the user has entered
	 * @param password - Password the user has entered
	 * @throws SQLException
	 */
	public static void closeConnection(String clientIP) {
		try {
			connection.close();
			System.out.println("Closing SQL Connection for client " + clientIP + " ...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executed from {@link server.Server#run Server.run()}, Checks to see if the
	 * Username + Password match an already Registered user when someone tries to
	 * Login
	 * 
	 * @param name     - Name the user has entered
	 * @param password - Password the user has entered
	 * @throws SQLException
	 */
	public static boolean queryForUser(String name, String password) throws SQLException {
		boolean found = false;
		String query = "SELECT * from users WHERE name ='" + name + "' AND password = SHA1('" + password + "')";
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			// userName is what was returned under the 'Name' column when the query was ran
			String userName = rs.getString("name");
			// userPassword is what was returned under the 'Password' col when query was run
			String userPassword = rs.getString("password");

			// If the name returned and the password returned match the name & pw
			if ((userName.equalsIgnoreCase(name)) && (userPassword.equals(Services.shaPassword(password)))) {
				found = true;
			} else {
				found = false;
			}
		}
		return found;
	}

	/**
	 * Executed from {@link games.War#runGame War.runGame()}, Inserts a new users
	 * save of the Gamestate into the [WAR_SAVES] table
	 * 
	 * @param name      - Name of the current Logged in User
	 * @param fileName  - The file that all the players hands will be saved in,
	 *                  these are always unique files for each save
	 * @param timeStamp - The exact time and date the save was created by the user
	 * @throws SQLException
	 */
	public static void insertWarSave(String name, String fileName, LocalDateTime timeStamp) throws SQLException {
		String insertUser = "INSERT INTO WAR_SAVES VALUES (0,'" + name + "', '" + fileName + "', '" + timeStamp + "')";
		statement = connection.createStatement();
		statement.execute(insertUser);
	}
	
	/**
	 * Executed from {@link games.Lives#runGame Lives.runGame()}, Inserts a new users
	 * save of the Gamestate into the [LIVES_SAVES] table
	 * 
	 * @param name      - Name of the current Logged in User
	 * @param fileName  - The file that all the players hands will be saved in,
	 *                  these are always unique files for each save
	 * @param timeStamp - The exact time and date the save was created by the user
	 * @throws SQLException
	 */
	public static void insertLivesSave(String name, String fileName, LocalDateTime timeStamp) throws SQLException {
		String insertUser = "INSERT INTO LIVES_SAVES VALUES (0,'" + name + "', '" + fileName + "', '" + timeStamp + "')";
		statement = connection.createStatement();
		statement.execute(insertUser);
	}

	/**
	 * Checks to see if there are saves in the databse from the specific Player,
	 * returns them if there is.
	 * 
	 * @param playerName - Name of the current Logged in User
	 * @return Returns all the saves that are found that were saved by the specific
	 *         <b>playerName</b>
	 * @throws SQLException
	 */
	public static String queryWarSaves(String playerName) throws SQLException {
		String query = "SELECT * from war_saves WHERE player_name ='" + playerName + "'";
		ResultSet rs = statement.executeQuery(query);
		String returnedSaves = "";
		while (rs.next()) {
			int saveId = rs.getInt("save_id");
			String playersName = rs.getString("player_name");
			String timeStamp = rs.getString("time_stamp");
			String currentSave = "Save ID: " + saveId + "| Name: " + playersName + "| Time Stamp: " + timeStamp + "\n";
			returnedSaves += currentSave;
		}
		return returnedSaves;
	}
	
	/**
	 * Checks to see if there are saves in the databse from the specific Player,
	 * returns them if there is.
	 * 
	 * @param playerName - Name of the current Logged in User
	 * @return Returns all the saves that are found that were saved by the specific
	 *         <b>playerName</b>
	 * @throws SQLException
	 */
	public static String queryLivesSaves(String playerName) throws SQLException {
		String query = "SELECT * from lives_saves WHERE player_name ='" + playerName + "'";
		ResultSet rs = statement.executeQuery(query);
		String returnedSaves = "";
		while (rs.next()) {
			int saveId = rs.getInt("save_id");
			String playersName = rs.getString("player_name");
			String timeStamp = rs.getString("time_stamp");
			String currentSave = "Save ID: " + saveId + "| Name: " + playersName + "| Time Stamp: " + timeStamp + "\n";
			returnedSaves += currentSave;
		}
		return returnedSaves;
	}

	/**
	 * Provides the user with the specific Save file they're looking for so they may
	 * continue where they left off.
	 * 
	 * @param gameTable  - Whatever game the user is looking to load from
	 * @param saveId     - Unique Save ID that they will use to reference the save
	 * @param playerName - Name of the current Logged in User
	 * @return Returns the save file the User has selected to Load in
	 * @throws SQLException
	 */
	public static String loadSave(String gameTable, int saveId, String playerName) throws SQLException {
		String query = "SELECT * from " + gameTable + " WHERE save_id =" + saveId + " AND player_name = '" + playerName
				+ "'";
		ResultSet rs = statement.executeQuery(query);
		String returnedSaveFile = "";
		while (rs.next()) {
			String file = rs.getString("file_name");
			returnedSaveFile = file;
		}
		return returnedSaveFile;
	}

}
