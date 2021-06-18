package util;

public class Rook {

	private final static String NAME = "Rook";

	private final static int VALUE = 40;

	static String getName() {		
		return NAME;
	}
	
	public static int getValue() {
		return VALUE;
	}
	
	public static boolean move(int r, int c, int r2, int c2) {
		
		return((((r2==r)&(c2==c-1||c2==c+1)) || ((r2==r-1||r2==r+1)&(c2==c))) &
		   ((r2>=0 & r2<=3) & (c2>=0 & c2<=2)));
	}
}