package ai;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utilpack.Matrix;

public class Memorizer {

	private static volatile Memorizer INSTANCE;
	
	public static Memorizer getInstance(){
		
		if(INSTANCE == null) {
			synchronized (Memorizer.class) {
				if(INSTANCE == null) {
					INSTANCE = new Memorizer();
				}
			}
		}		
		return INSTANCE;
	}
	
	private Map<String, Node> black = new ConcurrentHashMap<>();
	private Map<String, Node> white = new ConcurrentHashMap<>();
	
	// fills calculating hash
	void add(String[][] field, String side, Node edge) {
		
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
		Node entry;
		
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
	void update(String[][] field,  String side, Node edge) {
		
		String hash = Matrix.keyMaker(field);		
		Node entry;
		
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
