package sound;

import javax.sound.sampled.*;

public class Sound {
	
	private static final Sound INSTANCE = new Sound();
	
	public static Sound getInstance(){
		return INSTANCE;
	}
	
	static FloatControl volume;
	static float gain;	
	public static void setVol(float g){
		gain = g;
		}
	
	public void voice(String action){

		try (AudioInputStream audioStream = AudioSystem.getAudioInputStream
										   (getClass().getResource(bank(action))))
		{
			Clip audioClip = AudioSystem.getClip();
			audioClip.open(audioStream);
	        volume = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(gain);
				audioClip.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private String bank(String action) throws InterruptedException{
		
		String beep = " ";
		
		switch(action){
			case "Pawn":
			beep = "voice/pawn.wav";
			break;
			case "Rook":
			beep = "voice/rook.wav";
			break;
			case "Bishop":
			beep = "voice/bishop.wav";
			break;
			case "King":
			beep = "voice/king.wav";
			break;
			case "Queen":
			beep = "voice/queen.wav";
			break;
			case "check":				
				Thread.sleep(1000);
			beep = "voice/check.wav";
			break;
			case "mate":
				Thread.sleep(1000);
			beep = "voice/mate.wav";
			break;
			case "draw":
				Thread.sleep(1000);
			beep = "voice/draw.wav";
			break;
			case "takes":
				Thread.sleep(1000);
			beep = "voice/takes.wav";
			break;
			case "A1":
				Thread.sleep(1000);
			beep = "voice/a1.wav";
			break;
			case "A2":
				Thread.sleep(1000);
			beep = "voice/a2.wav";
			break;
			case "A3":
				Thread.sleep(1000);
			beep = "voice/a3.wav";
			break;
			case "A4":
				Thread.sleep(1000);
			beep = "voice/a4.wav";
			break;
			case "B1":
				Thread.sleep(1000);
			beep = "voice/b1.wav";
			break;
			case "B2":
				Thread.sleep(1000);
			beep = "voice/b2.wav";
			break;
			case "B3":
				Thread.sleep(1000);
			beep = "voice/b3.wav";
			break;
			case "B4":
				Thread.sleep(1000);
			beep = "voice/b4.wav";
			break;
			case "C1":
				Thread.sleep(1000);
			beep = "voice/c1.wav";
			break;
			case "C2":
				Thread.sleep(1000);
			beep = "voice/c2.wav";
			break;
			case "C3":
				Thread.sleep(1000);
			beep = "voice/c3.wav";
			break;
			case "C4":
				Thread.sleep(1000);
			beep = "voice/c4.wav";
			break;
		}
		return beep;
	}
}
