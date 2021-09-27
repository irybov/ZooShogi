package data;

public class Board {

	private String[][] board;
	private int score;
	private String message;

	public Board() {}
	
	public Board(String[][] board) {
		this.board = board;
	}
	
	public Board(String[][] board, int score) {
		this.board = board;
		this.score = score;
	}
	
	public Board(String[][] board, int score, String message) {
		this.board = board;
		this.score = score;
		this.message = message;
	}

	public String[][] getBoard() {
		return this.board;
	}
	public int getScore() {
		return this.score;
	}
	public String getMessage() {
		return this.message;
	}
	
}
