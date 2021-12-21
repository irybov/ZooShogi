package ai;

import java.util.HashSet;
import java.util.Set;

import utilpack.Matrix;

public class MovesList {
	
	private static Set<String> black = new HashSet<>();
	private static Set<String> white = new HashSet<>();
	
	// fills game moves list
	public static void add(String[][] field, String turn) {
		
		String hash = Matrix.keyMaker(field);
		
		if(turn.equals("black")) {
			black.add(hash);
		}
		else {
			white.add(hash);			
		}
		
	}

	//checks 3-times repetition
	static boolean repeat(String[][] field, String turn) {
		
		String hash = Matrix.keyMaker(field);
			
		if(turn.equals("black")) {
			return(black.contains(hash));
		}
		else {
			return(white.contains(hash));			
		}
		
	}
	
	static void clear() {		
		black.clear();
		white.clear();
	}
	
}
