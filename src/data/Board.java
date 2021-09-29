package data;

public class Board {

	private String[][] board;
	private long score;
	private String message;

	public Board() {}
	
	public Board(String[][] board) {
		this.board = board;
	}
	
	public Board(String[][] board, long score) {
		this.board = board;
		this.score = score;
	}
	
	public Board(String[][] board, long score, String message) {
		this.board = board;
		this.score = score;
		this.message = message;
	}

	public String[][] getBoard() {
		return board;
	}
	public long getScore() {
		return score;
	}
	public String getMessage() {
		return message;
	}
	
}
