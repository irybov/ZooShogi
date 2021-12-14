package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilpack.Capture;
import utilpack.Examiner;
import utilpack.Pieces;

public class Generator {
	
	private final Evaluator evaluator = new Evaluator();

	public List<Node> generateMoves(String[][] board, String turn) {	
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		if(turn.equals("black")) {
			for(int i=3; i<9; i++) {
				if(!board[0][i].equals(" ")){
					for(int r=0; r<4; r++){
						for(int c=0; c<3; c++){
							if(board[r][c].equals(" ")){
								legal.add(new Node(0, i, r, c, turn));
							}
						}
					}
				}
			}
				
			for(int r=0; r<4; r++){
				for(int c=0; c<3; c++){					
					if(board[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pieces.BPAWN.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, turn))){
							legal.add(new Node(r, c, r2, c2, turn));
						}
					}				
					else if(board[r][c].equals("r")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("k")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("b")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("q")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BQUEEN.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}
				}
			}
		}		
		else {	
			for(int i=3; i<9; i++) {
				if(!board[3][i].equals(" ")){
					for(int r=0; r<4; r++){
						for(int c=0; c<3; c++){
							if(board[r][c].equals(" ")){
								legal.add(new Node(3, i, r, c, turn));
							}
						}
					}
				}
			}
				
			for(int r=0; r<4; r++){
				for(int c=0; c<3; c++){					
					if(board[r][c].equals("P")){
						r2 = r-1;
						c2 = c;
						if((Pieces.WPAWN.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, turn))){
							legal.add(new Node(r, c, r2, c2, turn));
						}
					}
					else if(board[r][c].equals("R")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}
					else if(board[r][c].equals("K")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("B")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}				
					else if(board[r][c].equals("Q")){					
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.WQUEEN.move(r, c, r2, c2))&&
							   (Capture.check(board, r2, c2, turn))){
								legal.add(new Node(r, c, r2, c2, turn));
								}
							}							
						}
					}
				}
			}
		}
		return legal;
	}
	
	public List<Node> sortMoves(String[][] board, List<Node> legal,
			String turn, boolean prune) {
		
		List<Node> sorted = new ArrayList<>();
		
		int prev;
			if(Examiner.check(board, "white")){
				prev = -1000;
			}
			else if(Examiner.check(board, "black")){
				prev = 1000;
			}
			else{
				prev = evaluator.evaluationMaterial(board, false)
						+ evaluator.evaluationPositional(board);
			}
	
		for(int i=0; i<legal.size(); i++){

			int r = legal.get(i).getR();
			int c = legal.get(i).getC();
			int r2 = legal.get(i).getR2();
			int c2 = legal.get(i).getC2();
			String temp;
			String prom;
			int r3;
			int c3 = 9;

			if(turn.equals("black")){						
				r3 = 0;
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
				
				int value;				
				if(temp.equals("K")){
					value = 5000;	
				}
				else if(Examiner.winPromotion(board, "black") && !Examiner.check(board, "white")){
					value = 5000;
				}
				else if(Examiner.check(board, "black") && !Examiner.check(board, "white")) {
					value = 500;
				}
				else if(MovesList.repeat(board, "black")) {
					value = 0;
				}
				else if(Examiner.winPromotion(board, "white")){
					value = -5000;
				}
				else if(Examiner.check(board, "white")){
					value = -5000;
				}
				else{
					if(prune) {
						value = evaluator.evaluationMaterial(board, false)
								+ evaluator.evaluationPositional(board);
					}
					else {
						value = evaluator.evaluationMaterial(board, false);
					}
				}
				
				legal.get(i).setValue(value);
				sorted.add(legal.get(i));			
	
				Collections.sort(sorted, Collections.reverseOrder());			
				if(prune){
					sorted.removeIf(e -> e.getValue() < prev);
				}
			}		
			else{					
				r3 = 3;
				if(board[r][c].equals("P") & r==1){
					prom = "P";
					if(!board[r][c].equals(" ")) {
						c3 = Capture.take(board, r2, c2);
					}
					temp = board[r2][c2];
					board[r2][c2] = "Q";
					board[r][c] = " ";
				}
				else if(r==3 & c > 2){
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
		
				int value;				
				if(temp.equals("k")){
					value = -5000;	
				}
				else if(Examiner.winPromotion(board, "white") && !Examiner.check(board, "black")){
						value = -5000;
				}
				else if(Examiner.check(board, "white") && !Examiner.check(board, "black")) {
					value = -500;
				}
				else if(MovesList.repeat(board, "white")) {
					value = 0;
				}
				else if(Examiner.winPromotion(board, "black")){
						value = 5000;
				}
				else if(Examiner.check(board, "black")){
					value = 5000;
				}				
				else{
					if(prune) {
						value = evaluator.evaluationMaterial(board, false)
								+ evaluator.evaluationPositional(board);
					}
					else {
						value = evaluator.evaluationMaterial(board, false);
					}
				}
				
				legal.get(i).setValue(value);
				sorted.add(legal.get(i));
		
				Collections.sort(sorted);			
				if(prune){
					sorted.removeIf(e -> e.getValue() > prev);
				}
			}
			
			if(prom.equals("p")){
				board[r][c] = "p";
			}
			else if(prom.equals("P")){
				board[r][c] = "P";
			}
			else{
				board[r][c] = board[r2][c2];
			}
			board[r2][c2] = temp;
			if(c3 != 9)
			Capture.undo(board, r3, c3);
		}	
		return sorted;
	}
	
}
