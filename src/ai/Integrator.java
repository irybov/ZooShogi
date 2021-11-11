package ai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import control.Clocks;
import sound.Sound;
import ui.Gui;
import util.Capture;
import util.Copier;
import util.Message;
import util.Pieces;


public class Integrator {
	
	private static volatile Integrator INSTANCE;
	
	public static Integrator getInstance(){
		
		if(INSTANCE == null) {
			synchronized (Integrator.class) {
				if(INSTANCE == null) {
					INSTANCE = new Integrator();
				}
			}
		}		
		return INSTANCE;
	}
	
	private List<Node> moves = new CopyOnWriteArrayList<>();
	
	private boolean warn = true;
	public void setWarn(boolean warn){
		this.warn = warn;		
	}
	
	private boolean mute = false;
	public void setMute(boolean mute){
		this.mute = mute;		
	}
	
	private Sound sound = Sound.getInstance();
	
	private Deque<String> game = new ArrayDeque<>();
	private Set<String> exp;
	
	boolean getNote(String[][] field) {
		String note = Copier.keyMaker(field);
		return exp.contains(note);
	}
	public void newGame() {
		game.clear();
	}
	
	{
		File file = new File("experience.bin");
		if(!file.exists()) {
			try {
				file.createNewFile();
				exp = new CopyOnWriteArraySet<>();
			    try(FileOutputStream fos = new FileOutputStream("experience.bin");
			    		ObjectOutputStream oos = new ObjectOutputStream(fos)){
			           oos.writeObject(exp);
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(FileInputStream fis = new FileInputStream("experience.bin");
	    		ObjectInputStream ois = new ObjectInputStream(fis)){
	    	exp = (Set<String>) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	// adds results from a single thread
	public void mergeMoves(List<Node> input){		
		moves.addAll(input);
	}
	
	// merges results from multiple threads
	public void mergeMoves(Node input){		
		moves.add(input);
	}
	
	// do external engine's move
	public String[][] activate(String[][] field, int r, int c, int r2, int c2,
														int score, int nodes){

		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		Clocks.setNodes(nodes);
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

		if(field[r][c].equals("p") & r==2){
			if(r==0 & c > 2){
				field[r2][c2] = "p";
				field[r][c] = " ";
			}	
			else{
				Capture.take(field, r2, c2);
				field[r2][c2] = "q";
				field[r][c] = " ";
			}
		}
		else{
			Capture.take(field, r2, c2);
			field[r2][c2] = field[r][c];
			field[r][c] = " ";
		}
		
		String col = Message.colName(c);
		String col2 = Message.colName(c2);
		
		output(field, pieceName,c,col,r,spot,col2,r2);		
		return field;
	}
	
	// selects and makes best move
	public String[][] activate(String[][] field) {
		
		int score = Integer.MIN_VALUE+1;
		int r = -1;
		int c = -1;
		int r2 = -1;
		int c2 = -1;
		
		Node move;
		
		if(moves.size() == 1) {
			move = moves.get(0);
		}
		else {
			ArrayList<Node> random = new ArrayList<>();
			for(int i=0; i<moves.size(); i++) {
				if(score <= moves.get(i).getValue()){
					score = moves.get(i).getValue();				
					random.add(moves.get(i));
				}
			}
			
			Collections.sort(random, Collections.reverseOrder());
			
			int prev = random.get(0).getValue();
			random.removeIf(e -> e.getValue() < prev);
			
			int trap = random.get(0).getTrap();
			random.removeIf(e -> e.getTrap() < trap);
			
			if(random.size()==1){
				move = random.get(0);
			}
			else{
				move = random.get(new Random().nextInt(random.size()));
			}
		}
		score = move.getValue();
		r = move.getR();
		c = move.getC();
		r2 = move.getR2();
		c2 = move.getC2();	
		
		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

		if(field[r][c].equals("p") & r==2){
			if(r==0 & c > 2){
				field[r2][c2] = "p";
				field[r][c] = " ";
			}	
			else{
				Capture.take(field, r2, c2);
				field[r2][c2] = "q";
				field[r][c] = " ";
			}
		}
		else{
			Capture.take(field, r2, c2);
			field[r2][c2] = field[r][c];
			field[r][c] = " ";
		}
		
		if(score < -500) {
			if(!exp.contains(game.peek())) {
				exp.add(game.peek());
			    try(FileOutputStream fos = new FileOutputStream("experience.bin");
			    		ObjectOutputStream oos = new ObjectOutputStream(fos)){
			           oos.writeUnshared(exp);
			           oos.flush();
			           oos.reset();
			    }			
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		else {
			game.push(Copier.keyMaker(field));
		}
		
		String col = Message.colName(c);
		String col2 = Message.colName(c2);
		
		output(field, pieceName,c,col,r,spot,col2,r2);		
		return field;
	}
		
	private void output(String[][] field, String name,
						int c, String col, int r, String spot, String col2, int r2){
		
		Gui.output.setText(name+" "+(c>2?"drops":"from "+col+(r+1))+
				(spot.equals(" ")?" to ":" takes on ")+col2+(r2+1));
		if(!mute){
		sound.voice(name);
			if(!spot.equals(" ")){
				sound.voice("takes");			
			}
		sound.voice(col2+Integer.toString(r2+1));
			if(warn & check(field)){
				sound.voice("check");
			}
		}		
		moves.clear();
	}
	
	private boolean check(String[][] field) {		
		
		int r,c,r2,c2;
			
			for(r=0; r<4; r++){
				for(c=0; c<3; c++){					
					if(field[r][c].equals("p")){
						r2 = r+1;
						c2 = c;
						if((Pieces.BPAWN.move(r, c, r2, c2))&&
						   (field[r2][c2].equals("K"))){
							return true;
						}
					}
					
					else if(field[r][c].equals("r")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.ROOK.move(r, c, r2, c2))&&
								(field[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
	
					else if(field[r][c].equals("k")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.KING.move(r, c, r2, c2))&&
								(field[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c].equals("b")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BISHOP.move(r, c, r2, c2))&&
								(field[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
					
					else if(field[r][c].equals("q")){						
						for(r2=r-1; r2<r+2; r2++){
							for(c2=c-1; c2<c+2; c2++){
							if((Pieces.BQUEEN.move(r, c, r2, c2))&&
								(field[r2][c2].equals("K"))){
								return true;
								}
							}							
						}
					}
				}
			}				
	return false;
	}
	
}
