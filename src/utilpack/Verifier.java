package utilpack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Verifier {

	private Verifier() {}
	
	private final static Set<Character> blackPieces = new HashSet<>(Arrays.asList('b','k','p','q','r'));
	private final static Set<Character> whitePieces = new HashSet<>(Arrays.asList('B','K','P','Q','R'));
	
	private final static Map<Character, List<Direction>> DIRECTIONS;
	static{
		DIRECTIONS = new HashMap<>(8);
		DIRECTIONS.put('b', Arrays.asList(Direction.NW, Direction.NE, Direction.SW, Direction.SE));
		DIRECTIONS.put('p', Arrays.asList(Direction.SOUTH));
		DIRECTIONS.put('r', Arrays.asList(Direction.NORTH, Direction.WEST,
									  Direction.SOUTH, Direction.EAST));
		DIRECTIONS.put('q', Arrays.asList(Direction.NORTH, Direction.WEST,
					 				  Direction.SOUTH, Direction.EAST, Direction.SW, Direction.SE));
		DIRECTIONS.put('B', Arrays.asList(Direction.NW, Direction.NE, Direction.SW, Direction.SE));
		DIRECTIONS.put('P', Arrays.asList(Direction.NORTH));		
		DIRECTIONS.put('R', Arrays.asList(Direction.NORTH, Direction.WEST,
									  Direction.SOUTH, Direction.EAST));
		DIRECTIONS.put('Q', Arrays.asList(Direction.NORTH, Direction.WEST,
					 				  Direction.SOUTH, Direction.EAST, Direction.NW, Direction.NE));
	}
	public static List<Direction> getDirections(char piece){
		return DIRECTIONS.get(piece);
	}

	// checks if capture is possible
	public static boolean isMoveLegal(char[][] board, int r2, int c2, Turn turn) {
		
		if(turn.equals(Turn.WHITE)&(blackPieces.contains(board[r2][c2])
				|| (board[r2][c2]==(' ')))) {
			return true;
		}
		else if(turn.equals(Turn.BLACK)&(whitePieces.contains(board[r2][c2])
				|| (board[r2][c2]==(' ')))) {
			return true;
		}
		return false;
	}

	// checks capture extensions
	public static boolean isCapture(char piece, Turn side) {
		
		if(side.equals(Turn.WHITE) & blackPieces.contains(piece)) {
			return true;
		}
		else if(side.equals(Turn.BLACK) & whitePieces.contains(piece)) {
			return true;
		}
		return false;
	}

	// checks current king's state
	public static boolean isKingUnderCheck(char[][] field, int r2, int c2, Turn side) {
		
		if(side.equals(Turn.WHITE) & field[r2][c2]==('k')) {
			return true;
		}
		else if(side.equals(Turn.BLACK) & field[r2][c2]==('K')) {
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
