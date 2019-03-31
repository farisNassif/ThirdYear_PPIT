package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import services.Services;
import services.Validation;

public class Server extends Thread {
	Socket clientSocket;
	String message;
	int clientID = -1;
	boolean running = true;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	// Used to see if file information is duplicated
	private boolean unique = true;
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
				// Welcome, Enter 1 for Login or 2 for Registration
				// sendMessage(Services.welcomeUser());
				sendMessage("\nWelcome to our Online Card Game Library\nPlease enter 1 to Register OR 2 to Login");
				message = (String) in.readObject();

				// Passing 1,2 or Invalid input to this method + Sending result
				// sendMessage(Validation.loginOrRegister(message));

				// If the user entered 1 or 2, execute if
				if (message.equalsIgnoreCase("1")) {
					sendMessage("You have chosen to Register");

					// Writing to the file
					BufferedWriter writer = new BufferedWriter(new FileWriter("Players.txt", true));

					sendMessage("Please enter your Player Name");
					playerName = (String) in.readObject();

					sendMessage("Please enter your Password");
					playerPassword = (String) in.readObject();

					writer.newLine();
					// Writing their Player Name, Password & Client ID to server file
					writer.write((playerName + " " + playerPassword + " " + clientSocket.getInetAddress().getHostName()
							+ " SHOULD BE local address: " + clientSocket.getLocalAddress().getHostAddress()));
					sendMessage("Welcome " + playerName + ", Your account is now registered and you may Log in.");
					writer.close();
				} else if (message.equalsIgnoreCase("2")) {
					// Logged in = false until user verified/logged in correctly
					loggedIn = 0;
					// Reading the file for validation (Making sure email + id is unique)
					Scanner scanner = new Scanner(new File("players.txt"));

					sendMessage("You have chosen to Login");

					sendMessage("Please enter your player name (Case Sensitive)");
					playerLoginName = (String) in.readObject();

					sendMessage("Please enter your password");
					playerLoginPassword = (String) in.readObject();

					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.contains(playerLoginName) && line.contains(playerLoginPassword)) {
							// playerMatched
							loggedIn = 1;
							sendMessage("Login successful");
						}
					}
					if (loggedIn == 0) {
						sendMessage("The Player Name and Password do not match!");
					}
					if (loggedIn == 1)
						do {
							sendMessage("Enter exit to logout or anything else to loop");
							message = (String) in.readObject();
						} while (!message.equalsIgnoreCase("Exit"));
				}
				// Enter X to exit or anything else to return to the top of the do/while
				sendMessage(Services.terminateConnection());
				message = (String) in.readObject();

			} while (!message.equalsIgnoreCase("X"));

			// Just to know a Client has ended their connection
			sendMessage("Terminating your Client Connection : ID - " + clientID + " : Address - "
					+ clientSocket.getInetAddress().getHostName());
			System.out.println(
					"Ending Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loginUser() {

	}

	public static void registerUser() {

	}

}
