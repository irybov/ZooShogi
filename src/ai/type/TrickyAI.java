package ai.type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import ai.component.Board;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Copier;
import utilpack.Examiner;
import utilpack.MoveMaker;
import utilpack.Turn;

public class TrickyAI extends AI{

	public TrickyAI(Node root, String[][] board) {
		super(root, board);
	}
	
	@Override
	public Integer call() {
		calculate(board, 1);
		Clocks.addNodes(nodesCount);
		Clocks.setScore(root.getValue());
		return root.getValue();
	}

	// breadth-searching trappy minimax
	private void calculate(String[][] field, int depth) {

		String[][] board = Copier.deepCopy(field);
		
		Queue<String[][]> input = new LinkedList<>();
		input.add(board);		
		Queue<String[][]> poses = new LinkedList<>();
		List<Node> legal;
		Queue<List<Node>> moves = new LinkedList<List<Node>>();
		moves.add(Arrays.asList(root));
		
		Deque<Node> stack = new ArrayDeque<>();
				
		while(depth < 6) {
			
			while(!input.isEmpty()) {
				poses.add(input.remove());
			}
			
			while(!poses.isEmpty()) {
				
				board = poses.remove();
				legal = new ArrayList<>(moves.remove());
				
				for(int i=0; i<legal.size(); i++){
					
					int r = legal.get(i).getRowFrom();
					int c = legal.get(i).getColumnFrom();
					int r2 = legal.get(i).getRowTo();
					int c2 = legal.get(i).getColumnTo();
					String temp = null;
					String promotion = null;
					int r3;
					Board state;
					
					if(depth % 2 == 1){
						r3 = 0;
						state = MoveMaker.doBlackMove(board, r, c, r2, c2);
//						board = state.getBoard();
						temp = state.getTemp();
							
						hash.addMove(board, Turn.BLACK, depth);
						
						if(temp.equals("K")){
							legal.get(i).setValue(2000+(100/depth));
						}
						else if(Examiner.isPromotionWins(board, Turn.BLACK) &&
								!Examiner.isCheck(board, Turn.WHITE)){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(MovesList.isRepeated(board, Turn.BLACK)) {
							legal.get(i).setValue(0);
						}
						else if(hash.isRepeated(board, Turn.BLACK, depth)){
							legal.get(i).setValue(0);
						}	
						else if(integrator.isLost(board)) {
							legal.get(i).setValue(-1000);
						}						
						else if(Examiner.isPromotionWins(board, Turn.WHITE)){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(Examiner.isCheck(board, Turn.WHITE)){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else{
							legal.get(i).setValue(evaluator.evaluationMaterial(board, false));
							if(depth < 5) {
								input.add(Copier.deepCopy(board));
								legal.get(i).addChildren(generator.generateMoves(board, Turn.WHITE));
								for(Node child: legal.get(i).getChidren()) {
									child.addParent(legal.get(i));
								}
								moves.add(legal.get(i).getChidren());
							}
						}
					}				
					else{
						r3 = 3;
						state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
//						board = state.getBoard();
						temp = state.getTemp();					
						
						if(temp.equals("k")){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else if(Examiner.isPromotionWins(board, Turn.WHITE) &&
								!Examiner.isCheck(board, Turn.BLACK)){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(MovesList.isRepeated(board, Turn.WHITE)) {
							legal.get(i).setValue(0);
						}
						else if(Examiner.isPromotionWins(board, Turn.BLACK)){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(Examiner.isCheck(board, Turn.BLACK)){
							legal.get(i).setValue(2000+(100/depth));
						}
						else{
							legal.get(i).setValue(evaluator.evaluationMaterial(board, false));
							if(depth < 5) {
								input.add(Copier.deepCopy(board));
								legal.get(i).addChildren(generator.generateMoves(board, Turn.BLACK));
								for(Node child: legal.get(i).getChidren()) {
									child.addParent(legal.get(i));
								}
								moves.add(legal.get(i).getChidren());
							}
						}
					}
					legal.get(i).setDepth(depth);
					stack.push(legal.get(i));
					
					int c3 = state.getC3();
					promotion = state.getPromotion();
					MoveMaker.undoAnyMove(board, temp, promotion, r, c, r2, c2, r3, c3);
				}
				legal = null;
			}		
			depth++;			
		}		
		input = null;
		poses = null;
		
		Node node = root;
		int total = 0;
    	while(node.hasChildren()) {
	    	if(node.getDepth() % 2 == 1) {
	    		Collections.sort(node.getChidren());
	    	}
	    	else{
	    		Collections.sort(node.getChidren(), Collections.reverseOrder());
	    	}
	    	node = node.getChidren().get(0);
	    	if(node.hasChildren()) {
	    		total += node.getValue();
	    	}
	    	else {
	    		root.setProfit(node.getValue());
	    	}
    		node.setTrappiness(-total);
    	}
    	root.setTrappiness(-total);
		
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
//        root.setProfit(root.getProfit() - root.getValue());
//        root.setValue(root.getValue() + root.getProfit()/10);
        
		nodesCount = stack.size();
	}
	
}
