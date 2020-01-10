package bridge;

import bridge.Suit.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class Card {
	protected String c;
	protected Rank rank = new Rank();
	protected Suit.Color color;
	protected ArrayList<Card> cards = new ArrayList<Card>(52);
	
	public Card() {
		initialize();
	}
	
	public Card(String c, Suit.Color color) {
		this.c = c;
		this.color = color;
	}
	
	private void initialize() {
		for(int i = 0; i < 13; i++) {
			cards.add(new Card(rank.value[i], Color.SPADE));
		}
		
		for(int i = 13; i < 26; i++) {
			cards.add(new Card(rank.value[i - 13], Color.HEART));
		}
		
		for(int i = 26; i < 39; i++) {
			cards.add(new Card(rank.value[i - 26], Color.CLUB));
		}
		
		for(int i = 39; i < 52; i++) {
			cards.add(new Card(rank.value[i - 39], Color.DIAMOND));
		}
	}
	
	public void printCards() {
		Iterator<Card> it = cards.iterator();
		while(it.hasNext()) {
			Card tempCard = (Card)it.next();
			System.out.println(tempCard.c + " " + tempCard.color);
		}
	}
}
