package services;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Services {
	
	public static String welcomeUser() {
		return "\nWelcome to our Online Card Game Library\nPlease press 1 to Login OR 2 to Register";
	}

	public static String terminateConnection() {
		return "Press X to Terminate your connection OR any other key to Continue";

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
	
	

}
