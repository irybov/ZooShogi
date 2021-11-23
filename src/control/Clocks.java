package control;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ui.Gui;

public class Clocks{
	
	private int minB = 0;
	private int secB = 0;
	private int minW = 0;
	private int secW = 0;

	private volatile String turn = " ";
	private static AtomicInteger nodes = new AtomicInteger(0);
	
	private static Clocks INSTANCE;
	
	public static Clocks getInstance() {
		
		if(INSTANCE == null) {
			synchronized (Clocks.class) {
				if(INSTANCE == null) {
					INSTANCE = new Clocks();
				}
			}
		}		
		return INSTANCE;
	}
	
	public void setTurn(String turn) {
		this.turn = turn;		
	}
	public static void setNodes(int count) {
		nodes.set(count);
	}
	public static void addNodes(int count) {
		nodes.addAndGet(count);
	}
	
	public void setClock() {
		
//		new Thread(new Runnable() {
//			public void run() {
		
		while(true) {
			try {
				if(turn.equals("black")) {
					Gui.counter.setText(String.format("%,d", nodes.get()));
				if(secB < 59) {
						secB++;
					}
					else {
						secB = 0;
						minB++;
					}
				Gui.clockB.setText(String.format("%02d", minB)+":"+String.format("%02d", secB));
				}
				else if(turn.equals("white")) {
					if(secW < 59) {
						secW++;
					}
					else {
						secW = 0;
						minW++;
					}
				Gui.clockW.setText(String.format("%02d", minW)+":"+String.format("%02d", secW));
				}
			TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
//			}
//		}).start();
		
	}
	
	public void reset() {
		
		nodes.set(0);
		Gui.counter.setText(" ");
		turn = " ";
		minB = 0;
		secB = 0;
		minW = 0;
		secW = 0;
		Gui.clockB.setText(String.format("%02d", minB) + ":" + String.format("%02d", secB));
		Gui.clockW.setText(String.format("%02d", minW) + ":" + String.format("%02d", secW));
	}
	
}
