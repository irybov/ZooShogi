package data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameState implements Serializable {
	
	private String[][] board;
	private Map<String, Integer> moves = new HashMap<>();
	private int level;
	
	public GameState(String[][] board, Map<String, Integer> moves, int level) {
		this.board = board;
		this.moves = moves;
		this.level = level;
	}

	public String[][] getBoard() {
		return board;
	}
	public Map<String, Integer> getMoves() {
		return moves;
	}		
	public int getLevel() {
		return level;
	}
	
}
