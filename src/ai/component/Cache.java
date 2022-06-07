package ai.component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilpack.Capture;
import utilpack.Copier;
import utilpack.Matrix;

public class Cache {

	private static Map<String, Node> cache = new HashMap<>();
	
	public static void addTree(String[][] board, List<Node> moves) {	
		
		for(Node move: moves) {
			
			if(move.hasChildren()) {				
				int r = move.getRowFrom();
				int c = move.getColumnFrom();
				int r2 = move.getRowTo();
				int c2 = move.getColumnTo();
				
				String[][] field = Copier.deepCopy(board);	
				Capture.takenPiecePlacement(field, r2, c2);
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
				String state = Matrix.makeKey(field);
				
				List<Node> children = move.getChidren();
				Node child = children.stream().max(Comparator.comparing(Node::getValue)).get();
				cache.putIfAbsent(state, child);
				
				if(child.getValue() != Integer.MAX_VALUE) {				
					if(child.hasChildren()) {					
						r = child.getRowFrom();
						c = child.getColumnFrom();
						r2 = child.getRowTo();
						c2 = child.getColumnTo();
									
						Capture.takenPiecePlacement(field, r2, c2);
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
						addTree(Copier.deepCopy(field), child.getChidren());
					}
				}
			}
		}
	}
	
	public static boolean hasPosition(String move) {
		return cache.containsKey(move);
	}
	
	public static Node getMove(String move) {
		return cache.get(move);
	}
	
	public static boolean isEmpty() {
		return cache.isEmpty();
	}
	
	public static void clear() {
		cache.clear();
	}
	
}
