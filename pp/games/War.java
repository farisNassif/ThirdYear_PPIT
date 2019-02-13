package pp.games;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
//Cormac Raftery & Faris Nassif
public class War {

	static int pointsOnTheBattlefield;
	static int[] shuffledDeck = new int[156];
	static int num;
	static int[][] hands = new int[10][13];//first dimension is player number, second is their cards
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
	private static Scanner console;
	
	
	public static void main(String[] args) {
	{
		
		console = new Scanner(System.in);
		
		System.out.printf("Would you like to either: \n1.Start a new game\n2.Load the previously saved game\n");
		roundOption=console.nextInt();
		if (roundOption == 1)
		{
			System.out.printf("Please enter the number of players(2-10):");
			players=console.nextInt();
			if (players < 5)//shuffles from one deck
			{
				shuffledeck();
				deal();
			}
			else if (players < 9)//shuffles from two decks
			{
				shuffledeck();
				for (i = 0; i < 52; i++)
				{
					shuffledDeck[i + 52] = shuffledDeck[i];
				}
				shuffledeck();
				deal();
			}
			else//shuffles from three decks
			{
				shuffledeck();
				for (i = 0; i < 52; i++)
				{
					shuffledDeck[i + 104] = shuffledDeck[i];
				}
				shuffledeck();
				for (i = 0; i < 52; i++)
				{
					shuffledDeck[i + 52] = shuffledDeck[i];
				}
				shuffledeck();
				deal();
			}
		}
		else
		{
			//filep = fopen("gamestate.txt", "r");//opens the file to read
			BufferedReader filep = null;
			try {
				filep = new BufferedReader(new InputStreamReader(new FileInputStream("gamestate.txt")));
			} catch (FileNotFoundException e) {
				System.out.println(e);
			}
			String line = "";
			try {
				if ((line = filep.readLine()) == null)
				{
					System.out.printf("The file cannot be opened\n");
				}
				else
				{
					
					players=Integer.parseInt(line);
					line = filep.readLine();
					pointsOnTheBattlefield=Integer.parseInt(line);
					line = filep.readLine();
					roundNum=Integer.parseInt(line);
					line = filep.readLine();
					for (i = 0; i < players; i++)
					{
						playerPoints[i]=Integer.parseInt(line);
						line = filep.readLine();
					}
					for (i = 0; i < players; i++)
					{
						for (j = 0; j < 13; j++)
						{
							hands[i][j]=Integer.parseInt(line);
							line = filep.readLine();
						}
					}
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//close(filep);//close the file
			try {
				filep.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		while (roundNum > 0)//detects if the game is finished
		{
			System.out.printf("Would you like to: \n1.Complete the next round\n2.Save the game\n3.Output the game status\n4.Exit the game\n");
			roundOption=console.nextInt();
			while (roundOption != 1)
			{
				switch (roundOption)
				{
				case 1://next round
					break;
				case 2://save
					//playerPoints playerhands roundNum players and points on the battlefield
					try (FileWriter fw = new FileWriter("gamestate.txt", false);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter out = new PrintWriter(bw)) {
						out.println(players);
						out.println(pointsOnTheBattlefield);
						out.println(roundNum);
						for (i = 0; i < players; i++)
						{
							out.println(playerPoints[i]);
						}
						for (i = 0; i < players; i++)
						{
							for (j = 0; j < 13; j++)
							{
								out.println(hands[i][j]);
							}
						}
						out.close();
					} catch (IOException e) {
						// exception handling left as an exercise for the reader
					}
					System.out.printf("Would you like to: \n1.Complete the next round\n2.Save the game\n3.Output the game status\n4.Exit the game\n");
					roundOption=console.nextInt();
					break;
				case 3://show score
					for (i = 1; i <= players; i++)
					{
						System.out.printf("Player %d has %d points\n", i, playerPoints[i - 1]);
					}
					System.out.printf("Would you like to: \n1.Complete the next round\n2.Save the game\n3.Output the game status\n4.Exit the game\n");
					roundOption=console.nextInt();
					break;
				case 4://exit app
					System.exit(0);
					break;
				default:
					System.out.printf("Would you like to: \n1.Complete the next round\n2.Save the game\n3.Output the game status\n4.Exit the game\n");
					roundOption=console.nextInt();
					break;
				}
			}
			for (i = 1; i <= players; i++)//each player selects card
			{
				playround(i);
			}
			roundNum--;
			whowins();//determines the highest unique card
			totalRoundPoints = 0;
			for (i = 1; i <= players; i++)
			{
				if (playersCard[i - 1] <= 10)
				{
					System.out.printf("Player %d played: %d\n", i, playersCard[i - 1]);
				}
				else if (playersCard[i - 1] == 11)
				{
					System.out.printf("Player %d played: J\n", i);
				}
				else if (playersCard[i - 1] == 12)
				{
					System.out.printf("Player %d played: Q\n", i);
				}
				else if (playersCard[i - 1] == 13)
				{
					System.out.printf("Player %d played: K\n", i);
				}
				else if (playersCard[i - 1] == 14)
				{
					System.out.printf("Player %d played: A\n", i);
				}
			}
		}
	}
	}

	public static void whowins()
	{
		for (i = 0; i < players; i++)
		{
			totalRoundPoints += playerCard[i];//totals card values
		}
		largest = 0;
		for (i = 0; i < players; i++)
		{
			if (largest < playerCard[i] || largest == 0)
			{
				largest = playerCard[i];
				largestPosition = i;
			}
			else if (largest == playerCard[i])//set player cards to 0 but keeps value of largest incase of 2+ the same cards
			{
				for (j = 0; j < players; j++)
				{
					if (largest == playerCard[j])
					{
						playerCard[j] = 0;
					}
				}
				largest = 0;
				i = 0;
			}
		}
		if (largest == 0)//points get added to next round
		{
			pointsOnTheBattlefield = totalRoundPoints;
			totalRoundPoints = 0;
			System.out.printf("The points are on the battlefield! Next round's winner gets these %d points extra!\n",pointsOnTheBattlefield);
		}
		else
		{

			playerPoints[largestPosition] += totalRoundPoints+pointsOnTheBattlefield;
			System.out.printf("Player %d wins the round and earns %d points\n", (largestPosition + 1), (totalRoundPoints + pointsOnTheBattlefield));
			pointsOnTheBattlefield = 0;
		}
		j = 0;

	}
	public static void playround(int playerTurn)
	{
		System.out.printf("Player %d enter any number when ready:\n", playerTurn);//used to conceal cards from previous player
		j=console.nextInt();
		for (j = 0; j < roundNum; j++)
		{
			if (hands[(playerTurn - 1)][j] <= 10)
			{
				System.out.printf("%d.%d\n", j, hands[(playerTurn - 1)][j]);
			}
			else if (hands[(playerTurn - 1)][j] == 11)
			{
				System.out.printf("%d.J\n", j);
			}
			else if (hands[(playerTurn - 1)][j] == 12)
			{
				System.out.printf("%d.Q\n", j);
			}
			else if (hands[(playerTurn - 1)][j] == 13)
			{
				System.out.printf("%d.K\n", j);
			}
			else if (hands[(playerTurn - 1)][j] == 14)
			{
				System.out.printf("%d.A\n", j);
			}
		}
		System.out.printf("Player %d enter the index number of the card you would like to play\n", playerTurn);
		playerChoice=console.nextInt();
		playerCard[(playerTurn - 1)] = hands[(playerTurn - 1)][playerChoice];
		playersCard[(playerTurn - 1)] = hands[(playerTurn - 1)][playerChoice];//must have 2 similar arrays incase one gets set to 0 due to elimination
		for (j = playerChoice; j < 12; j++)
		{
			hands[(playerTurn - 1)][j] = hands[(playerTurn - 1)][j + 1];//moves selected card to end of the array 
		}
		//system("CLS");//clears terminal to hide cards

	}

	public static void deal()
	{
		k = 0;
		for (i = 0; i < 13; i++)
		{
			for (j = 0; j < players; j++)
			{
				hands[j][i] = shuffledDeck[k];//deals cards realistically 1 to each player at a time,also ensures decks get mixed when playing with 2-3 decks
				k++;
			}
		}

	}

	 static void shuffledeck()
	  {
		 int ar[] = { 2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,10,10,10,10,11,11,11,11,12,12,12,12,13,13,13,13,14,14,14,14 };
			
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	    for (j = num; j < 51; ++j)
		{
	    	shuffledDeck[j] = ar[j + 1];//copies random index number to end of array
		}
	  }
}