package control;

//import java.util.concurrent.TimeUnit;
import ui.Gui;

public class Clocks{

	private static Clocks INSTANCE = new Clocks();
	
	public static Clocks getInstance(){
		return INSTANCE;
	}
		
	private static int minB = 0;
	private static int secB = 0;
	private static int minW = 0;
	private static int secW = 0;
	
	boolean active = false;
	public String turn = " ";
	
	public void setClock() {
		
		active = true;
		
        new Thread(new Runnable() {
            public void run() {
		
		while(active) {
			try {
//				TimeUnit.SECONDS.sleep(1);
				Thread.sleep(1000);
				if(turn.equals("black")) {
				if(secB < 59) {
						secB++;
						Gui.clockB.setText(String.format("%02d", minB)
									+":"+String.format("%02d", secB));
					}
					else {
						secB = 0;
						minB++;
						Gui.clockB.setText(String.format("%02d", minB)
									+":"+String.format("%02d", secB));
					}
				}
				else if(turn.equals("white")) {
					if(secW < 59) {
						secW++;
						Gui.clockW.setText(String.format("%02d", minW)
									+":"+String.format("%02d", secW));
					}
					else {
						secW = 0;
						minW++;
						Gui.clockW.setText(String.format("%02d", minW)
									+":"+String.format("%02d", secW));
					}					
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
           }
        }).start();
		
	}

	public void stop() {
		
		active = false;
		turn =" ";
	}
	
	public void reset() {
		
		active = false;
		turn =" ";
		minB = 0;
		secB = 0;
		minW = 0;
		secW = 0;
	}
	
}
