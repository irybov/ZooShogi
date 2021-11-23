package control;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;

import ai.ArtIntel;
import ai.Integrator;
import ai.MoveList;
import ai.Node;
import ai.Generator;
import data.*;
import util.Copier;
import util.Matrix;
import util.Pieces;
import sound.Sound;
import ui.Gui;

public class Director{
	
	private static volatile Director INSTANCE;
	
	public static Director getInstance(){
		
		if(INSTANCE == null) {
			synchronized (Director.class) {
				if(INSTANCE == null) {
					INSTANCE = new Director();
				}
			}
		}		
		return INSTANCE;
	}
	
	private final Sound sound = Sound.getInstance();
	private final Integrator integrator = Integrator.getInstance();
	private final Generator generator = new Generator();
	
	private Clocks clocks = Clocks.getInstance();

	private Map<String, Integer> game = new HashMap<>();

	private Player player;
	private ScoreSheet ss;
	{
		File file = new File("table.bin");
		if(!file.exists()) {
			try {
				file.createNewFile();
				ss = new ScoreSheet();
			    try(ObjectOutputStream oos = new ObjectOutputStream
				   (new BufferedOutputStream(new FileOutputStream("table.bin")))){
			           oos.writeObject(ss);
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(ObjectInputStream ois = new ObjectInputStream
	 	   (new BufferedInputStream(new FileInputStream("table.bin")))){
	    	ss = (ScoreSheet) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	private int r, c, r2, c2;
	private String piece;
	private String[][] board;
	private int level;

	private boolean mute;
	
	private String[][] undo;

	public void aiMute(boolean mute){
		this.mute = mute;		
		integrator.setMute(mute);
	}
	public void aiWarn(boolean warn){	
		integrator.setWarn(warn);
	}
	
	public void setLevel(int level){
		this.level = level;
	}	
	private void setBoard(String[][] field){
		board = Copier.deepCopy(field);
	}
	private String[][] getBoard(){
		return Copier.deepCopy(board);
	}
	private void setMoves(Map<String, Integer> moves) {		
		game = Copier.deepCopy(moves);
	}	
	private Map<String, Integer> getMoves(){		
		return Copier.deepCopy(game);
	}
	
	public void initialize() {

		board = new String[][]{{"r","k","b"," "," "," "," "," "," "},
							   {" ","p"," "},
							   {" ","P"," "},
							   {"B","K","R"," "," "," "," "," "," "}};
							   
		undo = 	Copier.deepCopy(board);
	}
	
	public String refresh(int r, int c) {				
		return board[r][c];
	}
	
	public boolean list(String probe){		
		return probe.equals("K") || probe.equals("Q") || probe.equals("R")
								 || probe.equals("B") || probe.equals("P");
	}
	
	public boolean legal(String probe){		
		return probe.equals("k") || probe.equals("q") || probe.equals("r") ||
			   probe.equals("b") || probe.equals("p") || probe.equals(" ");
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
				return Pieces.WPAWN.move(r, c, r2, c2);
			case "R":
				return Pieces.ROOK.move(r, c, r2, c2);
			case "B":
				return Pieces.BISHOP.move(r, c, r2, c2);
			case "K":
				return Pieces.KING.move(r, c, r2, c2);
			case "Q":
				return Pieces.WQUEEN.move(r, c, r2, c2);
			default:
				return false;
			}
		}
	}
	
	public void move(){
		
		MoveList.add(board, "black");
		
		switch(board[r2][c2]){
		case "b":
			if(board[3][3].equals(" ")){
				board[3][3] = "B";
			}
			else if(!board[3][3].equals(" ")){
				board[3][6] = "B";
			}
			break;
		case "p":
			if(board[3][4].equals(" ")){
				board[3][4] = "P";
			}
			else if(!board[3][4].equals(" ")){
				board[3][7] = "P";
			}
			break;
		case "r":
			if(board[3][5].equals(" ")){
				board[3][5] = "R";
			}
			else if(!board[3][5].equals(" ")){
				board[3][8] = "R";
			}
			break;
		case "q":
			if(board[3][4].equals(" ")){
				board[3][4] = "P";
			}
			else if(!board[3][4].equals(" ")){
				board[3][7] = "P";
			}
			break;
		}
			
		if(board[r][c].equals("P") & r2==0){
			board[r2][c2] = "Q";
		}
		else{
			board[r2][c2] = board[r][c];
		}
		board[r][c] = " ";
		
		MoveList.add(board, "white");
	}
	
	public void drop(){
		
		MoveList.add(board, "black");
		
		board[r2][c2] = board[r][c];
		board[r][c] = " ";
		
		MoveList.add(board, "white");
	}
	
	public boolean beginning() {
		return game.isEmpty();
	}
	
	public void undoMove() {

		String hash = Matrix.keyMaker(undo);
		if(game.containsKey(hash)) {
			game.merge(hash, -1, (oldVal, newVal) -> oldVal + newVal);
		}
		hash = Matrix.keyMaker(board);		
		if(game.containsKey(hash)) {
			game.merge(hash, -1, (oldVal, newVal) -> oldVal + newVal);
		}
		board = Copier.deepCopy(undo);
	}
	
	public void compute() throws InterruptedException{
		
		undo = Copier.deepCopy(board);
		
		clocks.setTurn("black");
		if(endGame("black")){
			game.clear();
			Gui.lock();
			clocks.setTurn(" ");
			return;
		}
		
		if(client) {
			send();
			int[] move = receive();
			integrator.activate(board, move);
				try {
					TimeUnit.SECONDS.sleep(1);
				}
				catch (InterruptedException exc) {
					exc.printStackTrace();
				}
		}
		else {
			List<Node> nodes = generator.generateNodes(board);
			if(nodes.get(0).getValue() > 999) {
				integrator.mergeMoves(nodes.get(0));
			}
			else {
			switch(level){
			case 0:
			case 2:
			case 4:
				new ArtIntel(level, board).run();
				break;
			case 1:
			case 3:
			case 5:
			case 6:
			case 7:
				int cores = Runtime.getRuntime().availableProcessors();
				ExecutorService es = Executors.newFixedThreadPool(cores);
				nodes.forEach(node-> es.submit(new ArtIntel(node, Copier.deepCopy(board), level)));
				es.shutdown();			
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				break;
			}
				try {
					TimeUnit.SECONDS.sleep(1);
				}
				catch (InterruptedException exc) {
					exc.printStackTrace();
				}
			}
			integrator.activate(board);
		}

		Gui.doClick();
		
		if(endGame("white")){
			game.clear();
			Gui.lock();
			clocks.setTurn(" ");
			return;
		}		
		clocks.setTurn("white");
	}
	
	private boolean endGame(String turn)  {
		
		int a = 0;
		int b = 0;
		
		for(int r=0; r<4; r++){
			for(int c=0; c<3; c++){
				if(board[r][c].equals("K")){
					a = 2;
				}
				else if(board[r][c].equals("k")){
					b = 1;
				}
			}
		}
		
		if(addToList("black")){
			output("draw");
			voice("draw");
			return true;
		}
		else if((a+b==2 & turn.equals("black")) || (a+b==3 & turn.equals("white")) & 
				(board[0][0].equals("K")||board[0][1].equals("K")||board[0][2].equals("K"))){
			output("white");
			voice("mate");
			return true;
		}
		else if((a+b==1 & turn.equals("white")) || (a+b==3 & turn.equals("black")) & 
				(board[3][0].equals("k")||board[3][1].equals("k")||board[3][2].equals("k"))){
			output("black");
			voice("mate");
			return true;
		}		
		else
			return false;
	}
	
	private boolean addToList(String turn) {
			
		if(turn.equals("black")) {
			String hash = Matrix.keyMaker(board);		
			game.putIfAbsent(hash, 0);
			game.merge(hash, 1, (oldVal, newVal) -> oldVal + newVal);
			return game.get(hash)==3;
		}
		return false;
	}
	
	private void voice(String result) {
		
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
	
	public void newGame() {		
		game.clear();
		MoveList.clear();
		integrator.newGame();
	}

	private void output(String result) {
		
		switch(result){
		case "black":
			Gui.output.setText("Comp wins! You gain no points...");			
			break;
		case "white":
			Gui.output.setText("You win and gain +" + level*10 + " points!");
			try {
				new FileOutputStream("game.bin").close();
			}
		    catch (IOException ex) {
				ex.printStackTrace();
			}
			updateScore(10);
			break;
		case "draw":
			Gui.output.setText("Draw. You gain +" + level*5 + " points!");
			try {
				new FileOutputStream("game.bin").close();
			}
		    catch (IOException ex) {
				ex.printStackTrace();
			}
			updateScore(5);
			break;
		}		
	}
	
	public void saveGame() {
		
		GameState state = new GameState(getBoard(), getMoves(), level);
		
	    try(FileOutputStream fos = new FileOutputStream("game.bin");
	    		ObjectOutputStream oos = new ObjectOutputStream(fos)){
	           oos.writeUnshared(state);
	           oos.flush();
	           oos.reset();
	    }
	    catch (IOException ex) {
			ex.printStackTrace();
		}	    
	}
	
	public boolean loadGame() {
	
	    try(FileInputStream fis = new FileInputStream("game.bin");
	    		ObjectInputStream ois = new ObjectInputStream(fis)){
	    	GameState state = (GameState) ois.readUnshared();
			setBoard(state.getBoard());
			setMoves(state.getMoves());
			Gui.setLevel(state.getLevel());
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
			return false;
		}
	    return true;	    
	}
	
	public boolean createPlayer(String name, String pass) {
		
		List<Player> players = ss.getList();
		Player newPlayer = new Player(name, pass);
		for(Player current: players) {
			if(current.getName().equalsIgnoreCase(name)) {
				return false;
			}
		}		
		players.add(newPlayer);
    	player = newPlayer;
	    try(FileOutputStream fos = new FileOutputStream("table.bin");
	    		ObjectOutputStream oos = new ObjectOutputStream(fos)){
	           oos.writeObject(ss);
	    }
	    catch (IOException ex) {
			ex.printStackTrace();
		}
    return true;
	}
	
	public boolean selectPlayer(String name, String pass) {

		List<Player> players = ss.getList();
		for(Player current: players) {
			if(current.getName().equalsIgnoreCase(name) & current.getPass().equals(pass)) {
				player = current;
				return true;
			}
		}
		return false;
	}
	
	public void deletePlayer() {
		
		List<Player> players = ss.getList();
		if(players.contains(player)) {
			players.remove(player);
			try(ObjectOutputStream oos = new ObjectOutputStream
			   (new BufferedOutputStream(new FileOutputStream("table.bin")))){
			        oos.writeObject(ss);
			}			
			catch (IOException ex) {
				ex.printStackTrace();
			}
		Gui.output.setText("User profile deleted");
		}
	}
	
	private void updateScore(int scale) {
		
		List<Player> players = ss.getList();
		if(players.contains(player)) {
			player.setScore(player.getScore() + level*scale);
			try(ObjectOutputStream oos = new ObjectOutputStream
			   (new BufferedOutputStream(new FileOutputStream("table.bin")))){
			        oos.writeObject(ss);
			}			
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public List<Player> getList() {		
		return ss.getList();
	}
	
	private LocalServer server;
	private Driver driver;
	private boolean client = false;
		
	public LocalServer getServer() {
		return server;
	}
	public boolean checkClient() {
		return client;
	}	

	public void activate() {
		client = true;
	}
	
	public void establish() {
		setLevel(0);		
		client = true;
		if(driver == null) {
			driver = new Driver();
		}
		server = new LocalServer();
		server.start();
	}
	
	public void disconnect() {
		if(server != null) {
			server.setLine("quit");
		}
		client = false;
	}
	
	public void shutdown() {
		if(client) {
			server.setLine("quit");
			client = false;			
		}
		if(server != null) {
			server.interrupt();
			server = null;
		}
	}
	
	public void send() {
		String line = null;		
		line = driver.output(getBoard());
		server.setLine(line);
	}
	
	public int[] receive() {
		String reply = null;
		reply = server.getAnswer();
		return driver.input(reply);
		
	}
	
}
