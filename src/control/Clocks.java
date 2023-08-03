package control;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ui.Gui;
import utilpack.Turn;

public class Clocks{
	
	private static volatile int minutesBlack = 0;
	private static volatile int secondsBlack = 0;
	private static volatile int minutesWhite = 0;
	private static volatile int secondsWhite = 0;

	private static volatile Turn turn = Turn.PAUSE;
	private static volatile AtomicInteger nodes = new AtomicInteger(0);
	
	private static volatile Clocks INSTANCE;
	
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
	
	public static void setTurn(Turn turn) {
		Clocks.turn = turn;		
	}
	public static void setNodes(int count) {
		nodes.set(count);
	}
	public static void addNodes(int count) {
		nodes.addAndGet(count);
	}
	
	public void showClocks() {
		
		while(true) {
			try {
				TimeUnit.SECONDS.sleep(1);
				if(turn.equals(Turn.BLACK)) {
					Gui.nodes.setText(String.format("%,d", nodes.get()));
					if(secondsBlack < 59) {
						secondsBlack++;
					}
					else {
						secondsBlack = 0;
						minutesBlack++;
					}
				Gui.clockBlack.setText(String.format("%02d", minutesBlack)+":"
										+String.format("%02d", secondsBlack));
				}
				else if(turn.equals(Turn.WHITE)) {
					if(secondsWhite < 59) {
						secondsWhite++;
					}
					else {
						secondsWhite = 0;
						minutesWhite++;
					}
				Gui.clockWhite.setText(String.format("%02d", minutesWhite)+":"
										+String.format("%02d", secondsWhite));
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void resetClocks() {
		
		nodes.set(0);
		Gui.nodes.setText(" ");
		turn = Turn.PAUSE;
		minutesBlack = 0;
		secondsBlack = 0;
		minutesWhite = 0;
		secondsWhite = 0;
		Gui.clockBlack.setText(String.format("%02d", minutesBlack) + ":"
								+ String.format("%02d", secondsBlack));
		Gui.clockWhite.setText(String.format("%02d", minutesWhite) + ":"
								+ String.format("%02d", secondsWhite));
	}
	
}
