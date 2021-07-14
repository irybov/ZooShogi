package util;

public class Message {

	public static String colName(int col){
		
		String c = "";
		
		switch(col){
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
			c = Integer.toString(col);
			break;
		}
		return c;
 	}
	
	public static String pieceName(String piece){
		
		String name = "";
		
		switch(piece){
		case "P": case "p":
			name = Pawn.getName();
			break;
		case "R": case "r":
			name = Rook.getName();
			break;
		case "B": case "b":
			name = Bishop.getName();
			break;
		case "K": case "k":
			name = King.getName();
			break;
		case "Q": case "q":
			name = Queen.getName();
			break;
		}
		return name;
	}
	
}
