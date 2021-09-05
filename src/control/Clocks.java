package control;

import java.util.concurrent.TimeUnit;

import ui.Gui;

public class Clocks{
	
	private int minB = 0;
	private int secB = 0;
	private int minW = 0;
	private int secW = 0;

	volatile private String turn = " ";
	
	private static Clocks INSTANCE = new Clocks();
	
	public static Clocks getInstance() {
		return INSTANCE;
	}
	
	public void setTurn(String turn) {
		this.turn = turn;		
	}
	
	public void setClock() {
		
//		new Thread(new Runnable() {
//			public void run() {
		
		while(true) {
			try {
				Thread.sleep(1000);
				if(turn.equals("black")) {
				if(secB < 59) {
						secB++;
					}
					else {
						secB = 0;
						minB++;
					}
					Gui.clockB.setText(String.format("%02d", minB)+":"+String.format
							("%02d", secB));
				}
				else if(turn.equals("white")) {
					if(secW < 59) {
						secW++;
					}
					else {
						secW = 0;
						minW++;
					}
					Gui.clockW.setText(String.format("%02d", minW)+":"+String.format
							("%02d", secW));
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
//			}
//		}).start();
		
	}
	
	public void reset() {
		
		turn = " ";
		minB = 0;
		secB = 0;
		minW = 0;
		secW = 0;
		Gui.clockB.setText(String.format("%02d", minB) + ":" + String.format("%02d", secB));
		Gui.clockW.setText(String.format("%02d", minW) + ":" + String.format("%02d", secW));
	}
	
}
