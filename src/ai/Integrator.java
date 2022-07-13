package ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ai.component.Cache;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import control.Scribe;
import utilpack.Verifier;
import utilpack.Copier;
import utilpack.Matrix;
import utilpack.Turn;
import utilpack.Expositor;

public class Integrator {
	
	private static volatile Integrator INSTANCE;
	
	public static Integrator getInstance(){
		
		if(INSTANCE == null) {
			synchronized (Integrator.class) {
				if(INSTANCE == null) {
					INSTANCE = new Integrator();
				}
			}
		}		
		return INSTANCE;
	}
	
	private Messanger info = new Messanger();
	public void setCheckWarning(boolean warn){
		info.setCheckWarning(warn);		
	}
	public void setVolumeMute(boolean mute){
		info.setVolumeMute(mute);		
	}
	
	private Queue<Node> ring = new LinkedList<>();
	private Deque<String> game = new ArrayDeque<>();
	private Experience exp = new Experience();
	
	public boolean isLost(char[][] field) {
		return exp.bingo(Matrix.makeKey(field));
	}
	public boolean hasNode(char[][] board) {
		return exp.hasNode(Matrix.makeKey(board));
	}
	public Node getNode(char[][] board) {
		return exp.getNode(Matrix.makeKey(board));
	}
	public void newGame() {
		game.clear();
		MovesList.clear();
		Cache.clear();
	}
	public char[][] nextBest(char[][] board) {		
		if(!ring.isEmpty()) {
			ring.add(ring.poll());
			Node move = ring.peek();			
			return doMove(move, board);
		}
		else {
			return board;
		}
	}
	public char[][] nextBest(char[][] board, Node move) {
		return doMove(move, board);
	}
	
	// selects and makes best move
	public char[][] activate(char[][] board, List<Node> moves) {
		
		int score = Integer.MIN_VALUE+1;		
		Node move;
		ArrayList<Node> random = new ArrayList<>(moves.size());
		
		if(moves.size() == 1) {
			move = moves.get(0);
			random.add(moves.get(0));
		}
		else {
			for(int i=0; i<moves.size(); i++) {
				if(score <= moves.get(i).getValue()){
					score = moves.get(i).getValue();				
					random.add(moves.get(i));
				}
			}
			
			Collections.sort(random, Collections.reverseOrder());
			
			int prev = random.get(0).getValue();
			random.removeIf(e -> e.getValue() < prev);
			
			int trap = random.get(0).getTrappiness();
			random.removeIf(e -> e.getTrappiness() < trap);
			
			if(random.size()==1){
				move = random.get(0);
			}
			else{
				Collections.shuffle(random);
				move = random.get(0);				
			}
		}
		ring.clear();
		ring.addAll(random);

		moves.clear();
		return doMove(move, board);
	}

	// takes external engine's move
	public char[][] activate(char[][] field, final int[] args){
		
		final int r = args[0];
		final int c = args[1];
		final int r2 = args[2];
		final int c2 = args[3];
		final int score = args[4];
		final int nodes = args[5];
		Node move = new Node(r, c, r2, c2, Turn.BLACK);
		move.setValue(score);
		
		Clocks.setNodes(nodes);	
		return doMove(move, field);
	}

	private Scribe scribe = Scribe.getInstance();

	private char[][] doMove(final Node move, char[][] board){
		
		int score = move.getValue();
		int r = move.getRowFrom();
		int c = move.getColumnFrom();
		int r2 = move.getRowTo();
		int c2 = move.getColumnTo();
		
		if(score > 500 & Cache.isEmpty()) {
			String state = Matrix.makeKey(board);
			if(!exp.hasNode(state)) {
				exp.learn(state, move);
				String mirror = Matrix.swapKey(state);
				Copier.deepCopy(Arrays.asList(move), null, true);
				Node twin = Copier.getRoot();
				exp.learn(mirror, twin);
			}
		}
		
		String edge = Expositor.getEdge(r, c, r2, c2, board[r][c]);		
		scribe.writeGameNote("black", edge);
		
		char spot = board[r2][c2];
		String pieceName = Expositor.getPieceName(board[r][c]);

		if(board[r][c]==('p') & r==2){
			if(r==0 & c > 2){
				board[r2][c2] = 'p';
				board[r][c] = ' ';
			}	
			else{
				Verifier.takenPiecePlacement(board, r2, c2);
				board[r2][c2] = 'q';
				board[r][c] = ' ';
			}
		}
		else{
			Verifier.takenPiecePlacement(board, r2, c2);
			board[r2][c2] = board[r][c];
			board[r][c] = ' ';
		}
		
		if(score < -500) {
			if(!exp.bingo(game.peek())) {
				exp.learn(game.peek());
				exp.learn(Matrix.swapKey(game.peek()));
			}
		}
		else {
			game.push(Matrix.makeKey(board));
		}
		
		if(score > 500 & Cache.isEmpty()) {
			if(move.hasChildren()) {
				Cache.addTree(Copier.deepCopy(board), move.getChidren());
			}
		}
		
		String col = Expositor.getColumnName(c);
		String col2 = Expositor.getColumnName(c2);
		
		info.output(score, board, pieceName, c, col, r, spot, col2, r2);
		return board;		
	}
	
}
