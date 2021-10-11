package util;

public class King {
	
	private final static String NAME = "King";
	
	public String getName() {		
		return NAME;
	}

	public boolean move(int r, int c, int r2, int c2) {
		
		return(((r2==r||r2==r-1||r2==r+1) & (c2==c||c2==c-1||c2==c+1)) &
		   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
	}
}
