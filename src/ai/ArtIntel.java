package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import utilpack.Matrix;

public class ArtIntel implements Callable<Integer>{
	
	Node root;
	String[][] board;

	public ArtIntel(Node root, String[][] board) {		
		this.root = root;
		this.board = board;
	}
		
	InternalHash hash = new InternalHash();	
	final Generator generator = new Generator();
	final Evaluator evaluator = new Evaluator();
	final Integrator integrator = Integrator.getInstance();	
	int nodesCount = 0;

	class InternalHash {

		Map<String, Integer> blackMoves = new HashMap<>();
		Map<String, Integer> whiteMoves = new HashMap<>();	
		
		// fills calculating hash
		void addMove(String[][] field, String side, int depth) {
			
			String hash = Matrix.makeKey(field);
			
			if(side.equals("black")) {
				blackMoves.putIfAbsent(hash, depth);
			}
			else {
				whiteMoves.putIfAbsent(hash, depth);
			}
		}
		
		// checks repetitions while calculating	
		boolean isRepeated(String[][] field,  String side, int depth) {
			
			String hash = Matrix.makeKey(field);
								
			if(side.equals("black")) {
				if(blackMoves.containsKey(hash))
					return(blackMoves.get(hash) == depth+4);
			}
			else {
				if(whiteMoves.containsKey(hash))
					return(whiteMoves.get(hash) == depth+4);				
			}
			return false;
		}
		
	}
	
	@Override
	public Integer call(){		
		return null;
	}
	
}
