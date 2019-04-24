package games;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import database.SQL;
import services.Services;

/**
 * @version 1.1
 * @author Cormac Raftery & Faris Nassif <br>
 *         <br>
 *         War card game
 */
public class War {

	// Used to generate random string for the file
	private final int dealtCardsAmount = 13;
	int pointsOnTheBattlefield;
	int[] shuffledDeck = new int[156];
	int num;
	int[][] hands = new int[10][13];// first dimension is player number, second is their cards
	int[] playerPoints = new int[10];
	int i, j, k;
	int players, playerTurn;
	int playerChoice;
	int[] playerCard = new int[10];
	int[] playersCard = new int[10];
	int roundNum = 13;
	int totalRoundPoints;
	int largest, largestPosition;
	int roundOption;
	char facecard;
	String input = "";

	public War(String player, ObjectInputStream in, ObjectOutputStream out)
			throws ClassNotFoundException, IOException, SQLException {

		sendMessage(
				"Would you like to: \nEnter 1. Start a new game\nEnter 2. Load a previously saved game\n*Any other key will Return you to the Game Menu",
				out);
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
			warExecution(player, in, out);
		} else if (roundOption == 2) {
			if (SQL.queryWarSaves(player) == "") {
				sendMessage("Sorry, looks like you have no saves on your logged in account!", out);
			} else {
				sendMessage("Looks like you have saves on your logged in account!", out);
				sendMessage(SQL.queryWarSaves(player), out);
				sendMessage("Please Enter the ID of the save you wish to load", out);
				input = (String) in.readObject();
				int saveOption = Integer.parseInt(input);

				// Loads war save file
				String fileName = SQL.loadSave("war_saves", saveOption, player);

				if (fileName == "") {
					sendMessage("Save ID was not found for this player! Sorry", out);
				} else {
					sendMessage("Processing the save file ...\n<Save Processed>", out);
					BufferedReader filep = null;
					try {
						filep = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
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
						warExecution(player, in, out);
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					// close(filep);//close the file
					try {
						filep.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void warExecution(String player, ObjectInputStream in, ObjectOutputStream out)
			throws ClassNotFoundException, IOException, SQLException {
		while (roundNum > 0 && roundOption != 4)// detects if the game is finished
		{
			sendMessage(
					"Would you like to: \nEnter 1. Complete the next round\nEnter 2. Save the game\nEnter 3. Output the game status\nEnter 4. Exit the game\n",
					out);
			input = (String) in.readObject();
			roundOption = Integer.parseInt(input);
			switch (roundOption) {
			case 1:// next round
				break;
			case 2:
				String fileName = (Services.generateString() + ".txt");
				try (FileWriter fw = new FileWriter(fileName, false);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter write = new PrintWriter(bw)) {
					write.println(players);
					write.println(pointsOnTheBattlefield);
					write.println(roundNum);
					for (i = 0; i < players; i++) {
						write.println(playerPoints[i]);
					}
					for (i = 0; i < players; i++) {
						for (j = 0; j < 13; j++) {
							write.println(hands[i][j]);
						}
					}
					sendMessage(
							"Saving the Gamestate ...\n"
							+ "Game Saved!\n",
							out);
					write.close();
					DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					SQL.insertWarSave(player, fileName, now);
				} catch (IOException e) {
					System.out.println(e);
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
				// Exit
				roundOption = 4;
				break;
			default:
				sendMessage(
						"Would you like to: \nEnter 1. Complete the next round\nEnter 2. Save the game\nEnter 3. Output the game status\nEnter 4. Exit the game\n",
						out);
				input = (String) in.readObject();
				roundOption = Integer.parseInt(input);
				break;
			}

			if (roundOption != 4) {
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
	}

	public void whowins(ObjectInputStream in, ObjectOutputStream out) {
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

	public void playRound(int playerTurn, ObjectInputStream in, ObjectOutputStream out)
			throws ClassNotFoundException, IOException {
		sendMessage("Player " + playerTurn + " enter any number when ready:\n", out);// used to conceal cards from
		input = (String) in.readObject();
		j = Integer.parseInt(input); // previous
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
		sendMessage("Player " + playerTurn + " enter the index number of the card you would like to play\n", out);
		input = (String) in.readObject();
		playerChoice = Integer.parseInt(input);

		playerCard[(playerTurn - 1)] = hands[(playerTurn - 1)][playerChoice];
		playersCard[(playerTurn - 1)] = hands[(playerTurn - 1)][playerChoice];// must have 2 similar arrays incase one
																				// gets set to 0 due to elimination
		for (j = playerChoice; j < 12; j++) {
			hands[(playerTurn - 1)][j] = hands[(playerTurn - 1)][j + 1];// moves selected card to end of the array
		}

	}

	public void deal() {
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