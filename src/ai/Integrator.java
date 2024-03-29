package ai;

import java.util.ArrayDeque;
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
import utilpack.Copier;
import utilpack.Matrix;
import utilpack.MoveMaker;
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
	
	public boolean isLost(String[][] field) {
		return exp.bingo(Matrix.makeKey(field));
	}
	public boolean hasNode(String[][] field) {
		return exp.hasNode(Matrix.makeKey(field));
	}
	public Node getNode(String[][] field) {
		return exp.getNode(Matrix.makeKey(field));
	}
	public void newGame() {
		game.clear();
		MovesList.clear();
		Cache.clear();
	}
	public String[][] nextBest(String[][] field) {		
		if(!ring.isEmpty()) {
			ring.add(ring.poll());
			Node move = ring.peek();			
			return doMove(move, field);
		}
		else {
			return field;
		}
	}
	public String[][] nextBest(String[][] field, Node move) {
		return doMove(move, field);
	}
	
	// selects and makes best move
	public String[][] activate(String[][] field, List<Node> moves) {
			
		Collections.sort(moves, Collections.reverseOrder());
		
		int maxValue = moves.get(0).getValue();
		int maxProfit = moves.get(0).getProfit();
		int trappiness = moves.get(0).getTrappiness();
		moves.removeIf(e -> e.getValue() < maxValue);
		moves.removeIf(e -> e.getProfit() < maxProfit);
		moves.removeIf(e -> e.getTrappiness() < trappiness);
		
		Node move;
		if(moves.size()==1){
			move = moves.get(0);
		}
		else{
			Collections.shuffle(moves);
			move = moves.get(0);				
		}

		ring.clear();
		ring.addAll(moves);

		moves.clear();
		return doMove(move, field);
	}

	// takes external engine's move
	public String[][] activate(String[][] field, final int[] args){
		
		final int r = args[0];
		final int c = args[1];
		final int r2 = args[2];
		final int c2 = args[3];
		final int score = args[4];
//		final int nodes = args[5];
		Node move = new Node(r, c, r2, c2, Turn.BLACK);
		move.setValue(score);
		
//		Clocks.setNodes(nodes);
		return doMove(move, field);
	}

	private Scribe scribe = Scribe.getInstance();
	private boolean selfLearning = false;
	public void setSelfLearning(boolean selfLearning) {this.selfLearning = selfLearning;}

	private String[][] doMove(final Node move, String[][] field){
		
		int score = move.getValue();
		int r = move.getRowFrom();
		int c = move.getColumnFrom();
		int r2 = move.getRowTo();
		int c2 = move.getColumnTo();
		
		if(selfLearning) {
			if(score > 999 & Cache.isEmpty()) {
				String state = Matrix.makeKey(field);
				if(!exp.hasNode(state)) {
					exp.learn(state, move);
					String mirror = Matrix.swapKey(state);
					Copier.deepCopy(Arrays.asList(move), null, true);
					Node twin = Copier.getRoot();
					exp.learn(mirror, twin);
				}
			}
		}
		
		String edge = Expositor.getEdge(r, c, r2, c2, field[r][c]);		
		scribe.writeGameNote("black", edge);
		
		String spot = field[r2][c2];
		String pieceName = Expositor.getPieceName(field[r][c]);

		MoveMaker.doBlackMove(field, r, c, r2, c2);
		
		if(score < -999) {
			if(!exp.bingo(game.peek())) {
				exp.learn(game.peek());
				exp.learn(Matrix.swapKey(game.peek()));
			}
		}
		else {
			game.push(Matrix.makeKey(field));
		}
		
		if(selfLearning) {
			if(score > 999 & Cache.isEmpty()) {
				if(move.hasChildren()) {
					Cache.addTree(Copier.deepCopy(field), move.getChidren());
				}
			}
		}
		
		String col = Expositor.getColumnName(c);
		String col2 = Expositor.getColumnName(c2);
		
		info.output(score, field, pieceName, c, col, r, spot, col2, r2);
		return field;		
	}
	
}
