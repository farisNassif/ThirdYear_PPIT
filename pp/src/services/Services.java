package services;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Services {
	
	public static String welcomeUser() {
		return "\nWelcome to our Online Card Game Library\nPlease enter 1 to Register OR 2 to Login";
	}

	public static String loopMessage() {
		return "Enter X to Terminate your connection OR any other key to return to the Main Menu";
	}
	
	public static String terminatingConnection(int clientID, String address) {
		return "Terminating your Client Connection : ID - " + clientID + " : Address - "
				+ address;
	}

	public static void shuffledeck(int j, int num, int shuffledDeck[]) {
		int ar[] = { 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10,
				10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14 };

		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
		for (j = num; j < 51; ++j) {
			shuffledDeck[j] = ar[j + 1];// copies random index number to end of array
		}
	}
	
	// Called to generate a random string for the filename
	public static String generateString() {
		final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int maxLength = 9;
		Random random = new Random();
		StringBuilder builder = new StringBuilder(maxLength);

		// Looping 9 times, one for each char
		for (int i = 0; i < maxLength; i++) {
			builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
		}
		// Generates a random String that has may have a quintillion different combinations
		// (1/64^9)
		return builder.toString();
	}
	
	

}
