package util;

public class Pawn {

	private final static String NAME = "Pawn";

	private final static int VALUE = 20;
	
	public String getName() {		
		return NAME;
	}	
	public int getValue() {		
		return VALUE;
	}

	public boolean move(int r, int c, int r2, int c2) {
		
		return ((r2>=0 & r2<=3) & (c2>=0 & c2<=2));
	}			
}
