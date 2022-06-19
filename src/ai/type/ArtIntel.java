package ai.type;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import ai.Integrator;
import ai.component.Evaluator;
import ai.component.Generator;
import ai.component.Node;
import utilpack.Matrix;
import utilpack.Turn;

public class ArtIntel implements Callable<Integer>{
	
	Node root;
	String[][] board;

	public ArtIntel(Node root, String[][] board) {		
		this.root = root;
		this.board = board;
	}
		
	final InternalHash hash = new InternalHash();	
	final Generator generator = new Generator();
	final Evaluator evaluator = new Evaluator();
	final Integrator integrator = Integrator.getInstance();	
	int nodesCount = 0;

	class InternalHash {

		Map<String, Integer> blackMoves = new HashMap<>();
		Map<String, Integer> whiteMoves = new HashMap<>();	
		
		// fills calculating hash
		void addMove(String[][] field, Turn side, int depth) {
			
			String hash = Matrix.makeKey(field);
			
			if(side.equals(Turn.BLACK)) {
				blackMoves.putIfAbsent(hash, depth);
			}
			else if(side.equals(Turn.WHITE)){
				whiteMoves.putIfAbsent(hash, depth);
			}
		}
		
		// checks repetitions while calculating	
		boolean isRepeated(String[][] field,  Turn side, int depth) {
			
			String hash = Matrix.makeKey(field);
								
			if(side.equals(Turn.BLACK)) {
				if(blackMoves.containsKey(hash))
					return(blackMoves.get(hash) == depth+4);
			}
			else{
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
