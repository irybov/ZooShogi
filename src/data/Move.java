package data;

public class Move {

	private int rowFrom;
	private int colFrom;
	private int rowTo;
	private int colTo;
	private int score;
	private int nodes;
	
	public Move(int rowFrom, int colFrom, int rowTo, int colTo, int score, int nodes) {
		this.rowFrom = rowFrom;
		this.colFrom = colFrom;
		this.rowTo = rowTo;
		this.colTo = colTo;
		this.score = score;
		this.nodes = nodes;
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
	public int getScore() {
		return this.score;
	}
	public int getNodes() {
		return this.nodes;
	}
	
}
