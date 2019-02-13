package pp.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message = "";
	String ipaddress;
	Scanner input;

	Client() {
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

			// Client.....

			do {
				
				// List of options 1-2 for the user
				message = (String) in.readObject();
				System.out.println(message);
				message = input.next();
				sendMessage(message);

				if (message.equals("1")) {
					// Dealing with player registration
					registeringUser();
				}

			} while (!message.equalsIgnoreCase("x"));
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
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("Sending to server => " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	void registeringUser() throws ClassNotFoundException, IOException {
		if (message.equals("1")) {
			// Dealing with information message to user
			message = (String) in.readObject();
			System.out.println(message);

			// Dealing with name
			message = (String) in.readObject();
			System.out.println(message);
			// Needed two of these otherwise the program would just skip and send a blank
			// line
			message = input.nextLine();
			message = input.nextLine();
			sendMessage(message);

			// Dealing with Employee ID
			message = (String) in.readObject();
			System.out.println(message);
			message = input.next();
			sendMessage(message);

			// Dealing with Email
			message = (String) in.readObject();
			System.out.println(message);
			message = input.next();
			sendMessage(message.toLowerCase());

			// Dealing with Department
			message = (String) in.readObject();
			System.out.println(message);
			// Needed two of these otherwise the program would just skip and send a blank
			// line
			message = input.nextLine();
			message = input.nextLine();
			sendMessage(message);

			// Result of registration, either match was found or user was registered
			message = (String) in.readObject();
			System.out.println(message);
		}
	}
	public static void main(String args[]) {
		Client client = new Client();
		client.run();
	}
}