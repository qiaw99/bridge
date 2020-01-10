package bridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Play {
	Player player;
	Player starter;
	Player startCaller;
	
	Suit.Color trumpColor;
	ArrayList<Player> players;
	private int sum[] = new int[4];
	
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

	public static final String[] DIRECTIONS = new String[]{
			"NORTH",
			"EAST",
			"SOUTH", 
			"WEST"};
	public static final String[][] TRUMP = new String[][] {
		{"PASS"},
		{"1CLUB", "1HEART", "1SPADE", "1DIAMOND"},
		{"2CLUB", "2HEART", "2SPADE", "2DIAMOND"},	
		{"3CLUB", "3HEART", "3SPADE", "3DIAMOND"},
		{"4CLUB", "4HEART", "4SPADE", "4DIAMOND"},
		{"5CLUB", "5HEART", "5SPADE", "5DIAMOND"},
		{"6CLUB", "6HEART", "6SPADE", "6DIAMOND"},
		{"7CLUB", "7HEART", "7SPADE", "7DIAMOND"}
	};

	public Play() {
		// Initialize 4 players and corresponded cards
		player = new Player();
		starter = null;
		startCaller = null;
		players = player.players;
		
		hashMap.put("J", 1);
		hashMap.put("Q", 2);
		hashMap.put("K", 3);
		hashMap.put("a", 4);	
	}
	
	public void initialize() {
		//trumpColor;
	}
	
	public int getIndex(String direction) {
		for(int i = 0; i < 4; i++) {
			if(DIRECTIONS[i].compareTo(direction) == 0) {
				return i;
			}
		}
		// -1 means ERROR
		return -1;
	}
	
	public Suit.Color callTrump() {
		//set the start caller;
		beginToCall(players);
		String direction = this.startCaller.direction;
		int index = getIndex(direction);
		
		if(this.startCaller != null) {
			while(true) {
				// initialize with "pass"
				String tempString = TRUMP[0][0];
				/**
				if() {
					
				}
				return Color.HEART;
				*/
			}
		}else {
			System.out.println("No one has more than 12 points."
					+ " Please restart the game!");
			return null;
		}
	}
	
	public void setStarter(Player player){
		this.starter = player;
	}
	
	public Card showCard(Card card, Player player) {
		player.cards.remove(card);
		return card;
	}
	
	public void countCards() {
		for(int i = 0; i < players.size(); i++) {
			// calculate points of all cards for each player
			this.sum[i] = countPoints(players.get(i).cards);
		}
	}
	
	// add all cards with JQKA with help of HashMap
	public int countPoints(ArrayList<Card> cards) {
		int counter = 0; 
		Iterator<Card> iterator = cards.iterator();
		
		while(iterator.hasNext()) {
			Card temp = (Card)iterator.next();
			if((temp.c).compareTo("9") > 0) {
				counter += hashMap.get(temp.c);
			}
		}
		return counter;
	}
	
	public Player beginToCall(ArrayList<Player> players) {
		for(int i = 0; i < 4; i++) {
			if(sum[i] >= 12) {
				this.startCaller = players.get(i);
				return startCaller;
			}
		}
		return null;
	}
	
	public boolean canResponse(int index) {
		return (this.sum[index] >= 6);
	}
}
