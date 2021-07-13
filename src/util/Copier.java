package util;

import java.util.HashMap;
import java.util.Map;

public class Copier {

	public static String[][] deepCopy(String[][] original) {
		
		String[][] copy = new String[4][];
		copy[0] = new String[]{" "," "," "," "," "," "," "," "," "," "};
		copy[1] = new String[]{" "," "," "};
		copy[2] = new String[]{" "," "," "};		
		copy[3] = new String[]{" "," "," "," "," "," "," "," "," "," "};
		
	    for (int x = 0; x < original.length; x++) {
	    	for (int y = 0; y < original[x].length; y++) {
	          copy[x][y] = original[x][y];
	      	}
	    }
	    return copy;
	}
	
	public static HashMap<String, Integer> deepCopy(Map<String, Integer> original) {
		
		    HashMap<String, Integer> copy = new HashMap<String, Integer>();
		    for (Map.Entry<String, Integer> entry : original.entrySet()){
		        copy.put(entry.getKey(), new Integer(entry.getValue()));
		    }
		    return copy;
		}
	
}
