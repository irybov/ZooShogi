package utilpack;

public final class Capture {

	private Capture() {}

	// checks if capture is possible
	public static boolean isCaptureLegal(char[][] board, int r2, int c2, Turn turn) {
		
		if(turn.equals(Turn.WHITE)&(board[r2][c2]==(' ')||board[r2][c2]==('p')||
			board[r2][c2]==('q')||board[r2][c2]==('r')||board[r2][c2]==('b')
			||board[r2][c2]==('k'))) {
			return true;
		}
		else if(turn.equals(Turn.BLACK)&(board[r2][c2]==(' ')||board[r2][c2]==('P')||
				board[r2][c2]==('Q')||board[r2][c2]==('R')||board[r2][c2]==('B')
				||board[r2][c2]==('K'))) {
			return true;
		}
		return false;
	}

	// checks capture extensions
	public static boolean isExtension(char piece, Turn side) {
		
		if(side.equals(Turn.WHITE)&(piece==('p')||piece==('q')||piece==('r')
				||piece==('b'))) {
			return true;
		}
		else if(side.equals(Turn.BLACK)&(piece==('P')||piece==('Q')||piece==('R')
				||piece==('B'))) {
			return true;
		}
		return false;
	}

	// checks current king's state
	public static boolean isKingUnderCheck(String[][] field, int r2, int c2, Turn side) {
		
		if(side.equals(Turn.WHITE) & field[r2][c2].equals("k")) {
			return true;
		}
		else if(side.equals(Turn.BLACK) & field[r2][c2].equals("K")) {
			return true;
		}
		return false;
	}

	// checks king's promotion
	public static boolean isKingPromoted(char[][] board, int r2, int c2, Turn side) {
		
		if((side.equals(Turn.WHITE) & board[r2][c2]==('K')) & (r2==0&(c2==0||c2==1||c2==2))) {
			return true;
		}
		else if((side.equals(Turn.BLACK)&board[r2][c2]==('k'))&(r2==3&(c2==0||c2==1||c2==2))){
			return true;
		}
		return false;
	}
	
	// calculates attacking bonuses
	public static int attackBonusesCount(char[][] board, int r2, int c2, Turn side) {
		
		int value = 0;

		if(side.equals(Turn.BLACK)) {
			switch(board[r2][c2]){
			case 'P':				
				value = 2;
				break;
			case 'R':				
				value = 4;
				break;
			case 'B':				
				value = 3;
				break;
			case 'Q':				
				value = 5;
				break;
			case 'K':				
				value = 10;
				break;
			default:
				value = 1;
				break;
			}
		}
		else if(side.equals(Turn.WHITE)){
			switch(board[r2][c2]){
			case 'p':				
				value = -2;
				break;
			case 'r':				
				value = -4;
				break;
			case 'b':				
				value = -3;
				break;
			case 'q':				
				value = -5;
				break;
			case 'k':				
				value = -10;
				break;
			default:
				value = -1;
				break;				
			}
		}
		return value;
	}

	// controls piece's capturing
	public static int takenPiecePlacement(char[][] field, int r2, int c2) {
		
		int c3 = 9;
		
		switch(field[r2][c2]){
		case 'b':
			if(field[3][3]==(' ')){
				field[3][3] = 'B';
				c3 = 3;
			}
			else if(field[3][3]!=(' ')){
				field[3][6] = 'B';
				c3 = 6;
			}
			break;
		case 'p':
			if(field[3][4]==(' ')){
				field[3][4] = 'P';
				c3 = 4;
			}
			else if(field[3][4]!=(' ')){
				field[3][7] = 'P';
				c3 = 7;
			}
			break;
		case 'r':
			if(field[3][5]==(' ')){
				field[3][5] = 'R';
				c3 = 5;
			}
			else if(field[3][5]!=(' ')){
				field[3][8] = 'R';
				c3 = 8;
			}
			break;
		case 'q':
			if(field[3][4]==(' ')){
				field[3][4] = 'P';
				c3 = 4;
			}
			else if(field[3][4]!=(' ')){
				field[3][7] = 'P';
				c3 = 7;
			}
			break;						
		case 'B':
			if(field[0][3]==(' ')){
				field[0][3] = 'b';
				c3 = 3;
			}
			else if(field[0][3]!=(' ')){
				field[0][6] = 'b';
				c3 = 6;
			}
			break;
		case 'P':
			if(field[0][4]==(' ')){
				field[0][4] = 'p';
				c3 = 4;
			}
			else if(field[0][4]!=(' ')){
				field[0][7] = 'p';
				c3 = 7;
			}
			break;
		case 'R':
			if(field[0][5]==(' ')){
				field[0][5] = 'r';
				c3 = 5;
			}
			else if(field[0][5]!=(' ')){
				field[0][8] = 'r';
				c3 = 8;
			}
			break;
		case 'Q':
				if(field[0][4]==(' ')){
				field[0][4] = 'p';
				c3 = 4;
			}
			else if(field[0][4]!=(' ')){
				field[0][7] = 'p';
				c3 = 7;
			}
			break;
		}
		return c3;
	}

	// undo calculated move
	public static void undoMove(char[][] board, int r3, int c3){
		if(c3 != 9) {
			board[r3][c3] = ' ';
		}
	}
	
}
