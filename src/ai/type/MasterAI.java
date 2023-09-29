package ai.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.component.Board;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Capture;
import utilpack.Examiner;
import utilpack.MoveMaker;
import utilpack.Turn;

public class MasterAI extends AI{

	public MasterAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		try {
			latch.await();
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
		calculate(Turn.BLACK,1,Integer.MIN_VALUE+1,Integer.MAX_VALUE,false,Arrays.asList(root));
		Clocks.addNodes(nodesCount);
		Clocks.setScore(root.getValue());
		return root.getValue();
	}

	// minimax with capture and check extensions
	private int calculate(Turn turn, int depth, int alpha, int beta, boolean extend,
			List<Node> legalMoves) {
		
		if(turn.equals(Turn.WHITE) && integrator.isLost(board)) {
			legalMoves = null;
			return -1000;
		}		
		if(turn.equals(Turn.WHITE) && MovesList.isRepeated(board, Turn.BLACK)){
			legalMoves = null;
			return 0;
		}
		if((turn.equals(Turn.BLACK) && depth > 1) && MovesList.isRepeated(board, Turn.WHITE)){
			legalMoves = null;
			return 0;
		}		
		if(hash.isRepeated(board, turn, depth)){
			legalMoves = null;
			return 0;
		}		
		hash.addMove(board, turn, depth);
		
		if(depth > 1) {
			if(Examiner.isPositionWon(board, turn)){
				if(turn.equals(Turn.BLACK)){
					legalMoves = null;
					return 1000+(100/depth);
				}
				else {
					legalMoves = null;
					return -(1000+(100/depth));				
				}
			}			
			if(Examiner.isCheck(board, turn)){
				if(turn.equals(Turn.WHITE)){
					legalMoves = null;
					return -(1000+(100/depth));
				}
				else {
					legalMoves = null;
					return 1000+(100/depth);				
				}
			}
			if(Examiner.isPromotionWins(board, turn)){
				if(turn.equals(Turn.BLACK)){
					legalMoves = null;
					return 1000+(100/depth);
				}
				else {
					legalMoves = null;
					return -(1000+(100/depth));				
				}
			}
		}
		if(extend == false & depth > 5){
			legalMoves = null;
			return evaluator.evaluationMaterial(board, false);
		}		
		if(depth == 10){
			legalMoves = null;
			return evaluator.evaluationMaterial(board, false);
		}

		nodesCount += legalMoves.size();
		
		List<Integer> scores = new ArrayList<>(legalMoves.size());
		
		for(int i=0; i<legalMoves.size(); i++){

			int r = legalMoves.get(i).getRowFrom();
			int c = legalMoves.get(i).getColumnFrom();
			int r2 = legalMoves.get(i).getRowTo();
			int c2 = legalMoves.get(i).getColumnTo();
			String temp;
			String promotion;
			int r3;
			Board state;
			
			extend = false;
											
			if(turn.equals(Turn.BLACK)){				
				if(board[r][c].equals("k")){
					extend = Examiner.isCheck(board, Turn.WHITE);
				}
				
				r3 = 0;
				state = MoveMaker.doBlackMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
				
				if(extend == false){
					extend = (Capture.isExtension(temp, turn)
							|| Capture.isKingPromoted(board, r2, c2, turn)
							|| Examiner.isCheck(board, turn));
				}		
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "K" & (extend ? depth < 9 : depth < 5)) {
					children = generator.generateMoves(board, Turn.WHITE);
					sorted = generator.arrangeMoves(board, children, Turn.WHITE);
					for(Node child: sorted) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}
				
				int value = calculate(Turn.WHITE, depth+1, alpha, beta, extend, sorted);
				if(value > alpha){
					alpha = value;
					scores.add(value);
					legalMoves.get(i).setValue(value);
				}										
			}				
			else{				
				if(board[r][c].equals("K")){
					extend = Examiner.isCheck(board, Turn.BLACK);
				}
				
				r3 = 3;
				state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();

				if(extend == false){
				extend = (Capture.isExtension(temp, turn)
						|| Capture.isKingPromoted(board, r2, c2, turn)
						|| Examiner.isCheck(board, turn));
				}
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "k" & (extend ? depth < 9 : depth < 5)) {
					children = generator.generateMoves(board, Turn.BLACK);
					sorted = generator.arrangeMoves(board, children, Turn.BLACK);
					for(Node child: sorted) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}
				
				int value = calculate(Turn.BLACK, depth+1, alpha, beta, extend, sorted);
				if(value < beta){
					beta = value;
					scores.add(value);
					legalMoves.get(i).setValue(value);
				}
			}
			
			int c3 = state.getC3();
			promotion = state.getPromotion();
			MoveMaker.undoAnyMove(board, temp, promotion, r, c, r2, c2, r3, c3);
				
			if(alpha >= beta){
				break;
			}
		}
		
		if(turn.equals(Turn.BLACK)){
			return evaluator.alpha(scores, alpha, beta);
		}
		else{
			return evaluator.beta(scores, alpha, beta);
		}
	}
	
}
