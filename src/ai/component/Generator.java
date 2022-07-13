package ai.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilpack.Verifier;
import utilpack.Direction;
import utilpack.Examiner;
import utilpack.MoveMaker;
import utilpack.Piece;
import utilpack.Turn;

public class Generator {
	
	private final Evaluator evaluator = new Evaluator();

	public List<Node> generateMoves(char[][] board, Turn turn) {
		
		List<Node> legalMoves = new ArrayList<>();
		List<Direction> directions;
		
		int r2, c2;

		if(turn.equals(Turn.BLACK)) {
			for(int i=3; i<9; i++) {
				if(board[0][i]!=(' ')){
					for(int r=0; r<4; r++){
						for(int c=0; c<3; c++){
							if(board[r][c]==(' ')){
								legalMoves.add(new Node(0, i, r, c, turn));
							}
						}
					}
				}
			}
			
			for(int r=0; r<4; r++){
				for(int c=0; c<3; c++){					
					if(board[r][c]==('p')){
						r2 = r+1;
						c2 = c;
						if((Piece.BPAWN.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, turn))){
							legalMoves.add(new Node(r, c, r2, c2, turn));
						}
					}				
					else if(board[r][c]==('r')){
						directions = Verifier.getDirections('r');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.ROOK.isLegalMove(r, c, r2, c2))&&
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
							}							
						}
					}				
					else if(board[r][c]==('k')){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
								if(r2==0 && c2==0) {}
							if((Piece.KING.isLegalMove(r, c, r2, c2))&&
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
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
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
							}							
						}
					}				
					else if(board[r][c]==('q')){					
						directions = Verifier.getDirections('q');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.BQUEEN.isLegalMove(r, c, r2, c2))&&
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
							}							
						}
					}
				}
			}
		}		
		else if(turn.equals(Turn.WHITE)){	
			for(int i=3; i<9; i++) {
				if(board[3][i]!=(' ')){
					for(int r=0; r<4; r++){
						for(int c=0; c<3; c++){
							if(board[r][c]==(' ')){
								legalMoves.add(new Node(3, i, r, c, turn));
							}
						}
					}
				}
			}
				
			for(int r=0; r<4; r++){
				for(int c=0; c<3; c++){					
					if(board[r][c]==('P')){
						r2 = r-1;
						c2 = c;
						if((Piece.WPAWN.isLegalMove(r, c, r2, c2))&&
						   (Verifier.isCaptureLegal(board, r2, c2, turn))){
							legalMoves.add(new Node(r, c, r2, c2, turn));
						}
					}
					else if(board[r][c]==('R')){
						directions = Verifier.getDirections('R');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.ROOK.isLegalMove(r, c, r2, c2))&&
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
//								}
							}							
						}
					}
					else if(board[r][c]==('K')){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
								if(r2==0 && c2==0) {}
							if((Piece.KING.isLegalMove(r, c, r2, c2))&&
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
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
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
							}							
						}
					}				
					else if(board[r][c]==('Q')){					
						directions = Verifier.getDirections('Q');
						for(Direction point : directions){
							r2 = r + point.getX();
							c2 = c + point.getY();
							if((Piece.WQUEEN.isLegalMove(r, c, r2, c2))&&
							   (Verifier.isCaptureLegal(board, r2, c2, turn))){
								legalMoves.add(new Node(r, c, r2, c2, turn));
							}							
						}
					}
				}
			}
		}
		return legalMoves;
	}
	
	public List<Node> sortMoves(char[][] board, List<Node> legalMoves, Turn turn, boolean prune){
		
		List<Node> sortedMoves = new ArrayList<>(legalMoves.size());
		
		int prev;
			if(Examiner.isCheck(board, Turn.WHITE)){
				prev = -1000;
			}
			else if(Examiner.isCheck(board, Turn.BLACK)){
				prev = 1000;
			}
			else{
				prev = evaluator.evaluationMaterial(board, false)
						+ evaluator.evaluationPositional(board);
			}
	
		for(int i=0; i<legalMoves.size(); i++){

			int r = legalMoves.get(i).getRowFrom();
			int c = legalMoves.get(i).getColumnFrom();
			int r2 = legalMoves.get(i).getRowTo();
			int c2 = legalMoves.get(i).getColumnTo();
			char temp;
			char promotion;
			int r3;
			Board state;

			if(turn.equals(Turn.BLACK)){						
				r3 = 0;
				state = MoveMaker.doBlackMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
				
				int value;				
				if(temp==('K')){
					value = 5000;	
				}
				else if(Examiner.isPromotionWon(board, Turn.BLACK) &&
						!Examiner.isCheck(board, Turn.WHITE)){
					value = 4000;
				}
/*				else if(Examiner.isCheck(board, Turn.BLACK) &&
						!Examiner.isCheck(board, Turn.WHITE)) {
					value = 500;
				}*/
				else if(MovesList.isRepeated(board, Turn.BLACK)) {
					value = 0;
				}
				else if(Examiner.isPromotionWon(board, Turn.WHITE)){
					value = -4000;
				}
				else if(Examiner.isCheck(board, Turn.WHITE)){
					value = -5000;
				}
				else{
/*					if(prune) {
						value = evaluator.evaluationMaterial(board, false)
								+ evaluator.evaluationPositional(board);
					}
					else {
						value = evaluator.evaluationMaterial(board, false);
					}*/
					value = evaluator.evaluationMaterial(board, false)
							+ evaluator.evaluationPositional(board);
				}				
				legalMoves.get(i).setValue(value);
				sortedMoves.add(legalMoves.get(i));
			}		
			else{					
				r3 = 3;
				state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
		
				int value;				
				if(temp==('k')){
					value = -5000;	
				}
				else if(Examiner.isPromotionWon(board, Turn.WHITE) &&
						!Examiner.isCheck(board, Turn.BLACK)){
						value = -4000;
				}
/*				else if(Examiner.isCheck(board, Turn.WHITE) &&
						!Examiner.isCheck(board, Turn.BLACK)) {
					value = -500;
				}*/
				else if(MovesList.isRepeated(board, Turn.WHITE)) {
					value = 0;
				}
				else if(Examiner.isPromotionWon(board, Turn.BLACK)){
						value = 4000;
				}
				else if(Examiner.isCheck(board, Turn.BLACK)){
					value = 5000;
				}				
				else{
/*					if(prune) {
						value = evaluator.evaluationMaterial(board, false)
								+ evaluator.evaluationPositional(board);
					}
					else {
						value = evaluator.evaluationMaterial(board, false);
					}*/
					value = evaluator.evaluationMaterial(board, false)
							+ evaluator.evaluationPositional(board);
				}				
				legalMoves.get(i).setValue(value);
				sortedMoves.add(legalMoves.get(i));		
			}			
			int c3 = state.getC3();
			promotion = state.getPromotion();
			MoveMaker.undo(board, temp, promotion, r, c, r2, c2, r3, c3);
		}
				
		if(turn.equals(Turn.BLACK)){
			Collections.sort(sortedMoves, Collections.reverseOrder());
			if(prune){
				sortedMoves.removeIf(e -> e.getValue() < prev);
			}
		}
		else{
			Collections.sort(sortedMoves);
			if(prune){
				sortedMoves.removeIf(e -> e.getValue() > prev);
			}
		}
		return sortedMoves;
	}
	
	public List<Node> filterMoves(List<Node> sortedMoves, Turn turn){
		
		int limit = sortedMoves.get(0).getValue();
		
		if(turn.equals(Turn.BLACK)){
			sortedMoves.removeIf(e -> e.getValue() < limit);			
		}
		else {
			sortedMoves.removeIf(e -> e.getValue() > limit);			
		}		
		return sortedMoves;
	}
	
}
