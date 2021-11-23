package utilpack;

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
	
}
