package games;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Lives {
	private static Scanner console;
	static int roundNum = 0;
	static int playerLives[] = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
	static int deck[] = { 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9,
			9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14 };
	static int amtPlayers;
	static int[][] hands = new int[10][5];// first dimension is player number, second is their cards
	static int lastcard = 0;
	static boolean gameOver = false;

	public static void main(String[] deckgs) {
		console = new Scanner(System.in);
		int option=0;

		do {
			System.out.println("Would you like to:\n1. start a new game \n2. load a previous game");
			option = console.nextInt();

			if (option == 1) {
				System.out.println("How many players are there?(max 10)");
				amtPlayers = console.nextInt();
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
					playRound(i);
				}
				roundNum++;
			}
			System.out.print("At the end of the round the lives are: ");
			for (int i = 0; i < amtPlayers; i++) {
				System.out.print("player " + (i + 1) + " has " + playerLives[i] + " lives ");
			}
			System.out.println();
			roundNum = 0;
			checkLives();
			saveGame();
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

	private static void saveGame() {
		try (FileWriter fw = new FileWriter("gamestate.txt", false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(amtPlayers);
			System.out.println("Saving the Game!");
			for (int i = 0; i < amtPlayers; i++) {
				out.println(playerLives[i]);
			}
			out.close();
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

	public static void playRound(int playerTurn) {
		System.out.printf("Player %d enter any number when ready:\n", playerTurn + 1);// used to conceal cards from
																						// previous

		for (int j = 0; j < 5 - roundNum; j++) {
			if (hands[(playerTurn)][j] <= 10) {
				System.out.printf("%d.%d\n", j, hands[(playerTurn)][j]);
			} else if (hands[(playerTurn)][j] == 11) {
				System.out.printf("%d.J\n", j);
			} else if (hands[(playerTurn)][j] == 12) {
				System.out.printf("%d.Q\n", j);
			} else if (hands[(playerTurn)][j] == 13) {
				System.out.printf("%d.K\n", j);
			} else if (hands[(playerTurn)][j] == 14) {
				System.out.printf("%d.A\n", j);
			}
		}
		int playerChoice = 0;
		System.out.printf("Player %d enter the index number of the card you would like to play\n", playerTurn + 1);
		do {

			playerChoice = console.nextInt();

			if (playerChoice > 4 - roundNum || playerChoice < -1) {
				System.out.println("Please enter a valid int");
			}
		} while (playerChoice > 4 - roundNum || playerChoice < -1);

		if (hands[(playerTurn)][playerChoice] <= 10) {
			System.out.println("Player " + (playerTurn + 1) + " played a " + hands[(playerTurn)][playerChoice]);
		} else if (hands[(playerTurn)][playerChoice] == 11) {
			System.out.println("Player " + (playerTurn + 1) + " played a J");
		} else if (hands[(playerTurn)][playerChoice] == 12) {
			System.out.println("Player " + (playerTurn + 1) + " played a Q");
		} else if (hands[(playerTurn)][playerChoice] == 13) {
			System.out.println("Player " + (playerTurn + 1) + " played a K");
		} else if (hands[(playerTurn)][playerChoice] == 14) {
			System.out.println("Player " + (playerTurn + 1) + " played a A");
		}
		if (hands[(playerTurn)][playerChoice] == lastcard) {
			System.out.println("Player loses 1 life!");
			if (playerTurn == 0) {
				System.out.println("player " + (amtPlayers) + " loses life!");
				playerLives[amtPlayers - 1]--;
			} else {
				System.out.println("player " + playerTurn + " loses life!");
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