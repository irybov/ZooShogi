package ai.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Capture;
import utilpack.Examiner;
import utilpack.Turn;

public class ActiveAI extends ArtIntel{

	public ActiveAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate(Turn.BLACK, 6, Arrays.asList(root));
		Clocks.addNodes(nodesCount);
		return root.getValue();
	}

	// minimax with vintage forward pruning
	private int calculate(Turn turn, int depth, List<Node> legalMoves) {
		
		if(turn.equals(Turn.WHITE) && integrator.isLost(board)) {
			return -500;
		}		
		if(turn.equals(Turn.WHITE) && MovesList.isRepeated(board, Turn.BLACK)){
			return 0;
		}
		if((turn.equals(Turn.BLACK) && depth < 6) && MovesList.isRepeated(board, Turn.WHITE)){
			return 0;
		}		
		if(turn.equals(Turn.WHITE)){
			if(hash.isRepeated(board, turn, depth)){
				return 0;
			}		
			hash.addMove(board, turn, depth);
		}
		
		if(Examiner.isBlackPositionWon(board, turn)){
			return 2000+(depth*100);
		}
		if(Examiner.isWhitePositionWon(board, turn)){
			return -(2000+(depth*100));
		}		
		if(Examiner.isCheck(board, turn) && depth < 6){
			if(turn.equals(Turn.WHITE)){
				return -(1000+(depth*100));
			}
			else {
				return 1000+(depth*100);				
			}
		}
	
		if(depth == 1){
			return evaluator.evaluationMaterial(board, false)
					+ evaluator.evaluationPositional(board);
		}

		nodesCount += legalMoves.size();
				
		ArrayList<Integer> scores = new ArrayList<>(legalMoves.size());
		
		for(int i=0; i<legalMoves.size(); i++){

			int r = legalMoves.get(i).getRowFrom();
			int c = legalMoves.get(i).getColumnFrom();
			int r2 = legalMoves.get(i).getRowTo();
			int c2 = legalMoves.get(i).getColumnTo();
			String temp;
			String promotion;
			int r3;
			int c3 = 9;
											
			if(turn.equals(Turn.BLACK)){
				r3 = 0;
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
				
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "K" & depth > 2) {
					children = generator.generateMoves(board, Turn.WHITE);
					sorted = generator.sortMoves(board, children, Turn.WHITE, true);
					for(Node child: sorted) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}

				int value = calculate(Turn.WHITE, depth-1, sorted);
					scores.add(value);
					legalMoves.get(i).setValue(value);										
			}				
			else{
				r3 = 3;
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
				
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "k" & depth > 2) {
					children = generator.generateMoves(board, Turn.WHITE);
					sorted = generator.sortMoves(board, children, Turn.WHITE, false);
					for(Node child: sorted) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}
				
				int value = calculate(Turn.BLACK, depth-1, sorted);
					scores.add(value);
					legalMoves.get(i).setValue(value);
			}
			
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
		
		if(turn.equals(Turn.BLACK)){
			return evaluator.max(scores);
		}
		else{
			return evaluator.min(scores);
		}
	}
	
}
