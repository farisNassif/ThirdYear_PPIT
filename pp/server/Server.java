package pp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
  public static void main(String[] args) throws Exception 
  {
    ServerSocket m_ServerSocket = new ServerSocket(2004,10);
    int id = 0;
    while (true) 
    {
      Socket clientSocket = m_ServerSocket.accept();
      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
      cliThread.start();
    }
  }
}

class ClientServiceThread extends Thread {
  Socket clientSocket;
  String message;
  int clientID = -1;
  boolean running = true;
  ObjectOutputStream out;
  ObjectInputStream in;
  int num1, num2;
  int result;

  ClientServiceThread(Socket s, int i) {
    clientSocket = s;
    clientID = i;
  }

  void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
  
  public void run()
  {
    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
        + clientSocket.getInetAddress().getHostName());
    try 
    {
    	out = new ObjectOutputStream(clientSocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("Accepted Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
		
		
		//Server App......
		
		do
		{
		sendMessage("Press 1 to add\n Press 2 to substract\n Press 3 to multiple\n Press 4 to divide");
	
		message = (String)in.readObject();
	
		sendMessage("Please enter num 1");
	
		num1 = Integer.parseInt((String)in.readObject());
	
		sendMessage("Please enter num 2");
	
		num2 = Integer.parseInt((String)in.readObject());
	
		int result=-1;
		
		if(message.equalsIgnoreCase("1"))
		{
			result = num1 + num2;
		}
		
		else if(message.equalsIgnoreCase("2"))
		{
			result = num1 - num2;
		}
		
		else if(message.equalsIgnoreCase("3"))
		{
			result = num1 * num2;
		}
		
		else if(message.equalsIgnoreCase("4"))
		{
			result = num1 / num2;
		}
		
		sendMessage(""+result);
		
		sendMessage("Press Y to do another calculation or N to terminate");
		
		message = (String)in.readObject();
		
		}while(message.equalsIgnoreCase("Y"));

      
		System.out.println("Ending Client : ID - " + clientID + " : Address - "
		        + clientSocket.getInetAddress().getHostName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
