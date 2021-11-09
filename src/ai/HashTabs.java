package ai;

import java.util.HashMap;
import java.util.Map;

import util.Copier;

class HashTabs {

	private Map<String, Integer> black = new HashMap<>();
	private Map<String, Integer> white = new HashMap<>();	
	
	// fills calculating hash
	void add(String[][] field, String side, int depth) {
		
		String hash = Copier.keyMaker(field);
		
		if(side.equals("black")) {
			black.putIfAbsent(hash, depth);
		}
		else {
			white.putIfAbsent(hash, depth);
		}
	}
	
	// checks repetitions while calculating	
	boolean repeat(String[][] field,  String side, int depth) {
		
		String hash = Copier.keyMaker(field);
							
		if(side.equals("black") & black.containsKey(hash)) {
			if(black.containsKey(hash))
				return(black.get(hash) == depth+4);
		}
		else {
			if(white.containsKey(hash))
				return(white.get(hash) == depth+4);				
		}
		return false;
	}
	
	// clears calculating hash
	void clear() {		
		black = null;
		white = null;
	}
	
}
