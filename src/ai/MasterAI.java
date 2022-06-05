package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.Clocks;
import utilpack.Capture;
import utilpack.Examiner;

public class MasterAI extends ArtIntel{

	public MasterAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate("black", 6, Integer.MIN_VALUE+1, Integer.MAX_VALUE,false, Arrays.asList(root));
		Clocks.addNodes(nodesCount);
		return root.getValue();
	}

	// minimax with capture and check extensions
	private int calculate(String turn, int depth, int alpha, int beta, boolean node,
			List<Node> legalMoves) {
		
		if(turn.equals("white") && integrator.isLost(board)) {
			return -500;
		}		
		if(turn.equals("white") && MovesList.isRepeated(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MovesList.isRepeated(board, "white")){
			return 0;
		}		
		if(hash.isRepeated(board, turn, depth)){
			return 0;
		}		
		hash.addMove(board, turn, depth);
		
		if(Examiner.isBlackPositionWon(board, turn)){
			return 2000+(depth*100);	
		}
		if(Examiner.isWhitePositionWon(board, turn)){
			return -(2000+(depth*100));	
		}			
		if(Examiner.isCheck(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else {
				return 1000+(depth*100);				
			}
		}
	
		if(node==false & depth < 2){
			return evaluator.evaluationMaterial(board, false);
		}		
		if(depth == -3){
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
			int c3 = 9;
			
			node = false;
											
			if(turn.equals("black")){
				r3 = 0;
				
				if(board[r][c].equals("k")){
					node = Examiner.isCheck(board, "white");
				}
				
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
				
				if(node==false){
					node = (Capture.isExtension(temp, turn)
							||Capture.isKingPromoted(board, r2, c2, turn)
							||Examiner.isCheck(board, turn));
				}		
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "K" & (node ? depth > -2 : depth > 2)) {
					children = generator.generateMoves(board, "white");
					sorted = generator.sortMoves(board, children, "white", false);
					for(Node child: sorted) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}
				
				int value = calculate("white", depth-1, alpha, beta, node, sorted);
				if(value > alpha){
					alpha = value;
					scores.add(value);
					legalMoves.get(i).setValue(value);
				}										
			}				
			else{
				r3 = 3;
				
				if(board[r][c].equals("K")){
					node = Examiner.isCheck(board, "black");
				}
				
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
				
				if(node==false){
				node = (Capture.isExtension(temp, turn)
						||Capture.isKingPromoted(board, r2, c2, turn)
						||Examiner.isCheck(board, turn));
				}
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "k" & (node ? depth > -2 : depth > 2)) {
					children = generator.generateMoves(board, "black");
					sorted = generator.sortMoves(board, children, "black", false);
					for(Node child: sorted) {
						child.addParent(legalMoves.get(i));
					}
					legalMoves.get(i).addChildren(sorted);
				}
				
				int value = calculate("black", depth-1, alpha, beta, node, sorted);
				if(value < beta){
					beta = value;
					scores.add(value);
					legalMoves.get(i).setValue(value);
				}
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
				
				if(alpha >= beta){
					break;
				}
			}
		
		if(turn.equals("black")){
			return evaluator.alpha(scores, alpha, beta);
		}
		else{
			return evaluator.beta(scores, alpha, beta);
		}
	}
	
}
