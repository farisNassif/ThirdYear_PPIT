package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ServerSocket m_ServerSocket = new ServerSocket(2004, 10);
		int id = 0;
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			Server cliThread = new Server(clientSocket, id++);
			cliThread.start();
		}
	}
}
