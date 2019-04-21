package games;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Snap {
	private String str = "";
	private Scanner console;
	Timer timer = new Timer();
	int lastcard = 0;
	double amtPlayers;
	int[][] hands = new int[10][53];
	int[] winnerCards = new int[10];
	int kcount = 0;
	int cardCount = 0;
	int cardsPlayedCount = 0;
	int playernum = 0;
	int deck[] = { 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9,
			9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14 };

	//public void main(String[] args) throws Exception {
	public Snap(){
		console = new Scanner(System.in);
		int option = 0;
		System.out.println(
				"Press 1 if you would like to play with an automated timer for each flip\nPress 2 if you would like to flip the cards manually");
		option = console.nextInt();
		System.out.println("How many players are there?(max 10)");
		amtPlayers = console.nextDouble();

		shuffledeck();
		deal();
		if (option == 1) {

			@SuppressWarnings("unused")
			String cont = console.nextLine();

			while (cardsPlayedCount < 52) {
				System.out.println(
						"Press any key to continue, remember to have your finger over your number the players are from 0-"
								+ ((int) amtPlayers - 1));
				cont = console.nextLine();
				char winner = 'a';
				try {
					winner = new Snap().getInput();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cardsPlayedCount += cardCount;
				if (cardsPlayedCount < 54) {
					System.out.println("Winner is player: " + winner);
					winnerCards[(winner - 48)] += cardCount;
					cardCount = 0;
				}
			}
		} else if (option == 2) {
			int playerflip = -1;
			while (cardsPlayedCount < 51) {
				while (playerflip != playernum) {
					System.out.println("Player " + playernum + " Press " + playernum + " to flip a card!");
					playerflip = console.nextInt();
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
					System.out.println("That's all the cards! enter any key to view final scores!");
					cardsPlayedCount=52;
				}
				if (lastcard == currentcard) {
					int winner = console.nextInt();
					if (cardsPlayedCount < 51) {
						System.out.println("Player " + winner + " wins " + cardCount + " cards!");
						winnerCards[winner] += cardCount;
						cardCount = 0;
					}
				}
			}
		}
		for (int i = 0; i < amtPlayers; i++) {
			System.out.println("Player " + i + " scored: " + winnerCards[i]);
		}
	}

	public char getInput() throws Exception {
		timer.scheduleAtFixedRate(task, 1 * 1000, 1 * 500);

		System.out.println("Here we go!");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		str = in.readLine();
		
		return str.charAt(0);

	}

	TimerTask task = new TimerTask() {
		public void run() {
			if (str.equals("")) {
				System.out.println(hands[playernum][kcount]);
				if (playernum != 0) {
					lastcard = hands[playernum - 1][kcount];
				} else if (playernum == 0 && kcount != 0) {
					lastcard = hands[(int) amtPlayers - 1][kcount - 1];
				}
				cardCount++;
				int currentcard = hands[playernum][kcount];
				playernum++;
				if (currentcard == 0) {
					cardsPlayedCount=55;
					System.out.println("That's all the cards! enter any key to view final scores!");
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
}