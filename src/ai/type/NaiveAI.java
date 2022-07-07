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

public class NaiveAI extends ArtIntel{

	public NaiveAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate(Turn.BLACK, 1, Arrays.asList(root));
		Clocks.addNodes(nodesCount);
		return root.getValue()/10;
	}

	private int calculate(Turn turn, int depth, List<Node> legalMoves) {
		
		if(turn.equals(Turn.WHITE) && integrator.isLost(board)) {
			return -6000;
		}		
		if(turn.equals(Turn.WHITE) && MovesList.isRepeated(board, Turn.BLACK)){
			return 0;
		}
		if((turn.equals(Turn.BLACK) && depth > 1) && MovesList.isRepeated(board, Turn.WHITE)){
			return 0;
		}		
		if(turn.equals(Turn.WHITE)){
			if(hash.isRepeated(board, turn, depth)){
				return 0;
			}		
			hash.addMove(board, turn, depth);
		}
		
		if(Examiner.isBlackPositionWon(board, turn)){
			return 100*depth;
		}
		if(Examiner.isWhitePositionWon(board, turn)){
			return -(1000/depth);
		}		
		if(Examiner.isCheck(board, turn) && depth > 1){
			if(turn.equals(Turn.WHITE)){
				return -(1000/depth);
			}
			else {
				return 100*depth;				
			}
		}
		
		if(depth == 6){
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
				if(board[r][c].equals("P") & r==1){
					promotion = "P";
					c3 = Capture.takenPiecePlacement(board, r2, c2);
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
					c3 = Capture.takenPiecePlacement(board, r2, c2);
					temp = board[r2][c2];
					board[r2][c2] = board[r][c];
					board[r][c] = " ";	
				}
				
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
		else {
			return evaluator.expected(scores);
		}
	}
	
}
