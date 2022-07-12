package data;

import java.io.Serializable;
import java.util.Map;

public class GameState implements Serializable {
	
	private final char[][] board;
	private final Map<String, Integer> moves;
	private final int level;
	
	public GameState(char[][] board, Map<String, Integer> moves, int level) {
		this.board = board;
		this.moves = moves;
		this.level = level;
	}

	public char[][] getBoard() {
		return board;
	}
	public Map<String, Integer> getMoves() {
		return moves;
	}		
	public int getLevel() {
		return level;
	}
	
}
