package util;

public class Capture {

	// checks if capture is possible
	public static boolean check(String[][] field, int r2, int c2, String side) {
		
		if((side == "white") & (field[r2][c2]==" "||field[r2][c2]=="p"||field[r2][c2]=="q"||
								field[r2][c2]=="r"||field[r2][c2]=="b"||field[r2][c2]=="k")) {
			return true;
		}
		else if((side == "black") & (field[r2][c2]==" "||field[r2][c2]=="P"||field[r2][c2]=="Q"||
								field[r2][c2]=="R"||field[r2][c2]=="B"||field[r2][c2]=="K")) {
			return true;
		}
		else
		return false;
	}

	// checks capture extensions
	public static boolean extend(String piece, String side) {
		
		if((side == "white") & (piece=="p"||piece=="q"||piece=="r"||piece=="b"||piece=="k")) {
			return true;
		}
		else if((side == "black") & (piece=="P"||piece=="Q"||piece=="R"||piece=="B"||piece=="K")) {
			return true;
		}
		else
		return false;
	}

	// checks current king's state
	public static boolean king(String[][] field, int r2, int c2, String side) {
		
		if(side == "white" & field[r2][c2]=="k") {
			return true;
		}
		else if(side == "black" & field[r2][c2]=="K") {
			return true;
		}
		else
		return false;
	}

	// checks king's promotion
	public static boolean prom(String[][] field, int r2, int c2, String side) {
		
		if(((side == "white") & (field[r2][c2]=="K")) & (r2==0&(c2==0||c2==1||c2==2))) {
			return true;
		}
		else if(((side == "black") & (field[r2][c2]=="k")) & (r2==3&(c2==0||c2==1||c2==2))) {
			return true;
		}
		else
		return false;
	}
	
	// calculates attacking bonuses
	public static int attack(String[][] field, int r2, int c2, String side) {
		
		int value = 0;

		if(side == "black") {
			switch(field[r2][c2]){
			case " ":				
				value = 1;
				break;
			case "P":				
				value = 2;
				break;
			case "R":				
				value = 4;
				break;
			case "B":				
				value = 3;
				break;
			case "Q":				
				value = 5;
				break;
			case "K":				
				value = 10;
				break;
			}
		}
		else {
			switch(field[r2][c2]){
			case " ":				
				value = -1;
				break;
			case "p":				
				value = -2;
				break;
			case "r":				
				value = -4;
				break;
			case "b":				
				value = -3;
				break;
			case "q":				
				value = -5;
				break;
			case "k":				
				value = -10;
				break;
			}
		}
		return value;
	}

	// controls piece's capture
	public static int take(String[][] field, int r2, int c2) {
		
		int c3 = 9;
		
		switch(field[r2][c2]){
		case "b":
			if(field[3][3]==" "){
			field[3][3] = "B";
			c3 = 3;
			}
			else if(field[3][3]!=" "){
			field[3][6] = "B";
			c3 = 6;
			}
			break;
		case "p":
			if(field[3][4]==" "){
			field[3][4] = "P";
			c3 = 4;
			}
			else if(field[3][4]!=" "){
			field[3][7] = "P";
			c3 = 7;
			}
			break;
		case "r":
			if(field[3][5]==" "){
			field[3][5] = "R";
			c3 = 5;
			}
			else if(field[3][5]!=" "){
			field[3][8] = "R";
			c3 = 8;
			}
			break;
		case "q":
			if(field[3][4]==" "){
			field[3][4] = "P";
			c3 = 4;
			}
			else if(field[3][4]!=" "){
			field[3][7] = "P";
			c3 = 7;
			}
			break;						
		case "B":
			if(field[0][3]==" "){
			field[0][3] = "b";
			c3 = 3;
			}
			else if(field[0][3]!=" "){
			field[0][6] = "b";
			c3 = 6;
			}
			break;
		case "P":
			if(field[0][4]==" "){
			field[0][4] = "p";
			c3 = 4;
			}
			else if(field[0][4]!=" "){
			field[0][7] = "p";
			c3 = 7;
			}
			break;
		case "R":
			if(field[0][5]==" "){
			field[0][5] = "r";
			c3 = 5;
			}
			else if(field[0][5]!=" "){
			field[0][8] = "r";
			c3 = 8;
			}
			break;
		case "Q":
			if(field[0][4]==" "){
			field[0][4] = "p";
			c3 = 4;
			}
			else if(field[0][4]!=" "){
			field[0][7] = "p";
			c3 = 7;
			}
			break;
		}
		return c3;
	}

	// undo calculated move
	public static void undo(String[][] field, int r3, int c3){
		
		field[r3][c3] = " ";
	}
	
}