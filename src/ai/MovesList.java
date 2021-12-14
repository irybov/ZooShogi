package ai;

import java.util.HashSet;
import java.util.Set;

import utilpack.Matrix;

public class MovesList {
	
	private static Set<String> listB = new HashSet<>();
	private static Set<String> listW = new HashSet<>();
	
	// fills game moves list
	public static void add(String[][] field, String turn) {
		
		String hash = Matrix.keyMaker(field);
		
		if(turn.equals("black")) {
			listB.add(hash);
		}
		else {
			listW.add(hash);			
		}
		
	}

	//checks 3-times repetition
	static boolean repeat(String[][] field, String turn) {
		
		String hash = Matrix.keyMaker(field);
			
		if(turn.equals("black")) {
			return(listB.contains(hash));
		}
		else {
			return(listW.contains(hash));			
		}
		
	}
	
	static void clear() {		
		listB.clear();
		listW.clear();
	}
	
}
