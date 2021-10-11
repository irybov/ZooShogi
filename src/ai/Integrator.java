package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import sound.Sound;
import ui.Gui;
import util.Capture;
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

	// merges results from different threads
	public void mergeMoves(List<Node> input){		
		moves.addAll(input);
	}
	
	// merges results from different threads
	public void mergeMoves(Node input){		
		moves.add(input);
	}
	
	// sends data to move selector
	public String[][] activate(String[][] field){
		return best(moves, field);
	}
	
	// do external engine's move
	public String[][] activate(String[][] field, int r, int c, int r2, int c2, int score){

		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

		if(field[r][c].equals("p") & (r2==3 & (c2==0||c2==1||c2==2))){
			if(r==0 & (c==4||c==7)){
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
		
		output(pieceName,c,col,r,spot,col2,r2);
		if(!mute){
		if(warn & check(field)){
			sound.voice("check");
			}
		}	
	
		moves.clear();
		MoveList.add(field);
		
		return field;
	}
	
	// selects best move
	private String[][] best(List<Node> spots, String[][] field) {
		
		int score = Integer.MIN_VALUE+1;
		int r = -1;
		int c = -1;
		int r2 = -1;
		int c2 = -1;

		ArrayList<Node> random = new ArrayList<>();
		for(int i=0; i<spots.size(); i++) {
			if(score <= spots.get(i).getValue()){
				score = spots.get(i).getValue();				
				random.add(spots.get(i));
			}
		}
		
		Collections.sort(random, Collections.reverseOrder());
		
		int prev = random.get(0).getValue();
		random.removeIf(e -> e.getValue() < prev);
		
		if(random.size()==1){
			
			r = random.get(0).getR();
			c = random.get(0).getC();
			r2 = random.get(0).getR2();
			c2 = random.get(0).getC2();	
		}
		else{
			int i = new Random().nextInt(random.size());
			
			r = random.get(i).getR();
			c = random.get(i).getC();
			r2 = random.get(i).getR2();
			c2 = random.get(i).getC2();	
		}
		
		if(score==Integer.MIN_VALUE+2) {
			score = 0;
		}
		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

		if(field[r][c].equals("p") & (r2==3 & (c2==0||c2==1||c2==2))){
			if(r==0 & (c==4||c==7)){
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
		
		output(pieceName,c,col,r,spot,col2,r2);
		if(!mute){
		if(warn & check(field)){
			sound.voice("check");
			}
		}	
	
		moves.clear();
		MoveList.add(field);
		
		return field;
	}
		
	private void output(String name, int c, String col, int r, String spot, String col2, int r2){
		
		Gui.output.setText(name+" "+(c>2?"drops":"from "+col+(r+1))+
				(spot.equals(" ")?" to ":" takes on ")+col2+(r2+1));
		if(!mute){
		sound.voice(name);
		if(!spot.equals(" ")){
			sound.voice("takes");			
		}
		sound.voice(col2+String.valueOf(r2+1));
		}
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
