package ai;

import java.util.ArrayList;
import java.util.List;

import util.Bishop;
import util.Capture;
import util.King;
import util.Pawn;
import util.Queen;
import util.Rook;

public class Separator {
	
	private static List<Node> head = new ArrayList<>();
	private static List<Node> tail = new ArrayList<>();

	public static List<Node> getHead() {
		return head;
	}

	public static List<Node> getTail() {
		return tail;
	}

	public static List<Node> generateNodes(String[][] field) {
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		for(int i=3; i<9; i++) {
			if(field[0][i]!=" "){
				for(int r=0; r<4; r++){
					for(int c=0; c<3; c++){
						if(field[r][c]==" "){
							legal.add(new Node(0, i, r, c));
						}
					}
				}
			}
		}
			
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){					
				if(field[r][c]=="p"){
					r2 = r+1;
					c2 = c;
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(field, r2, c2, "black"))){
						legal.add(new Node(r, c, r2, c2));
					}
				}				
				else if(field[r][c]=="r"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c]=="k"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c]=="b"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c]=="q"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "black"))&&
						   (Capture.check(field, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
			}
		}
//		separateMoveList(legal);
		return legal;
	}
/*
	private static void separateMoveList(List<Node> legal){

		if(legal.size() % 2 == 0){
			head.addAll(legal.subList(0, legal.size()/2));			
			tail.addAll(legal.subList(legal.size()/2, legal.size()));
		}
		else{
			head.addAll(legal.subList(0, (legal.size()/2)-1));			
			tail.addAll(legal.subList((legal.size()/2)-1, legal.size()));
		}
	}
	
	public static void clearLists(){
		
		head.clear();
		tail.clear();
	}
*/	
}
