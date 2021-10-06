package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
	
	private HashTabs hash;
	private List<Node> moves;
	
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
			moves = new ArrayList<>();
			random();
			sendMovelist();			
			break;
		case 1:
			expectimax("black", 6);
			break;		
		case 2:
			moves = new ArrayList<>();
			greedy();
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
			moves = new ArrayList<>();
			forward("black", 6);
			sendMovelist();
			break;
		case 5:
			minimax("black", 6);
			break;
		case 6:
			hash = new HashTabs();
			minimaxAB("black", 8, Integer.MIN_VALUE+1, Integer.MAX_VALUE);
			break;
		case 7:
			hash = new HashTabs();
			minimaxEX("black", 6, Integer.MIN_VALUE+1, Integer.MAX_VALUE, false);
			break;
			}
	}
	

	private List<Node> generateBlackMoves(String[][] board) {
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		for(int i=3; i<9; i++) {
			if(!board[0][i].equals(" ")){
				for(int r=0; r<4; r++){
					for(int c=0; c<3; c++){
						if(board[r][c].equals(" ")){
							legal.add(new Node(0, i, r, c));
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
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(board, r2, c2, "black"))){
						legal.add(new Node(r, c, r2, c2));
					}
				}				
				else if(board[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(board[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(board[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(board[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "black"))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
			}
		}
		return legal;
	}

	
	private List<Node> generateWhiteMoves(String[][] board) {
		
		List<Node> legal = new ArrayList<>();
		
		int r2, c2;

		for(int i=3; i<9; i++) {
			if(!board[3][i].equals(" ")){
				for(int r=0; r<4; r++){
					for(int c=0; c<3; c++){
						if(board[r][c].equals(" ")){
							legal.add(new Node(3, i, r, c));
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
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(board, r2, c2, "white"))){
						legal.add(new Node(r, c, r2, c2));
					}
				}
				else if(board[r][c].equals("R")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
				else if(board[r][c].equals("K")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(board[r][c].equals("B")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}				
				else if(board[r][c].equals("Q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "white"))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2));
							}
						}							
					}
				}
			}
		}
		return legal;
	}

	private boolean winPositionBlack(String[][] board, String turn)  {		
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c].equals("K")){
					a = 2;
				}
				if(board[r][c].equals("k")){
					b = 1;
				}
			}
		}		
		return((a+b==1) || ((a+b==3 & turn.equals("black")) & 
			   (board[3][0].equals("k")||board[3][1].equals("k")||board[3][2].equals("k"))));
	}
	
	private boolean winPositionWhite(String[][] board, String turn)  {		
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c].equals("K")){
					a = 2;
				}
				if(board[r][c].equals("k")){
					b = 1;
				}
			}
		}		
		return((a+b==2) || ((a+b==3 & turn.equals("white")) & 
			   (board[0][0].equals("K")||board[0][1].equals("K")||board[0][2].equals("K"))));
	}	
	
	private int evaluationMaterial(String[][] board, boolean exp) {
		
		int score = 0;

		for(int r=0; r<4; r++){
			for(int c=0; c<board[r].length; c++){
				if(!board[r][c].equals(" ")){
					switch(board[r][c]){
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

	private int evaluationPositional(String[][] board) {		
		
		int score = 0;
		int r,c,r2,c2;
	
		for(r=0; r<4; r++){
			for(c=0; c<3; c++){					
				if(board[r][c].equals("p")){
					r2 = r+1;
					c2 = c;
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(board, r2, c2, "black"))){
						score += (Capture.attack(board, r2, c2, "black"));
					}
				}
				
				else if(board[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "black"))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
	
				else if(board[r][c].equals("P")){
					r2 = r-1;
					c2 = c;
					if((Pawn.move(r, c, r2, c2))&&
					   (Capture.check(board, r2, c2, "white"))){
						score += (Capture.attack(board, r2, c2, "white"));
					}
				}
				
				else if(board[r][c].equals("R")){				
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Rook.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("K")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((King.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("B")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Bishop.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
	
				else if(board[r][c].equals("Q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Queen.move(r, c, r2, c2, "white"))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
			}
		}						
		return score;
	}

	private boolean check(String[][] board, String turn) {		
		
			int r,c,r2,c2;
			
		if(turn.equals("black")){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("K"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("r")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("k")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("b")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "black"))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
				}
			}
		}
				
		else{
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("P")){
						r2 = r-1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("k"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("R")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("K")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("B")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("Q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "white"))&&
								(board[r2][c2].equals("k"))){
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

	private boolean unsafe(String[][] board, String turn) {		
		
			int r,c,r2,c2;
		
		if(turn.equals("white")){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("K"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("r")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("k")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("b")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "black"))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
				}
			}
		}
				
		else{
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("P")){
						r2 = r-1;
						c2 = c;
						if((Pawn.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("k"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("R")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Rook.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("K")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((King.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("B")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Bishop.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("Q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Queen.move(r, c, r2, c2, "white"))&&
								(board[r2][c2].equals("k"))){
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
		return scores.stream().reduce(Integer.MIN_VALUE+1, Integer::max);
	}	
	
	private int min(List<Integer> scores) {		
		return scores.stream().reduce(Integer.MAX_VALUE, Integer::min);
	}
	
	private int exp(List<Integer> scores) {		
		return scores.stream().reduce(0, Integer::sum) / scores.size();	
	}
	
	private int alpha(List<Integer> scores, int alpha, int beta) {
		
		for(int i=0; i<scores.size(); i++){
			if (scores.get(i) > alpha){
				alpha = scores.get(i);
			}
			if(alpha >= beta){
				break;
				}
		}
		return alpha;
	}
	
	private int beta(List<Integer> scores, int alpha, int beta) {
		
		for(int i=0; i<scores.size(); i++){
			if (scores.get(i) < beta){
				beta = scores.get(i);
			}
			if(alpha >= beta){
				break;
				}
		}
		return beta;
	}

	private List<Node> sortingMoveList(String[][] board, List<Node> legal,
			String turn, boolean prune) {
		
		List<Node> sorted = new ArrayList<>();
		
		int prev;
			if(check(board, "white")){
				prev = -1000;
			}
			else if(check(board, "black")){
				prev = 1000;
			}
			else{
				prev = evaluationMaterial(board, false) + evaluationPositional(board);
		}
	
		if(turn.equals("black")){	
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
					if((board[r][c].equals("p")&(r!=0&(c!=4||c!=7)))&(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";
					}
					else if(board[r][c].equals("p") & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "p";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					
					int value;				
					if(temp.equals("K")){
						value = 2000;	
						}
					else if((board[3][0].equals("k")||board[3][1].equals("k")||board[3][2].equals("k"))
							&check(board, "white")==false){
						value = 1000;
					}				
					else if(check(board, "white")){
						value = -1000;
					}
					else{
						if(prune) {
						value = evaluationMaterial(board, false) + evaluationPositional(board);
						}
						else {
						value = evaluationMaterial(board, false);
						}
					}
					
					legal.get(i).setValue(value);
					sorted.add(legal.get(i));
					
					if(prom.equals("p") & board[r2][c2].equals("q")){
						board[r][c] = "p";
						board[r2][c2] = temp;
						Capture.undo(board, r3, c3);
					}
					else{
						board[r][c] = board[r2][c2];
						board[r2][c2] = temp;
						Capture.undo(board, r3, c3);
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
					if((board[r][c].equals("P")&(r!=3&(c!=4||c!=7)))&(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";
					}
					else if(board[r][c].equals("P") & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "P";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
			
					int value;				
					if(temp.equals("k")){
						value = -2000;	
						}
					else if((board[0][0].equals("K")||board[0][1].equals("K")||board[0][2].equals("K"))
							&check(board, "black")==false){
						value = -1000;
					}				
					else if(check(board, "black")){
						value = 1000;
					}
					else{
						if(prune) {
						value = evaluationMaterial(board, false) + evaluationPositional(board);
						}
						else {
						value = evaluationMaterial(board, false);
						}
					}
					
					legal.get(i).setValue(value);
					sorted.add(legal.get(i));
					
					if(prom.equals("P") & board[r2][c2].equals("Q")){
						board[r][c] = "P";
						board[r2][c2] = temp;
						Capture.undo(board, r3, c3);
					}
					else{
						board[r][c] = board[r2][c2];
						board[r2][c2] = temp;
						Capture.undo(board, r3, c3);
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
	private int minimaxEX(String turn, int depth, int alpha, int beta, boolean node) {
		
		if(turn.equals("white") && MoveList.repeat(board)){
			return 0;
		}
		
		if(hash.repeat(board, turn, depth)){
			return 0;
		}
		
		hash.add(board, turn, depth);
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*10);	
			}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*10));	
			}	
		
		if(check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*10));
			}
			else if(turn.equals("black")){
				return 1000+(depth*10);
			}
		}
	
		if(node==false & depth<=1){
			return evaluationMaterial(board, false);
			}
		
		if(depth == -3){
			return  evaluationMaterial(board, false);
			}
		
		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();

		if(depth == 6){
			legal.add(root);
		}
		else if(depth != 6 && turn.equals("black")){
			start = generateBlackMoves(board);
			legal.addAll(sortingMoveList(board, start, "black", false));			
		}
		else if(depth != 6 && turn.equals("white")){
			start = generateWhiteMoves(board);
			legal.addAll(sortingMoveList(board, start, "white", false));			
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
												
				if(turn.equals("black")){
					
					if(board[r][c].equals("k")){
						node = unsafe(board, "black");
						}
					
					r3 = 0;
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";
					}
					else if(board[r][c].equals("p") & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "p";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					
					if(node==false){
						node = (Capture.extend(temp, "black")||Capture.prom(board, r2, c2, "black")
							||check(board, "black"));
						}
			
					int value = minimaxEX("white", depth-1, alpha, beta, node);
					if(value > alpha){
						alpha = value;
						scores.add(value);
						}
					if(depth == 6){
						root.setValue(value);
						Integrator.mergeMoves(root);
						hash.clear();
						}										
				}				
				else{
					
					if(board[r][c].equals("K")){
						node = unsafe(board, "white");
						}
					
					r3 = 3;
					if((board[r][c].equals("P")&(r!=3 & (c!=4||c!=7)))&
										(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";
					}
					else if(board[r][c].equals("P") & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "P";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					
					if(node==false){
					node = (Capture.extend(temp, "white")||Capture.prom(board, r2, c2, "white")
							||check(board, "white"));
					}
						
					int value = minimaxEX("black", depth-1, alpha, beta, node);
					if(value < beta){
						beta = value;
						scores.add(value);
						}
				}
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if(prom.equals("P") & board[r2][c2].equals("Q")){
					board[r][c] = "P";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
/*				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
*/				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				if(alpha >= beta){
					break;
					}
			}
		
		if(turn.equals("black")){
			return alpha(scores, alpha, beta);
		}
		else{
			return beta(scores, alpha, beta);
		}
	}

	// minimax algorithm with alpha-beta pruning
	private int minimaxAB(String turn, int depth, int alpha, int beta) {
		
		if(turn.equals("white") && MoveList.repeat(board)){
			return 0;
		}
		
		if(hash.repeat(board, turn, depth)){
			return 0;
		}
		
		hash.add(board, turn, depth);
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*10);	
			}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*10));	
			}	
		
		if(check(board, turn) && depth < 8){
			if(turn.equals("white")){
				return -(1000+(depth*10));
			}
			else if(turn.equals("black")){
				return 1000+(depth*10);
			}
		}
	
		if(depth == 1){
			return  evaluationMaterial(board, false);
			}

		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();

		if(depth == 8){
			legal.add(root);
		}
		else if(depth != 8 && turn.equals("black")){
			start = generateBlackMoves(board);
			legal.addAll(sortingMoveList(board, start, "black", false));			
		}
		else if(depth != 8 && turn.equals("white")){
			start = generateWhiteMoves(board);
			legal.addAll(sortingMoveList(board, start, "white", false));			
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
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";
					}
					else if(board[r][c].equals("p") & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "p";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
	
					int value = minimaxAB("white", depth-1, alpha, beta);
					if(value > alpha){
						alpha = value;
						scores.add(value);
						}
					if(depth == 8){
						root.setValue(value);
						Integrator.mergeMoves(root);
						hash.clear();
						}										
				}				
				else{				
					r3 = 3;
					if((board[r][c].equals("P")&(r!=3 & (c!=4||c!=7)))&
										(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";
					}
					else if(board[r][c].equals("P") & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "P";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
				
					int value = minimaxAB("black", depth-1, alpha, beta);
					if(value < beta){
						beta = value;
						scores.add(value);
						}
				}
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if(prom.equals("P") & board[r2][c2].equals("Q")){
					board[r][c] = "P";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
/*				else if((r==0 || r==3) & (c>2 & c<9)){
					field[r][c] = temp;
					field[r2][c2] = " ";
				}
*/				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				if(alpha >= beta){
					break;
					}
			}
		
		if(turn.equals("black")){
			return alpha(scores, alpha, beta);
		}
		else{
			return beta(scores, alpha, beta);
		}
	}
	
	// basic minimax algorithm
	private int minimax(String turn, int depth) {
		
		if(turn.equals("white") && MoveList.repeat(board)){
			return 0;
		}
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*100);
			}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*100));
			}
		
		if(check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else if(turn.equals("black")){
				return 1000+(depth*100);
			}
		}

		if(depth == 1){
			return evaluationMaterial(board, false);
			}
		
		List<Node> legal = new ArrayList<>();
		
		if(depth == 6){
			legal.add(root);
		}
		else if(depth != 6 && turn=="black"){
		legal = generateBlackMoves(board);
		}
		else if(depth != 6 && turn=="white"){
		legal = generateWhiteMoves(board);
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
												
				if(turn.equals("black")){
					r3 = 0;
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";	
					}
					else if(r==0 & (c>2 & c<9)){
						prom = " ";
						temp = board[r][c];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					
					int value = minimax("white", depth-1);
						scores.add(value);
					if(depth == 6){
						root.setValue(value);
						Integrator.mergeMoves(root);
						}
				}				
				else{
					r3 = 3;
					if((board[r][c].equals("P")&(r!=3 & (c!=4||c!=7)))&
										(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";	
					}
					else if(r==3 & (c>2 & c<9)){
						prom = " ";
						temp = board[r][c];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					
					int value = minimax("black", depth-1);
						scores.add(value);	
				}
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if(prom.equals("P") & board[r2][c2].equals("Q")){
					board[r][c] = "P";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					board[r][c] = temp;
					board[r2][c2] = " ";
				}
				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
			}
		
		if(turn.equals("black")){
			return max(scores);
		}
		else{
			return min(scores);
		}
	}

	// expectimax (a.k.a. poker) algorithm
	private int expectimax(String turn, int depth) {
		
		if(turn.equals("white") && MoveList.repeat(board)){
			return 0;
		}
		
		if(winPositionBlack(board, turn)){
			return 100/(depth);
			}
		if(winPositionWhite(board, turn)){
			return -(2000*depth);
			}
		
		if(check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(2000*depth);
			}
			else if(turn.equals("black")){
				return 50/depth;
			}
		}
		
		if(depth == 1){
			return evaluationMaterial(board, true)/depth;
			}
		
		List<Node> legal = new ArrayList<>();
		
		if(depth == 6){
			legal.add(root);
		}
		else if(depth != 6 && turn.equals("black")){
		legal = generateBlackMoves(board);
		}
		else if(depth != 6 && turn.equals("white")){
		legal = generateWhiteMoves(board);
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
												
				if(turn.equals("black")){
					r3 = 0;
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";	
					}
					else if(board[r][c].equals("p") & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "p";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					
					int value = expectimax("white", depth-1);
						scores.add(value);
					if(depth == 6){
						root.setValue(value);
						Integrator.mergeMoves(root);
						}
				}				
				else{
					r3 = 3;
					if((board[r][c].equals("P")&(r!=3 & (c!=4||c!=7)))&
										(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";	
					}
					else if(board[r][c].equals("P") & (r==3 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "P";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					
					int value = expectimax("black", depth-1);
						scores.add(value);	
				}
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if(prom.equals("P") & board[r2][c2].equals("Q")){
					board[r][c] = "P";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					board[r][c] = temp;
					board[r2][c2] = " ";
				}
				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
			}
		
		if(turn.equals("black")){
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

		if(depth > 1){
		for(String[][] board: plate.keySet()){
			List<Node> legal = plate.get(board);
			
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
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";	
					}
					else if(r==0 & (c>2 & c<9)){
						prom = " ";
						temp = board[r][c];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					nodes.putIfAbsent(Copier.deepCopy(board), generateWhiteMoves(board));
					
					if(temp.equals("K")){
						scores.add(2000+(depth*100));
					}
					else if(unsafe(board, turn)){
						scores.add(-(1000+(depth*100)));
					}
					else{
						scores.add(evaluationMaterial(board, false));
					}
				}				
				else{
					r3 = 3;
					if((board[r][c].equals("P")&(r!=3 & (c!=4||c!=7)))&
										(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";	
					}
					else if(r==3 & (c>2 & c<9)){
						prom = " ";
						temp = board[r][c];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					nodes.putIfAbsent(Copier.deepCopy(board), generateBlackMoves(board));
					
					if(temp.equals("k")){
						scores.add(-(2000+(depth*100)));
					}
					else if(unsafe(board, turn)){
						scores.add(1000+(depth*100));
					}
					else{
						scores.add(evaluationMaterial(board, false));
					}
				}
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if(prom.equals("P") & board[r2][c2].equals("Q")){
					board[r][c] = "P";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					board[r][c] = temp;
					board[r2][c2] = " ";
				}
				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
					}
				}
			}
			int value;
			if(turn.equals("black")){
				value = breadthMM(nodes, "white", depth-1);
				if(depth == 4){
					root.setValue(value);
					Integrator.mergeMoves(root);
					}
			}
			else{
				value = breadthMM(nodes, "black", depth-1);
			}
		}				
			if(turn.equals("black")){
				return max(scores);
			}
			else{
				return min(scores);
			}

	}
	
	// minimax with vintage forward pruning
	private int forward(String turn, int depth) {
		
		if(turn.equals("white" )&& MoveList.repeat(board)){
			return 0;
		}
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*100);
			}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*100));
			}
		
		if(check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else if(turn.equals("black")){
				return 1000+(depth*100);
			}
		}
	
		if(depth == 1){
			return evaluationMaterial(board, false) + evaluationPositional(board);
			}
		
		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();
	
		if(turn.equals("black")){
			start = generateBlackMoves(board);
			legal.addAll(sortingMoveList(board, start, "black", true));			
		}
		else if(turn.equals("white")){
			start = generateWhiteMoves(board);
			legal.addAll(sortingMoveList(board, start, "white", true));			
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
												
				if(turn.equals("black")){
					r3 = 0;
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";	
					}
					else if(r==0 & (c>2 & c<9)){
						prom = " ";
						temp = board[r][c];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					
					int value = forward("white", depth-1);
						scores.add(value);
					if(depth == 6){
						legal.get(i).setValue(value);
						moves.add(legal.get(i));
						}
				}				
				else{
					r3 = 3;
					if((board[r][c].equals("P")&(r!=3 & (c!=4||c!=7)))&
										(r2==0&(c2==0||c2==1||c2==2))){
						prom = "P";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "Q";
						board[r][c] = " ";	
					}
					else if(r==3 & (c>2 & c<9)){
						prom = " ";
						temp = board[r][c];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
					
					int value = forward("black", depth-1);
						scores.add(value);	
				}
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if(prom.equals("P") & board[r2][c2].equals("Q")){
					board[r][c] = "P";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else if((r==0 || r==3) & (c>2 & c<9)){
					board[r][c] = temp;
					board[r2][c2] = " ";
				}
				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
			}
		
		if(turn.equals("black")){
			return max(scores);
		}
		else{
			return min(scores);
		}
	}

	// greedy algorithm
	private void greedy() {
		
		List<Node> legal = new ArrayList<>();
		legal = generateBlackMoves(board);
		
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
	
					if((board[r][c].equals("p")&(r!=0 & (c!=4||c!=7)))&
										(r2==3&(c2==0||c2==1||c2==2))){
						prom = "p";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = "q";
						board[r][c] = " ";	
					}
					else if(board[r][c].equals("p") & (r==0 & (c==4||c==7))){
						prom = " ";
						temp = board[r2][c2];
						board[r2][c2] = "p";
						board[r][c] = " ";
					}
					else{
						prom = " ";
						c3 = Capture.take(board, r2, c2);
						temp = board[r2][c2];
						board[r2][c2] = board[r][c];
						board[r][c] = " ";	
					}
				
				if(temp.equals("K")){
					score = 1000;
				}
				else if(check(board, "white")){
					score = -1000;
				}
				else{
					score = evaluationMaterial(board, false);
				}
				legal.get(i).setValue(score);
				moves.add(legal.get(i));
				
				if(prom.equals("p") & board[r2][c2].equals("q")){
					board[r][c] = "p";
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
				else{
					board[r][c] = board[r2][c2];
					board[r2][c2] = temp;
					Capture.undo(board, r3, c3);
				}
			}			
		}
	
	// randomly moving algorithm
	private void random() {
		
		List<Node> legal = new ArrayList<>();
		legal = generateBlackMoves(board);
		
		int i = new Random().nextInt(legal.size());
		
		legal.get(i).setValue(Integer.MIN_VALUE+2);
		moves.add(legal.get(i));
	}

	private void sendMovelist(){
		
		if(moves.isEmpty()){
			random();
		}
		Integrator.mergeMoves(moves);
	}
	
}
