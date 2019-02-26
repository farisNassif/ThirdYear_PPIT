package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import services.Services;
import services.Validation;

public class ServerRunner extends Thread {
	Socket clientSocket;
	String message;
	int clientID = -1;
	boolean running = true;
	static ObjectOutputStream out;
	static ObjectInputStream in;

	ServerRunner(Socket s, int i) {
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
				// Welcome, Press 1 for Login or 2 for Registration
				sendMessage(Services.welcomeUser());
				message = (String) in.readObject();

				// Passing 1,2 or Invalid input to this method + Sending result
				sendMessage(Validation.loginOrRegister(message));

				// Press X to exit or anything else to return to the top of the do/while
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
		//sendMessage("D");

	}

	public static void registerUser() {

	}

}
