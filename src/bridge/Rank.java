package bridge;

public class Rank {
	protected String value[];
	
	public Rank() {
		this.value = new String[13];
		for(int i = 0; i < 10; i++) {
			value[i] = String.valueOf(i + 2);
		}
		value[9] = "J";
		value[10] = "Q";
		value[11] = "K";
		value[12] = "a";
	}
	
	public void printRank() {
		for(int i = 0; i < 12; i++) {
			System.out.println(value[i]);
		}
	}
}
