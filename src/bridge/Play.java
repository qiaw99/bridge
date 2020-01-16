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
	private HashMap<String, Integer> trumpValueHashMap = new HashMap<String, Integer>();
	private static final String[] DIRECTIONS = new String[]{
			"NORTH",
			"EAST",
			"SOUTH", 
			"WEST"};
	private final String[][] TRUMP = new String[][] {
		{"PASS"},
		{"1CLUB", "1HEART", "1SPADE", "1DIAMOND", "1NT"},
		{"2CLUB", "2HEART", "2SPADE", "2DIAMOND", "2NT"},	
		{"3CLUB", "3HEART", "3SPADE", "3DIAMOND", "3NT"},
		{"4CLUB", "4HEART", "4SPADE", "4DIAMOND", "4NT"},
		{"5CLUB", "5HEART", "5SPADE", "5DIAMOND", "5NT"},
		{"6CLUB", "6HEART", "6SPADE", "6DIAMOND", "6NT"},
		{"7CLUB", "7HEART", "7SPADE", "7DIAMOND", "7NT"}
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
		trumpValueHashMap.put("CLUB", 1);
		trumpValueHashMap.put("DIAMOND", 2);
		trumpValueHashMap.put("HEART", 3);
		trumpValueHashMap.put("SPADE", 4);
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
		
		if(Character.isDigit(str.charAt(0)) && str.length() > 1) {
			int temp = (int)(str.charAt(0)) - 48;
			boolean status = contains(this.TRUMP[temp], str);
			if(status) {
				if(!(this.recordArrayList.isEmpty())) {
					if((int)(str.charAt(0)) < (int)(this.recordArrayList.get(this.recordArrayList.size() - 1).charAt(0))){
						System.out.println("The entered number is smaller than the last one! Try agin.");
						System.out.println("**************************************");
						return false;
					}else {
						if(trumpValueHashMap.get(str.substring(1)) < trumpValueHashMap.get(this.recordArrayList.get(this.recordArrayList.size() - 1).substring(1))){
							System.out.println("The entered trump is not stronger than the last one! Try agin.");
							System.out.println("**************************************");
							return false;
						}else {
							return true;
						}
					}
				}
				return true;
			}else {
				System.out.println("The input of trump was wrong! Please use Chapital. Try agin.");
				System.out.println("**************************************");
				return false;
			}
		}else if(str.equals("PASS")) {
			return true;
		}else {
			System.out.println("The input was wrong! Please start with number. Try agin.");
			System.out.println("**************************************");
			return false;
		}
	}
	
	@SuppressWarnings("finally")
	private String getInput() {
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		try {
			while(!rightInput(str)) {
				System.out.println("What kind of trump do you want to call?");
				str = bReader.readLine();
			}
		}catch(IOException e){
			
		}finally {
			// null means ERROR
			return str;
		}	
	}
	
	public void callTrump() {
		//set the start caller;
		beginToCall(players);
		
		// initialize with "pass"
		String tempString = TRUMP[0][0];
		String direction = null;
		String inputString = null;
		int index;
		
		if(this.startCaller != null) {
			direction = this.startCaller.direction;
			index = getIndex(direction);
			
			System.out.println("Form of input: Number+Trump, like 1HEART.");
			while(!canPass(this.recordArrayList)) {
				printRecordArrayList();
				if(canResponse(index)) {
					inputString = getInput();
					tempString = inputString;
					if(rightInput(inputString)) {
						recordArrayList.add(inputString);
					}
				}else {
					System.out.println("You have less than 6 points, please enter \"PASS\"");
					if(rightInput(inputString)) {
						recordArrayList.add(inputString);
					}
					tempString = "PASS";
				}
				
				if(index < this.players.size() - 1) {
					index ++;
				}else {
					index = 0;
				}
				System.out.println("**************************************");
			}
			System.out.println("The phase of calling trumps is ended!");
			// if other 3 players pass
			tempString = recordArrayList.get(recordArrayList.size() - 4);
			trumpColor = tempString.substring(1);
			findStarter();
			System.out.println("The trump is: " + tempString);
			findStarter();
			System.out.println("The starter is: " + this.starter.direction);
		}else {
			System.out.println("No one has more than 12 points."
					+ " Please restart the game!");
		}
	}
	
	public void printRecordArrayList() {
		Iterator<String> iterator = this.recordArrayList.iterator();
		while(iterator.hasNext()) {
			System.out.println((String)iterator.next());
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
		if(recordArrayList.size() < 4) {
			return false;
		}else {
			for(int i = 0; i < 3; i++) {
				if(!(recordArrayList.get(recordArrayList.size() - 1 - i)).equals("PASS")) {
					return false;
				}
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
		for(int i = 0; i < this.players.size(); i++) {
			// calculate points of all cards for each player
			this.sum[i] = countPoints(this.players.get(i).cards);
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
		countCards();
		for(int i = 0; i < 4; i++) {
			if(sum[i] >= 12) {
				this.startCaller = players.get(i);
				System.out.println("The start caller is: " + startCaller.direction);
				return startCaller;
			}
		}
		return null;
	}
	
	private boolean canResponse(int index) {
		return (this.sum[index] >= 6);
	}
}
