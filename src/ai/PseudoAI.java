package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import control.Clocks;
import utilpack.Capture;
import utilpack.Examiner;

public class PseudoAI implements Runnable {

	private List<Node> moves;
	
	private final Generator generator = new Generator();
	private final Evaluator evaluator = new Evaluator();
	
	private final int level;
	private String[][] board; 
	
	public PseudoAI(int level, String[][] board) {		
		this.level = level;
		this.board = board;
	}
	
	private final Integrator integrator = Integrator.getInstance();
	
	@Override
	public void run() {
		algorithmSelector();		
	}	
	private void algorithmSelector(){

		moves = new ArrayList<>();
		switch(level){
		case 0:
			random();
			break;
		case 1:
			greedy();			
			break;
		}
		sendMoves();
	}
	
	// greedy algorithm
	private void greedy() {
		
		List<Node> legal = new ArrayList<>(generator.generateMoves(board, "black"));
		
		int score;
		String temp;
		
		for(int i=0; i<legal.size(); i++){

			int r = legal.get(i).getR();
			int c = legal.get(i).getC();
			int r2 = legal.get(i).getR2();
			int c2 = legal.get(i).getC2();
			String prom;
			int r3 = 0;
			int c3 = 9;

				if(board[r][c].equals("p") & r==2){
					prom = "p";
					if(!board[r][c].equals(" ")) {
						c3 = Capture.take(board, r2, c2);
					}
					temp = board[r2][c2];
					board[r2][c2] = "q";
					board[r][c] = " ";	
				}
				else if(r==0 & c > 2){
					prom = " ";
					temp = " ";
					board[r2][c2] = board[r][c];
					board[r][c] = " ";
				}
				else{
					prom = " ";
					if(!board[r][c].equals(" ")) {
						c3 = Capture.take(board, r2, c2);
					}
					temp = board[r2][c2];
					board[r2][c2] = board[r][c];
					board[r][c] = " ";	
				}
			
			if(temp.equals("K")){
				score = 5000;
			}
			else if(Examiner.winPromotion(board, "black") && !Examiner.check(board, "white")){
				score = 5000;
			}
			else if(MovesList.repeat(board, "black")) {
				score = 0;
			}
			else if(integrator.getNote(board)) {
				score = -500;							
			}
			else if(Examiner.winPromotion(board, "white")){
				score = -5000;
			}
			else if(Examiner.check(board, "white")){
				score = -5000;
			}
			else{
				score = evaluator.evaluationMaterial(board, false);
			}
			legal.get(i).setValue(score);
			
			if(prom.equals("p")){
				board[r][c] = "p";
			}
			else{
				board[r][c] = board[r2][c2];
			}
			board[r2][c2] = temp;
			Capture.undo(board, r3, c3);
		}
		moves.addAll(legal);
		Clocks.setNodes(legal.size());		
	}
	
	// randomly moving algorithm
	private void random() {
		
		List<Node> legal = new ArrayList<>(generator.generateMoves(board, "black"));
			
		moves.add(legal.get(new Random().nextInt(legal.size())));
		Clocks.setNodes(legal.size());		
	}

	private void sendMoves(){
		
		if(moves.isEmpty()){
			random();
		}
		integrator.mergeMoves(moves);
	}

}
