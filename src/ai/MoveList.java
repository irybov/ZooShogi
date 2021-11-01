package ai;

import java.util.HashSet;
import java.util.Set;

public class MoveList {
	
	private static Set<String> listB = new HashSet<>();
	private static Set<String> listW = new HashSet<>();
	
	// fills game moves list
	public static void add(String[][] field, String turn) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<field.length ; r++){
			for(int c=0; c<field[r].length ; c++){
				current.append(field[r][c]);				
			}
		}
		String hash = current.toString();
		
		if(turn.equals("black")) {
			listB.add(hash);
		}
		else {
			listW.add(hash);			
		}
		
	}

	//checks 3-times repetition
	static boolean repeat(String[][] field, String turn) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<field.length ; r++){
			for(int c=0; c<field[r].length ; c++){
				current.append(field[r][c]);				
			}
		}		
		String hash = current.toString();
			
		if(turn.equals("black")) {
			return(listB.contains(hash));
		}
		else {
			return(listW.contains(hash));			
		}
		
	}
	
	public static void clear() {		
		listB.clear();
		listW.clear();
	}
	
}
