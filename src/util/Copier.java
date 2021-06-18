package util;

public class Copier {

	public static String[][] deepCopy(String[][] A) {
		String[][] B = new String[4][];
		B[0] = new String[]{" "," "," "," "," "," "," "," "," "," "};
		B[1] = new String[]{" "," "," "};
		B[2] = new String[]{" "," "," "};		
		B[3] = new String[]{" "," "," "," "," "," "," "," "," "," "};
	    for (int x = 0; x < A.length; x++) {
	      for (int y = 0; y < A[x].length; y++) {
	          B[x][y] = A[x][y];
	      	}
	    }
	    return B;
	  }
}
