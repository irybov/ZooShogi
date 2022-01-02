package ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

import control.Clocks;
import utilpack.Capture;
import utilpack.Copier;
import utilpack.Examiner;

import java.util.Deque;
import java.util.LinkedList;

public class ArtIntel implements Callable<Integer>{
	
	private HashTabs hash;	
	private final Generator generator = new Generator();
	private final Evaluator evaluator = new Evaluator();
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
	public Integer call(){		
		algorithmSelector();
		return root.getValue();
	}
	
	private void algorithmSelector(){

		hash = new HashTabs();
		switch(level){
		case 1:
			expectimax("black", 6, Arrays.asList(root));
			break;		
		case 3:
			trappy(board, 1);
			break;			
		case 4:
			forward("black", 6, Arrays.asList(root));
			break;
		case 5:
			minimax("black", 6, Arrays.asList(root));
			break;
		case 6:
			minimaxAB("black", 8, Integer.MIN_VALUE+1, Integer.MAX_VALUE, Arrays.asList(root));
			break;
		case 7:
			minimaxEX("black",6,Integer.MIN_VALUE+1,Integer.MAX_VALUE,false,Arrays.asList(root));
			break;
		}
		Clocks.addNodes(count);
	}
	
	// minimax with capture and check extensions
	private int minimaxEX(String turn, int depth, int alpha, int beta, boolean node,
			List<Node> legal) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}		
		if(turn.equals("white") && MovesList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MovesList.repeat(board, "white")){
			return 0;
		}		
		if(hash.repeat(board, turn, depth)){
			return 0;
		}		
		hash.add(board, turn, depth);
		
		if(Examiner.winPositionBlack(board, turn)){
			return 2000+(depth*100);	
		}
		if(Examiner.winPositionWhite(board, turn)){
			return -(2000+(depth*100));	
		}			
		if(Examiner.check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else {
				return 1000+(depth*100);				
			}
		}
	
		if(node==false & depth < 2){
			return evaluator.evaluationMaterial(board, false);
		}		
		if(depth == -3){
			return evaluator.evaluationMaterial(board, false);
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
			
			node = false;
											
			if(turn.equals("black")){
				r3 = 0;
				
				if(board[r][c].equals("k")){
					node = Examiner.check(board, "white");
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
					node = (Capture.extend(temp, turn)||Capture.prom(board, r2, c2, turn)
															||Examiner.check(board, turn));
				}		
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "K" & (node ? depth > -2 : depth > 2)) {
					children = generator.generateMoves(board, "white");
					sorted = generator.sortMoves(board, children, "white", false);
					for(Node child: sorted) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(sorted);
				}
				
				int value = minimaxEX("white", depth-1, alpha, beta, node, sorted);
				if(value > alpha){
					alpha = value;
					scores.add(value);
					legal.get(i).setValue(value);
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
					node = Examiner.check(board, "black");
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
				node = (Capture.extend(temp, turn)||Capture.prom(board, r2, c2, turn)
														||Examiner.check(board, turn));
				}
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "k" & (node ? depth > -2 : depth > 2)) {
					children = generator.generateMoves(board, "black");
					sorted = generator.sortMoves(board, children, "black", false);
					for(Node child: sorted) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(sorted);
				}
				
				int value = minimaxEX("black", depth-1, alpha, beta, node, sorted);
				if(value < beta){
					beta = value;
					scores.add(value);
					legal.get(i).setValue(value);
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
			return evaluator.alpha(scores, alpha, beta);
		}
		else{
			return evaluator.beta(scores, alpha, beta);
		}
	}

	// minimax with alpha-beta pruning
	private int minimaxAB(String turn, int depth, int alpha, int beta, List<Node> legal) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}		
		if(turn.equals("white") && MovesList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 8) && MovesList.repeat(board, "white")){
			return 0;
		}		
		if(hash.repeat(board, turn, depth)){
			return 0;
		}		
		hash.add(board, turn, depth);
		
		if(Examiner.winPositionBlack(board, turn)){
			return 2000+(depth*100);	
		}
		if(Examiner.winPositionWhite(board, turn)){
			return -(2000+(depth*100));	
		}			
		if(Examiner.check(board, turn) && depth < 6){
			if(turn.equals("white")){
				return -(1000+(depth*100));
			}
			else {
				return 1000+(depth*100);				
			}
		}
	
		if(depth == 1){
			return evaluator.evaluationMaterial(board, false);
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
				
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "K" & depth > 2) {
					children = generator.generateMoves(board, "white");
					sorted = generator.sortMoves(board, children, "white", false);
					for(Node child: sorted) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(sorted);
				}

				int value = minimaxAB("white", depth-1, alpha, beta, sorted);
				if(value > alpha){
					alpha = value;
					scores.add(value);
					legal.get(i).setValue(value);
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
			
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "k" & depth > 2) {
					children = generator.generateMoves(board, "black");
					sorted = generator.sortMoves(board, children, "black", false);
					for(Node child: sorted) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(sorted);
				}
				
				int value = minimaxAB("black", depth-1, alpha, beta, sorted);
				if(value < beta){
					beta = value;
					scores.add(value);
					legal.get(i).setValue(value);
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
			return evaluator.alpha(scores, alpha, beta);
		}
		else{
			return evaluator.beta(scores, alpha, beta);
		}
	}
	
	// basic minimax algorithm
	private int minimax(String turn, int depth, List<Node> legal) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}		
		if(turn.equals("white") && MovesList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MovesList.repeat(board, "white")){
			return 0;
		}		
		if(turn.equals("white")){
			if(hash.repeat(board, turn, depth)){
				return 0;
			}		
			hash.add(board, turn, depth);
		}
		
		if(Examiner.winPositionBlack(board, turn)){
			return 2000+(depth*100);
		}
		if(Examiner.winPositionWhite(board, turn)){
			return -(2000+(depth*100));
		}
		if(turn.equals("white") && depth < 6){
			if(Examiner.check(board, turn)){
				return -(1000+(depth*100));
			}
		}

		if(depth == 1){
			return evaluator.evaluationMaterial(board, false);
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
				
				List<Node> children = null;
				if(temp != "K" & depth > 2) {
					children = generator.generateMoves(board, "white");
					for(Node child: children) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(children);
				}
				
				int value = minimax("white", depth-1, children);
					scores.add(value);
					legal.get(i).setValue(value);
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
				
				List<Node> children = null;
				if(temp != "k" & depth > 2) {
					children = generator.generateMoves(board, "black");
					for(Node child: children) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(children);
				}
				
				int value = minimax("black", depth-1, children);
					scores.add(value);
					legal.get(i).setValue(value);
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
			return evaluator.max(scores);
		}
		else{
			return evaluator.min(scores);
		}
	}

	// gambling (a.k.a. poker) algorithm
	private int expectimax(String turn, int depth, List<Node> legal) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -5000;
		}		
		if(turn.equals("white") && MovesList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MovesList.repeat(board, "white")){
			return 0;
		}		
		if(turn.equals("white")){
			if(hash.repeat(board, turn, depth)){
				return 0;
			}		
			hash.add(board, turn, depth);
		}
		
		if(Examiner.winPositionBlack(board, turn)){
			return 100/depth;
		}
		if(Examiner.winPositionWhite(board, turn)){
			return -(1000*depth);
		}		
		if(turn.equals("white") && depth < 6){
			if(Examiner.check(board, turn)){
				return -(1000*depth);
			}
		}
		
		if(depth == 1){
			return evaluator.evaluationMaterial(board, true)/depth;
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
				
				List<Node> children = null;
				if(temp != "K" & depth > 2) {
					children = generator.generateMoves(board, "white");
					for(Node child: children) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(children);
				}
				
				int value = expectimax("white", depth-1, children);
					scores.add(value);
					legal.get(i).setValue(value);
				if(depth == 6){
					root.setValue(value/10);
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
				
				List<Node> children = null;
				if(temp != "k" & depth > 2) {
					children = generator.generateMoves(board, "black");
					for(Node child: children) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(children);
				}
				
				int value = expectimax("black", depth-1, children);
					scores.add(value);
					legal.get(i).setValue(value);
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
			return evaluator.max(scores);
		}
		else{
			return evaluator.exp(scores);
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
							
						hash.add(board, "black", depth+4);
						
						if(temp.equals("K")){
							legal.get(i).setValue(2000+(100/depth));
						}
						else if(Examiner.winPromotion(board, "black") &&
								!Examiner.check(board, "white")){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(MovesList.repeat(board, "black")) {
							legal.get(i).setValue(0);
						}
						else if(hash.repeat(board, "black", depth-4)){
							legal.get(i).setValue(0);
						}	
						else if(integrator.getNote(board)) {
							legal.get(i).setValue(-500);							
						}						
						else if(Examiner.winPromotion(board, "white")){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(Examiner.check(board, "white")){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else{
							legal.get(i).setValue(evaluator.evaluationMaterial(board, false));
							if(depth < 5) {
								input.add(Copier.deepCopy(board));
								legal.get(i).addChildren(generator.generateMoves(board, "white"));
								for(Node child: legal.get(i).getChidren()) {
									child.addParent(legal.get(i));
								}
								moves.add(legal.get(i).getChidren());
							}
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
						
						if(temp.equals("k")){
							legal.get(i).setValue(-(2000+(100/depth)));
						}
						else if(Examiner.winPromotion(board, "white") &&
								!Examiner.check(board, "black")){
							legal.get(i).setValue(-(1000+(100/depth)));
						}
						else if(MovesList.repeat(board, "white")) {
							legal.get(i).setValue(0);
						}
						else if(Examiner.winPromotion(board, "black")){
							legal.get(i).setValue(1000+(100/depth));
						}
						else if(Examiner.check(board, "black")){
							legal.get(i).setValue(2000+(100/depth));
						}
						else{
							legal.get(i).setValue(evaluator.evaluationMaterial(board, false));
							if(depth < 5) {
								input.add(Copier.deepCopy(board));
								legal.get(i).addChildren(generator.generateMoves(board, "black"));
								for(Node child: legal.get(i).getChidren()) {
									child.addParent(legal.get(i));
								}
								moves.add(legal.get(i).getChidren());
							}
						}
					}
					legal.get(i).setDepth(depth);
					stack.push(legal.get(i));
					
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
                	if(move.getDepth() % 2 == 1) {
                		move.setValue(evaluator.min(scores));
                	}
                	else{
                		move.setValue(evaluator.max(scores));
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
	private int forward(String turn, int depth, List<Node> legal) {
		
		if(turn.equals("white") && integrator.getNote(board)) {
			return -500;
		}		
		if(turn.equals("white") && MovesList.repeat(board, "black")){
			return 0;
		}
		if((turn.equals("black") && depth < 6) && MovesList.repeat(board, "white")){
			return 0;
		}		
		if(turn.equals("white")){
			if(hash.repeat(board, turn, depth)){
				return 0;
			}		
			hash.add(board, turn, depth);
		}
		
		if(Examiner.winPositionBlack(board, turn)){
			return 2000+(depth*100);
		}
		if(Examiner.winPositionWhite(board, turn)){
			return -(2000+(depth*100));
		}		
		if(turn.equals("white") && depth < 6){
			if(Examiner.check(board, turn)){
				return -(1000+(depth*100));
			}
		}
	
		if(depth == 1){
			return evaluator.evaluationMaterial(board, false)
					+ evaluator.evaluationPositional(board);
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
				
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "K" & depth > 2) {
					children = generator.generateMoves(board, "white");
					sorted = generator.sortMoves(board, children, "white", true);
					for(Node child: sorted) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(sorted);
				}

				int value = forward("white", depth-1, sorted);
					scores.add(value);
					legal.get(i).setValue(value);
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
				
				List<Node> children = null;
				List<Node> sorted = null;
				if(temp != "k" & depth > 2) {
					children = generator.generateMoves(board, "black");
					sorted = generator.sortMoves(board, children, "black", false);
					for(Node child: sorted) {
						child.addParent(legal.get(i));
					}
					legal.get(i).addChildren(sorted);
				}
				
				int value = forward("black", depth-1, sorted);
					scores.add(value);
					legal.get(i).setValue(value);
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
			return evaluator.max(scores);
		}
		else{
			return evaluator.min(scores);
		}
	}
	
}
