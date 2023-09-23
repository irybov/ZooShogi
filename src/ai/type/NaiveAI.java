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

public class NaiveAI extends AI{

	public NaiveAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate(Turn.BLACK, 1, Arrays.asList(root));
		Clocks.addNodes(nodesCount);
		Clocks.setScore(root.getValue()/10);
		return root.getValue()/10;
	}

	// expectimax gambling algorithm
	private int calculate(Turn turn, int depth, List<Node> legalMoves) {
		
		if(turn.equals(Turn.WHITE) && integrator.isLost(board)) {
			legalMoves = null;
			return -6000/depth;
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
			return 100*depth;
		}
		if(Examiner.isWhitePositionWin(board, turn)){
			legalMoves = null;
			return -6000/depth;
		}		
		if(Examiner.isCheck(board, turn) && depth > 1){
			if(turn.equals(Turn.WHITE)){
				legalMoves = null;
				return -6000/depth;
			}
			else {
				legalMoves = null;
				return 100*depth;				
			}
		}
		
		if(depth == 6){
			legalMoves = null;
			return evaluator.evaluationMaterial(board, true)/depth;
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
				if(temp != "K" & depth < 5) {
					children = generator.generateMoves(board, Turn.WHITE);
					for(Node child: children) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(children);
				}
				
				int value = calculate(Turn.WHITE, depth+1, children);
					scores.add(value);
					legalMoves.get(i).setValue(value);
			}				
			else{
				r3 = 3;
				state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
				board = state.getBoard();
				temp = state.getTemp();
				
				List<Node> children = null;
				if(temp != "k" & depth < 5) {
					children = generator.generateMoves(board, Turn.BLACK);
					for(Node child: children) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(children);
				}
				
				int value = calculate(Turn.BLACK, depth+1, children);
					scores.add(value);
					legalMoves.get(i).setValue(value);
			}
			
			int c3 = state.getC3();
			promotion = state.getPromotion();
			MoveMaker.undoAnyMove(board, temp, promotion, r, c, r2, c2, r3, c3);
		}
		
		if(turn.equals(Turn.BLACK)){
			return evaluator.max(scores);
		}
		else {
			return evaluator.expected(scores);
		}
	}
	
}
