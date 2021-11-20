package ai;

import sound.Sound;
import ui.Gui;
import util.Pieces;

public class Info {

	private Sound sound = Sound.getInstance();
	private boolean mute = false;	
	private boolean warn = true;
	
	public void setMute(boolean mute) {
		this.mute = mute;
	}
	public void setWarn(boolean warn) {
		this.warn = warn;
	}

	void output(int score, String[][] field, String name,
			int c, String col, int r, String spot, String col2, int r2){

		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
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
