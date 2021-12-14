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
			
			if(move.hasChildren()) {				
				int r = move.getR();
				int c = move.getC();
				int r2 = move.getR2();
				int c2 = move.getC2();
				
				String[][] field = Copier.deepCopy(board);	
				Capture.take(field, r2, c2);
				if(field[r][c].equals("P")){	
					if(r==1){
						field[r2][c2] = "Q";
					}
					else {
						field[r2][c2] = "P";					
					}
				}
				else{
					field[r2][c2] = field[r][c];
				}
				field[r][c] = " ";
				String state = Matrix.keyMaker(field);
				
				List<Node> children = move.getChidren();
				Node child = children.stream().max(Comparator.comparing(Node::getValue)).get();
				cache.putIfAbsent(state, child);
				
				if(child.hasChildren()) {					
					r = child.getR();
					c = child.getC();
					r2 = child.getR2();
					c2 = child.getC2();
								
					Capture.take(field, r2, c2);
					if(field[r][c].equals("p")){	
						if(r==2){
							field[r2][c2] = "q";
						}
						else {
							field[r2][c2] = "p";					
						}
					}
					else{
						field[r2][c2] = field[r][c];
					}
					field[r][c] = " ";
					add(Copier.deepCopy(field), child.getChidren());
				}
			}
		}
	}
	
	public static boolean has(String move) {
		return cache.containsKey(move);
	}
	
	public static Node get(String move) {
		return cache.get(move);
	}
	
	public static boolean empty() {
		return cache.isEmpty();
	}
	
	static void clear() {
		cache.clear();
	}
	
}
