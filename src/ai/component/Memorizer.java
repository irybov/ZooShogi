package ai.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utilpack.Matrix;
import utilpack.Turn;

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
	public void add(String[][] field, Turn side, Edge edge) {
		
		String hash = Matrix.makeKey(field);
		
		if(side.equals(Turn.BLACK)) {
			black.putIfAbsent(hash, edge);
		}
		else {
			white.putIfAbsent(hash, edge);
		}
	}

	// checks repetitions while calculating	
	public boolean has(String[][] field, Turn side) {
		
		String hash = Matrix.makeKey(field);
							
		if(side.equals(Turn.BLACK)) {
			return(black.containsKey(hash));
		}
		else {
			return(white.containsKey(hash));
		}	
	}
	
	public boolean precise(String[][] field, Turn side, int depth) {
		
		String hash = Matrix.makeKey(field);
		Edge entry;
		
		if(side.equals(Turn.BLACK)) {
			entry = black.get(hash);
		}
		else {
			entry = white.get(hash);
		}
		return depth <= entry.getDepth();
	}
	
	// returns previously calculated results
	public int get(String[][] field,  Turn side) {
		
		String hash = Matrix.makeKey(field);
		Edge entry;
		
		if(side.equals(Turn.BLACK)) {
			entry = black.get(hash);			
			return entry.getValue();
		}
		else {
			entry = white.get(hash);
			return entry.getValue();
		}		
	}
	
	// updates previously calculated results	
	public void update(String[][] field, Turn side, Edge edge) {
		
		String hash = Matrix.makeKey(field);		
		Edge entry;
		
		if(side.equals(Turn.BLACK)) {
			entry = black.get(hash);
			if(edge.getDepth() < entry.getDepth()) {
				entry.setValue(edge.getValue());
			}
		}
		else {
			entry = white.get(hash);
			if(edge.getDepth() < entry.getDepth()) {
				entry.setValue(edge.getValue());				
			}
		}
	}
	
	void clear() {
		black.clear();
		white.clear();
	}
	
}
