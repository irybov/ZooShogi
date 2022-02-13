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
	
	private int moveNumber = 1;
	private boolean isNewGame = true;
	public void writeGameNote(String action, String data) {

		try(BufferedWriter scribe = new BufferedWriter(new FileWriter("games.txt", true));
			RandomAccessFile file = new RandomAccessFile("games.txt", "rw");) {
			switch(action) {
			case "new": case "end":
				if(action.equals("new") & moveNumber > 1) {
					moveNumber = 1;
					isNewGame = true;
				}
				else if(action.equals("end")){
					scribe.append(data);
					moveNumber = 1;
					isNewGame = true;
				}
				break;
			case "undo":
				moveNumber--;
				file.setLength(file.length()-5);
				break;
			case "white": case "black":
				if(moveNumber == 1 & isNewGame) {
					if(file.length() != 0) {
						scribe.newLine();
						scribe.newLine();					
					}
					LocalDateTime now = LocalDateTime.now();
					scribe.append(now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					scribe.newLine();
					scribe.append(Gui.getPlayerName() + " vs AI " + Gui.getLevel());
					scribe.newLine();
					scribe.newLine();
					scribe.append(moveNumber + ". ");
					if(action.equals("white")) {
						scribe.append(data + " ");
					}
					else {
						scribe.append("0000" +  " " + data + " ");
						moveNumber++;
					}
					isNewGame = false;
				}
				else {
					if(action.equals("white")) {
						scribe.append(moveNumber + ". ");
					}
					else {
						moveNumber++;						
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
