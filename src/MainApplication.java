import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class MainApplication {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Let's Play Cards!");
		TheTable.PlayCards(scanner);

		scanner.close();
	}
}

class TheTable {

	// CardDeck deck = new CardDeck();
	public static void PlayCards(Scanner scanner) {
		CardDeck.GenerateCardDeck();
		// I now have a card deck
		GamePlayers p = new GamePlayers();
		p.GeneratePlayers();

//		System.out.println(CardDeck._cardDeck.size());

		System.out.println("------------- Round one ------------");
		Round.swap(scanner);
		System.out.println("------------- Round two ------------");
		Round.playNextRound(); // Playing Round 2
		Round.swap(scanner);
		System.out.println("------------- Round three ------------");
		Round.playNextRound(); // Playing Round 3
		Round.swap(scanner);

		GamePlayers.players.forEach(plr -> plr.CalculateHandValue());

		// Getting player with highest score.
		Player winner = GamePlayers.players.get(0);
		for (int i = 1; i < GamePlayers.players.size(); i++) {
			if (GamePlayers.players.get(i).getHandValue() > winner.getHandValue()) {
				winner = GamePlayers.players.get(i); // considering case where only one player can be winner.
														// Where tie breaker is order in which cards are revealed
			}
		}

		// Display scores of all players.
		for (Player plr : GamePlayers.players) {
			System.out.println("\n" + plr.getPlayerName() + " Scored: " + plr.getHandValue());
			plr.display();
		}

		System.out.println("\n\n---------------------------------------");
		System.out.println("	Winner is : " + winner.getPlayerName());
		System.out.println("---------------------------------------");

	}

}

class Round {
	public static void playNextRound() {
		for (Player currentPlayer : GamePlayers.players) {
			currentPlayer.playRound();
		}
	}

	public static void swap(Scanner scanner) {
		ArrayList<Card> giveOut = new ArrayList<>();
		int ch = 0;
		try {
			for (Player curPlayer : GamePlayers.players) {
				System.out.println(curPlayer.getPlayerName() + " how many cards do you want to swap (max 3 cards) ::");
				ch = scanner.nextInt();		//As the player will be restricted to only positive int values we dont consider -ive values or floats.
				if (ch > 3) {
					System.out.println("Only 3 cards allowed. Giving out 3 cards");
					ch = 3;
				}
//				System.out.println("++++" + CardDeck._cardDeck.size());
				Collections.shuffle(CardDeck._cardDeck);
				// Lets assume user give 'ch' number of cards back to the dealer (where card
				// suite and number is not shown).
				// Here we select cards as though user is giving out. (which cards are given out
				// is only known to user).
				// We assume user kept cards to discard at the bottom of the pile.
				for (int i = 0; i < ch; i++) {
					giveOut.add(curPlayer.playersCardSet.remove(0)); // giveOut contains all the cards player is
																		// discarding
					curPlayer.playersCardSet.add(CardDeck._cardDeck.remove(0)); // we give player an equal number of
																				// cards back
				}

				// Here we add the user discard pile to the cardDeck.
				CardDeck._cardDeck.addAll(giveOut);
				giveOut.clear(); // clearing discard pile

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception occured:" + e.getMessage());
			e.printStackTrace();
		}

	}
}

class CardDeck {
	public static ArrayList<Card> _cardDeck = new ArrayList<Card>();
	static String[] suites = { "Clubs", "Diamonds", "Hearts", "Spades" };

	public static void GenerateCardDeck() {
		String[] suites;
		int[] cardValue;
		suites = new String[4];
		suites[0] = "Clubs";
		suites[1] = "Diamonds";
		suites[2] = "Hearts";
		suites[3] = "Spades";

		cardValue = new int[13];
		cardValue[0] = 2;
		cardValue[1] = 3;
		cardValue[2] = 4;
		cardValue[3] = 5;
		cardValue[4] = 6;
		cardValue[5] = 7;
		cardValue[6] = 8;
		cardValue[7] = 9;
		cardValue[8] = 10;
		cardValue[9] = 11;
		cardValue[10] = 12;
		cardValue[11] = 13;
		cardValue[12] = 14;

		for (int i = 0; i < suites.length; i++) {
			for (int j = 0; j < cardValue.length; j++) {
				CardDeck._cardDeck.add(new Card(suites[i], cardValue[j]));
			}
		}

		// shuffle the card deck
		Collections.shuffle(CardDeck._cardDeck);
	}
}

class Card {

	private String suite;
//	private int suitevalue;
	private int cardvalue;
	private int value;

	public Card(String _suite, int _cardvalue) {
		this.suite = _suite;
		this.cardvalue = _cardvalue;
	}

	public String toString() {
		return this.suite + " " + this.cardvalue;
	}

	public int getCardValue() {
		if (suite.equals("Clubs")) {
			this.value = this.cardvalue * 1;
		} else if (suite.equals("Diamonds")) {
			this.value = this.cardvalue * 2;
		} else if (suite.equals("Hearts")) {
			this.value = this.cardvalue * 3;
		} else {
			this.value = this.cardvalue * 4;
		}
		return this.value;
	}
}

class GamePlayers {

	public GamePlayers() {

	}

	public static ArrayList<Player> players = new ArrayList<Player>();

	public static void GeneratePlayers() {
//		for (int m = 0; m < 4; m++) {		//TODO: removed loop to reduce player count. Remove comments to add if required.
		players.add(new Player("Bill"));
		players.add(new Player("Mary"));
		players.add(new Player("Steve"));
		players.add(new Player("Susan"));
//		}
	}

}

class Player {
	public int getHandValue() {
		return handValue;
	}

	public String getPlayerName() {
		return PlayerName;
	}

	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}

	public Player(String playerName) {
		PlayerName = playerName;
		setOfCards = new Hand();
		playersCardSet.addAll(Arrays.asList(setOfCards.IssueHand()));
	}

	String PlayerName;
	Hand setOfCards;
	// Card[] playersCardSet = new Card[5];
	ArrayList<Card> playersCardSet = new ArrayList<>();
	private int handValue;

	public int CalculateHandValue() {
		handValue = 0;
		for (Card cd : playersCardSet) {
			handValue += cd.getCardValue();
		}

		return handValue;
	}

	public void playRound() {
		playersCardSet.addAll(Arrays.asList(setOfCards.IssueHand()));
	}

	public void display() {
		System.out.print(this.getPlayerName() + " deck: ");
		for (Card c : playersCardSet) {
			System.out.print(c + ", ");
		}
	}
}

class Hand {
	// the Hand is the cards the player holds

	Card[] PlayersHand = new Card[4];

	public Hand() {

	}

	public Card[] IssueHand() {
		for (int j = 0; j < 4; j++) {
//			PlayersHand[j] = (CardDeck._cardDeck).get(j);		//TODO: Remove comments if required. commented as this 'gets' same card for all players.
			PlayersHand[j] = CardDeck._cardDeck.remove(j); // TODO: added this method to assign different cards to
															// different players.
		}
		return PlayersHand;
	}

}
