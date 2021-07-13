package control;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameState implements Serializable {
	
	private String[][] board;
	private Map<String, Integer> moves = new HashMap<>();
	
	public GameState(String[][] board, Map<String, Integer> moves) {
		this.board = board;
		this.moves = moves;
	}

	public String[][] getBoard() {
		return board;
	}
	public Map<String, Integer> getMoves() {
		return moves;
	}	
	
}
