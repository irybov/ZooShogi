package ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import control.Clocks;
import util.Capture;
import util.Matrix;
import util.Message;

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
	
	private List<Node> moves = new CopyOnWriteArrayList<>();
	
	private Info info = new Info();
	public void setWarn(boolean warn){
		info.setWarn(warn);		
	}
	public void setMute(boolean mute){
		info.setMute(mute);		
	}
	
	private Deque<String> game = new ArrayDeque<>();
	private Experience exp = new Experience();
	
	boolean getNote(String[][] field) {
		return exp.bingo(Matrix.keyMaker(field));
	}
	public void newGame() {
		game.clear();
	}
		
	// adds results from a single thread
	public void mergeMoves(List<Node> input){		
		moves.addAll(input);
	}
	
	// merges results from multiple threads
	public void mergeMoves(Node input){		
		moves.add(input);
	}
	
	// do external engine's move
	public String[][] activate(String[][] field, int r, int c, int r2, int c2,
														int score, int nodes){
		Clocks.setNodes(nodes);
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

		if(field[r][c].equals("p") & r==2){
			if(r==0 & c > 2){
				field[r2][c2] = "p";
				field[r][c] = " ";
			}	
			else{
				Capture.take(field, r2, c2);
				field[r2][c2] = "q";
				field[r][c] = " ";
			}
		}
		else{
			Capture.take(field, r2, c2);
			field[r2][c2] = field[r][c];
			field[r][c] = " ";
		}
		
		String col = Message.colName(c);
		String col2 = Message.colName(c2);
		
		info.output(score, field, pieceName,c,col,r,spot,col2,r2);		
		return field;
	}
	
	// selects and makes best move
	public String[][] activate(String[][] field) {
		
		int score = Integer.MIN_VALUE+1;
		int r = -1;
		int c = -1;
		int r2 = -1;
		int c2 = -1;
		
		Node move;
		
		if(moves.size() == 1) {
			move = moves.get(0);
		}
		else {
			ArrayList<Node> random = new ArrayList<>();
			for(int i=0; i<moves.size(); i++) {
				if(score <= moves.get(i).getValue()){
					score = moves.get(i).getValue();				
					random.add(moves.get(i));
				}
			}
			
			Collections.sort(random, Collections.reverseOrder());
			
			int prev = random.get(0).getValue();
			random.removeIf(e -> e.getValue() < prev);
			
			int trap = random.get(0).getTrap();
			random.removeIf(e -> e.getTrap() < trap);
			
			if(random.size()==1){
				move = random.get(0);
			}
			else{
				move = random.get(new Random().nextInt(random.size()));
			}
		}
		score = move.getValue();
		r = move.getR();
		c = move.getC();
		r2 = move.getR2();
		c2 = move.getC2();	
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

		if(field[r][c].equals("p") & r==2){
			if(r==0 & c > 2){
				field[r2][c2] = "p";
				field[r][c] = " ";
			}	
			else{
				Capture.take(field, r2, c2);
				field[r2][c2] = "q";
				field[r][c] = " ";
			}
		}
		else{
			Capture.take(field, r2, c2);
			field[r2][c2] = field[r][c];
			field[r][c] = " ";
		}
		
		if(score < -500) {
			if(!exp.bingo(game.peek())) {
				exp.learn(game.peek());
			}
		}
		else {
			game.push(Matrix.keyMaker(field));
		}
		
		String col = Message.colName(c);
		String col2 = Message.colName(c2);
		
		info.output(score, field, pieceName,c,col,r,spot,col2,r2);	
		moves.clear();
		return field;
	}
	
}
