package ai.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilpack.Capture;
import utilpack.Examiner;
import utilpack.MoveMaker;
import utilpack.Pieces;
import utilpack.Turn;

public class Generator {
	
	private final Evaluator evaluator = new Evaluator();

	public List<Node> generateMoves(String[][] board, Turn turn) {	
		
		ArrayList<Node> legal = new ArrayList<>();
		
		int r2, c2;

		if(turn.equals(Turn.BLACK)) {
			for(int i=3; i<9; i++) {
				if(!board[0][i].equals(" ")){
					for(int r=0; r<4; r++){
						for(int c=0; c<3; c++){
							if(board[r][c].equals(" ")){
								legal.add(new Node(0, i, r, c, turn));
							}
						}
					}
				}
			}
				
			for(int r=0; r<4; r++){
				for(int c=0; c<3; c++){					
					if(board[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pieces.BPAWN.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, turn))){
							legal.add(new Node(r, c, r2, c2, turn));
						}
					}				
					else if(board[r][c].equals("r")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("k")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("b")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("q")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BQUEEN.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}
				}
			}
		}		
		else if(turn.equals(Turn.WHITE)){	
			for(int i=3; i<9; i++) {
				if(!board[3][i].equals(" ")){
					for(int r=0; r<4; r++){
						for(int c=0; c<3; c++){
							if(board[r][c].equals(" ")){
								legal.add(new Node(3, i, r, c, turn));
							}
						}
					}
				}
			}
				
			for(int r=0; r<4; r++){
				for(int c=0; c<3; c++){					
					if(board[r][c].equals("P")){
						r2 = r-1;
						c2 = c;
						if((Pieces.WPAWN.isLegalMove(r, c, r2, c2))&&
						   (Capture.isCaptureLegal(board, r2, c2, turn))){
							legal.add(new Node(r, c, r2, c2, turn));
						}
					}
					else if(board[r][c].equals("R")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}
					else if(board[r][c].equals("K")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("B")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("Q")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.WQUEEN.isLegalMove(r, c, r2, c2))&&
							   (Capture.isCaptureLegal(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}
				}
			}
		}
		legal.trimToSize();
		return legal;
	}
	
	public List<Node> arrangeMoves(String[][] board, List<Node> legalMoves, Turn turn){
	
		for(int i=0; i<legalMoves.size(); i++){

			int r = legalMoves.get(i).getRowFrom();
			int c = legalMoves.get(i).getColumnFrom();
			int r2 = legalMoves.get(i).getRowTo();
			int c2 = legalMoves.get(i).getColumnTo();
			String temp;
			String promotion;
			int r3;
			Board state;
			int value;

			if(turn.equals(Turn.BLACK)){
				r3 = 0;
				state = MoveMaker.doBlackMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
								
				if(temp.equals("K")){
					value = 5000;	
				}
				else if(Examiner.isCheck(board, Turn.WHITE)){
					value = -5000;
				}
				else if(Examiner.isPromotionWins(board, Turn.BLACK)){
					value = 4000;
				}
				else if(Examiner.isPromotionWins(board, Turn.WHITE)){
					value = -4000;
				}
/*				else if(Examiner.isCheck(board, Turn.BLACK) &&
						!Examiner.isCheck(board, Turn.WHITE)) {
					value = 500;
				}*/
				else if(MovesList.isRepeated(board, Turn.BLACK)) {
					value = 0;
				}
				else{
					value = evaluator.evaluationMaterial(board, false)
							+ evaluator.evaluationPositional(board);
				}				
				legalMoves.get(i).setValue(value);
			}		
			else{
				r3 = 3;
				state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
						
				if(temp.equals("k")){
					value = -5000;	
				}
				else if(Examiner.isCheck(board, Turn.BLACK)){
					value = 5000;
				}
				else if(Examiner.isPromotionWins(board, Turn.WHITE)){
						value = -4000;
				}
				else if(Examiner.isPromotionWins(board, Turn.BLACK)){
					value = 4000;
				}
/*				else if(Examiner.isCheck(board, Turn.WHITE) &&
						!Examiner.isCheck(board, Turn.BLACK)) {
					value = -500;
				}*/
				else if(MovesList.isRepeated(board, Turn.WHITE)) {
					value = 0;
				}				
				else{
					value = evaluator.evaluationMaterial(board, false)
							+ evaluator.evaluationPositional(board);
				}				
				legalMoves.get(i).setValue(value);		
			}			
			int c3 = state.getC3();
			promotion = state.getPromotion();
			MoveMaker.undoAnyMove(board, temp, promotion, r, c, r2, c2, r3, c3);
		}
				
		if(turn.equals(Turn.BLACK)){Collections.sort(legalMoves, Collections.reverseOrder());}
		else{Collections.sort(legalMoves);}
		return legalMoves;
	}
	
	public List<Node> filterMoves(List<Node> sortedMoves, Turn turn){
		
		int limit = sortedMoves.get(0).getValue();
		return doFilter(sortedMoves, turn, limit);
	}
	
	public List<Node> filterMoves(String[][] board, List<Node> sortedMoves, Turn turn){
		
		int prev;
		if(Examiner.isCheck(board, Turn.WHITE)){prev = -1000;}
		else if(Examiner.isCheck(board, Turn.BLACK)){prev = 1000;}
		else{prev = evaluator.evaluationMaterial(board, false)
					+ evaluator.evaluationPositional(board);}
		
		return doFilter(sortedMoves, turn, prev);
	}
	
	private List<Node> doFilter(List<Node> sortedMoves, Turn turn, int value){
		
		if(turn.equals(Turn.BLACK)){sortedMoves.removeIf(e -> e.getValue() < value);}
		else{sortedMoves.removeIf(e -> e.getValue() > value);}
		return sortedMoves;
	}
	
}
