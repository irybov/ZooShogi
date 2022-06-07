package ai.type;

import java.util.List;

import ai.Integrator;
import ai.component.Evaluator;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Capture;
import utilpack.Examiner;
import utilpack.Turn;

public class PseudoAI implements Runnable {
	
	private final Evaluator evaluator = new Evaluator();
	
	private final int level;
	private String[][] board; 
	private List<Node> legal;
	
	public PseudoAI(int level, String[][] board, List<Node> legal) {		
		this.level = level;
		this.board = board;
		this.legal = legal;
	}
	
	private final Integrator integrator = Integrator.getInstance();
	
	@Override
	public void run() {
		algorithmSelector();		
	}	
	private void algorithmSelector(){
		
		switch(level){
		case 0:
			break;
		case 1:
			greedy();			
			break;
		}
		Clocks.setNodes(legal.size());
	}
	
	// greedy algorithm
	private void greedy() {
		
		int score;
		String temp;
		
		for(int i=0; i<legal.size(); i++){

			int r = legal.get(i).getRowFrom();
			int c = legal.get(i).getColumnFrom();
			int r2 = legal.get(i).getRowTo();
			int c2 = legal.get(i).getColumnTo();
			String prom;
			int r3 = 0;
			int c3 = 9;

				if(board[r][c].equals("p") & r==2){
					prom = "p";
					if(!board[r][c].equals(" ")) {
						c3 = Capture.takenPiecePlacement(board, r2, c2);
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
						c3 = Capture.takenPiecePlacement(board, r2, c2);
					}
					temp = board[r2][c2];
					board[r2][c2] = board[r][c];
					board[r][c] = " ";	
				}
			
			if(temp.equals("K")){
				score = 2000;
			}
			else if(Examiner.isPromotionWon(board, Turn.BLACK) &&
					!Examiner.isCheck(board, Turn.WHITE)){
				score = 1000;
			}
			else if(MovesList.isRepeated(board, Turn.BLACK)) {
				score = 0;
			}
			else if(integrator.isLost(board)) {
				score = -500;							
			}
			else if(Examiner.isPromotionWon(board, Turn.WHITE)){
				score = -1000;
			}
			else if(Examiner.isCheck(board, Turn.WHITE)){
				score = -2000;
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
			Capture.undoMove(board, r3, c3);
		}	
	}

}