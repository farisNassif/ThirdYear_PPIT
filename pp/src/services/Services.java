package services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Within this class there are Methods that may be considered for use in
 * multiple classes that may see high reusability
 * 
 * @author Faris
 *
 */
public class Services {

<<<<<<< HEAD
	/**
	 * Just a Welcome menu message for the user which instructs them on what to
	 * press to navigate the program
	 * 
	 * @author Faris
	 */
=======
>>>>>>> 49e411997643e6056dbd7d943e71d59967b17bea
	public static String welcomeUser() {
		return "\nWelcome to our Online Card Game Library\nPlease enter 1 to Register OR 2 to Login";
	}

	/**
	 * Just a menu message for the user which instructs them on what to press to
	 * navigate the program
	 * 
	 * @author Faris
	 */
	public static String loopMessage() {
<<<<<<< HEAD
		return "Enter [X] to Terminate your connection to the Server\nEnter any other Key to Login/Register";
	}

	/**
	 * Message that alerts the client of their connection termination to the Server
	 * 
	 * @author Faris
	 */
=======
		return "Enter [X] to Terminate your connection to the Server\nEnter any other Key to Continue";
	}

>>>>>>> 49e411997643e6056dbd7d943e71d59967b17bea
	public static String terminatingConnection(int clientID, String address) {
		return "Terminating your Client Connection : ID - " + clientID + " : Address - " + address;
	}

	/**
	 * Shuffles a deck of cards, can be implemented for any card game
	 * 
	 * @param j
	 * @param num
	 * @param shuffledDeck
	 */
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

	/**
	 * @author Faris Nassif
	 * @return Returns a Random Unique String (1/64^9)
	 */
	public static String generateString() {
		final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int maxLength = 9;
		Random random = new Random();
		StringBuilder builder = new StringBuilder(maxLength);

		// Looping 9 times, one for each char
		for (int i = 0; i < maxLength; i++) {
			builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
		}
		// Generates a random String may have a quintillion different combinations
		// (1/64^9)
		return builder.toString();
	}

	/**
	 * @author Faris Nassif
	 * @param password
	 * @return Returns the sha'd Password
	 */
	public static String shaPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	/**
	 * @author Faris Nassif
	 * @param Accepts a hash after padding which should have returned an array of
	 *                bytes
	 * @return Returns the result of formatting the hash which should be the sha
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
