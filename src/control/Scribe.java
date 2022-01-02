package control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	private int number = 1;
	private boolean nova = true;
	public void writeGame(String action, String data) {

		try(BufferedWriter scribe = new BufferedWriter(new FileWriter("games.txt", true));
			RandomAccessFile file = new RandomAccessFile("games.txt", "rw");) {
			switch(action) {
			case "new": case "end":
				if(action.equals("new") & number > 1) {
					number = 1;
					nova = true;
				}
				else if(action.equals("end")){
					scribe.append(data);
					number = 1;
					nova = true;
				}
				break;
			case "undo":
				number--;
				file.setLength(file.length()-5);
				break;
			case "white": case "black":
				if(number == 1 & nova) {
					if(file.length() != 0) {
						scribe.newLine();
						scribe.newLine();					
					}
					LocalDateTime now = LocalDateTime.now();
					scribe.append(now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					scribe.newLine();
					scribe.append(Gui.getHuman() + " vs AI " + Gui.getLevel());
					scribe.newLine();
					scribe.newLine();
					scribe.append(number + ". ");
					if(action.equals("white")) {
						scribe.append(data + " ");
					}
					else {
						scribe.append("0000" +  " " + data + " ");
						number++;
					}
					nova = false;
				}
				else {
					if(action.equals("white")) {
						scribe.append(number + ". ");
					}
					else {
						number++;						
					}
					scribe.append(data + " ");
				}
				break;
			}
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
}
