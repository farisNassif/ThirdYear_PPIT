package games;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Snap {
	private String str = "";
	Timer timer = new Timer();
	int lastcard = 0;
	double amtPlayers;
	int[][] hands = new int[10][53];
	int[] winnerCards = new int[10];
	int kcount = 0;
	String input;
	int cardCount = 0;
	int cardsPlayedCount = 0;
	int playernum = 0;
	int deck[] = { 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10,
			10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14 };
	ObjectInputStream in;
	ObjectOutputStream out;

	public Snap(ObjectInputStream in, ObjectOutputStream out) throws Exception {
		this.in = in;
		this.out = out;
		int option = 0;
		sendMessage(
				"Press 1 if you would like to play with an automated timer for each flip\nPress 2 if you would like to flip the cards manually",
				out);
		input = (String) in.readObject();
		option = Integer.parseInt(input);
		sendMessage("How many players are there?(max 10)", out);
		input = (String) in.readObject();
		amtPlayers = Integer.parseInt(input);

		shuffledeck();
		deal();
		if (option == 1) {


			while (cardsPlayedCount < 52) {
				sendMessage(
						"Press any key to continue, remember to have your finger over your number the players are from 0-"
								+ ((int) amtPlayers - 1),
						out);
				input = (String) in.readObject();
				char winner = 'a';
					winner = new Snap(in, out).getInput();

				cardsPlayedCount += cardCount;
				if (cardsPlayedCount < 54) {
					sendMessage("Winner is player: " + winner, out);
					winnerCards[(winner - 48)] += cardCount;
					cardCount = 0;
				}
			}
		} else if (option == 2) {
			int playerflip = -1;
			while (cardsPlayedCount < 51) {
				while (playerflip != playernum) {
					sendMessage("Player " + playernum + " Press " + playernum + " to flip a card!", out);
					input = (String) in.readObject();
					playerflip = Integer.parseInt(input);
				}
				System.out.println(hands[playernum][kcount]);
				int currentcard = hands[playernum][kcount];

				if (playernum != 0) {
					lastcard = hands[playernum - 1][kcount];
				} else if (playernum == 0 && kcount != 0) {
					lastcard = hands[(int) amtPlayers - 1][kcount - 1];
				}
				playernum++;
				cardCount++;
				if (playernum >= amtPlayers) {
					playernum = 0;
					kcount++;
				}
				if (currentcard == 0) {
					sendMessage("That's all the cards! enter any key to view final scores!", out);
					cardsPlayedCount = 52;
				}
				if (lastcard == currentcard) {
					input = (String) in.readObject();
					int winner = Integer.parseInt(input);
					if (cardsPlayedCount < 51) {
						sendMessage("Player " + winner + " wins " + cardCount + " cards!", out);
						winnerCards[winner] += cardCount;
						cardCount = 0;
					}
				}
			}
		}
		for (int i = 0; i < amtPlayers; i++) {
			sendMessage("Player " + i + " scored: " + winnerCards[i], out);
		}
	}

	public char getInput() throws Exception {
		timer.scheduleAtFixedRate(task, 1 * 1000, 1 * 500);
		sendMessage("Here we go!", out);
		input = (String) in.readObject();
		str=input;

		return str.charAt(0);
	}

	TimerTask task = new TimerTask() {
		public void run() {
			if (str.equals("")) {
				sendMessage(Integer.toString(hands[playernum][kcount]), out);
				if (playernum != 0) {
					lastcard = hands[playernum - 1][kcount];
				} else if (playernum == 0 && kcount != 0) {
					lastcard = hands[(int) amtPlayers - 1][kcount - 1];
				}
				cardCount++;
				int currentcard = hands[playernum][kcount];
				playernum++;
				if (currentcard == 0) {
					cardsPlayedCount = 55;
					System.out.println("That's all the cards! enter any key to view final scores!");
					sendMessage("That's all the cards! enter any key to view final scores!", out);
					timer.cancel();
				}
				if (playernum >= amtPlayers) {
					playernum = 0;
					kcount++;
				}
				if (lastcard == currentcard) {
					timer.cancel();
				}
			}
		}
	};

	public void deal() {
		int k = 0;
		double m = Math.ceil(52.0 / amtPlayers);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < amtPlayers; j++) {
				if (k > 51) {
					break;
				}
				hands[j][i] = deck[k];// deals cards realistically 1 to each player at a time
				k++;
			}
		}
	}

	void shuffledeck() {
		Random rnd = ThreadLocalRandom.current();
		for (int i = deck.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = deck[index];
			deck[index] = deck[i];
			deck[i] = a;
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
}