package utilpack;

public class Message {

	public static String getColumnName(final int column){
		
		String c;
		
		switch(column){
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
	
	public static String getPieceName(final String piece){
		
		String name = "";
		
		switch(piece){
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
		return (C < 3 ? Message.getColumnName(C) + (R+1) : piece.toUpperCase()
				+ Message.getColumnName(C)) + Message.getColumnName(C2) + (R2+1);
	}
	
}
