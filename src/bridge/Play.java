package bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Play {
	private Player player;
	private Player starter;
	private Player startCaller;
	
	private String trumpColor;
	private ArrayList<Player> players;
	private int sum[] = new int[4];
	
	private ArrayList<String> recordArrayList = new ArrayList<String>();
	private ArrayList<String> showedArrayList = new ArrayList<String>();
	
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> trumpValueHashMap = new HashMap<String, Integer>();
	private final String[] DIRECTIONS = new String[]{
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
		
		initializeHashtable();
		
		initialize();
	}
	
	private void initializeHashtable() {
		hashMap.put("J", 1);
		hashMap.put("Q", 2);
		hashMap.put("K", 3);
		hashMap.put("a", 4);
		trumpValueHashMap.put("CLUB", 1);
		trumpValueHashMap.put("DIAMOND", 2);
		trumpValueHashMap.put("HEART", 3);
		trumpValueHashMap.put("SPADE", 4);
		trumpValueHashMap.put("NT", 5);
	}
	
	private void initialize() {
		callTrump();
		playTheCards();
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
	private String getInput(int difCase, int index) {
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		try {
			if(difCase == 0) {
				while(!rightInput(str)) {
					System.out.println("What kind of trump do you want to call?");
					str = bReader.readLine();
				}
			}else if(difCase == 1){
				while(!rightCard(str, index)) {
					System.out.println("Which card do you want to show?");
					str = bReader.readLine();
				}
			}else {
				return null;
			}
		}catch(IOException e){
			
		}finally {
			// null means ERROR
			return str;
		}	
	}
	
	private void callTrump() {
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
					inputString = getInput(0, index);
					tempString = inputString;
					if(rightInput(inputString)) {
						recordArrayList.add(inputString);
					}
				}else {
					System.out.println("You have less than 6 points, automatic PASS");
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
	
	private void printRecordArrayList() {
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

	/**
	 * To check whether the input card is correct
	 * @param str
	 * @param index(the current player)
	 * @return true/false
	 */
	public boolean rightCard(String str, int index) {
		if(str == null) {
			return false;
		}
		if(str.contains(" ")) {
			int indexOfSpace = str.indexOf(' ');
			
			// Check the suit
			String charString = str.substring(0, indexOfSpace);
			System.out.println("The charstring: " + charString);
			if(!(charString.equals("SPADE") || charString.equals("DIAMOND") || charString.equals("CLUB") || charString.equals("HEART"))) {
				System.out.println("The spell of color was wrong!");
				return false;
			}
			
			// The string represents the number of the card
			String c = str.substring(indexOfSpace + 1);
			// To check whether the last character is a number	
			if(str.length() >= 3 && (Character.isDigit(str.charAt(indexOfSpace + 1)) || c.equals("J") || c.equals("Q") || c.equals("K") || c.equals("a"))) {
				if(c.length() == 2) {
					// if the input is not equal 10
					if(c.charAt(str.length() - 1) != '0' || c.charAt(indexOfSpace + 1) != '1') {
						System.out.println("The number is greater than 10!");
						return false;
					}else {
						return true;
					}
				}else if(c.length() == 1) {
					System.out.println("The result is: " + contains(players.get(index).cards, new Card(c, str.substring(0, indexOfSpace))));
					//TODO
					if(contains(players.get(index).cards, new Card(c, str.substring(0, indexOfSpace)))) {
						return true;
					}else {
						System.out.println("You don't have this card!");
						return false;
					}
				}else {
					System.out.println("The length of number war wrong!");
					return false;
				}
			}else {
				System.out.println("The length is not suitable or the character is wrong!");
				return false;
			}
		}else {
			System.out.println("\" \" is required!");
			return false;
		}
	}
	
	/**
	 * Check whether the player has this card
	 * @param players
	 * @param temp
	 * @return
	 */
	private boolean contains(ArrayList<Card> players, Card temp) {
		String cString = temp.c;
		String colorString = temp.color;
		System.out.println("The char is " + cString.toString() + ", the color is: " + colorString.toString());
		Card tempString = null;
		Iterator<Card> iterator = players.iterator();
		
		int counter = 0;
		while(iterator.hasNext()) {
			tempString = (Card)iterator.next();
			counter++;
			boolean b = (tempString.color).equals(colorString) && (tempString.c).equals(cString);
			System.out.println("counter: " + counter + b);
			if(b) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * To check whether the showed card can win the last one
	 * @param str(the card)
	 * @return true/false 
	 */
	private boolean canWin(String str) {
		int indexOfSpace = str.indexOf(' ');
		String trumpString = str.substring(0, indexOfSpace);
		
		if(trumpString.equals(trumpColor)) {
			// The first time to show the card is always the winner
			if(this.showedArrayList.isEmpty()) {
				return true;
			}
			// The number is greater than the last one
			if((str.substring(indexOfSpace + 1)).compareTo((this.showedArrayList.get(this.showedArrayList.size() - 1)).substring(str.length() - 1)) > 0) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	private int getNextIndex(int i) {
		if(i < 0 || i > 3) {
			// -1 means ERROR
			return -1;
		}else {
			if(i + 1 < 3) {
				return (i + 1);
			}else {
				return 0;
			}
		}
	}
	
	public void playTheCards() {
		// the first player to play is the next player after the starter
		int index = getNext(starter);
		int counterIn = 0, counterOut = 0;
		int indexOfSpace;
		String inputString = null;
		String tempWinnerString = null;
		
		while(counterOut < 13) {
			// In each round, 4 cards will be showed
			while(counterIn < 4) {
				players.get(index).printAllCards();
				System.out.println("index = " + index);
				inputString = getInput(1, index);
				indexOfSpace = inputString.indexOf(' ');
				if(rightCard(inputString, index)) {
					showedArrayList.add(inputString + " index: " + index);
					players.get(index).cards.remove(new Card(inputString.substring(indexOfSpace + 1), inputString.substring(0, indexOfSpace)));
					if(canWin(inputString)) {
						tempWinnerString = inputString;
					}
					counterIn ++;
					index = getNextIndex(index);
				}
			}
			// The winner has the ability to show the card first
			index = this.showedArrayList.indexOf(tempWinnerString);
			counterIn = 0;
			counterOut ++;
		}
		System.out.println("Game over.");
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
