package util;

import java.util.HashMap;
import java.util.Map;

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
	
	public static String keyMaker(final String[][] FIELD) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<FIELD.length ; r++){
			for(int c=0; c<FIELD[r].length ; c++){
				if(FIELD[r][c].equals(" ")) {
//					current.append(Integer.toString(r) + Integer.toString(c));
					current.append(Matrix.getCode(r, c));
				}
				else {
					current.append(FIELD[r][c]);
				}				
			}
		}		
		final String HASH = current.toString();
		return HASH;		
	}
	
}
