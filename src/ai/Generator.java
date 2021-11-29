package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilpack.Capture;
import utilpack.Examiner;
import utilpack.Pieces;

public class Generator {

	public List<Node> generateNodes(String[][] field) {
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		for(int i=3; i<9; i++) {
			if(!field[0][i].equals(" ")){
				for(int r=0; r<4; r++){
					for(int c=0; c<3; c++){
						if(field[r][c].equals(" ")){
							legal.add(new Node(0, i, r, c, "black"));
						}
					}
				}
			}
		}
			
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){					
				if(field[r][c].equals("p")){
					r2 = r+1;
					c2 = c;
					if((Pieces.BPAWN.move(r, c, r2, c2))&&
					   (Capture.check(field, r2, c2, "black"))){
						legal.add(new Node(r, c, r2, c2, "black"));
					}
				}				
				else if(field[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}				
				else if(field[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}				
				else if(field[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}				
				else if(field[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BQUEEN.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}
			}
		}
		return sortingMoves(field, legal);
	}

	
	private List<Node> sortingMoves(String[][] board, List<Node> legal) {
	
	List<Node> sorted = new ArrayList<>();
	
		for(int i=0; i<legal.size(); i++){
	
			int r = legal.get(i).getR();
			int c = legal.get(i).getC();
			int r2 = legal.get(i).getR2();
			int c2 = legal.get(i).getC2();
			String temp;
			String prom;
			int r3;
			int c3 = 9;						
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
				else if(MoveList.repeat(board, "black")) {
					value = 0;
				}
				else if(Examiner.winPromotion(board, "white")){
					value = -5000;
				}
				else if(Examiner.check(board, "white")){
					value = -5000;
				}
				else{
					value = evaluation(board);
				}
				
				legal.get(i).setValue(value);
				sorted.add(legal.get(i));							
			
			if(prom.equals("p")){
				board[r][c] = "p";
			}
			else{
				board[r][c] = board[r2][c2];
			}
			board[r2][c2] = temp;
			if(c3 != 9)
			Capture.undo(board, r3, c3);
		}
		Collections.sort(sorted, Collections.reverseOrder());
		
		return sorted;
	}

	private int evaluation(String[][] board) {
		
		int score = 0;

		for(int r=0; r<4; r++){
			for(int c=0; c<board[r].length; c++){
				if(!board[r][c].equals(" ")){
					switch(board[r][c]){
						case "p":
							score += Pieces.BPAWN.getValue();
							break;
						case "r":
							score += Pieces.ROOK.getValue();
							break;
						case "b":
							score += Pieces.BISHOP.getValue();
							break;
						case "q":
							score += Pieces.BQUEEN.getValue();
							break;
						case "P":
							score += -Pieces.WPAWN.getValue();
							break;
						case "R":
							score += -Pieces.ROOK.getValue();
							break;
						case "B":
							score += -Pieces.BISHOP.getValue();
							break;
						case "Q":
							score += -Pieces.WQUEEN.getValue();
							break;
						}
					}
				}
			}
		return score;
	}
	
}
