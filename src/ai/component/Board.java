package ai.component;

public class Board {

	private final String[][] board;
	private final String temp;
	private final String promotion;
	private final int c3;
	
	public Board(String[][] board, String temp, String promotion, int c3) {
		this.board = board;
		this.temp = temp;
		this.promotion = promotion;
		this.c3 = c3;
	}

	public String[][] getBoard() {
		return this.board;
	}
	public String getTemp() {
		return this.temp;
	}
	public String getPromotion() {
		return this.promotion;
	}
	public int getC3() {
		return this.c3;
	}
	
}
