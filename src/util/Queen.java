package util;

public class Queen {
	
	private final static String NAME = "Queen";

	private final static int VALUE = 50;
	
	public String getName() {		
		return NAME;
	}	
	public int getValue() {		
		return VALUE;
	}

	public boolean move(int r, int c, int r2, int c2, String turn) {
		
		if((turn == "white") & ((r2 == r+1) & (c2 == c-1 || c2 == c+1))) {
			return false;
		}				
		else if((turn == "black") & ((r2 == r-1) & (c2 == c-1 || c2 == c+1))) {
			return false;
		}		
		return(((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1)) &
		   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
	}
}
