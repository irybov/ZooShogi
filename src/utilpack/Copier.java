package utilpack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.Node;

public class Copier {

	public static String[][] deepCopy(final String[][] ORIGINAL) {
		
		String[][] copy = new String[][]{{" "," "," "," "," "," "," "," "," "},
			   							 {" "," "," "},
			   							 {" "," "," "},
			   							 {" "," "," "," "," "," "," "," "," "}};
		
	    for (int x = 0; x < ORIGINAL.length; x++) {
	    	for (int y = 0; y < ORIGINAL[x].length; y++) {
	          copy[x][y] = ORIGINAL[x][y];
	      	}
	    }
	    return copy;
	}
	
	public static HashMap<String, Integer> deepCopy(final Map<String, Integer> ORIGINAL) {
	
	    HashMap<String, Integer> copy = new HashMap<String, Integer>();
	    for (Map.Entry<String, Integer> entry : ORIGINAL.entrySet()){
	        copy.put(entry.getKey(), new Integer(entry.getValue()));
	    }
	    return copy;
	}
	
	private static Node root;
	public static void deepCopy(List<Node> original, List<Node> modified, boolean ceil) {
		
		if(ceil) {
			modified = new ArrayList<>(original.size());
			int c = original.get(0).getColumnFrom() > 2 ?
					original.get(0).getColumnFrom() : getIndex(original.get(0).getColumnFrom());
			int c2 = getIndex(original.get(0).getColumnTo());
			root = new Node(original.get(0).getRowFrom(), c, original.get(0).getRowTo(), c2,
					original.get(0).getSide());
			root.setValue(original.get(0).getValue());
			modified.add(root);
		}
		
		for(int i=0; i<original.size(); i++) {

			if(original.get(i).hasChildren()) {
				
				List<Node> children = original.get(i).getChidren();
				List<Node> twins = new ArrayList<>(children.size());
				
				for(int j=0; j<children.size(); j++) {
					int c = children.get(j).getColumnFrom() > 2 ?
							children.get(j).getColumnFrom() : getIndex(children.get(j).getColumnFrom());
					int c2 = getIndex(children.get(j).getColumnTo());									
					twins.add(new Node(children.get(j).getRowFrom(), c, children.get(j).getRowTo(), c2,
							children.get(j).getSide()));
					twins.get(j).setValue(children.get(j).getValue());
				}
				for(Node twin: twins) {
					twin.addParent(modified.get(i));
				}
				modified.get(i).addChildren(twins);
				deepCopy(children, twins, false);
			}
		}
	}
	public static Node getRoot() {
		return root;
	}	
	private static int getIndex(int col) {
		if(col == 0) {
			col = 2;
		}
		else if(col == 2) {
			col = 0;
		}
		return col;
	}
	
}
