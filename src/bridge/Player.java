package bridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import bridge.Suit.Color;

public class Player {
	private Card allCards;
	
	protected String direction;
	private static final String[] directions = new String[]{
			"NORTH", "SOUTH", "WEST", "EAST"
	};
	
	protected ArrayList<Card> cards;
	protected ArrayList<Player> players = new ArrayList<Player>(4);
	
	private Random random = new Random();
	
	protected ArrayList<Card> heartArrayList = new ArrayList<Card>();
	protected ArrayList<Card> clubArrayList = new ArrayList<Card>();
	protected ArrayList<Card> diamondArrayList = new ArrayList<Card>();
	protected ArrayList<Card> spadeArrayList = new ArrayList<Card>();
	
	public Player() {
		// Owned cards
		cards = new ArrayList<Card>(13);
		allCards = new Card();
		
		cardsInitialize();
	}
	
	public Player(ArrayList<Card> cards, String direction) {
		this.cards = cards;
		this.direction = direction;
	}
	
	private void cardsInitialize() {
		int temp;
		for(int j = 0; j < 4; j++) {
			// Distribute the cards
			ArrayList<Card> tempCards = new ArrayList<Card>(13);
			for(int i = 0; i < 13; i++) {
				temp = random.nextInt(52 - j * 13 - i);
				tempCards.add(allCards.cards.get(temp));
				// Remove the distributed card
				allCards.cards.remove(temp);
			}
			players.add(new Player(tempCards, directions[j]));
			// remove all the elements in cards
			//tempCards.clear();
		}
	}
	
	public void printAllPlayers(){
		Iterator<Player> iterator1 = players.iterator();
		Player tempPlayer;
		while(iterator1.hasNext()) {
			tempPlayer = (Player)iterator1.next();
			tempPlayer.printAllCards();
		}
	}
	
	private void printAllCards() {
		sortCards(this.cards);
		Iterator<Card> iterator2 = this.cards.iterator();
		Card tempCard;
		int counter = 0;
		
		while(iterator2.hasNext()) {
			tempCard = (Card)iterator2.next();
			counter ++;
			System.out.println(tempCard.color + " " + tempCard.c);
			if(counter % 13 == 0) {
				System.out.println();
			}
		}
	}
	
	private void sortCards(ArrayList<Card> cards) {		
		Iterator<Card> iterator = cards.iterator();
		Card temp;
		while(iterator.hasNext()) {
			 temp = (Card)iterator.next();
			 if((temp.color).equals(Color.CLUB)) {
				 this.clubArrayList.add(temp);
			 }else if((temp.color).equals(Color.HEART)) {
				 this.heartArrayList.add(temp);
			 }else if((temp.color).equals(Color.SPADE)) {
				 this.spadeArrayList.add(temp);
			 }else {
				 this.diamondArrayList.add(temp);
			 }
		}
		// Sort all cards according to the ranks and suits
		bubbleSort(this.spadeArrayList);
		bubbleSort(this.diamondArrayList);
		bubbleSort(this.clubArrayList);
		bubbleSort(this.heartArrayList);
		 
		cards.clear();
		 
		Iterator<Card> it1 = this.spadeArrayList.iterator();
		Iterator<Card> it2 = this.diamondArrayList.iterator();
		Iterator<Card> it3 = this.clubArrayList.iterator();
		Iterator<Card> it4 = this.heartArrayList.iterator();
		 
		while(it1.hasNext()) {
			cards.add((Card)it1.next());
		}
		 
		while(it2.hasNext()) {
			cards.add((Card)it2.next());
		}
		 
		while(it3.hasNext()) {
			cards.add((Card)it3.next());
		}
		 
		while(it4.hasNext()) {
			cards.add((Card)it4.next());
		}			
	}
	
	private void bubbleSort(ArrayList<Card> cards) {
		Card tempCard;
		for(int i = 0; i < cards.size(); i++) {
			for(int j = 0; j < cards.size() - 1 - i; j++) {
				// cards.get(Index) -> Object of Card
				// cards.get(Index).c -> The value of a card
				if((cards.get(j).c).compareTo(cards.get(j + 1).c) > 0 && (cards.get(j).c.length() == 1) && (cards.get(j + 1).c.length() == 1)) {
					tempCard = cards.get(j);
					cards.set(j, cards.get(j + 1));
					cards.set(j + 1, tempCard);
				}else if((cards.get(j).c.length() == 1) || (cards.get(j + 1).c.length() == 2)) {
					// To check whether the first card includes JQKA
					int temp = (cards.get(j).c).compareTo("9");
					if(temp > 0) {
						tempCard = cards.get(j);
						cards.set(j, cards.get(j + 1));
						cards.set(j + 1, tempCard);
					}
				}else if((cards.get(j).c.length() == 2) || (cards.get(j + 1).c.length() == 1)) {
					int temp = (cards.get(j + 1).c).compareTo("9");
					if(temp <= 0) {
						tempCard = cards.get(j);
						cards.set(j, cards.get(j + 1));
						cards.set(j + 1, tempCard);
					}
				}
			}
		}
	}
}
