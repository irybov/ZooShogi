package ai.component;

import java.util.List;

import utilpack.Capture;
import utilpack.Pieces;
import utilpack.Turn;

public class Evaluator {

	public int evaluationMaterial(String[][] board, boolean exp) {
		
		int score = 0;

		for(int r=0; r<4; r++){
			for(int c=0; c<board[r].length; c++){
				if(!board[r][c].equals(" ")){
					switch(board[r][c]){
					case "p":
						score += exp ? Pieces.BPAWN.getValue()*10 : Pieces.BPAWN.getValue();
						break;
					case "r":
						score += exp ? Pieces.ROOK.getValue()*10 : Pieces.ROOK.getValue();
						break;
					case "b":
						score += exp ? Pieces.BISHOP.getValue()*10 : Pieces.BISHOP.getValue();
						break;
					case "q":
						score += exp ? Pieces.BQUEEN.getValue()*10 : Pieces.BQUEEN.getValue();
						break;
					case "P":
						score += -Pieces.WPAWN.getValue();
						break;
					case "R":
						score += -Pieces.ROOK.getValue();
						break;
					case "B":
						score += -Pieces.BISHOP.getValue();
						break;
					case "Q":
						score += -Pieces.WQUEEN.getValue();
						break;
					}
				}
			}
		}
		return score;
	}

	public int evaluationPositional(String[][] board) {		
		
		int score = 0;
		int r,c,r2,c2;
	
		for(r=0; r<4; r++){
			for(c=0; c<3; c++){					
				if(board[r][c].equals("p")){
					r2 = r+1;
					c2 = c;
					if((Pieces.BPAWN.isLegalMove(r, c, r2, c2))&&
					   (Capture.isCaptureLegal(board, r2, c2, Turn.BLACK))){
						score += (Capture.attackBonusesCount(board, r2, c2, Turn.BLACK));
					}
				}
				
				else if(board[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.BLACK));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.BLACK));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.BLACK));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BQUEEN.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.BLACK));
							}
						}							
					}
				}
	
				else if(board[r][c].equals("P")){
					r2 = r-1;
					c2 = c;
					if((Pieces.WPAWN.isLegalMove(r, c, r2, c2))&&
					   (Capture.isCaptureLegal(board, r2, c2, Turn.WHITE))){
						score += (Capture.attackBonusesCount(board, r2, c2, Turn.WHITE));
					}
				}
				
				else if(board[r][c].equals("R")){				
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.WHITE));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("K")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.WHITE));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("B")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.WHITE));
							}
						}							
					}
				}
	
				else if(board[r][c].equals("Q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.WQUEEN.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Capture.attackBonusesCount(board, r2, c2, Turn.WHITE));
							}
						}							
					}
				}
			}
		}						
		return score;
	}

	public int max(List<Integer> scores) {		
		return scores.stream().reduce(Integer.MIN_VALUE+1, Integer::max);
	}	
	
	public int min(List<Integer> scores) {		
		return scores.stream().reduce(Integer.MAX_VALUE, Integer::min);
	}
	
	public int expected(List<Integer> scores) {		
		return scores.stream().mapToInt(x -> x).sum() / scores.size();	
	}
	
	public int alpha(List<Integer> scores, int alpha, int beta) {
		
		for(int i=0; i<scores.size(); i++){
			if (scores.get(i) > alpha){
				alpha = scores.get(i);
			}
			if(alpha >= beta){
				break;
				}
		}
		return alpha;
	}
	
	public int beta(List<Integer> scores, int alpha, int beta) {
		
		for(int i=0; i<scores.size(); i++){
			if (scores.get(i) < beta){
				beta = scores.get(i);
			}
			if(alpha >= beta){
				break;
				}
		}
		return beta;
	}
	
}
