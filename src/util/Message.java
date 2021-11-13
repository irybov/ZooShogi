package util;

public class Message {

	public static String colName(final int COL){
		
		String c = "";
		
		switch(COL){
		case 0:
			c = "A";
			break;
		case 1:
			c = "B";
			break;
		case 2:
			c = "C";
			break;
		case 3: case 4: case 5: case 6: case 7: case 8:
			c = Integer.toString(COL+1);
			break;
		}
		return c;
 	}
	
	public static String pieceName(final String PIECE){
		
		String name = "";
		
		switch(PIECE){
		case "P": case "p":
			name = Pieces.BPAWN.getName();
			break;
		case "R": case "r":
			name = Pieces.ROOK.getName();
			break;
		case "B": case "b":
			name = Pieces.BISHOP.getName();
			break;
		case "K": case "k":
			name = Pieces.KING.getName();
			break;
		case "Q": case "q":
			name = Pieces.BQUEEN.getName();
			break;
		}
		return name;
	}
	
}
