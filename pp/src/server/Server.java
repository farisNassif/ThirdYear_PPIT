package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import services.Services;
import services.Validation;
import database.SQL;

/**
 * @version 1.4
 * @author Faris Nassif & Cormac Raftery <br>
 *         <br>
 *         The class <b>Server</b> extends <b>Thread</b> <br>
 *         Here the client will be able to access all functionality of the
 *         Program
 */
public class Server extends Thread {
	Socket clientSocket;
	String message;
	int clientID = -1;
	boolean running = true;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	// Used for registration/login
	private String playerName;
	private String playerPassword;
	private int loggedIn;
	private String playerLoginName;
	private String playerLoginPassword;

	Server(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	/**
	 * @param msg Accepts a String which is then sent to the specified Client
	 */
	private static void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("To Client ==> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * Runs the client thread through the server, allowing them to navigate the
	 * Program. The bulk of code contained within {@link server#run()} is wrapped
	 * within other class methods such as {@link SQL#openConnection()} |
	 * {@link Services#terminatingConnection(int clientID, String address)} .
	 */
	public void run() {
		// Client Accepted
		System.out.println(" client ip address =" + clientSocket.getRemoteSocketAddress().toString());
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Accepted Client : ID - " + clientID + " : Address - "
					+ clientSocket.getInetAddress().getHostName());

			do {
				// Initiates the SQL Connection
				SQL.main();
				// Welcome, Enter 1 for Login or 2 for Registration
				sendMessage(Services.welcomeUser());
				message = (String) in.readObject();

				// If the user wants to register
				if (message.equalsIgnoreCase("1")) {
					sendMessage("You have chosen to Register");

					sendMessage("Please enter your Player Name");
					playerName = (String) in.readObject();

					sendMessage("Please enter your Password (Least 6 Characters)");
					playerPassword = (String) in.readObject();

					// Loop until it's 6 characters
					while (playerPassword.length() < 6) {
						sendMessage("Please enter your Password (Least 6 Characters)");
						playerPassword = (String) in.readObject();
					}

					// If theres already someone reigstered with the same name + password
					if (SQL.queryForUser(playerName, playerPassword) == true) {
						sendMessage("Sorry there is already a user with that name and password");
					} else {
						sendMessage(playerName + " you are now Registered and may Login!");
						SQL.insertUser(playerName, playerPassword);
					}

					// Else if user wants to just Login
				} else if (message.equalsIgnoreCase("2")) {
					// Logged in = false until user verified/logged in correctly
					loggedIn = 0;

					sendMessage("You have chosen to Login");

					sendMessage("Please enter your player name (Case Sensitive)");
					playerLoginName = (String) in.readObject();

					sendMessage("Please enter your password");
					playerLoginPassword = (String) in.readObject();

					if (SQL.queryForUser(playerLoginName, playerLoginPassword) == true) {
						// Player is matched and found in the database!
						loggedIn = 1;
						sendMessage("Login successful, Welcome " + playerLoginName);

						// Executes what needs to execute once a user is Logged in
						do {
							sendMessage("Enter 1 to play lives or anything else to not");
							String livesOrNot = (String) in.readObject();

							if (livesOrNot.equals("1")) {
								games.Lives.runGame(in, out);
							}

							sendMessage("Enter exit to logout or anything else to loop");
							message = (String) in.readObject();
						} while (!message.equalsIgnoreCase("Exit"));
					} else {
						sendMessage("\nDetails not found within the Database, please try again");
					}
				}
				// Enter X to exit or anything else to return to the top of the do/while
				sendMessage(Services.loopMessage());
				message = (String) in.readObject();

			} while (!message.equalsIgnoreCase("X"));

			// Message that alerts client of connection termination
			sendMessage(Services.terminatingConnection(clientID, clientSocket.getInetAddress().getHostName()));
			// Closing the SQL Connection;
			SQL.closeConnection(clientSocket.getInetAddress().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
