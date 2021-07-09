package control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;

import ai.ArtIntel;
import ai.Integrator;
import ai.Node;
import ai.Separator;
import util.Bishop;
import util.Copier;
import util.King;
import util.Message;
import util.Queen;
import util.Rook;
import sound.Sound;
import ui.Gui;

public class Director{
	
	private static final Director INSTANCE = new Director();
	
	public static Director getInstance(){
		return INSTANCE;
	}
	
	private Sound sound = Sound.getInstance();
	private Integrator integrator = new Integrator(sound);

	private Map<String, Integer> game = new HashMap<>();
	
	private int r, c, r2, c2;
	private String piece;
	private String[][] board = new String[4][];
	private int level;

	private boolean mute;

	public void aiMute(boolean mute){
		this.mute = mute;		
		integrator.setMute(mute);
	}
	public void aiWarn(boolean c){	
		integrator.setWarn(c);
	}
	public void setLevel(int level){
		this.level = level;
	}	
	private void setBoard(String[][] field){
		board = Copier.deepCopy(field);
/*		
	    for(int i=0; i<board.length; i++){
	    	for(int j=0; j<board[0].length; j++){
	    		System.out.print(board[i][j]);
	    	}
	    }
	    System.out.println();
*/	}
	public String[][] getBoard(){
		String[][] field = Copier.deepCopy(board);
		return field;
	}
	
	public void initialize() {
		
		board[0] = new String[]{" "," "," "," "," "," "," "," "," "," "};
		board[1] = new String[]{" "," "," "};
		board[2] = new String[]{" "," "," "};		
		board[3] = new String[]{" "," "," "," "," "," "," "," "," "," "};
/*		
		for(r=0; r<board.length; r++){
			for(c=0; c<board[0].length; c++){
				board[r][c] = " ";
			}
*/			board[1][1] = "p";
			board[0][0] = "r";
			board[0][1] = "k";
			board[0][2] = "b";			
						
			board[2][1] = "P";
			board[3][0] = "B";
			board[3][1] = "K";
			board[3][2] = "R";
//			}
	}
	
	public String refresh(int r, int c) {
				
		return board[r][c];
	}
	
	public boolean list(String probe){
		
		return probe == "K" || probe == "Q" || probe == "R" || probe == "B" || probe == "P";
	}
	
	public boolean legal(String probe){
		
		return probe=="k" || probe=="q" || probe=="r" || probe=="b" || probe=="p" || probe==" ";
	}

	public void from(int x, int y, String probe) {
		
		r = x;
		c = y;
		piece = probe;
	}
	
	public boolean to(int x2, int y2) {
		
		r2 = x2;
		c2 = y2;
		
		if(r==3 & (c>2 & c<9)){
			return true;
		}
		else{
		switch(piece){
			case "P":
				return (r2==r-1 & c2==c);
			case "R":
				return Rook.move(r, c, r2, c2);
			case "B":
				return Bishop.move(r, c, r2, c2);
			case "K":
				return King.move(r, c, r2, c2);
			case "Q":
				return Queen.move(r, c, r2, c2, "white");
			default:
				return false;
			}
		}
	}
	
	public void move(){
		
		switch(board[r2][c2]){
		case "b":
			if(board[3][3]==" "){
			board[3][3] = "B";
			}
			else if(board[3][3]!=" "){
			board[3][6] = "B";
			}
			break;
		case "p":
			if(board[3][4]==" "){
			board[3][4] = "P";
			}
			else if(board[3][4]!=" "){
			board[3][7] = "P";
			}
			break;
		case "r":
			if(board[3][5]==" "){
			board[3][5] = "R";
			}
			else if(board[3][5]!=" "){
			board[3][8] = "R";
			}
			break;
		case "q":
			if(board[3][4]==" "){
			board[3][4] = "P";
			}
			else if(board[3][4]!=" "){
			board[3][7] = "P";
			}
			break;
		}
			
		if(board[r][c]=="P" & r2==0){
			board[r2][c2] = "Q";
			}
		else{
			board[r2][c2] = board[r][c];
			}
		board[r][c] = " ";
	}
	
	public void drop(){

		board[r2][c2] = board[r][c];
		board[r][c] = " ";
	}
	
	public void compute() throws InterruptedException{

		if(endGame("black")){
			game.clear();
			Gui.lock();
			return;
		}
		switch(level){
		case 0:
		case 2:
		case 4:	
			ArtIntel t0 = new ArtIntel(level, board);
			t0.run();
			break;
		case 1:
		case 3:
		case 5:
		case 6:
		case 7:
			int cores = Runtime.getRuntime().availableProcessors();
			List<Node> nodes = Separator.generateNodes(board);
			ExecutorService es = Executors.newFixedThreadPool(cores);
			for(int i = 0; i <nodes.size(); i++){
				es.submit(new ArtIntel(nodes.get(i), Copier.deepCopy(board), level));
			}
			es.shutdown();			
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
/*			
			ArtIntel t1 = new ArtIntel('1', level);
			ArtIntel t2 = new ArtIntel('2', level);
			
			t1.setBoard(deepCopy(board));
			t2.setBoard(deepCopy(board));
			
			Separator.generateNodes(board);

			t1.start();
			t2.start();						
			t1.join();
			t2.join();
			
			integrator.activate(board);				
			Separator.clearLists();			
*/			break;
		}
		integrator.activate(board);	
		
		Gui.doClick();		
		
		if(endGame("white")){
			game.clear();
			Gui.lock();
			return;
		}
	}
	
	private boolean endGame(String turn)  {
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c]=="K"){
					a = 2;
				}
				else if(board[r][c]=="k"){
					b = 1;
				}
			}
		}
		
		if(addToList("black")){
			Message.output("draw");
			voice("draw");
			return true;
		}
		else if((a+b==2 & turn=="black")||(a+b==3 & turn=="white") & 
				(board[0][0]=="K"||board[0][1]=="K"||board[0][2]=="K")){
			Message.output("white");
			voice("mate");
			return true;
		}
		else if((a+b==1 & turn=="white")||(a+b==3 & turn=="black") & 
				(board[3][0]=="k"||board[3][1]=="k"||board[3][2]=="k")){
			Message.output("black");
			voice("mate");
			return true;
		}		
		else{
			return false;
		}
	}
	
	private boolean addToList(String turn) {
		
		int v = 0;
		
		StringBuilder current = new StringBuilder(board.length * board[0].length);
		
		if(turn=="black"){
		for(int r=0; r<board.length ; r++){
			for(int c=0; c<board[r].length ; c++){
				current.append(board[r][c]);				
			}
		}
		String hash = current.toString();
		
		game.putIfAbsent(hash, v);
		game.merge(hash, 1, (oldVal, newVal) -> oldVal + newVal);		
		return(game.get(hash)==3);
		}
		else
			return false;
	}
	
	private void voice(String result){
		
		if(!mute){
			switch(result){
			case "draw":
				sound.voice("draw");			
				break;
			case "mate":
				sound.voice("mate");				
				break;
			}
		}
	}
	
	public void clearing(){
		
		game.clear();
	}

	public void saveGame(){
	
		String[][] field = getBoard();
		
	    try(FileOutputStream fos = new FileOutputStream("game.ser");
	    		ObjectOutputStream oos = new ObjectOutputStream(fos)){
	           oos.writeUnshared(field);
	           oos.flush();
	           oos.reset();
	    }
	    catch (IOException ex) {
			ex.printStackTrace();
		}
/*	    
	    for(int i=0; i<field.length; i++){
	    	for(int j=0; j<field[0].length; j++){
	    		System.out.print(field[i][j]);
	    	}
	    }
	    System.out.println();
*/	    
	}
	
	public void loadGame(){
	
	    try(FileInputStream fis = new FileInputStream("game.ser");
	    		ObjectInputStream ois = new ObjectInputStream(fis)){
	    	String[][] field = (String[][]) ois.readUnshared();
			setBoard(field);
//			push.setEnabled(false);
			Gui.doClick();
/*			
		    for(int i=0; i<field.length; i++){
		    	for(int j=0; j<field[0].length; j++){
		    		System.out.print(field[i][j]);
		    	}
		    }
		    System.out.println();
*/		    	
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	    
	}
	
}
