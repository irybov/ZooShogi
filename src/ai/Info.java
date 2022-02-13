package ai;

import sound.Sound;
import ui.Gui;
import utilpack.Examiner;

public class Info {

	private Sound sound = Sound.getInstance();
	private boolean mutedVolume = false;	
	private boolean checkWarningEnabled = true;
	
	public void setVolumeMute(boolean mute) {
		this.mutedVolume = mute;
	}
	public void setCheckWarning(boolean warn) {
		this.checkWarningEnabled = warn;
	}

	void output(int score, String[][] field, String name,
			int c, String col, int r, String spot, String col2, int r2){

		Gui.score.setText(score > 0 ? "+" + Integer.toString(score) : Integer.toString(score));
		
		Gui.output.setText(name+" "+(c>2?"drops":"from "+col+(r+1))+
				(spot.equals(" ")?" to ":" takes on ")+col2+(r2+1));
		if(!mutedVolume){
		sound.playVoice(name);
			if(!spot.equals(" ")){
				sound.playVoice("takes");			
			}
			sound.playVoice(col2+Integer.toString(r2+1));
			if(checkWarningEnabled & Examiner.isCheck(field, "black")){
				sound.playVoice("check");
			}
		}		
	}
	
}
