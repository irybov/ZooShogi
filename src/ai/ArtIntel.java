package ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import control.Clocks;
import utilpack.Capture;
import utilpack.Copier;
import utilpack.Examiner;
import utilpack.Pieces;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

public class ArtIntel implements Runnable{
	
	private HashTabs hash;
	private List<Node> moves;
	
	private Node root;
	
	private final int level;
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
	
	private final Integrator integrator = Integrator.getInstance();
	
	private int count = 0;
	
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
			hash = new HashTabs();
			expectimax("black", 6);
			Clocks.addNodes(count);
			break;		
		case 2:
			moves = new ArrayList<>();
			greedy();
			sendMovelist();
			break;
		case 3:
			trappy(board, 1);
			break;			
		case 4:
			moves = new ArrayList<>();
			forward("black", 6);
			Clocks.addNodes(count);
			sendMovelist();
			break;
		case 5:
			hash = new HashTabs();
			minimax("black", 6);
			Clocks.addNodes(count);
			break;
		case 6:
			hash = new HashTabs();
			minimaxAB("black", 8, Integer.MIN_VALUE+1, Integer.MAX_VALUE);
			Clocks.addNodes(count);
			break;
		case 7:
			hash = new HashTabs();
			minimaxEX("black", 6, Integer.MIN_VALUE+1, Integer.MAX_VALUE, false);
			Clocks.addNodes(count);
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
							legal.add(new Node(0, i, r, c, "black"));
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
					   (Capture.check(board, r2, c2, "black"))){
						legal.add(new Node(r, c, r2, c2, "black"));
					}
				}				
				else if(board[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}				
				else if(board[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}				
				else if(board[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
							}
						}							
					}
				}				
				else if(board[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BQUEEN.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							legal.add(new Node(r, c, r2, c2, "black"));
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
							legal.add(new Node(3, i, r, c, "white"));
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
					   (Capture.check(board, r2, c2, "white"))){
						legal.add(new Node(r, c, r2, c2, "white"));
					}
				}
				else if(board[r][c].equals("R")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2, "white"));
							}
						}							
					}
				}
				else if(board[r][c].equals("K")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2, "white"));
							}
						}							
					}
				}				
				else if(board[r][c].equals("B")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2, "white"));
							}
						}							
					}
				}				
				else if(board[r][c].equals("Q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.WQUEEN.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							legal.add(new Node(r, c, r2, c2, "white"));
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
						score += exp ? Pieces.BPAWN.getValue()*10 : Pieces.BPAWN.getValue();
						break;
					case "r":
						score += exp ? Pieces.ROOK.getValue()*10 : Pieces.ROOK.getValue();
						break;
					case "b":
						score += exp ? Pieces.BISHOP.getValue()*10 : Pieces.BISHOP.getValue();
						break;
					case "q":
						score += exp ? Pieces.BQUEEN.getValue()*10 : Pieces.BQUEEN.getValue();
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

	private int evaluationPositional(String[][] board) {		
		
		int score = 0;
		int r,c,r2,c2;
	
		for(r=0; r<4; r++){
			for(c=0; c<3; c++){					
				if(board[r][c].equals("p")){
					r2 = r+1;
					c2 = c;
					if((Pieces.BPAWN.move(r, c, r2, c2))&&
					   (Capture.check(board, r2, c2, "black"))){
						score += (Capture.attack(board, r2, c2, "black"));
					}
				}
				
				else if(board[r][c].equals("r")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("k")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("b")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BQUEEN.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "black"))){
							score += (Capture.attack(board, r2, c2, "black"));
							}
						}							
					}
				}
	
				else if(board[r][c].equals("P")){
					r2 = r-1;
					c2 = c;
					if((Pieces.WPAWN.move(r, c, r2, c2))&&
					   (Capture.check(board, r2, c2, "white"))){
						score += (Capture.attack(board, r2, c2, "white"));
					}
				}
				
				else if(board[r][c].equals("R")){				
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.ROOK.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("K")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.KING.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
				
				else if(board[r][c].equals("B")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.BISHOP.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
	
				else if(board[r][c].equals("Q")){					
					for(r2=r-1; r2<r+2; r2++){
						for(c2=c-1; c2<c+2; c2++){
						if((Pieces.WQUEEN.move(r, c, r2, c2))&&
						   (Capture.check(board, r2, c2, "white"))){
							score += (Capture.attack(board, r2, c2, "white"));
							}
						}							
					}
				}
			}
		}						
		return score;
	}

	private boolean unsafe(String[][] board, String turn) {		
		
		int r,c,r2,c2;
		
		if(turn.equals("white")){	
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(board[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pieces.BPAWN.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("K"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("r")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("k")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("b")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
								(board[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BQUEEN.move(r, c, r2, c2))&&
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
						if((Pieces.WPAWN.move(r, c, r2, c2))&&
						   (board[r2][c2].equals("k"))){
							return true;
						}
					}
					
					else if(board[r][c].equals("R")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("K")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}

					else if(board[r][c].equals("B")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
								(board[r2][c2].equals("k"))){
								return true;
								}
							}							
						}
					}
					
					else if(board[r][c].equals("Q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.WQUEEN.move(r, c, r2, c2))&&
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
			if(Examiner.check(board, "white")){
				prev = -1000;
			}
			else if(Examiner.check(board, "black")){
				prev = 1000;
			}
			else{
				prev = evaluationMaterial(board, false) + evaluationPositional(board);
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
					value = 2000;	
				}
				else if(Examiner.winPromotion(board, "white") && !Examiner.check(board, "white")){
					value = 1000;
				}
				else if(Examiner.check(board, "black") && !Examiner.check(board, "white")) {
					value = 500;
				}
				else if(MoveList.repeat(board, "black")) {
					value = 0;
				}
				else if(Examiner.winPromotion(board, "black") && !Examiner.check(board, "black")){
					value = -1000;
				}
				else if(Examiner.check(board, "white")){
					value = -2000;
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
					value = -2000;	
				}
				else if(Examiner.winPromotion(board, "black") && !Examiner.check(board, "black")){
						value = -1000;
				}
				else if(Examiner.check(board, "white") && !Examiner.check(board, "black")) {
					value = -500;
				}
				else if(MoveList.repeat(board, "white")) {
					value = 0;
				}
				else if(Examiner.winPromotion(board, "white") && !Examiner.check(board, "white")){
						value = 1000;
				}
				else if(Examiner.check(board, "black")){
					value = 2000;
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

	// minimax with capture and check extensions
	private int minimaxEX(String turn, int depth, int alpha, int beta, boolean node) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}
		
		if(turn.equals("white") && MoveList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MoveList.repeat(board, "white")){
			return 0;
		}
		
		if(hash.repeat(board, turn, depth)){
			return 0;
		}		
		hash.add(board, turn, depth);
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*100);	
		}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*100));	
		}	
		
		if(Examiner.check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else{
				return 1000+(depth*100);
			}
		}
	
		if(node==false & depth < 2){
			return evaluationMaterial(board, false);
		}
		
		if(depth == -3){
			return evaluationMaterial(board, false);
		}
		
		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();

		if(depth == 6){
			legal.add(root);
		}
		else if(depth < 6){
			if(turn.equals("black")) {
				start = generateBlackMoves(board);
				legal = sortingMoveList(board, start, turn, false);
			}
			else {
				start = generateWhiteMoves(board);
				legal = sortingMoveList(board, start, turn, false);
			}
		}
		count += legal.size();
		
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
				r3 = 0;
				
				if(board[r][c].equals("k")){
					node = unsafe(board, "black");
				}
				
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
				
				if(node==false){
					node = (Capture.extend(temp, "black")||Capture.prom(board, r2, c2, "black")
															||Examiner.check(board, "black"));
				}
		
				int value = minimaxEX("white", depth-1, alpha, beta, node);
				if(value > alpha){
					alpha = value;
					scores.add(value);
				}
				if(depth == 6){
					root.setValue(value);
					integrator.mergeMoves(root);
					hash.clear();
				}										
			}				
			else{
				r3 = 3;
				
				if(board[r][c].equals("K")){
					node = unsafe(board, "white");
				}
				
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
				
				if(node==false){
				node = (Capture.extend(temp, "white")||Capture.prom(board, r2, c2, "white")
														||Examiner.check(board, "white"));
				}
					
				int value = minimaxEX("black", depth-1, alpha, beta, node);
				if(value < beta){
					beta = value;
					scores.add(value);
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
				Capture.undo(board, r3, c3);
				
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

	// minimax with alpha-beta pruning
	private int minimaxAB(String turn, int depth, int alpha, int beta) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}
		
		if(turn.equals("white") && MoveList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 8) && MoveList.repeat(board, "white")){
			return 0;
		}
		
		if(hash.repeat(board, turn, depth)){
			return 0;
		}		
		hash.add(board, turn, depth);
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*100);	
		}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*100));	
		}	
		
		if(Examiner.check(board, turn) && depth < 8){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else{
				return 1000+(depth*100);
			}
		}
	
		if(depth == 1){
			return evaluationMaterial(board, false);
		}

		List<Node> legal = new ArrayList<>();
		List<Node> start = new ArrayList<>();

		if(depth == 8){
			legal.add(root);
		}
		else if(depth < 8){
			if(turn.equals("black")) {
				start = generateBlackMoves(board);
				legal = sortingMoveList(board, start, turn, false);
			}
			else {
				start = generateWhiteMoves(board);
				legal = sortingMoveList(board, start, turn, false);
			}
		}
		count += legal.size();
		
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
				if(board[r][c].equals("p") & r==2){
					prom = "p";
					if(!board[r][c].equals(" ")) {
						c3 = Capture.take(board, r2, c2);
					}
					temp = board[r2][c2];
					board[r2][c2] = "q";
					board[r][c] = " ";
				}
				else if(r==0 & c > 2 ){
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

				int value = minimaxAB("white", depth-1, alpha, beta);
				if(value > alpha){
					alpha = value;
					scores.add(value);
				}
				if(depth == 8){
					root.setValue(value);
					integrator.mergeMoves(root);
					hash.clear();
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
			
				int value = minimaxAB("black", depth-1, alpha, beta);
				if(value < beta){
					beta = value;
					scores.add(value);
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
			Capture.undo(board, r3, c3);
			
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
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}
		
		if(turn.equals("white") && MoveList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MoveList.repeat(board, "white")){
			return 0;
		}
		
		if(turn.equals("black")){
			if(hash.repeat(board, turn, depth)){
				return 0;
			}		
			hash.add(board, turn, depth);
		}
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*100);
		}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*100));
		}
		
		if(Examiner.check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else{
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
		else if(depth < 6){
			if(turn.equals("black")) {
				legal = generateBlackMoves(board);
			}
			else {
				legal = generateWhiteMoves(board);				
			}
		}
		count += legal.size();
				
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
				
				int value = minimax("white", depth-1);
					scores.add(value);
				if(depth == 6){
					root.setValue(value);
					integrator.mergeMoves(root);
					hash.clear();
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
				
				int value = minimax("black", depth-1);
					scores.add(value);	
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
			Capture.undo(board, r3, c3);
		}
		
		if(turn.equals("black")){
			return max(scores);
		}
		else{
			return min(scores);
		}
	}

	// gambling (a.k.a. poker) algorithm
	private int expectimax(String turn, int depth) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -5000;
		}
		
		if(turn.equals("white") && MoveList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MoveList.repeat(board, "white")){
			return 0;
		}
		
		if(turn.equals("black")){
			if(hash.repeat(board, turn, depth)){
				return 0;
			}		
			hash.add(board, turn, depth);
		}
		
		if(winPositionBlack(board, turn)){
			return 100/depth;
		}
		if(winPositionWhite(board, turn)){
			return -(1000*depth);
		}
		
		if(Examiner.check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000*depth);
			}
			else{
				return 100/depth;
			}
		}
		
		if(depth == 1){
			return evaluationMaterial(board, true)/depth;
		}
		
		List<Node> legal = new ArrayList<>();
		
		if(depth == 6){
			legal.add(root);
		}
		else if(depth < 6){
			if(turn.equals("black")) {
				legal = generateBlackMoves(board);
			}
			else {
				legal = generateWhiteMoves(board);				
			}
		}
		count += legal.size();
		
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
				
				int value = expectimax("white", depth-1);
					scores.add(value);
				if(depth == 6){
					root.setValue(value);
					integrator.mergeMoves(root);
					hash.clear();
				}
			}				
			else{
				r3 = 3;
				if(board[r][c].equals("P") & r==1){
					prom = "P";
					c3 = Capture.take(board, r2, c2);
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
					c3 = Capture.take(board, r2, c2);
					temp = board[r2][c2];
					board[r2][c2] = board[r][c];
					board[r][c] = " ";	
				}
				
				int value = expectimax("black", depth-1);
					scores.add(value);	
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
			Capture.undo(board, r3, c3);
		}
		
		if(turn.equals("black")){
			return max(scores);
		}
		else{
			return exp(scores);
		}
	}

	// breadth-searching trappy minimax
	private void trappy(String[][] field, int depth) {

		String[][] board = Copier.deepCopy(field);
		
		Queue<String[][]> input = new LinkedList<>();
		input.add(board);		
		Queue<String[][]> poses = new LinkedList<>();
		List<Node> legal;
		
		Deque<Node> stack = new ArrayDeque<>();
		
		Queue<List<Node>> moves = new LinkedList<List<Node>>();
		
		while(depth < 6) {
			
			while(!input.isEmpty()) {
			poses.add(input.remove());
			}
			
			while(!poses.isEmpty()) {
				board = poses.remove();
				
				if(depth == 1){
					legal = new ArrayList<>(1);
					legal.add(root);
				}
				else {
					legal = new ArrayList<>(moves.remove());
				}
			
				for(int i=0; i<legal.size(); i++){	
					int r = legal.get(i).getR();
					int c = legal.get(i).getC();
					int r2 = legal.get(i).getR2();
					int c2 = legal.get(i).getC2();
					String temp = null;
					String prom = null;
					int r3;
					int c3 = 9;
					
					if(depth % 2 == 1){
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
						
						if(temp.equals("K")){
							legal.get(i).setValue(2000+(100/depth));
						}
						else if(Examiner.winPromotion(board, "white") &&
								!Examiner.check(board, "white")){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(MoveList.repeat(board, "black")) {
							legal.get(i).setValue(0);
						}
						else if(integrator.getNote(board)) {
							legal.get(i).setValue(-500);							
						}						
						else if(Examiner.winPromotion(board, "black") &&
								!Examiner.check(board, "black")){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(Examiner.check(board, "white")){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else{
							legal.get(i).setValue(evaluationMaterial(board, false));
								if(depth < 5) {
									input.add(Copier.deepCopy(board));
									legal.get(i).addChildren(generateWhiteMoves(board));
									for(Node child: legal.get(i).getChidren()) {
										child.addParent(legal.get(i));
									}
									moves.add(legal.get(i).getChidren());
								}
						}		
						legal.get(i).setDepth(depth);
						stack.push(legal.get(i));
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
						
						if(temp.equals("k")){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else if(Examiner.winPromotion(board, "black") &&
								!Examiner.check(board, "black")){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(MoveList.repeat(board, "white")) {
							legal.get(i).setValue(0);
						}
						else if(Examiner.winPromotion(board, "white") &&
								!Examiner.check(board, "white")){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(Examiner.check(board, "black")){
							legal.get(i).setValue(2000+(100/depth));
						}
						else{
							legal.get(i).setValue(evaluationMaterial(board, false));
								if(depth < 5) {
									input.add(Copier.deepCopy(board));
									legal.get(i).addChildren(generateBlackMoves(board));
									for(Node child: legal.get(i).getChidren()) {
										child.addParent(legal.get(i));
									}
									moves.add(legal.get(i).getChidren());
								}
						}		
						legal.get(i).setDepth(depth);
						stack.push(legal.get(i));
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
					Capture.undo(board, r3, c3);
				}
				legal = null;
			}		
			depth++;			
		}
		
		legal = null;
		input = null;
		poses = null;
		
		List<Integer> scores;

        for (Node move: stack) {
        	if(move.hasChildren()) {
        		scores = new ArrayList<>(move.getChidren().size());
        		for(Node child: move.getChidren()) {
        			scores.add(child.getValue());
        		}
                	if(move.getSide().equals("black")) {        				
                		move.setValue(min(scores));
                	}
                	else{
                		move.setValue(max(scores));
                	}
        	}
        }        
        for (Node move: stack) {
        	if(!move.hasChildren()) {        	
        		int x = move.getValue();
        		int y = 0;
        		if(move.hasParent()) {
        			Node current = move.getParent();
	        		while(current.hasParent()) {
	        			y += current.getValue();
	        			current = current.getParent();
	        		}
        		}
        		int newTrap = x-y;
        		if(root.getTrap() < newTrap) {
        			root.setTrap(newTrap);
        		}
        	}
        }        
		integrator.mergeMoves(root);
		Clocks.addNodes(stack.size());

	}
	
	// minimax with vintage forward pruning
	private int forward(String turn, int depth) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}
		
		if(turn.equals("white") && MoveList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MoveList.repeat(board, "white")){
			return 0;
		}
		
		if(winPositionBlack(board, turn)){
			return 2000+(depth*100);
		}
		if(winPositionWhite(board, turn)){
			return -(2000+(depth*100));
		}
		
		if(Examiner.check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else{
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
			legal = sortingMoveList(board, start, turn, true);
		}
		else{
			start = generateWhiteMoves(board);
			legal = sortingMoveList(board, start, turn, true);
		}
		count += legal.size();
				
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
				
				int value = forward("white", depth-1);
					scores.add(value);
				if(depth == 6){
					legal.get(i).setValue(value);
					moves.add(legal.get(i));
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
				
				int value = forward("black", depth-1);
					scores.add(value);	
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
			Capture.undo(board, r3, c3);
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
		
		List<Node> legal = new ArrayList<>(generateBlackMoves(board));
		
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
				score = 2000;
			}
			else if(Examiner.winPromotion(board, "white") && !Examiner.check(board, "white")){
				score = 1000;
			}
			else if(MoveList.repeat(board, "black")) {
				score = 0;
			}
			else if(integrator.getNote(board)) {
				score = -500;							
			}
			else if(Examiner.winPromotion(board, "black") && !Examiner.check(board, "black")){
				score = -1000;
			}
			else if(Examiner.check(board, "white")){
				score = -2000;
			}
			else{
				score = evaluationMaterial(board, false);
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
		
		List<Node> legal = new ArrayList<>(generateBlackMoves(board));
			
		moves.add(legal.get(new Random().nextInt(legal.size())));
		Clocks.setNodes(legal.size());		
	}

	private void sendMovelist(){
		
		if(moves.isEmpty()){
			random();
		}
		integrator.mergeMoves(moves);
	}
	
}
