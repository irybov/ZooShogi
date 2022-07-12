package ai.component;

public class Board {

	private final char[][] board;
	private final char temp;
	private final char promotion;
	private final int c3;
	
	public Board(char[][] board2, char temp2, char promotion2, int c3) {
		this.board = board2;
		this.temp = temp2;
		this.promotion = promotion2;
		this.c3 = c3;
	}

	public char[][] getBoard() {
		return this.board;
	}
	public char getTemp() {
		return this.temp;
	}
	public char getPromotion() {
		return this.promotion;
	}
	public int getC3() {
		return this.c3;
	}
	
}
