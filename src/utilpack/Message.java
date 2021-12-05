package utilpack;

public class Message {

	public static String getColName(final int COL){
		
		String c;
		
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
		default:
			c = "#";
			break;
		}
		return c;
 	}
	
	public static String getPieceName(final String PIECE){
		
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
	
	public static String getEdge(int R, int C, int R2, int C2, String piece) {
		return (C < 3 ? Message.getColName(C) + (R+1) : piece.toUpperCase()
				+ Message.getColName(C)) + Message.getColName(C2) + (R2+1);
	}
	
}
