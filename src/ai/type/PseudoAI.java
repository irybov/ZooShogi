package ai.type;

import java.util.List;

import ai.Integrator;
import ai.component.Board;
import ai.component.Evaluator;
import ai.component.MovesList;
import ai.component.Node;
import control.Clocks;
import utilpack.Examiner;
import utilpack.MoveMaker;
import utilpack.Turn;

public class PseudoAI implements Runnable {
	
	private final Evaluator evaluator = new Evaluator();
	
	private final int level;
	private char[][] board; 
	private List<Node> legal;
	
	public PseudoAI(int level, char[][] board2, List<Node> legal) {		
		this.level = level;
		this.board = board2;
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
		char temp;
		
		for(int i=0; i<legal.size(); i++){

			int r = legal.get(i).getRowFrom();
			int c = legal.get(i).getColumnFrom();
			int r2 = legal.get(i).getRowTo();
			int c2 = legal.get(i).getColumnTo();
			char promotion;
			int r3 = 0;
			int c3 = 9;
			Board state;
			
			state = MoveMaker.doBlackMove(board, r, c, r2, c2);
			board = state.getBoard();
			temp = state.getTemp();
			
			if(temp==('K')){
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
				score = -1000;							
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
			
			c3 = state.getC3();
			promotion = state.getPromotion();
			MoveMaker.undo(board, temp, promotion, r, c, r2, c2, r3, c3);
		}	
	}

}
