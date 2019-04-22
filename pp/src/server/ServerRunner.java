package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * <b>ServerRunner</b> implements a <a href=
 * "https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html"><i>ServerSocket</i></a>
 * that listens for a Client Connection. This class will run the code within {@link Server}
 */
public class ServerRunner {
	public static void main(String[] args) throws Exception {
		ServerSocket m_ServerSocket = new ServerSocket(2007, 10);
		int id = 0;
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			Server cliThread = new Server(clientSocket, id++);
			cliThread.start();
		}
	}

}
