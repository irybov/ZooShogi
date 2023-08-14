package ai.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.component.Board;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Examiner;
import utilpack.MoveMaker;
import utilpack.Turn;

public class ActiveAI extends AI{

	public ActiveAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate(Turn.BLACK, 1, Integer.MIN_VALUE+1, Integer.MAX_VALUE, Arrays.asList(root));
		Clocks.addNodes(nodesCount);
		return root.getValue();
	}

	// minimax with vintage forward pruning
	private int calculate(Turn turn, int depth, int alpha, int beta, List<Node> legalMoves) {
		
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
		if(turn.equals(Turn.WHITE)){
			if(hash.isRepeated(board, turn, depth)){
				legalMoves = null;
				return 0;
			}		
			hash.addMove(board, turn, depth);
		}
		
		if(Examiner.isBlackPositionWin(board, turn)){
			legalMoves = null;
			return 2000+(100/depth);
		}
		if(Examiner.isWhitePositionWin(board, turn)){
			legalMoves = null;
			return -(2000+(100/depth));
		}		
		if(Examiner.isCheck(board, turn) && depth > 1){
			if(turn.equals(Turn.WHITE)){
				legalMoves = null;
				return -(1000+(100/depth));
			}
			else {
				legalMoves = null;
				return 1000+(100/depth);				
			}
		}
	
		if(depth == 8){
			legalMoves = null;
			return evaluator.evaluationMaterial(board, false)
					+ evaluator.evaluationPositional(board);
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
											
			if(turn.equals(Turn.BLACK)){
				r3 = 0;
				state = MoveMaker.doBlackMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
				
				List<Node> children = null;
				List<Node> sorted = null;
				List<Node> filtered = null;
				if(temp != "K" & depth < 7) {
					children = generator.generateMoves(board, Turn.WHITE);
					sorted = generator.arrangeMoves(board, children, Turn.WHITE);
					filtered = generator.filterMoves(board, sorted, Turn.WHITE);
					if(filtered.size()==0) {
						children = generator.generateMoves(board, Turn.WHITE);
						sorted = generator.arrangeMoves(board, children, Turn.WHITE);
						filtered = generator.filterMoves(sorted, Turn.WHITE);
					}
					for(Node child: filtered) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}

				int value = calculate(Turn.WHITE, depth+1, alpha, beta, sorted);
				if(value > alpha){
					alpha = value;
					scores.add(value);
					legalMoves.get(i).setValue(value);
				}									
			}				
			else{
				r3 = 3;
				state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
				
				List<Node> children = null;
				List<Node> sorted = null;
				List<Node> filtered = null;
				if(temp != "k" & depth < 7) {
					children = generator.generateMoves(board, Turn.BLACK);
					sorted = generator.arrangeMoves(board, children, Turn.BLACK);
					filtered = generator.filterMoves(board, sorted, Turn.BLACK);
					if(filtered.size()==0) {
						children = generator.generateMoves(board, Turn.BLACK);
						sorted = generator.arrangeMoves(board, children, Turn.BLACK);
						filtered = generator.filterMoves(sorted, Turn.BLACK);
					}
					for(Node child: filtered) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}
				
				int value = calculate(Turn.BLACK, depth+1, alpha, beta, sorted);
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
