package ai.component;

import java.util.List;

import utilpack.Verifier;
import utilpack.Direction;
import utilpack.Piece;
import utilpack.Turn;

public class Evaluator {

	public int evaluationMaterial(char[][] board, boolean exp) {
		
		int score = 0;

		for(int r=0; r<4; r++){
			for(int c=0; c<board[r].length; c++){
				if(board[r][c]!=(' ')){
					switch(board[r][c]){
					case 'p':
						score += exp ? Piece.BPAWN.getValue()*10 : Piece.BPAWN.getValue();
						break;
					case 'r':
						score += exp ? Piece.ROOK.getValue()*10 : Piece.ROOK.getValue();
						break;
					case 'b':
						score += exp ? Piece.BISHOP.getValue()*10 : Piece.BISHOP.getValue();
						break;
					case 'q':
						score += exp ? Piece.BQUEEN.getValue()*10 : Piece.BQUEEN.getValue();
						break;
					case 'P':
						score += -Piece.WPAWN.getValue();
						break;
					case 'R':
						score += -Piece.ROOK.getValue();
						break;
					case 'B':
						score += -Piece.BISHOP.getValue();
						break;
					case 'Q':
						score += -Piece.WQUEEN.getValue();
						break;
					}
				}
			}
		}
		return score;
	}

	public int evaluationPositional(char[][] board) {
		
		List<Direction> directions;
		
		int score = 0;
		int r,c,r2,c2;
	
		for(r=0; r<4; r++){
			for(c=0; c<3; c++){					
				if(board[r][c]==('p')){
					r2 = r+1;
					c2 = c;
					if((Piece.BPAWN.isLegalMove(r, c, r2, c2))&&
					   (Verifier.isCaptureLegal(board, r2, c2, Turn.BLACK))){
						score += (Verifier.attackBonusesCount(board, r2, c2, Turn.BLACK));
					}
				}
				
				else if(board[r][c]==('r')){					
					directions = Verifier.getDirections('r');
					for(Direction point : directions){
						r2 = r + point.getX();
						c2 = c + point.getY();
						if((Piece.ROOK.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.BLACK));
						}							
					}
				}
				
				else if(board[r][c]==('k')){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
							if(r2==0 && c2==0) {}
						if((Piece.KING.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.BLACK));
							}
						}							
					}
				}
				
				else if(board[r][c]==('b')){					
					directions = Verifier.getDirections('b');
					for(Direction point : directions){
						r2 = r + point.getX();
						c2 = c + point.getY();
						if((Piece.BISHOP.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.BLACK));
						}							
					}
				}
				
				else if(board[r][c]==('q')){					
					directions = Verifier.getDirections('q');
					for(Direction point : directions){
						r2 = r + point.getX();
						c2 = c + point.getY();
						if((Piece.BQUEEN.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.BLACK))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.BLACK));
						}							
					}
				}
	
				else if(board[r][c]==('P')){
					r2 = r-1;
					c2 = c;
					if((Piece.WPAWN.isLegalMove(r, c, r2, c2))&&
					   (Verifier.isCaptureLegal(board, r2, c2, Turn.WHITE))){
						score += (Verifier.attackBonusesCount(board, r2, c2, Turn.WHITE));
					}
				}
				
				else if(board[r][c]==('R')){				
					directions = Verifier.getDirections('R');
					for(Direction point : directions){
						r2 = r + point.getX();
						c2 = c + point.getY();
						if((Piece.ROOK.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.WHITE));
						}							
					}
				}
				
				else if(board[r][c]==('K')){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
							if(r2==0 && c2==0) {}
						if((Piece.KING.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.WHITE));
							}
						}							
					}
				}
				
				else if(board[r][c]==('B')){					
					directions = Verifier.getDirections('B');
					for(Direction point : directions){
						r2 = r + point.getX();
						c2 = c + point.getY();
						if((Piece.BISHOP.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.WHITE));
						}							
					}
				}
	
				else if(board[r][c]==('Q')){					
					directions = Verifier.getDirections('Q');
					for(Direction point : directions){
						r2 = r + point.getX();
						c2 = c + point.getY();
						if((Piece.WQUEEN.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, Turn.WHITE))){
							score += (Verifier.attackBonusesCount(board, r2, c2, Turn.WHITE));
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
		return scores.stream().reduce(0, Integer::sum) / scores.size();	
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
