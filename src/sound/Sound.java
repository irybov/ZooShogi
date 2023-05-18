package sound;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.*;

import control.Clocks;
import utilpack.Turn;

public class Sound {
	
	private static final Sound INSTANCE = new Sound();
	
	public static Sound getInstance(){
		return INSTANCE;
	}
	
	static FloatControl volume;
	static float gain;	
	public static void setVolumeLevel(float g){
		gain = g;
	}
	
	private static final String SLASH = System.getProperty("file.separator");
	
	private static final File PAWN = new File(String.join(SLASH, "sound", "voice", "pieces", "pawn.wav"));
	private static final File ROOK = new File(String.join(SLASH, "sound", "voice", "pieces", "rook.wav"));
	private static final File BISHOP = new File(String.join(SLASH, "sound", "voice", "pieces", "bishop.wav"));
	private static final File KING = new File(String.join(SLASH, "sound", "voice", "pieces", "king.wav"));
	private static final File QUEEN = new File(String.join(SLASH, "sound", "voice", "pieces", "queen.wav"));
	private static final File CHECK = new File(String.join(SLASH, "sound", "voice", "actions", "check.wav"));
	private static final File MATE = new File(String.join(SLASH, "sound", "voice", "actions", "mate.wav"));
	private static final File DRAW = new File(String.join(SLASH, "sound", "voice", "actions", "draw.wav"));
	private static final File TAKES = new File(String.join(SLASH, "sound", "voice", "actions", "takes.wav"));
	private static final File A1 = new File(String.join(SLASH, "sound", "voice", "squares", "a1.wav"));
	private static final File A2 = new File(String.join(SLASH, "sound", "voice", "squares", "a2.wav"));
	private static final File A3 = new File(String.join(SLASH, "sound", "voice", "squares", "a3.wav"));
	private static final File A4 = new File(String.join(SLASH, "sound", "voice", "squares", "a4.wav"));
	private static final File B1 = new File(String.join(SLASH, "sound", "voice", "squares", "b1.wav"));
	private static final File B2 = new File(String.join(SLASH, "sound", "voice", "squares", "b2.wav"));
	private static final File B3 = new File(String.join(SLASH, "sound", "voice", "squares", "b3.wav"));
	private static final File B4 = new File(String.join(SLASH, "sound", "voice", "squares", "b4.wav"));
	private static final File C1 = new File(String.join(SLASH, "sound", "voice", "squares", "c1.wav"));
	private static final File C2 = new File(String.join(SLASH, "sound", "voice", "squares", "c2.wav"));
	private static final File C3 = new File(String.join(SLASH, "sound", "voice", "squares", "c3.wav"));
	private static final File C4 = new File(String.join(SLASH, "sound", "voice", "squares", "c4.wav"));
	
	public void playVoice(String action){

		try(AudioInputStream ais = AudioSystem.getAudioInputStream(selectSound(action))) {
			
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

	private File selectSound(String action) throws InterruptedException{
		
		Clocks.setTurn(Turn.PAUSE);
		
		switch(action){
			case "Pawn":
				return PAWN;
			case "Rook":
				return ROOK;
			case "Bishop":
				return BISHOP;
			case "King":
				return KING;
			case "Queen":
				return QUEEN;
			case "check":				
				TimeUnit.SECONDS.sleep(1);
				return CHECK;
			case "mate":
				TimeUnit.SECONDS.sleep(1);
				return MATE;
			case "draw":
				TimeUnit.SECONDS.sleep(1);
				return DRAW;
			case "takes":
				TimeUnit.SECONDS.sleep(1);
				return TAKES;
			case "A1":
				TimeUnit.SECONDS.sleep(1);
				return A1;
			case "A2":
				TimeUnit.SECONDS.sleep(1);
				return A2;
			case "A3":
				TimeUnit.SECONDS.sleep(1);
				return A3;
			case "A4":
				TimeUnit.SECONDS.sleep(1);
				return A4;
			case "B1":
				TimeUnit.SECONDS.sleep(1);
				return B1;
			case "B2":
				TimeUnit.SECONDS.sleep(1);
				return B2;
			case "B3":
				TimeUnit.SECONDS.sleep(1);
				return B3;
			case "B4":
				TimeUnit.SECONDS.sleep(1);
				return B4;
			case "C1":
				TimeUnit.SECONDS.sleep(1);
				return C1;
			case "C2":
				TimeUnit.SECONDS.sleep(1);
				return C2;
			case "C3":
				TimeUnit.SECONDS.sleep(1);
				return C3;
			case "C4":
				TimeUnit.SECONDS.sleep(1);
				return C4;
			default:
				return null;
		}
	}
}
