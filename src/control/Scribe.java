package control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import ui.Gui;

public class Scribe {
	
	private static volatile Scribe INSTANCE;
	
	public static Scribe getInstance(){
		
		if(INSTANCE == null) {
			synchronized (Scribe.class) {
				if(INSTANCE == null) {
					INSTANCE = new Scribe();
				}
			}
		}		
		return INSTANCE;
	}
	
	private String level;
	private void setLevel(){
		String level = Gui.getLevel();
		this.level = level;
	}
	
	private int number = 1;	
	private Writer games = null;
	{
		try {
			games = new BufferedWriter(new FileWriter("games.txt"));
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	
	
}
