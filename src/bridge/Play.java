package bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Play {
	Player player;
	private Player starter;
	private Player startCaller;
	
	String trumpColor;
	ArrayList<Player> players;
	private int sum[] = new int[4];
	
	private ArrayList<String> recordArrayList = new ArrayList<String>();
	
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

	private static final String[] DIRECTIONS = new String[]{
			"NORTH",
			"EAST",
			"SOUTH", 
			"WEST"};
	private final String[][] TRUMP = new String[][] {
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
	
	private int getIndex(String direction) {
		for(int i = 0; i < 4; i++) {
			if(DIRECTIONS[i].compareTo(direction) == 0) {
				return i;
			}
		}
		// -1 means ERROR
		return -1;
	}
	
	private boolean contains(String[] strings, String str) {
		for(int i = 0; i < strings.length; i++) {
			if(strings[i].equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * NUMMBER + (SPADE CLUB DIAMOND HEART)
	 * @param str
	 * @return
	 */
	private boolean rightInput(String str) {
		if(str == null) {
			return false;
		}
		
		int temp;
		try {
			temp = (int)(str.charAt(0));
		}catch(ClassCastException e) {
			e.printStackTrace();
			return false;
		}
		return contains(this.TRUMP[temp], str);
	}
	
	@SuppressWarnings("finally")
	private String getInput() {
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		try {
			while(!rightInput(str)) {
				System.out.println("What kind of trump do you want to call?\n");
				str = bReader.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			// null means ERROR
			return str;
		}	
	}
	
	public void callTrump() {
		//set the start caller;
		beginToCall(players);
		
		String direction = this.startCaller.direction;
		int index = getIndex(direction);
		int temp = index - 1;
		
		// initialize with "pass"
		String tempString = TRUMP[0][0];
		
		if(this.startCaller != null) {
			while(true) {
				index = temp + 1;
				while(true) {
					if(index == temp) {
						break;
					}
					if(index < this.players.size() - 1) {
						if(canResponse(index)) {
							String inputString = getInput();
							tempString = inputString;
							recordArrayList.add(inputString);
						}else {
							recordArrayList.add("PASS");
							tempString = "PASS";
						}
						index ++;
					}else {
						index = 0;
					}
				}
				// if other 3 players pass
				if(canPass(recordArrayList)) {
					tempString = recordArrayList.get(recordArrayList.size() - 4);
					trumpColor = tempString.substring(1);
				}
			}
		}else {
			System.out.println("No one has more than 12 points."
					+ " Please restart the game!");
		}
	}
	
	private int getNext(Player player) {
		int index = getIndex(player.direction);
		if(index + 1 <= 3) {
			return (index + 1);
		}else {
			return 0;
		}
	}
	
	private void findStarter() {
		int size = recordArrayList.size();
		int rest = size % 4;
		int temp = getNext(startCaller);
		
		switch (rest) {
		case 0:
			setStarter(startCaller);
			break;
		case 1:
			setStarter(this.players.get(temp));
			break;
		case 2:
			temp = getNext(startCaller);
			setStarter(this.players.get(temp));
			break;
		case 3:
			temp = getNext(startCaller);
			setStarter(this.players.get(temp));
			break;
		default:
			break;
		}
	}
	
	private boolean canPass(ArrayList<String> recordArrayList) {
		for(int i = 0; i < 3; i++) {
			if(!(recordArrayList.get(recordArrayList.size() - 1 - i)).equals("PASS")) {
				return false;
			}
		}
		return true;
	}
	
	private void setStarter(Player player){
		this.starter = player;
	}
	
	public Card showCard(Card card, Player player) {
		player.cards.remove(card);
		return card;
	}
	
	private void countCards() {
		for(int i = 0; i < players.size(); i++) {
			// calculate points of all cards for each player
			this.sum[i] = countPoints(players.get(i).cards);
		}
	}
	
	// add all cards with JQKA with help of HashMap
	private int countPoints(ArrayList<Card> cards) {
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
	
	private Player beginToCall(ArrayList<Player> players) {
		for(int i = 0; i < 4; i++) {
			if(sum[i] >= 12) {
				this.startCaller = players.get(i);
				return startCaller;
			}
		}
		return null;
	}
	
	private boolean canResponse(int index) {
		return (this.sum[index] >= 6);
	}
}
