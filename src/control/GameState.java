package control;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameState implements Serializable {
	
	private String[][] board;
	private Map<String, Integer> moves = new HashMap<>();
	private int level;
	
	GameState(String[][] board, Map<String, Integer> moves, int level) {
		this.board = board;
		this.moves = moves;
		this.level = level;
	}

	String[][] getBoard() {
		return board;
	}
	Map<String, Integer> getMoves() {
		return moves;
	}	
	
	int getLevel() {
		return level;
	}
	
}
