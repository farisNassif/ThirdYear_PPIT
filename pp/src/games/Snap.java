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
	public static String input = "";
	static ObjectOutputStream out;
	static ObjectInputStream in;
	//Timer timer;
	int lastcard = 0;
	double amtPlayers;
	int[][] hands = new int[10][53];
	int[] winnerCards = new int[10];
	int kcount = 0;
	int cardCount = 0;
	int cardsPlayedCount = 0;
	int playernum = 0;
	int deck[] = { 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 10,
			10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14 };

	public Snap(ObjectInputStream in, ObjectOutputStream out) throws Exception {
		this.in=in;
		this.out=out;
		int option = 0;
		sendMessage(
				"Enter 1 if you would like to play with an automated timer for each flip\nPress 2 if you would like to flip the cards manually", out);
		input = (String) in.readObject();
		option = Integer.parseInt(input);
		
		sendMessage("Enter how many players are there?(max 10)",out);
		input = (String) in.readObject();
		amtPlayers = Integer.parseInt(input);

		shuffledeck();
		deal();
		if (option == 1) {

			while (cardsPlayedCount < 52) {
				sendMessage(
						"Enter any key to continue, remember to have your finger over your number the players are from 0-"
								+ ((int) amtPlayers - 1), out);
				input = (String) in.readObject();
				char winner = '9';
				winner = getInput();
				//winner = new Snap(in,out).getInput();
				cardsPlayedCount += cardCount;
				if (cardsPlayedCount < 54) {
					sendMessage("Winner is player: " + winner, out);
					winnerCards[(winner - 48)] += cardCount;
					cardCount = 0;
				}
			}
		} else if (option == 2) {
			int playerFlip = -1;
			while (cardsPlayedCount < 51) {
				while (playerFlip != playernum) {
					sendMessage("Player " + playernum + " Enter " + playernum + " to flip a card!",out);
					input = (String) in.readObject();
					int playerFlipVar = Integer.parseInt(input);
					playerFlip = playerFlipVar;

				}
				sendMessage(Integer.toString(hands[playernum][kcount]), out);
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
					sendMessage("That's all the cards! Enter any key to view final scores!", out);
					input = (String) in.readObject();
					cardsPlayedCount = 52;
				}
				if (lastcard == currentcard) {
					sendMessage("Enter!", out);
					input = (String) in.readObject();
					int winner =Integer.parseInt(input);
					System.out.println("line 98");
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

	public void sendMessage(String msg, ObjectOutputStream out) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("To Client ==> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public char getInput() throws Exception {
		Timer timer = new Timer();
		
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
						sendMessage("That's all the cards! enter any key to view final scores!", out);
						try {
							input = (String) in.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						timer.cancel();
					}
					if (playernum >= amtPlayers) {
						playernum = 0;
						kcount++;
					}
					if (lastcard == currentcard) {
						sendMessage("Enter!",out);
						timer.cancel();
					}
				}
			}
		};
		
		timer.scheduleAtFixedRate(task, 1 * 1000, 1 * 1000);
		System.out.println("162");
		str = (String) in.readObject();
		System.out.println("164");
		return str.charAt(0);

	}

	

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