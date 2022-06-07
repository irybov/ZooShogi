package ai.type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Capture;
import utilpack.Copier;
import utilpack.Examiner;
import utilpack.Turn;

public class TrickyAI extends ArtIntel{

	public TrickyAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate(board, 1);
		Clocks.addNodes(nodesCount);
		return root.getValue();
	}

	// breadth-searching trappy minimax
	private void calculate(String[][] field, int depth) {

		String[][] board = Copier.deepCopy(field);
		
		Queue<String[][]> input = new LinkedList<>();
		input.add(board);		
		Queue<String[][]> poses = new LinkedList<>();
		List<Node> legal;
		
		Deque<Node> stack = new ArrayDeque<>();
		
		Queue<List<Node>> moves = new LinkedList<List<Node>>();
		
		while(depth < 6) {
			
			while(!input.isEmpty()) {
			poses.add(input.remove());
			}
			
			while(!poses.isEmpty()) {
				board = poses.remove();
				
				if(depth == 1){
					legal = new ArrayList<>(1);
					legal.add(root);
				}
				else {
					legal = new ArrayList<>(moves.remove());
				}
			
				for(int i=0; i<legal.size(); i++){	
					int r = legal.get(i).getRowFrom();
					int c = legal.get(i).getColumnFrom();
					int r2 = legal.get(i).getRowTo();
					int c2 = legal.get(i).getColumnTo();
					String temp = null;
					String promotion = null;
					int r3;
					int c3 = 9;
					
					if(depth % 2 == 1){
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
							
						hash.addMove(board, Turn.BLACK, depth+4);
						
						if(temp.equals("K")){
							legal.get(i).setValue(2000+(100/depth));
						}
						else if(Examiner.isPromotionWon(board, Turn.BLACK) &&
								!Examiner.isCheck(board, Turn.WHITE)){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(MovesList.isRepeated(board, Turn.BLACK)) {
							legal.get(i).setValue(0);
						}
						else if(hash.isRepeated(board, Turn.BLACK, depth-4)){
							legal.get(i).setValue(0);
						}	
						else if(integrator.isLost(board)) {
							legal.get(i).setValue(-500);							
						}						
						else if(Examiner.isPromotionWon(board, Turn.WHITE)){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(Examiner.isCheck(board, Turn.WHITE)){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else{
							legal.get(i).setValue(evaluator.evaluationMaterial(board, false));
							if(depth < 5) {
								input.add(Copier.deepCopy(board));
								legal.get(i).addChildren
								(generator.generateMoves(board, Turn.WHITE));
								for(Node child: legal.get(i).getChidren()) {
									child.addParent(legal.get(i));
								}
								moves.add(legal.get(i).getChidren());
							}
						}
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
						
						if(temp.equals("k")){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else if(Examiner.isPromotionWon(board, Turn.WHITE) &&
								!Examiner.isCheck(board, Turn.BLACK)){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(MovesList.isRepeated(board, Turn.WHITE)) {
							legal.get(i).setValue(0);
						}
						else if(Examiner.isPromotionWon(board, Turn.BLACK)){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(Examiner.isCheck(board, Turn.BLACK)){
							legal.get(i).setValue(2000+(100/depth));
						}
						else{
							legal.get(i).setValue(evaluator.evaluationMaterial(board, false));
							if(depth < 5) {
								input.add(Copier.deepCopy(board));
								legal.get(i).addChildren
								(generator.generateMoves(board, Turn.BLACK));
								for(Node child: legal.get(i).getChidren()) {
									child.addParent(legal.get(i));
								}
								moves.add(legal.get(i).getChidren());
							}
						}
					}
					legal.get(i).setDepth(depth);
					stack.push(legal.get(i));
					
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
				legal = null;
			}		
			depth++;			
		}
		
		legal = null;
		input = null;
		poses = null;
		
		List<Integer> scores;

        for (Node move: stack) {
        	if(move.hasChildren()) {
        		scores = new ArrayList<>(move.getChidren().size());
        		for(Node child: move.getChidren()) {
        			scores.add(child.getValue());
        		}
                	if(move.getDepth() % 2 == 1) {
                		move.setValue(evaluator.min(scores));
                	}
                	else{
                		move.setValue(evaluator.max(scores));
                	}
        	}
        }        
        for (Node move: stack) {
        	if(!move.hasChildren()) {        	
        		int x = move.getValue();
        		int y = 0;
        		if(move.hasParent()) {
        			Node current = move.getParent();
	        		while(current.hasParent()) {
	        			y += current.getValue();
	        			current = current.getParent();
	        		}
        		}
        		int newTrap = x-y;
        		if(root.getTrappiness() < newTrap) {
        			root.setTrappiness(newTrap);
        		}
        	}
        }        
		nodesCount = stack.size();
	}
	
}
