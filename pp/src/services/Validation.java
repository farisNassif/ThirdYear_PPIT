package services;

import server.Server;

public class Validation {

	public static String loginOrRegister(String message) {
		if (message.equals("1")) {
			Server.loginUser();
			return "You have chosen to Register";
		} else if (message.equals("2")) {
			Server.registerUser();		
			return "You have chosen to Login";
		} else {
			return "Invalid input, Please enter 1 OR 2";
		}
	}

}
