package ai;

import java.util.HashSet;
import java.util.Set;

public class MoveList {
	
	private static Set<String> list = new HashSet<>();
	
	// fills game moves list
	static void add(String[][] field) {
		
		StringBuilder current = new StringBuilder(26);
		
		for(int r=0; r<field.length ; r++){
			for(int c=0; c<field[r].length ; c++){
				current.append(field[r][c]);				
			}
		}
		String hash = current.toString();
		list.add(hash);		
	}

	//checks 3-times repetition
	static boolean repeat(String[][] field) {
		
		StringBuilder current = new StringBuilder(26);
		
		for(int r=0; r<field.length ; r++){
			for(int c=0; c<field[r].length ; c++){
				current.append(field[r][c]);				
			}
		}		
		String hash = current.toString();
				
		return(list.contains(hash));
	}
}
