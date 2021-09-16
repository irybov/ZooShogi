package data;

public class Board {

	private String[][] board;

	public Board() {}
	
	public Board(String[][] board) {
		this.board = board;		
	}

	public String[][] getBoard() {
		return this.board;
	}
	public void setBoard(String[][] board) {
		this.board = board;
	}
		
}
