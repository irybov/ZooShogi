package utilpack;

import ai.component.Board;

public final class MoveMaker {

	private MoveMaker() {}
	
	public static Board doBlackMove(String[][] board, int ... args) {
		
		int r = args[0];
		int c = args[1];
		int r2 = args[2];
		int c2 = args[3];
		String temp;
		String promotion;
		int c3 = 9;
		
		if(board[r][c].equals("p") & r==2){
			promotion = "p";
			if(!board[r][c].equals(" ")) {
				c3 = Capture.takenPiecePlacement(board, r2, c2);
			}
			temp = board[r2][c2];
			board[r2][c2] = "q";
			board[r][c] = " ";	
		}
		else if(r==0 & c > 2){
			promotion = " ";
			temp = " ";
			board[r2][c2] = board[r][c];
			board[r][c] = " ";
		}
		else{
			promotion = " ";
			if(!board[r][c].equals(" ")) {
				c3 = Capture.takenPiecePlacement(board, r2, c2);
			}
			temp = board[r2][c2];
			board[r2][c2] = board[r][c];
			board[r][c] = " ";	
		}
		
		return new Board(board, temp, promotion, c3);
	}
	
	public static Board doWhiteMove(String[][] board, int ... args) {
		
		int r = args[0];
		int c = args[1];
		int r2 = args[2];
		int c2 = args[3];
		String temp;
		String promotion;
		int c3 = 9;
		
		if(board[r][c].equals("P") & r==1){
			promotion = "P";
			if(!board[r][c].equals(" ")) {
				c3 = Capture.takenPiecePlacement(board, r2, c2);
			}
			temp = board[r2][c2];
			board[r2][c2] = "Q";
			board[r][c] = " ";	
		}
		else if(r==3 & c > 2){
			promotion = " ";
			temp = " ";
			board[r2][c2] = board[r][c];
			board[r][c] = " ";
		}
		else{
			promotion = " ";
			if(!board[r][c].equals(" ")) {
				c3 = Capture.takenPiecePlacement(board, r2, c2);
			}
			temp = board[r2][c2];
			board[r2][c2] = board[r][c];
			board[r][c] = " ";	
		}
		
		return new Board(board, temp, promotion, c3);
	}
	
	public static void undoAnyMove(String[][] board, String temp, String promotion, int ... args) {
		
		int r = args[0];
		int c = args[1];
		int r2 = args[2];
		int c2 = args[3];
		int r3 = args[4];
		int c3 = args[5];
		
		if(promotion.equals("p")){
			board[r][c] = "p";
		}
		else if(promotion.equals("P")){
			board[r][c] = "P";
		}
		else{
			board[r][c] = board[r2][c2];
		}
		board[r2][c2] = temp;
		Capture.undoMove(board, r3, c3);
	}
	
}
