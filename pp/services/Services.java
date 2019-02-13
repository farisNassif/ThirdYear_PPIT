package pp.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Services {

	ObjectOutputStream out;
	ObjectInputStream in;
	
	protected void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	protected void readInAndPrint(String msg)
	{
		 try {
			msg = (String)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 System.out.println(msg);
	}
}
