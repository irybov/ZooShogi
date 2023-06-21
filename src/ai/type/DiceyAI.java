package ai.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ai.component.Board;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
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

		if(board[r][c].equals("p") & r==2){
			board[r2][c2] = "q";
			board[r][c] = " ";	
		}
		else if(r==0 & c > 2){
			board[r2][c2] = board[r][c];
			board[r][c] = " ";
		}
		else{
			board[r2][c2] = board[r][c];
			board[r][c] = " ";	
		}
		
		List<Node> children = generator.generateMoves(board, Turn.WHITE);
		nodesCount += children.size();		
		
		for(Node child : children) {
			child.addParent(root);
			child.setValue(calculate(Turn.WHITE, 2, child));
			hash.clear();
		}
		root.addChildren(children);
		
		int sum = children.stream().mapToInt(Node::getValue).sum();		
		return sum / children.size();
	}
	
	// monte-carlo algorithm
	private int calculate(Turn turn, int depth, Node move) {
		
		if(turn.equals(Turn.WHITE) && integrator.isLost(board)) {
//			if(depth>2) {move=null;}
			return -1000;
		}		
		if(turn.equals(Turn.WHITE) && MovesList.isRepeated(board, Turn.BLACK)){
//			if(depth>2) {move=null;}
			return 0;
		}
		if(turn.equals(Turn.BLACK) && MovesList.isRepeated(board, Turn.WHITE)){
//			if(depth>2) {move=null;}
			return 0;
		}		
		if(hash.isRepeated(board, turn)){
//			if(depth>2) {move=null;}
			return 0;
		}		
		
		if(Examiner.isBlackPositionWon(board, turn)){
//			if(depth>2) {move=null;}
			return 1000;
		}
		if(Examiner.isWhitePositionWon(board, turn)){
//			if(depth>2) {move=null;}
			return -1000;
		}
		
		if(Examiner.isCheck(board, turn)){
			if(turn.equals(Turn.WHITE)){
//				if(depth>2) {move=null;}
				return -1000;
			}
			else {
//				if(depth>2) {move=null;}
				return 1000;
			}
		}

		hash.addMove(board, turn);
		
/*		if(depth == 100){
			move = null;
			return evaluator.evaluationMaterial(board, false)
					+ evaluator.evaluationPositional(board);
		}*/

		nodesCount++;
	
		int r = move.getRowFrom();
		int c = move.getColumnFrom();
		int r2 = move.getRowTo();
		int c2 = move.getColumnTo();
		String temp;
		String promotion;
		int r3;
		Board state;
/*				
		List<Node> children = null;
		List<Node> sorted = null;
		List<Node> filtered = null;
		List<Node> leaves = null;
		List<Integer> scores = null;
		Node child = null;
*/
		if(turn.equals(Turn.BLACK)){
			r3 = 0;
			state = MoveMaker.doBlackMove(board, r, c, r2, c2);
			board = state.getBoard();
			temp = state.getTemp();

			Node child = null;
			if(temp != "K") {
				List<Node> children = generator.generateMoves(board, Turn.WHITE);
				nodesCount += children.size();
				
				for(Node current : children) {
					
					int y = current.getRowFrom();
					int x = current.getColumnFrom();
					int y2 = current.getRowTo();
					int x2 = current.getColumnTo();
					
					int y3 = 3;
					Board state0 = MoveMaker.doWhiteMove(board, y, x, y2, x2);
					board = state0.getBoard();
					String temp0 = state0.getTemp();
					
					List<Node> leaves = generator.generateMoves(board, Turn.BLACK);
					nodesCount += leaves.size();
					List<Integer> scores = leaves.stream().map(Node::getValue).collect(Collectors.toList());
					current.setValue(evaluator.max(scores));
					
					int x3 = state.getC3();
					String promotion0 = state0.getPromotion();
					MoveMaker.undoAnyMove(board, temp0, promotion0, y, x, y2, x2, y3, x3);
				}				

				Collections.sort(children);
				List<Node> filtered = generator.filterMoves(children, Turn.WHITE);
				child = filtered.get(random.nextInt(filtered.size()));
				child.addParent(move);
				move.addChildren(Arrays.asList(child));
			}
//			else {child = new Node(-1, -1, -1, -1, Turn.PAUSE);}			
			int value = calculate(Turn.WHITE, depth+1, child);
				move.setValue(value);
		}
		else{
			r3 = 3;
			state = MoveMaker.doWhiteMove(board, r, c, r2, c2);
			board = state.getBoard();
			temp = state.getTemp();

			Node child = null;
			if(temp != "k") {
				List<Node> children = generator.generateMoves(board, Turn.BLACK);
				nodesCount += children.size();
				
				for(Node current : children) {
					
					int y = current.getRowFrom();
					int x = current.getColumnFrom();
					int y2 = current.getRowTo();
					int x2 = current.getColumnTo();
					
					int y3 = 0;
					Board state0 = MoveMaker.doBlackMove(board, y, x, y2, x2);
					board = state0.getBoard();
					String temp0 = state0.getTemp();
					
					List<Node> leaves = generator.generateMoves(board, Turn.WHITE);
					nodesCount += leaves.size();
					List<Integer> scores = leaves.stream().map(Node::getValue).collect(Collectors.toList());
					current.setValue(evaluator.min(scores));
					
					int x3 = state.getC3();
					String promotion0 = state0.getPromotion();
					MoveMaker.undoAnyMove(board, temp0, promotion0, y, x, y2, x2, y3, x3);
				}				
				
				Collections.sort(children, Collections.reverseOrder());
				List<Node> filtered = generator.filterMoves(children, Turn.BLACK);
				child = filtered.get(random.nextInt(filtered.size()));
				child.addParent(move);
				move.addChildren(Arrays.asList(child));
			}
//			else {child = new Node(-1, -1, -1, -1, Turn.PAUSE);}
			int value = calculate(Turn.BLACK, depth+1, child);
				move.setValue(value);
		}
			
		int c3 = state.getC3();
		promotion = state.getPromotion();
		MoveMaker.undoAnyMove(board, temp, promotion, r, c, r2, c2, r3, c3);
		
		return move.getValue();
	}
	
}
