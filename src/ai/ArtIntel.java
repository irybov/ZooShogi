package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import util.Bishop;
import util.Capture;
import util.Copier;
import util.King;
import util.Pawn;
import util.Queen;
import util.Rook;

public class ArtIntel implements Runnable{
	
	private HashTabs hash = new HashTabs();
	private List<Node> moves = new ArrayList<>();
	
	private Node root;
	
	private int level;
	private String[][] board; 

	public ArtIntel(int level, String[][] board) {		
		this.level = level;
		this.board = board;
	}
	public ArtIntel(Node root, String[][] board, int level) {		
		this.root = root;
		this.board = board;
		this.level = level;
	}
	
	@Override
	public void run(){
		
		algorithmSelector();
	}
	
	private void algorithmSelector(){
		
		switch(level){
		case 0:
			random(board);
			sendMovelist();			
			break;
		case 1:
			expectimax(board, "black", 6);
			break;		
		case 2:
			greedy(board);
			sendMovelist();
			break;
		case 3:
			Map<String[][], List<Node>> start = new HashMap<>();
			List<Node> node = new ArrayList<>();
			node.add(root);
			start.put(board, node);
			breadthMM(start, "black", 4);
			break;
		case 4:
			forward(board, "black", 6);
			sendMovelist();
			break;
		case 5:
			minimax(board, "black", 6);
			break;
		case 6:
			minimaxAB(board, "black", 10, Integer.MIN_VALUE+1, Integer.MAX_VALUE);
			break;
		case 7:
			minimaxEX(board, "black", 8, Integer.MIN_VALUE+1, Integer.MAX_VALUE, false);
			break;
			}
	}
	

	private List<Node> generateBlackMoves(String[][] field) {
		
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
		return legal;
	}

	
	private List<Node> generateWhiteMoves(String[][] field) {
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		for(int i=3; i<9; i++) {
			if(field[3][i]!=" "){
				for(int r=0; r<4; r++){
					for(int c=0; c<3; c++){
						if(field[r][c]==" "){
							legal.add(new Node(3, i, r, c));
						}
					}
				}
			}
		}
			
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){					
				if(field[r][c]=="P"){
					r2 = r-1;
					c2 = c;
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(field, r2, c2, "white"))){
						legal.add(new Node(r, c, r2, c2));
					}
				}
				else if(field[r][c]=="R"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
				else if(field[r][c]=="K"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c]=="B"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(field[r][c]=="Q"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "white"))&&
						   (Capture.check(field, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
			}
		}
		return legal;
	}

	private boolean winPositionBlack(String[][] field, String turn)  {		
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(field[r][c]=="K"){
					a = 2;
				}
				if(field[r][c]=="k"){
					b = 1;
				}
			}
		}		
		return((a+b==1) || ((a+b==3 & turn=="black") & 
			   (field[3][0]=="k"||field[3][1]=="k"||field[3][2]=="k")));
	}
	
	private boolean winPositionWhite(String[][] field, String turn)  {		
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(field[r][c]=="K"){
					a = 2;
				}
				if(field[r][c]=="k"){
					b = 1;
				}
			}
		}		
		return((a+b==2) || ((a+b==3 & turn=="white") & 
			   (field[0][0]=="K"||field[0][1]=="K"||field[0][2]=="K")));
	}	
	
	private int evaluationMaterial(String[][] field, boolean exp) {
		
		int score = 0;

		for(int r=0; r<4; r++){
			for(int c=0; c<field[r].length; c++){
				if(field[r][c]!=" "){
					switch(field[r][c]){
						case "p":
							score += exp ? Pawn.getValue()*10 : Pawn.getValue();
							break;
						case "r":
							score += exp ? Rook.getValue()*10 : Rook.getValue();
							break;
						case "b":
							score += exp ? Bishop.getValue()*10 : Bishop.getValue();
							break;
						case "q":
							score += exp ? Queen.getValue()*10 : Queen.getValue();
							break;
						case "P":
							score += -Pawn.getValue();
							break;
						case "R":
							score += -Rook.getValue();
							break;
						case "B":
							score += -Bishop.getValue();
							break;
						case "Q":
							score += -Queen.getValue();
							break;
						}
					}
				}
			}
		return score;
	}

	private int evaluationPositional(String[][] field) {		
		
		int score = 0;
		int r,c,r2,c2;
	
		for(r=0; r<4; r++){
			for(c=0; c<3; c++){					
				if(field[r][c]=="p"){
					r2 = r+1;
					c2 = c;
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(field, r2, c2, "black"))){
						score += (Capture.attack(field, r2, c2, "black"));
					}
				}
				
				else if(field[r][c]=="r"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							score += (Capture.attack(field, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(field[r][c]=="k"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							score += (Capture.attack(field, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(field[r][c]=="b"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "black"))){
							score += (Capture.attack(field, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(field[r][c]=="q"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "black"))&&
						   (Capture.check(field, r2, c2, "black"))){
							score += (Capture.attack(field, r2, c2, "black"));
							}
						}							
					}
				}
	
				else if(field[r][c]=="P"){
					r2 = r-1;
					c2 = c;
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(field, r2, c2, "white"))){
						score += (Capture.attack(field, r2, c2, "white"));
					}
				}
				
				else if(field[r][c]=="R"){				
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "white"))){
							score += (Capture.attack(field, r2, c2, "white"));
							}
						}							
					}
				}
				
				else if(field[r][c]=="K"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "white"))){
							score += (Capture.attack(field, r2, c2, "white"));
							}
						}							
					}
				}
				
				else if(field[r][c]=="B"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(field, r2, c2, "white"))){
							score += (Capture.attack(field, r2, c2, "white"));
							}
						}							
					}
				}
	
				else if(field[r][c]=="Q"){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "white"))&&
						   (Capture.check(field, r2, c2, "white"))){
							score += (Capture.attack(field, r2, c2, "black"));
							}
						}							
					}
				}
			}
		}						
		return score;
	}

	private boolean check(String[][] field, String turn) {		
		
			int r,c,r2,c2;
			
		if(turn=="black"){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(field[r][c]=="p"){
						r2 = r+1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (field[r2][c2]=="K")){
							return true;
						}
					}
					
					else if(field[r][c]=="r"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}

					else if(field[r][c]=="k"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="b"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="q"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "black"))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}
				}
			}
		}
				
		else if(turn=="white"){
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(field[r][c]=="P"){
						r2 = r-1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (field[r2][c2]=="k")){
							return true;
						}
					}
					
					else if(field[r][c]=="R"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="K"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}

					else if(field[r][c]=="B"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="Q"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "white"))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}
				}
			}
		}
	return false;
	}

	private boolean unsafe(String[][] field, String turn) {		
		
			int r,c,r2,c2;
		
		if(turn=="white"){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(field[r][c]=="p"){
						r2 = r+1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (field[r2][c2]=="K")){
							return true;
						}
					}
					
					else if(field[r][c]=="r"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="k"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}

					else if(field[r][c]=="b"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="q"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "black"))&&
								(field[r2][c2]=="K")){
								return true;
								}
							}							
						}
					}
				}
			}
		}
				
		else if(turn=="black"){
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(field[r][c]=="P"){
						r2 = r-1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (field[r2][c2]=="k")){
							return true;
						}
					}
					
					else if(field[r][c]=="R"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="K"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}

					else if(field[r][c]=="B"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c]=="Q"){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "white"))&&
								(field[r2][c2]=="k")){
								return true;
								}
							}							
						}
					}
				}
			}
		}
	return false;
	}
	
	private int max(List<Integer> scores) {	

		int max = Integer.MIN_VALUE+1;
		for(int i=0; i<scores.size();i++){
			if (scores.get(i)>max){
				max = scores.get(i);
			}
		}	
	return max;	
	}	
	
	private int min(List<Integer> scores) {
			
		int min = Integer.MAX_VALUE;
		for (int i=0; i<scores.size();i++){
			if (scores.get(i)<min){
				min = scores.get(i);
			}
		}	
	return min;
	}
	
	private int exp(List<Integer> scores) {
		
		return scores.stream().reduce(0, Integer::sum) / scores.size();	
		}
	
	private int alpha(List<Integer> scores, int alpha, int beta) {
		
		for(int i=0; i<scores.size();i++){
			if (scores.get(i)>alpha){
				alpha = scores.get(i);
			}
			if(alpha>=beta){
				break;
				}
		}
		return alpha;
	}
	
	private int beta(List<Integer> scores, int alpha, int beta) {
		
		for(int i=0; i<scores.size();i++){
			if (scores.get(i)<beta){
				beta = scores.get(i);
			}
			if(alpha>=beta){
				break;
				}
		}
		return beta;
	}

	private List<Node> sortingMoveList(String[][] field, List<Node> legal,
			String turn, boolean prune) {
		
		List<Node> sorted = new ArrayList<>();
		
		int prev;
			if(check(field, "white")){
				prev = -1000;
			}
			else if(check(field, "black")){
				prev = 1000;
			}
			else{
				prev = evaluationMaterial(field, false) + evaluationPositional(field);
		}
	
	if(turn=="black"){	
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
				if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
					prom = "p";
					c3 = Capture.take(field, r2, c2);
					temp = field[r2][c2];
					field[r2][c2] = "q";
					field[r][c] = " ";
				}
				else if(field[r][c]=="p" & (r==0 & (c==4||c==7))){
					prom = " ";
					temp = field[r2][c2];
					field[r2][c2] = "p";
					field[r][c] = " ";
				}
				else{
					prom = " ";
					c3 = Capture.take(field, r2, c2);
					temp = field[r2][c2];
					field[r2][c2] = field[r][c];
					field[r][c] = " ";
				}
				
				int value;				
				if(temp=="K"){
					value = 2000;	
					}
				else if((field[3][0]=="k"||field[3][1]=="k"||field[3][2]=="k") &
						check(field, "white")==false){
					value = 1000;
				}				
				else if(check(field, "white")){
					value = -1000;
				}
				else{
					if(prune)
					value = evaluationMaterial(field, false) + evaluationPositional(field);
					else
					value = evaluationMaterial(field, false);
				}
				
				legal.get(i).setValue(value);
				sorted.add(legal.get(i));
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}			
			}

		Collections.sort(sorted, Collections.reverseOrder());
		
		if(prune){
			sorted.removeIf(e -> e.getValue() < prev);
		}		
	}
	
	else{		
		for(int i=0; i<legal.size(); i++){

				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
		
				r3 = 3;
				if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
					prom = "P";
					c3 = Capture.take(field, r2, c2);
					temp = field[r2][c2];
					field[r2][c2] = "Q";
					field[r][c] = " ";
				}
				else if(field[r][c]=="P" & (r==3 & (c==4||c==7))){
					prom = " ";
					temp = field[r2][c2];
					field[r2][c2] = "P";
					field[r][c] = " ";
				}
				else{
					prom = " ";
					c3 = Capture.take(field, r2, c2);
					temp = field[r2][c2];
					field[r2][c2] = field[r][c];
					field[r][c] = " ";
				}
		
				int value;				
				if(temp=="k"){
					value = -2000;	
					}
				else if((field[0][0]=="K"||field[0][1]=="K"||field[0][2]=="K") &
						check(field, "black")==false){
					value = -1000;
				}				
				else if(check(field, "black")){
					value = 1000;
				}
				else{
					if(prune)
					value = evaluationMaterial(field, false) + evaluationPositional(field);
					else
					value = evaluationMaterial(field, false);
				}
				
				legal.get(i).setValue(value);
				sorted.add(legal.get(i));
				
				if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
			}			

		Collections.sort(sorted);
		
		if(prune){
			sorted.removeIf(e -> e.getValue() > prev);
		}
	}	
		return sorted;
	}

	// minimax with capture and check extensions
	private int minimaxEX(String[][] field, String turn, int depth,
			int alpha, int beta, boolean node) {
		
		if(turn == "white" && MoveList.repeat(field)){
			return 0;
		}
		
		if(hash.repeat(field, turn, depth)){
			return 0;
		}
		
		hash.add(field, turn, depth);
		
		if(winPositionBlack(field, turn)){
			return 2000+(depth*10);	
			}
		if(winPositionWhite(field, turn)){
			return -(2000+(depth*10));	
			}	
		
		if(check(field, turn) && depth < 8){
			if(turn=="white"){
				return -(1000+(depth*10));
			}
			else if(turn=="black"){
				return 1000+(depth*10);
			}
		}
	
		if(node==false & depth<=1){
			return evaluationMaterial(field, false);
			}
		
		if(depth == -3){
			return  evaluationMaterial(field, false);
			}
		
		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();

		if(depth == 8){
			legal.add(root);
		}
		else if(depth != 8 && turn=="black"){
			start = generateBlackMoves(field);
			legal.addAll(sortingMoveList(field, start, "black", false));			
		}
		else if(depth != 8 && turn=="white"){
			start = generateWhiteMoves(field);
			legal.addAll(sortingMoveList(field, start, "white", false));			
		}
		
		ArrayList<Integer> scores = new ArrayList<>();
		
		for(int i=0; i<legal.size(); i++){

				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
				
				node = false;
												
				if(turn=="black"){
					
					if(field[r][c]=="k"){
						node = unsafe(field, "black");
						}
					
					r3 = 0;
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";
					}
					else if(field[r][c]=="p" & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "p";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					
					if(node==false){
						node = (Capture.extend(temp, "black")||Capture.prom(field, r2, c2, "black")
							||check(field, "black"));
						}
			
					int value = minimaxEX(field, "white", depth-1, alpha, beta, node);
					if(value > alpha){
						alpha = value;
						scores.add(value);
						}
					if(depth==8){
						root.setValue(value);
						Integrator.mergeMoves(root);
						hash.clear();
						}										
				}				
				else{
					
					if(field[r][c]=="K"){
						node = unsafe(field, "white");
						}
					
					r3 = 3;
					if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "Q";
						field[r][c] = " ";
					}
					else if(field[r][c]=="P" & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "P";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					
					if(node==false){
					node = (Capture.extend(temp, "white")||Capture.prom(field, r2, c2, "white")
							||check(field, "white"));
					}
						
					int value = minimaxEX(field, "black", depth-1, alpha, beta, node);
					if(value < beta){
						beta = value;
						scores.add(value);
						}
				}
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
/*				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
*/				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				if(alpha >= beta){
					break;
					}
			}
		
		if(turn=="black"){
			return alpha(scores, alpha, beta);
		}
		else{
			return beta(scores, alpha, beta);
		}
	}

	// minimax algorithm with alpha-beta pruning
	private int minimaxAB(String[][] field, String turn, int depth, int alpha, int beta) {
		
		if(turn == "white" && MoveList.repeat(field)){
			return 0;
		}
		
		if(hash.repeat(field, turn, depth)){
			return 0;
		}
		
		hash.add(field, turn, depth);
		
		if(winPositionBlack(field, turn)){
			return 2000+(depth*10);	
			}
		if(winPositionWhite(field, turn)){
			return -(2000+(depth*10));	
			}	
		
		if(check(field, turn) && depth < 10){
			if(turn=="white"){
				return -(1000+(depth*10));
			}
			else if(turn=="black"){
				return 1000+(depth*10);
			}
		}
	
		if(depth==1){
			return  evaluationMaterial(field, false);
			}

		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();

		if(depth == 10){
			legal.add(root);
		}
		else if(depth != 10 && turn=="black"){
			start = generateBlackMoves(field);
			legal.addAll(sortingMoveList(field, start, "black", false));			
		}
		else if(depth != 10 && turn=="white"){
			start = generateWhiteMoves(field);
			legal.addAll(sortingMoveList(field, start, "white", false));			
		}
		
		ArrayList<Integer> scores = new ArrayList<>();
		
		for(int i=0; i<legal.size(); i++){

				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
												
				if(turn=="black"){					
					r3 = 0;
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";
					}
					else if(field[r][c]=="p" & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "p";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
	
					int value = minimaxAB(field, "white", depth-1, alpha, beta);
					if(value > alpha){
						alpha = value;
						scores.add(value);
						}
					if(depth==10){
						root.setValue(value);
						Integrator.mergeMoves(root);
						hash.clear();
						}										
				}				
				else{				
					r3 = 3;
					if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "Q";
						field[r][c] = " ";
					}
					else if(field[r][c]=="P" & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "P";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
				
					int value = minimaxAB(field, "black", depth-1, alpha, beta);
					if(value < beta){
						beta = value;
						scores.add(value);
						}
				}
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
/*				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
*/				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				if(alpha >= beta){
					break;
					}
			}
		
		if(turn=="black"){
			return alpha(scores, alpha, beta);
		}
		else{
			return beta(scores, alpha, beta);
		}
	}
	
	// basic minimax algorithm
	private int minimax(String[][] field, String turn, int depth) {
		
		if(turn == "white" && MoveList.repeat(field)){
			return 0;
		}
		
		if(winPositionBlack(field, turn)){
			return 2000+(depth*100);
			}
		if(winPositionWhite(field, turn)){
			return -(2000+(depth*100));
			}
		
		if(check(field, turn) && depth < 6){
			if(turn=="white"){
				return -(1000+(depth*100));
			}
			else if(turn=="black"){
				return 1000+(depth*100);
			}
		}

		if(depth==1){
			return evaluationMaterial(field, false);
			}
		
		List<Node> legal = new ArrayList<>();
		
		if(depth == 6){
			legal.add(root);
		}
		else if(depth != 6 && turn=="black"){
		legal = generateBlackMoves(field);
		}
		else if(depth != 6 && turn=="white"){
		legal = generateWhiteMoves(field);
		}
				
		List<Integer> scores = new ArrayList<>();
		
		for(int i=0; i<legal.size(); i++){
	
				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
												
				if(turn=="black"){
					r3 = 0;
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";	
					}
					else if(r==0 & (c>2 & c<9)){
						prom = " ";
						temp = field[r][c];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					
					int value = minimax(field, "white", depth-1);
						scores.add(value);
					if(depth==6){
						root.setValue(value);
						Integrator.mergeMoves(root);
						}
				}				
				else{
					r3 = 3;
					if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "Q";
						field[r][c] = " ";	
					}
					else if(r==3 & (c>2 & c<9)){
						prom = " ";
						temp = field[r][c];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					
					int value = minimax(field, "black", depth-1);
						scores.add(value);	
				}
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
			}
		
		if(turn=="black"){
			return max(scores);
		}
		else{
			return min(scores);
		}
	}

	// expectimax (a.k.a. poker) algorithm
	private int expectimax(String[][] field, String turn, int depth) {
		
		if(turn == "white" && MoveList.repeat(field)){
			return 0;
		}
		
		if(winPositionBlack(field, turn)){
			return 100/(depth);
			}
		if(winPositionWhite(field, turn)){
			return -(2000*depth);
			}
		
		if(check(field, turn) && depth < 6){
			if(turn=="white"){
				return -(2000*depth);
			}
			else if(turn=="black"){
				return 50/depth;
			}
		}
		
		if(depth==1){
			return evaluationMaterial(field, true)/depth;
			}
		
		List<Node> legal = new ArrayList<>();
		
		if(depth == 6){
			legal.add(root);
		}
		else if(depth != 6 && turn=="black"){
		legal = generateBlackMoves(field);
		}
		else if(depth != 6 && turn=="white"){
		legal = generateWhiteMoves(field);
		}
		
		List<Integer> scores = new ArrayList<>();
		
		for(int i=0; i<legal.size(); i++){
	
				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
												
				if(turn=="black"){
					r3 = 0;
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";	
					}
					else if(field[r][c]=="p" & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "p";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					
					int value = expectimax(field, "white", depth-1);
						scores.add(value);
					if(depth==6){
						root.setValue(value);
						Integrator.mergeMoves(root);
						}
				}				
				else{
					r3 = 3;
					if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "Q";
						field[r][c] = " ";	
					}
					else if(field[r][c]=="P" & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "P";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					
					int value = expectimax(field, "black", depth-1);
						scores.add(value);	
				}
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
			}
		
		if(turn=="black"){
			return max(scores);
		}
		else{
			return exp(scores);
		}
	}

	// gambling algorithm
	private int breadthMM(Map<String[][], List<Node>> plate, String turn, int depth){
		
		Map<String[][], List<Node>> nodes = new HashMap<>();
		List<Integer> scores = new ArrayList<>();

		if(depth>1){
		for(String[][] field: plate.keySet()){
			List<Node> legal = plate.get(field);
			
		for(int i=0; i<legal.size(); i++){	
				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
				
				if(turn=="black"){
					r3 = 0;
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";	
					}
					else if(r==0 & (c>2 & c<9)){
						prom = " ";
						temp = field[r][c];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					nodes.putIfAbsent(Copier.deepCopy(field), generateWhiteMoves(field));
					
					if(temp=="K"){
						scores.add(2000+(depth*100));
					}
					else if(unsafe(field, turn)){
						scores.add(-(1000+(depth*100)));
					}
					else{
						scores.add(evaluationMaterial(field, false));
					}
				}				
				else{
					r3 = 3;
					if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "Q";
						field[r][c] = " ";	
					}
					else if(r==3 & (c>2 & c<9)){
						prom = " ";
						temp = field[r][c];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					nodes.putIfAbsent(Copier.deepCopy(field), generateBlackMoves(field));
					
					if(temp=="k"){
						scores.add(-(2000+(depth*100)));
					}
					else if(unsafe(field, turn)){
						scores.add(1000+(depth*100));
					}
					else{
						scores.add(evaluationMaterial(field, false));
					}
				}
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
					}
				}
			}
			int value;
			if(turn=="black"){
				value = breadthMM(nodes, "white", depth-1);
				if(depth==4){
					root.setValue(value);
					Integrator.mergeMoves(root);
					}
			}
			else{
				value = breadthMM(nodes, "black", depth-1);
			}
		}				
			if(turn=="black"){
				return max(scores);
			}
			else{
				return min(scores);
			}

	}
	
	private int forward(String[][] field, String turn, int depth) {
		
		if(turn == "white" && MoveList.repeat(field)){
			return 0;
		}
		
		if(winPositionBlack(field, turn)){
			return 2000+(depth*100);
			}
		if(winPositionWhite(field, turn)){
			return -(2000+(depth*100));
			}
		
		if(check(field, turn) && depth < 6){
			if(turn=="white"){
				return -(1000+(depth*100));
			}
			else if(turn=="black"){
				return 1000+(depth*100);
			}
		}
	
		if(depth==1){
			return evaluationMaterial(field, false) + evaluationPositional(field);
			}
		
		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();
	
		if(turn=="black"){
			start = generateBlackMoves(field);
			legal.addAll(sortingMoveList(field, start, "black", true));			
		}
		else if(turn=="white"){
			start = generateWhiteMoves(field);
			legal.addAll(sortingMoveList(field, start, "white", true));			
		}
				
		ArrayList<Integer> scores = new ArrayList<>();
		
		for(int i=0; i<legal.size(); i++){

				int r = legal.get(i).getR();
				int c = legal.get(i).getC();
				int r2 = legal.get(i).getR2();
				int c2 = legal.get(i).getC2();
				String temp;
				String prom;
				int r3;
				int c3 = 9;
												
				if(turn=="black"){
					r3 = 0;
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";	
					}
					else if(r==0 & (c>2 & c<9)){
						prom = " ";
						temp = field[r][c];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					
					int value = forward(field, "white", depth-1);
						scores.add(value);
					if(depth==6){
						legal.get(i).setValue(value);
						moves.add(legal.get(i));
						}
				}				
				else{
					r3 = 3;
					if((field[r][c]=="P" & (r!=3 & (c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "Q";
						field[r][c] = " ";	
					}
					else if(r==3 & (c>2 & c<9)){
						prom = " ";
						temp = field[r][c];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
					
					int value = forward(field, "black", depth-1);
						scores.add(value);	
				}
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if(prom == "P" & field[r2][c2]=="Q"){
					field[r][c] = "P";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
			}
		
		if(turn=="black"){
			return max(scores);
		}
		else{
			return min(scores);
		}
	}

	private void greedy(String[][] field) {
		
		List<Node> legal = new ArrayList<>();
		legal = generateBlackMoves(field);
		
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
	
					if((field[r][c]=="p" & (r!=0 & (c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = "q";
						field[r][c] = " ";	
					}
					else if(field[r][c]=="p" & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = field[r2][c2];
						field[r2][c2] = "p";
						field[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(field, r2, c2);
						temp = field[r2][c2];
						field[r2][c2] = field[r][c];
						field[r][c] = " ";	
					}
				
				if(temp=="K"){
					score = 1000;
				}
				else if(check(field, "white")){
					score = -1000;
				}
				else{
					score = evaluationMaterial(field, false);
				}
				legal.get(i).setValue(score);
				moves.add(legal.get(i));
				
				if(prom == "p" & field[r2][c2]=="q"){
					field[r][c] = "p";
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
				else{
					field[r][c] = field[r2][c2];
					field[r2][c2] = temp;
					Capture.undo(field, r3, c3);
				}
			}			
		}

	private void random(String[][] field) {
		
		List<Node> legal = new ArrayList<>();
		legal = generateBlackMoves(field);
		
		int i = (int)(Math.random()*legal.size());
		
		legal.get(i).setValue(Integer.MIN_VALUE+2);
		moves.add(legal.get(i));
	}

	private void sendMovelist(){
		
		if(moves.isEmpty()){
			random(board);
		}
		Integrator.mergeMoves(moves);
	}
	
}
