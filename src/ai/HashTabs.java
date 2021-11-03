package ai;

import java.util.HashMap;
import java.util.Map;

class HashTabs {

	private Map<String, Integer> black = new HashMap<>();
	private Map<String, Integer> white = new HashMap<>();	
	
	// fills calculating hash
	void add(String[][] field, String side, int depth) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<field.length ; r++){
			for(int c=0; c<field[r].length ; c++){
				if(field[r][c].equals(" ")) {
					current.append(String.valueOf(r) + String.valueOf(c));
				}
				else {
					current.append(field[r][c]);
				}				
			}
		}		
		String hash = current.toString();
		
		if(side.equals("black")) {
			black.putIfAbsent(hash, depth);
		}
		else {
			white.putIfAbsent(hash, depth);
		}
	}
	
	// checks repetitions while calculating	
	boolean repeat(String[][] field,  String side, int depth) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<field.length ; r++){
			for(int c=0; c<field[r].length ; c++){
				if(field[r][c].equals(" ")) {
					current.append(String.valueOf(r) + String.valueOf(c));
				}
				else {
					current.append(field[r][c]);
				}				
			}
		}		
		String hash = current.toString();
			
		if(side.equals("white") & white.containsKey(hash)) {			
			return(white.get(hash) == depth+4);
		}				
		else if(side.equals("black") & black.containsKey(hash)) {
			return(black.get(hash) == depth+4);
		}
		else {
			return false;
		}
	}
	
	// clears calculating hash
	void clear() {		
		black = null;
		white = null;
	}
	
}
