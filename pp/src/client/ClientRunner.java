package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientRunner {
	Socket requestSocket;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	String message;
	String ipaddress;
	Scanner input;

	ClientRunner() {
	}
	
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

			message = (String) in.readObject();
			// Will loop until the user opts to exit
			do {
				// Initial object read
				
				// If the message from the server contains enter, traverse the if
				if (message.toLowerCase().contains("enter")) {
					System.out.println(message);
					message = input.nextLine();
					sendMessage(message);
				} else
				// Otherwise it's just a display message
				{
					System.out.println(message);
				}
				message = (String) in.readObject();
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

	// Sending messages to the server
	private static void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("To server => " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) {
		// Where program is ran from
		ClientRunner client = new ClientRunner();
		client.run();
	}
}