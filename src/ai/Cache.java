package ai;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilpack.Capture;
import utilpack.Copier;
import utilpack.Matrix;

public class Cache {

	private static Map<String, Node> cache = new HashMap<>();
	
	static void add(String[][] board, List<Node> moves) {	
		
		for(Node move: moves) {
			
			int r = move.getR();
			int c = move.getC();
			int r2 = move.getR2();
			int c2 = move.getC2();
			
			String[][] field = Copier.deepCopy(board);			
			if(field[r][c].equals("p") & r==2){
				if(r==0 & c > 2){
					field[r2][c2] = "p";
					field[r][c] = " ";
				}	
				else{
					Capture.take(field, r2, c2);
					field[r2][c2] = "q";
					field[r][c] = " ";
				}
			}
			else{
				Capture.take(field, r2, c2);
				field[r2][c2] = field[r][c];
				field[r][c] = " ";
			}			
			String state = Matrix.keyMaker(field);
			
			List<Node> children = move.getChidren();
			Node child = children.stream().max(Comparator.comparing(Node::getValue)).get();
			cache.putIfAbsent(state, child);
			
			if(child.hasChildren()) {
				add(field, child.getChidren());
			}
		}
	}
	
	public static boolean has(String move) {
		return cache.containsKey(move);
	}
	
	public static Node get(String move) {
		return cache.get(move);
	}
	
	static boolean empty() {
		return cache.isEmpty();
	}
	
	static void clear() {
		cache.clear();
	}
	
}
