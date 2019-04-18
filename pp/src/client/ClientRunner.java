package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import database.SQL;
import services.Services;

/**
 * @version 1.2
 * @author Faris Nassif & Cormac Raftery <br>
 *         <br>
 *         The class <b>Client</b> contains all relevant Client code needed to
 *         communicate with the {@link server.Server Server} class. The majority
 *         of code is found Server side.
 */
public class ClientRunner {
	Socket requestSocket;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	String message;
	String ipaddress;
	Scanner input;

	/**
	 * Executes the {@link #run()} method allowing connection to the Specified
	 * Server
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		ClientRunner client = new ClientRunner();
		client.run();
	}

	/**
	 * Runs the Client socket on the specified server's IP & Port.
	 */
	void run() {
		input = new Scanner(System.in);
		try {

			// 1. creating a socket to connect to the server
			ipaddress = "127.0.0.1";
			System.out.println("Connection to the Server Established");
			requestSocket = new Socket(ipaddress, 2004);
			System.out.println("Connected to " + ipaddress + " in port 2004");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			
			// Will loop until the user opts to exit
			do {
				// Initial object read
				message = (String) in.readObject();
				// If the message from the server contains enter, traverse the if
				if (message.toLowerCase().contains("enter")) {
					System.out.println(message);
					message = input.nextLine();
					sendMessage(message);
				} else {
					// Jut prints out the server message for the client
					System.out.println(message);
				}
			} while (!message.equalsIgnoreCase("x"));

			// Notifying client of connection termination
			message = (String) in.readObject();
			System.out.println(message);
		}

		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	/**
	 * @param msg Accepts a String which is then sent to the Server
	 */
	private static void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}