package ai;

import java.util.HashSet;
import java.util.Set;

import utilpack.Matrix;

public class MovesList {
	
	private static Set<String> blackMoves = new HashSet<>();
	private static Set<String> whiteMoves = new HashSet<>();
	
	// fills game moves list
	public static void addMove(String[][] field, String turn) {
		
		String hash = Matrix.makeKey(field);
		
		if(turn.equals("black")) {
			blackMoves.add(hash);
		}
		else {
			whiteMoves.add(hash);			
		}
		
	}

	//checks 3-times repetition
	static boolean isRepeated(String[][] field, String turn) {
		
		String hash = Matrix.makeKey(field);
			
		if(turn.equals("black")) {
			return(blackMoves.contains(hash));
		}
		else {
			return(whiteMoves.contains(hash));			
		}
		
	}
	
	static void clear() {		
		blackMoves.clear();
		whiteMoves.clear();
	}
	
}
