package games;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.io.ObjectInputStream;

public class Lives {
	static String input = "";
	static int roundNum = 0;
	static int playerLives[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	static int deck[] = { 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9,
			9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14 };
	static int amtPlayers;
	static int[][] hands = new int[10][5];// first dimension is player number, second is their cards
	static int lastcard = 0;
	static boolean gameOver = false;

	public static void runGame(ObjectInputStream in, ObjectOutputStream out)
			throws ClassNotFoundException, IOException {

		int option = 0;

		do {
			sendMessage(
					"Would you like to:\nEnter 1. Start a new game of Lives\nEnter 2. Load a previous game of Lives",
					out);
			input = (String) in.readObject();
			Integer.parseInt(input);
			option = Integer.parseInt(input);

			if (option == 1) {
				sendMessage("Enter how many players are there?(max 10)", out);
				input = (String) in.readObject();
				Integer.parseInt(input);
				amtPlayers = Integer.parseInt(input);
				
				// Making sure there are only a minimum of 2 or max of 10
				while (amtPlayers > 10 || amtPlayers < 2)
				{
					sendMessage("Enter at least 2 players and at most 10", out);
					input = (String) in.readObject();
					Integer.parseInt(input);
					amtPlayers = Integer.parseInt(input);
				}
				

			} else if (option == 2) {
				loadGame();
			}
		} while (option != 1 && option != 2);
		while (!gameOver) {
			shuffledeck();
			deal();
			while (roundNum < 5) {
				for (int i = 0; i < amtPlayers; i++)// each player selects card
				{
					playRound(i, out, in);
				}
				roundNum++;
			}
			sendMessage("At the end of the round the lives are: ", out);
			for (int i = 0; i < amtPlayers; i++) {
				sendMessage("player " + (i + 1) + " has " + playerLives[i] + " lives ", out);
			}
			roundNum = 0;
			checkLives();
			saveGame(out);

		}
	}

	public static void sendMessage(String msg, ObjectOutputStream out) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("To Client ==> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private static void loadGame() {

		BufferedReader filep = null;
		try {
			filep = new BufferedReader(new InputStreamReader(new FileInputStream("gamestate.txt")));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		String line = "";
		try {
			if ((line = filep.readLine()) == null) {
				System.out.printf("The file cannot be opened\n");
			} else {

				amtPlayers = Integer.parseInt(line);
				line = filep.readLine();

				for (int i = 0; i < amtPlayers; i++) {
					playerLives[i] = Integer.parseInt(line);
					line = filep.readLine();
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			filep.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveGame(ObjectOutputStream out) {
		try (FileWriter fw = new FileWriter("gamestate.txt", false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter outt = new PrintWriter(bw)) {
			outt.println(amtPlayers);
			sendMessage("Saving the Game!", out);
			for (int i = 0; i < amtPlayers; i++) {
				outt.println(playerLives[i]);
			}
			outt.close();
		} catch (IOException e) {
		}

	}

	private static void checkLives() {
		int leastLives = 5;
		for (int i = 0; i < amtPlayers; i++) {
			if (playerLives[i] < leastLives) {
				leastLives = playerLives[i];
			}
		}
		if (leastLives <= 0) {
			gameOver = true;
		}
	}

	public static void deal() {
		int k = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < amtPlayers; j++) {
				hands[j][i] = deck[k];// deals cards realistically 1 to each player at a time
				k++;
			}
		}
	}

	public static void playRound(int playerTurn, ObjectOutputStream out, ObjectInputStream in)
			throws ClassNotFoundException, IOException {
		// Used to conceal cards from other players
		sendMessage("Player " + (playerTurn + 1) + " Enter something when you're ready to see your cards", out);
		String input = (String) in.readObject();

		for (int j = 0; j < 5 - roundNum; j++) {
			if (hands[(playerTurn)][j] <= 10) {
				sendMessage(j + "." + hands[(playerTurn)][j], out);
			} else if (hands[(playerTurn)][j] == 11) {
				sendMessage(j + ".J", out);
			} else if (hands[(playerTurn)][j] == 12) {
				sendMessage(j + ".Q", out);
			} else if (hands[(playerTurn)][j] == 13) {
				sendMessage(j + ".K", out);
			} else if (hands[(playerTurn)][j] == 14) {
				sendMessage(j + ".A", out);
			}
		}
		int playerChoice = 0;
		sendMessage("Player " + (playerTurn + 1) + " Enter the index number of the card you would like to play\n", out);
		do {

			input = (String) in.readObject();
			Integer.parseInt(input);
			playerChoice = Integer.parseInt(input);

			if (playerChoice > 4 - roundNum || playerChoice < -1) {
				sendMessage("Please enter a valid input", out);
			}
		} while (playerChoice > 4 - roundNum || playerChoice < -1);

		if (hands[(playerTurn)][playerChoice] <= 10) {
			sendMessage("Player " + (playerTurn + 1) + " played a " + hands[(playerTurn)][playerChoice], out);
		} else if (hands[(playerTurn)][playerChoice] == 11) {
			sendMessage("Player " + (playerTurn + 1) + " played a J", out);
		} else if (hands[(playerTurn)][playerChoice] == 12) {
			sendMessage("Player " + (playerTurn + 1) + " played a Q", out);
		} else if (hands[(playerTurn)][playerChoice] == 13) {
			sendMessage("Player " + (playerTurn + 1) + " played a K", out);
		} else if (hands[(playerTurn)][playerChoice] == 14) {
			sendMessage("Player " + (playerTurn + 1) + " played a A", out);
		}
		if (hands[(playerTurn)][playerChoice] == lastcard) {
			sendMessage("Player loses 1 life!", out);
			if (playerTurn == 0) {
				sendMessage("player " + (amtPlayers) + " loses life!", out);
				playerLives[amtPlayers - 1]--;
			} else {
				sendMessage("player " + playerTurn + " loses life!", out);
				playerLives[playerTurn - 1]--;
			}
		}
		lastcard = hands[(playerTurn)][playerChoice];
		int a = hands[(playerTurn)][playerChoice];
		hands[(playerTurn)][playerChoice] = hands[(playerTurn)][4 - roundNum];
		hands[(playerTurn)][4 - roundNum] = a;
	}

	static void shuffledeck() {
		Random rnd = ThreadLocalRandom.current();
		for (int i = deck.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = deck[index];
			deck[index] = deck[i];
			deck[i] = a;
		}
	}

}