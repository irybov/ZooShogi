package sound;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.*;

import control.Clocks;

public class Sound {
	
	private static final Sound INSTANCE = new Sound();
	
	public static Sound getInstance(){
		return INSTANCE;
	}
	
	private Clocks clocks = Clocks.getInstance();
	
	static FloatControl volume;
	static float gain;	
	public static void setVol(float g){
		gain = g;
		}
	
	public void voice(String action){

		try(AudioInputStream ais = AudioSystem.getAudioInputStream(new File(bank(action)))) {
			
			Clip audioClip = AudioSystem.getClip();
			audioClip.open(ais);
	        volume = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(gain);
				audioClip.start();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private String bank(String action) throws InterruptedException{
		
		clocks.setTurn(" ");
		
		String beep = " ";
		
		switch(action){
			case "Pawn":
				beep = "sound/voice/pieces/pawn.wav";
				break;
			case "Rook":
				beep = "sound/voice/pieces/rook.wav";
				break;
			case "Bishop":
				beep = "sound/voice/pieces/bishop.wav";
				break;
			case "King":
				beep = "sound/voice/pieces/king.wav";
				break;
			case "Queen":
				beep = "sound/voice/pieces/queen.wav";
				break;
			case "check":				
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/actions/check.wav";
				break;
			case "mate":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/actions/mate.wav";
				break;
			case "draw":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/actions/draw.wav";
				break;
			case "takes":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/actions/takes.wav";
				break;
			case "A1":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/a1.wav";
				break;
			case "A2":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/a2.wav";
				break;
			case "A3":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/a3.wav";
				break;
			case "A4":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/a4.wav";
				break;
			case "B1":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/b1.wav";
				break;
			case "B2":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/b2.wav";
				break;
			case "B3":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/b3.wav";
				break;
			case "B4":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/b4.wav";
				break;
			case "C1":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/c1.wav";
				break;
			case "C2":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/c2.wav";
				break;
			case "C3":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/c3.wav";
				break;
			case "C4":
				TimeUnit.SECONDS.sleep(1);
				beep = "sound/voice/squares/c4.wav";
				break;
		}
		return beep;
	}
}
