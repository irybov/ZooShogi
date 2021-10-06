package data;

public class Move {

	private int rowFrom;
	private int colFrom;
	private int rowTo;
	private int colTo;
	private long score;
	
	public Move(int rowFrom, int colFrom, int rowTo, int colTo, long score) {
		this.rowFrom = rowFrom;
		this.colFrom = colFrom;
		this.rowTo = rowTo;
		this.colTo = colTo;
		this.score = score;
	}

	public int getRowFrom() {
		return this.rowFrom;
	}
	public int getColFrom() {
		return this.colFrom;
	}
	public int getRowTo() {
		return this.rowTo;
	}
	public int getColTo() {
		return this.colTo;
	}
	public long getScore() {
		return this.score;
	}
		
}
