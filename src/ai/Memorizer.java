package ai;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utilpack.Matrix;

public class Memorizer {
	
    private Memorizer(){}    
    private static class SingletonHolder {
        public static final Memorizer HOLDER_INSTANCE = new Memorizer();
    }    
    public static Memorizer getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }
	
	private Map<String, Edge> black = new ConcurrentHashMap<>();
	private Map<String, Edge> white = new ConcurrentHashMap<>();
	
	// fills calculating hash
	void add(String[][] field, String side, Edge edge) {
		
		String hash = Matrix.keyMaker(field);
		
		if(side.equals("black")) {
			black.putIfAbsent(hash, edge);
		}
		else {
			white.putIfAbsent(hash, edge);
		}
	}

	// checks repetitions while calculating	
	boolean has(String[][] field,  String side) {
		
		String hash = Matrix.keyMaker(field);
							
		if(side.equals("black")) {
			return(black.containsKey(hash));
		}
		else {
			return(white.containsKey(hash));
		}	
	}
	
	// returns previously calculated results
	int get(String[][] field,  String side) {
		
		String hash = Matrix.keyMaker(field);
		Edge entry;
		
		if(side.equals("black")) {
			entry = black.get(hash);			
			return entry.getValue();
		}
		else {
			entry = white.get(hash);
			return entry.getValue();
		}		
	}
	
	// updates previously calculated results	
	void update(String[][] field,  String side, Edge edge) {
		
		String hash = Matrix.keyMaker(field);		
		Edge entry;
		
		if(side.equals("black")) {
			entry = black.get(hash);
			if(edge.getDepth() > entry.getDepth()) {
				entry.setValue(edge.getValue());
			}
		}
		else {
			entry = white.get(hash);
			if(edge.getDepth() > entry.getDepth()) {
				entry.setValue(edge.getValue());				
			}
		}
	}
	
	void clear() {
		black.clear();
		white.clear();
	}
	
}
