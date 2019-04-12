package games;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import services.Services;

/**
 * @version 1.1
 * @author Cormac Raftery & Faris Nassif <br>
 *         <br>
 *         War card game
 */
public class War {

	private final static int dealtCardsAmount = 13;
	static int pointsOnTheBattlefield;
	static int[] shuffledDeck = new int[156];
	static int num;
	static int[][] hands = new int[10][13];// first dimension is player number, second is their cards
	static int[] playerPoints = new int[10];
	static int i, j, k;
	static int players, playerTurn;
	static int playerChoice;
	static int[] playerCard = new int[10];
	static int[] playersCard = new int[10];
	static int roundNum = 13;
	static int totalRoundPoints;
	static int largest, largestPosition;
	static int roundOption;
	static char facecard;
	static String input = "";
	private static Scanner console;

	public static void runGame(ObjectInputStream in, ObjectOutputStream out)
			throws ClassNotFoundException, IOException {
		console = new Scanner(System.in);

		sendMessage("Would you like to: \nEnter 1. Start a new game\nEnter 2. Load the previously saved game\n", out);
		input = (String) in.readObject();
		roundOption = Integer.parseInt(input);

		if (roundOption == 1) {
			sendMessage("Please enter the number of players(2-10):", out);
			input = (String) in.readObject();
			players = Integer.parseInt(input);
			if (players < 5)// shuffles from one deck
			{
				Services.shuffledeck(j, num, shuffledDeck);
				deal();
			} else if (players < 9)// shuffles from two decks
			{
				Services.shuffledeck(j, num, shuffledDeck);
				for (i = 0; i < 52; i++) {
					Services.shuffledeck(j, num, shuffledDeck);
				}
				Services.shuffledeck(j, num, shuffledDeck);
				deal();
			} else// shuffles from three decks
			{
				Services.shuffledeck(j, num, shuffledDeck);
				for (i = 0; i < 52; i++) {
					shuffledDeck[i + 104] = shuffledDeck[i];
				}
				Services.shuffledeck(j, num, shuffledDeck);
				for (i = 0; i < 52; i++) {
					shuffledDeck[i + 52] = shuffledDeck[i];
				}
				Services.shuffledeck(j, num, shuffledDeck);
				deal();
			}
		} else {
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

					players = Integer.parseInt(line);
					line = filep.readLine();
					pointsOnTheBattlefield = Integer.parseInt(line);
					line = filep.readLine();
					roundNum = Integer.parseInt(line);
					line = filep.readLine();
					for (i = 0; i < players; i++) {
						playerPoints[i] = Integer.parseInt(line);
						line = filep.readLine();
					}
					for (i = 0; i < players; i++) {
						for (j = 0; j < 13; j++) {
							hands[i][j] = Integer.parseInt(line);
							line = filep.readLine();
						}
					}
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// Closes the file
			try {
				filep.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		while (roundNum > 0)// detects if the game is finished
		{
			sendMessage(
					"Would you like to: \nEnter 1. Complete the next round\nEnter 2. Save the game\nEnter 3. Output the game status\nEnter 4. Exit the game\n",
					out);
			input = (String) in.readObject();
			roundOption = Integer.parseInt(input);
			while (roundOption != 1) {
				switch (roundOption) {
				case 1:// next round
					break;
				case 2:
					// Saves the game
					try (FileWriter fw = new FileWriter("gamestate.txt", false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter outt = new PrintWriter(bw)) {
						outt.println(players);
						outt.println(pointsOnTheBattlefield);
						outt.println(roundNum);
						for (i = 0; i < players; i++) {
							outt.println(playerPoints[i]);
						}
						for (i = 0; i < players; i++) {
							for (j = 0; j < 13; j++) {
								outt.println(hands[i][j]);
							}
						}
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					sendMessage(
							"Would you like to: \nEnter 1. Complete the next round\nEnter 2. Save the game\nEnter 3. Output the game status\nEnter 4. Exit the game\n",
							out);
					input = (String) in.readObject();
					roundOption = Integer.parseInt(input);
					break;
				case 3:
					// Show Score
					for (i = 1; i <= players; i++) {
						sendMessage("Player " + i + " has " + playerPoints[i - 1] + " points", out);
					}
					sendMessage(
							"Would you like to: \nEnter 1. Complete the next round\nEnter 2. Save the game\nEnter 3. Output the game status\nEnter 4. Exit the game\n",
							out);
					input = (String) in.readObject();
					roundOption = Integer.parseInt(input);
					break;
				case 4:
					// Exits the Game
					System.exit(0);
					break;
				default:
					sendMessage(
							"Would you like to: \nEnter 1. Complete the next round\nEnter 2. Save the game\nEnter 3. Output the game status\nEnter 4. Exit the game\n",
							out);
					input = (String) in.readObject();
					roundOption = Integer.parseInt(input);
					break;
				}
			}
			for (i = 1; i <= players; i++)// each player selects card
			{
				playRound(i, in, out);
			}
			roundNum--;
			// Determines the highest unique card value
			whowins(in, out);
			totalRoundPoints = 0;
			// Print out players played card
			for (i = 1; i <= players; i++) {
				// Handles cards 2-10
				if (playersCard[i - 1] <= 10) {
					sendMessage("Player " + i + " played: " + playersCard[i - 1] + "\n", out);
					// Handles cards J - A
				} else if (playersCard[i - 1] == 11) {
					sendMessage("Player " + i + "  played: J\n", out);
				} else if (playersCard[i - 1] == 12) {
					sendMessage("Player " + i + "  played: Q\n", out);
				} else if (playersCard[i - 1] == 13) {
					sendMessage("Player " + i + "  played: K\n", out);
				} else if (playersCard[i - 1] == 14) {
					sendMessage("Player " + i + "  played: A\n", out);
				}
			}
		}
	}

	public static void whowins(ObjectInputStream in, ObjectOutputStream out) {
		for (i = 0; i < players; i++) {
			totalRoundPoints += playerCard[i];// totals card values
		}
		largest = 0;
		for (i = 0; i < players; i++) {
			if (largest < playerCard[i] || largest == 0) {
				largest = playerCard[i];
				largestPosition = i;
			} else if (largest == playerCard[i])// set player cards to 0 but keeps value of largest incase of 2+ the
												// same cards
			{
				for (j = 0; j < players; j++) {
					if (largest == playerCard[j]) {
						playerCard[j] = 0;
					}
				}
				largest = 0;
				i = 0;
			}
		}
		if (largest == 0)// points get added to next round
		{
			pointsOnTheBattlefield = totalRoundPoints;
			totalRoundPoints = 0;
			sendMessage("The points are on the battlefield! Next round's winner gets these " + pointsOnTheBattlefield
					+ " points extra!\n", out);
		} else {

			playerPoints[largestPosition] += totalRoundPoints + pointsOnTheBattlefield;
			sendMessage("Player " + (largestPosition + 1) + " wins the round and earns "
					+ (totalRoundPoints + pointsOnTheBattlefield) + " points\n", out);
			pointsOnTheBattlefield = 0;
		}
		j = 0;

	}

	public static void playRound(int playerTurn, ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException, IOException {
		sendMessage("Player " + playerTurn + " enter any number when ready:\n", out);// used to conceal cards from
		input = (String) in.readObject();
		j = Integer.parseInt(input);																				// previous
																						// player
		for (j = 0; j < roundNum; j++) {
			if (hands[(playerTurn - 1)][j] <= 10) {
				sendMessage(j + "." + hands[(playerTurn - 1)][j], out);
			} else if (hands[(playerTurn - 1)][j] == 11) {
				sendMessage(j + ".J", out);
			} else if (hands[(playerTurn - 1)][j] == 12) {
				sendMessage(j + ".Q", out);
			} else if (hands[(playerTurn - 1)][j] == 13) {
				sendMessage(j + ".K", out);
			} else if (hands[(playerTurn - 1)][j] == 14) {
				sendMessage(j + ".A", out);
			}
		}
		sendMessage("Player " + playerTurn +  " enter the index number of the card you would like to play\n", out);
		input = (String) in.readObject();
		playerChoice = Integer.parseInt(input);
		
		playerCard[(playerTurn - 1)] = hands[(playerTurn - 1)][playerChoice];
		playersCard[(playerTurn - 1)] = hands[(playerTurn - 1)][playerChoice];// must have 2 similar arrays incase one
																				// gets set to 0 due to elimination
		for (j = playerChoice; j < 12; j++) {
			hands[(playerTurn - 1)][j] = hands[(playerTurn - 1)][j + 1];// moves selected card to end of the array
		}

	} //

	public static void deal() {
		k = 0;
		for (i = 0; i < dealtCardsAmount; i++) {
			for (j = 0; j < players; j++) {
				hands[j][i] = shuffledDeck[k];// deals cards realistically 1 to each player at a time,also ensures decks
												// get mixed when playing with 2-3 decks
				k++;
			}
		}
	} // Deal Method

	public static void sendMessage(String msg, ObjectOutputStream out) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("To Client ==> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
} // War Class