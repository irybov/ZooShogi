package utilpack;

public final class Expositor {
	
	private Expositor() {}

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
	
	public static String getPieceName(final char piece){
		
		String name;
		
		switch(piece){
		case 'P': case 'p':
			name = Pieces.BPAWN.getName();
			break;
		case 'R': case 'r':
			name = Pieces.ROOK.getName();
			break;
		case 'B': case 'b':
			name = Pieces.BISHOP.getName();
			break;
		case 'K': case 'k':
			name = Pieces.KING.getName();
			break;
		case 'Q': case 'q':
			name = Pieces.BQUEEN.getName();
			break;
		default:
			name = null;
		}
		return name;
	}
	
	public static String getEdge(int R, int C, int R2, int C2, char board) {
		return (C < 3 ? Expositor.getColumnName(C) + (R+1) : Character.toUpperCase(board)
				+ Expositor.getColumnName(C)) + Expositor.getColumnName(C2) + (R2+1);
	}
	
}
