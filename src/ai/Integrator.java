package ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import control.Clocks;
import control.Scribe;
import utilpack.Capture;
import utilpack.Copier;
import utilpack.Matrix;
import utilpack.Message;

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
	
	private List<Node> moves = new ArrayList<>();
	
	private Info info = new Info();
	public void setCheckWarning(boolean warn){
		info.setCheckWarning(warn);		
	}
	public void setVolumeMute(boolean mute){
		info.setVolumeMute(mute);		
	}
	
	private Queue<Node> ring = new LinkedList<>();
	private Deque<String> game = new ArrayDeque<>();
	private Experience exp = new Experience();
	
	boolean getNote(String[][] field) {
		return exp.bingo(Matrix.keyMaker(field));
	}
	public boolean hasNode(String[][] field) {
		return exp.has(Matrix.keyMaker(field));
	}
	public Node getNode(String[][] field) {
		return exp.get(Matrix.keyMaker(field));
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
	
	// adds results from a single thread
	void mergeMoves(List<Node> input){		
		moves.addAll(input);
	}	
	// merges results from multiple threads
	void mergeMoves(Node input){		
		moves.add(input);
	}
	
	// selects and makes best move
	public String[][] activate(String[][] field) {
		
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
		return doMove(move, field);
	}

	// takes external engine's move
	public String[][] activate(String[][] field, final int[] args){
		
		final int r = args[0];
		final int c = args[1];
		final int r2 = args[2];
		final int c2 = args[3];
		final int score = args[4];
		final int nodes = args[5];
		Node move = new Node(r, c, r2, c2, "black");
		move.setValue(score);
		
		Clocks.setNodes(nodes);	
		return doMove(move, field);
	}

	private Scribe scribe = Scribe.getInstance();

	private String[][] doMove(final Node move, String[][] field){
		
		int score = move.getValue();
		int r = move.getRowFrom();
		int c = move.getColumnFrom();
		int r2 = move.getRowTo();
		int c2 = move.getColumnTo();
		
		if(score > 500 & Cache.isEmpty()) {
			String state = Matrix.keyMaker(field);
			if(!exp.has(state)) {
				exp.learn(state, move);
				String mirror = Matrix.keySwapper(state);
				Copier.deepCopy(Arrays.asList(move), null, true);
				Node twin = Copier.getRoot();
				exp.learn(mirror, twin);
			}
		}
		
		String edge = Message.getEdge(r, c, r2, c2, field[r][c]);		
		scribe.writeGameNote("black", edge);
		
		String spot = field[r2][c2];
		String pieceName = Message.getPieceName(field[r][c]);

		if(field[r][c].equals("p") & r==2){
			if(r==0 & c > 2){
				field[r2][c2] = "p";
				field[r][c] = " ";
			}	
			else{
				Capture.takenPiecePlacement(field, r2, c2);
				field[r2][c2] = "q";
				field[r][c] = " ";
			}
		}
		else{
			Capture.takenPiecePlacement(field, r2, c2);
			field[r2][c2] = field[r][c];
			field[r][c] = " ";
		}
		
		if(score < -500) {
			if(!exp.bingo(game.peek())) {
				exp.learn(game.peek());
				exp.learn(Matrix.keySwapper(game.peek()));
			}
		}
		else {
			game.push(Matrix.keyMaker(field));
		}
		
		if(score > 500 & Cache.isEmpty()) {
			if(move.hasChildren()) {
				Cache.addTree(Copier.deepCopy(field), move.getChidren());
			}
		}
		
		String col = Message.getColumnName(c);
		String col2 = Message.getColumnName(c2);
		
		info.output(score, field, pieceName, c, col, r, spot, col2, r2);
		return field;		
	}
	
}
