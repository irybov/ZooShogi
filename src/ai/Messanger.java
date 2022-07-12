package ai;

import sound.Sound;
import ui.Gui;
import utilpack.Examiner;
import utilpack.Turn;

public class Messanger {

	private Sound sound = Sound.getInstance();
	private boolean mutedVolume = false;	
	private boolean checkWarningEnabled = true;
	
	public void setVolumeMute(boolean mute) {
		this.mutedVolume = mute;
	}
	public void setCheckWarning(boolean warn) {
		this.checkWarningEnabled = warn;
	}

	void output(int score, char[][] field, String name,
			int c, String col, int r, char spot, String col2, int r2){

		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
		Gui.output.setText(name+" "+(c>2?"drops":"from "+col+(r+1))+
				(spot==(' ')?" to ":" takes on ")+col2+(r2+1));
		if(!mutedVolume){
		sound.playVoice(name);
			if(spot!=(' ')){
				sound.playVoice("takes");			
			}
			sound.playVoice(col2+Integer.toString(r2+1));
			if(checkWarningEnabled & Examiner.isCheck(field, Turn.BLACK)){
				sound.playVoice("check");
			}
		}		
	}
	
}
