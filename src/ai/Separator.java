package ai;

import java.util.ArrayList;
import java.util.List;

import util.Capture;
import util.Pieces;

public class Separator {

	public static List<Node> generateNodes(String[][] field) {
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		for(int i=3; i<9; i++) {
			if(!field[0][i].equals(" ")){
				for(int r=0; r<4; r++){
					for(int c=0; c<3; c++){
						if(field[r][c].equals(" ")){
							legal.add(new Node(0, i, r, c));
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
						legal.add(new Node(r, c, r2, c2));
					}
				}				
				else if(field[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BQUEEN.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
			}
		}
		return legal;
	}

}
