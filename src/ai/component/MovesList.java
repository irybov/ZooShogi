package ai.component;

import java.util.HashSet;
import java.util.Set;

import utilpack.Matrix;
import utilpack.Turn;

public class MovesList {
	
	private static Set<String> blackMoves = new HashSet<>();
	private static Set<String> whiteMoves = new HashSet<>();
	
	// fills game moves list
	public static void addMove(String[][] field, Turn turn) {
		
		String hash = Matrix.makeKey(field);
		
		if(turn.equals(Turn.BLACK)) {
			blackMoves.add(hash);
		}
		else if(turn.equals(Turn.WHITE)){
			whiteMoves.add(hash);			
		}
		
	}

	//checks 3-times repetition
	public static boolean isRepeated(String[][] field, Turn white) {
		
		String hash = Matrix.makeKey(field);
			
		if(white.equals(Turn.BLACK)) {
			return(blackMoves.contains(hash));
		}
		else{
			return(whiteMoves.contains(hash));			
		}
		
	}
	
	public static void clear() {		
		blackMoves.clear();
		whiteMoves.clear();
	}
	
}
