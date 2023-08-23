package ai.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ai.component.Board;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Copier;
import utilpack.Examiner;
import utilpack.Matrix;
import utilpack.MoveMaker;
import utilpack.Turn;

public class DiceyAI extends AI {

	public DiceyAI(Node root, String[][] board) {
		super(root, board);
	}
	
	private final InternalHash hash = new InternalHash();
	private final Random random = new Random();
	
	private class InternalHash {

		Set<String> blackMoves = new HashSet<>();
		Set<String> whiteMoves = new HashSet<>();	
		
		// fills game moves list
		private void addMove(String[][] field, Turn turn) {
			
			String hash = Matrix.makeKey(field);
			
			if(turn.equals(Turn.BLACK)) {
				blackMoves.add(hash);
			}
			else if(turn.equals(Turn.WHITE)){
				whiteMoves.add(hash);			
			}
			
		}

		//checks 3-times repetition
		private boolean isRepeated(String[][] field, Turn turn) {
			
			String hash = Matrix.makeKey(field);
				
			if(turn.equals(Turn.BLACK)) {
				return(blackMoves.contains(hash));
			}
			else{
				return(whiteMoves.contains(hash));			
			}
			
		}
		
		private void clear() {		
			blackMoves.clear();
			whiteMoves.clear();
		}
		
	}

	@Override
	public Integer call() {
		root.setValue(prepare());
		Clocks.addNodes(nodesCount);
		return root.getValue();
	}
	
	private int prepare() {
		
		int r = root.getRowFrom();
		int c = root.getColumnFrom();
		int r2 = root.getRowTo();
		int c2 = root.getColumnTo();

		MoveMaker.doBlackMove(board, r, c, r2, c2);
		
		List<Node> children = generator.generateMoves(board, Turn.WHITE);
		nodesCount += children.size();
		root.addChildren(children);
		
		for(Node child : children) {
			child.addParent(root);
			child.setValue(calculate(child));
			hash.clear();
		}
		
//		int sum = children.stream().mapToInt(Node::getValue).sum();		
//		return sum / children.size();
		OptionalInt result = children.stream().mapToInt(Node::getValue).min();
		return result.getAsInt();
	}
	
	// monte-carlo algorithm
	private int calculate(Node vertex) {

		int depth = 1;
		int result = 1000;
		Turn turn;
		Node move = vertex;
		String[][] board = Copier.deepCopy(this.board);
		
		while(true) {
		
			turn = move.getSide();
			
			int r = move.getRowFrom();
			int c = move.getColumnFrom();
			int r2 = move.getRowTo();
			int c2 = move.getColumnTo();
			String temp = null;
			Board state = null;
			Node child = null;

			if(hash.isRepeated(board, turn)) {return 0;}
			hash.addMove(board, turn);
			nodesCount++;

			if(turn.equals(Turn.BLACK)){

				state = MoveMaker.doBlackMove(board, r, c, r2, c2);
//				board = state.getBoard();
				temp = state.getTemp();
				if(temp.equals("K")) {return result/depth;}
				if(Examiner.isCheck(board, Turn.WHITE)) {return -result/depth;}
				if(Examiner.isPromotionWins(board, turn)) {return result/depth;}
				if(MovesList.isRepeated(board, turn)) {return 0;}
//				else if(integrator.isLost(board)) {return -result/depth;}
//				else {
					List<Node> children = generator.generateMoves(board, Turn.WHITE);
					nodesCount += children.size();
					
					for(Node current : children) {
						
						int y = current.getRowFrom();
						int x = current.getColumnFrom();
						int y2 = current.getRowTo();
						int x2 = current.getColumnTo();
						
						int y3 = 3;
						Board state0 = MoveMaker.doWhiteMove(board, y, x, y2, x2);
//						board = state0.getBoard();
						String temp0 = state0.getTemp();
						
						List<Node> leaves = generator.generateMoves(board, turn);
						nodesCount += leaves.size();
						List<Node> sorted = generator.arrangeMoves(board, leaves, turn);
						List<Integer> scores = sorted.stream().map(Node::getValue)
													 .collect(Collectors.toList());
						current.setValue(evaluator.max(scores));
						
						int x3 = state0.getC3();
						String promotion0 = state0.getPromotion();
						MoveMaker.undoAnyMove(board, temp0, promotion0, y, x, y2, x2, y3, x3);
					}				
	
					Collections.sort(children);
					List<Node> filtered = generator.filterMoves(children, Turn.WHITE);
					child = filtered.get(random.nextInt(filtered.size()));
					child.addParent(move);
					move.addChildren(Arrays.asList(child));
//				}
//				else {return result/depth;}
				depth++;
			}
			else{

				state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
//				board = state.getBoard();
				temp = state.getTemp();
				if(temp.equals("k")) {return -result/depth;}
				if(Examiner.isCheck(board, Turn.BLACK)) {return result/depth;}
				if(Examiner.isPromotionWins(board, turn)) {return -result/depth;}
				if(MovesList.isRepeated(board, turn)) {return 0;}
//				else {
					List<Node> children = generator.generateMoves(board, Turn.BLACK);
					nodesCount += children.size();
					
					for(Node current : children) {
						
						int y = current.getRowFrom();
						int x = current.getColumnFrom();
						int y2 = current.getRowTo();
						int x2 = current.getColumnTo();
						
						int y3 = 0;
						Board state0 = MoveMaker.doBlackMove(board, y, x, y2, x2);
//						board = state0.getBoard();
						String temp0 = state0.getTemp();
						
						List<Node> leaves = generator.generateMoves(board, turn);
						nodesCount += leaves.size();
						List<Node> sorted = generator.arrangeMoves(board, leaves, turn);
						List<Integer> scores = sorted.stream().map(Node::getValue)
													 .collect(Collectors.toList());
						current.setValue(evaluator.min(scores));
						
						int x3 = state0.getC3();
						String promotion0 = state0.getPromotion();
						MoveMaker.undoAnyMove(board, temp0, promotion0, y, x, y2, x2, y3, x3);
					}				
					
					Collections.sort(children, Collections.reverseOrder());
					List<Node> filtered = generator.filterMoves(children, Turn.BLACK);
					child = filtered.get(random.nextInt(filtered.size()));
					child.addParent(move);
					move.addChildren(Arrays.asList(child));
//				}
//				else {return -result/depth;}
			}
			move = child;
		}
	}
	
}
