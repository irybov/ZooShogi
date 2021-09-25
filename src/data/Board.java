package data;

public class Board {

	private String[][] board;
	private String score;
	private String message;

	public Board() {}
	
	public Board(String[][] board) {
		this.board = board;
	}
	
	public Board(String[][] board, String score) {
		this.board = board;
		this.score = score;
	}
	
	public Board(String[][] board, String score, String message) {
		this.board = board;
		this.score = score;
		this.message = message;
	}

	public String[][] getBoard() {
		return this.board;
	}
	public String getScore() {
		return this.score;
	}
	public String getMessage() {
		return this.message;
	}
	
}
