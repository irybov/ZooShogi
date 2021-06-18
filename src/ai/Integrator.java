package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sound.Sound;
import ui.Gui;
import util.Bishop;
import util.Capture;
import util.King;
import util.Message;
import util.Pawn;
import util.Queen;
import util.Rook;

public class Integrator {
	
	private static List<Node> moves = new ArrayList<>();
	
	private boolean warn = true;
	public void setWarn(boolean warn){
		this.warn = warn;		
	}
	
	private boolean mute = false;
	public void setMute(boolean mute){
		this.mute = mute;		
	}
	
	private Sound sound;
	public Integrator(Sound sound) {
		this.sound = sound;
	}

	// merges results from different threads
	public static synchronized void mergeMoves(List<Node> input){
		
		moves.addAll(input);
	}
	
	// merges results from different threads
	public static synchronized void mergeMoves(Node input){
		
		moves.add(input);
	}
	
	// sends data to move selector
	public String[][] activate(String[][] field){

		return best(moves, field);
	}
	
/*	// sends data to move selector
	public String[][] activateAB(String[][] field){
		
		return bestAB(moves, field);
	}
*/
	// selects best move
	private String[][] best(List<Node> spots, String[][] field) {
		
		int score = Integer.MIN_VALUE+1;
		int r = -1;
		int c = -1;
		int r2 = -1;
		int c2 = -1;

		ArrayList<Node> random = new ArrayList<>();
		for(int i=0; i<spots.size(); i++) {
			if(score<=spots.get(i).getValue()){
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
			int i = (int)(Math.random()*random.size());
			
			r = random.get(i).getR();
			c = random.get(i).getC();
			r2 = random.get(i).getR2();
			c2 = random.get(i).getC2();	
		}
		
		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

	if(field[r][c]=="p" & (r2==3 & (c2==0||c2==1||c2==2))){
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
	
/*	// selects best move
	private String[][] bestAB(List<Node> spots, String[][] field) {
		
		int score = Integer.MIN_VALUE+1;
		int r = -1;
		int c = -1;
		int r2 = -1;
		int c2 = -1;

		for(int i=0; i<spots.size(); i++) {
			if(score<spots.get(i).getValue()){
				score = spots.get(i).getValue();				
				r = spots.get(i).getR();
				c = spots.get(i).getC();
				r2 = spots.get(i).getR2();
				c2 = spots.get(i).getC2();
			}
		}

		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));

		String spot = field[r2][c2];
		String pieceName = Message.pieceName(field[r][c]);

	if(field[r][c]=="p" & (r2==3 & (c2==0||c2==1||c2==2))){
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
*/	
	private void output(String name, int c, String col, int r, String spot, String col2, int r2){
		
		Gui.output.setText(name+" "+(c>2?"drops":"from "+col+(r+1))+(spot==" "?" to ":" takes on ")+col2+(r2+1));
		if(!mute){
		sound.voice(name);
		if(spot != " "){
			sound.voice("takes");			
		}
		sound.voice(col2+String.valueOf(r2+1));
		}
	}
	
	private static boolean check(String[][] field) {		
		
		int r,c,r2,c2;
			
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
	return false;
	}
	
}