package util;

public class Matrix {
	
	public static String keyMaker(final String[][] FIELD) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<FIELD.length ; r++){
			for(int c=0; c<FIELD[r].length ; c++){
				if(FIELD[r][c].equals(" ")) {
					current.append("#");
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
