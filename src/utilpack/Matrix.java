package utilpack;

public class Matrix {
	
	public static String keyMaker(final String[][] FIELD) {
		
		StringBuilder current = new StringBuilder(24);
		
		for(int r=0; r<4 ; r++){
			for(int c=0; c<3 ; c++){
				if(FIELD[r][c].equals(" ")) {
					current.append("#");
				}
				else {
					current.append(FIELD[r][c]);
				}				
			}
		}
		for(int i=3; i<9; i++) {
			if(FIELD[0][i].equals(" ")) {
				current.append("#");
			}
			else {
				current.append(FIELD[0][i]);
			}	
		}
		for(int i=3; i<9; i++) {
			if(FIELD[3][i].equals(" ")) {
				current.append("#");
			}
			else {
				current.append(FIELD[3][i]);
			}	
		}
		final String HASH = current.toString();
		return HASH;		
	}
	
	public static String keySwapper(final String note) {
		
		char[] temp = note.toCharArray();
		for(int i=0; i<temp.length/2; i+=3) {
			char tmp = temp[i];
			temp[i] = temp[i+2];
			temp[i+2] = tmp;
		}
		return new String(temp);		
	}
	
}
