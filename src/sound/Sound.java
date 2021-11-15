package sound;

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
		
		clocks.setTurn(" ");
		
		String beep = " ";
		String divider = System.getProperty("file.separator");
		
		switch(action){
			case "Pawn":
			beep = "voice" + divider + "pawn.wav";
			break;
			case "Rook":
			beep = "voice" + divider + "rook.wav";
			break;
			case "Bishop":
			beep = "voice" + divider + "bishop.wav";
			break;
			case "King":
			beep = "voice" + divider + "king.wav";
			break;
			case "Queen":
			beep = "voice" + divider + "queen.wav";
			break;
			case "check":				
				Thread.sleep(1000);
			beep = "voice" + divider + "check.wav";
			break;
			case "mate":
				Thread.sleep(1000);
			beep = "voice" + divider + "mate.wav";
			break;
			case "draw":
				Thread.sleep(1000);
			beep = "voice" + divider + "draw.wav";
			break;
			case "takes":
				Thread.sleep(1000);
			beep = "voice" + divider + "takes.wav";
			break;
			case "A1":
				Thread.sleep(1000);
			beep = "voice" + divider + "a1.wav";
			break;
			case "A2":
				Thread.sleep(1000);
			beep = "voice" + divider + "a2.wav";
			break;
			case "A3":
				Thread.sleep(1000);
			beep = "voice" + divider + "a3.wav";
			break;
			case "A4":
				Thread.sleep(1000);
			beep = "voice" + divider + "a4.wav";
			break;
			case "B1":
				Thread.sleep(1000);
			beep = "voice" + divider + "b1.wav";
			break;
			case "B2":
				Thread.sleep(1000);
			beep = "voice" + divider + "b2.wav";
			break;
			case "B3":
				Thread.sleep(1000);
			beep = "voice" + divider + "b3.wav";
			break;
			case "B4":
				Thread.sleep(1000);
			beep = "voice" + divider + "b4.wav";
			break;
			case "C1":
				Thread.sleep(1000);
			beep = "voice" + divider + "c1.wav";
			break;
			case "C2":
				Thread.sleep(1000);
			beep = "voice" + divider + "c2.wav";
			break;
			case "C3":
				Thread.sleep(1000);
			beep = "voice" + divider + "c3.wav";
			break;
			case "C4":
				Thread.sleep(1000);
			beep = "voice" + divider + "c4.wav";
			break;
		}
		return beep;
	}
}
