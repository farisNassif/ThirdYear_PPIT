package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import services.Services;
import services.Validation;
import database.SQL;

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

	private static void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("To Client ==> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void run() {
		// Client Accepted
		System.out.println(
				"Accepted Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
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

				// If the user entered 1 or 2, execute if
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

					// Saving their information in a database
					SQL.insertUser(playerName, playerPassword);
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
							games.Lives.runGame(in, out);
							
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
