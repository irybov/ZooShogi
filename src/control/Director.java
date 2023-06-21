package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.io.*;

import ai.AIFactory;
import ai.Integrator;
import ai.component.Cache;
import ai.component.Generator;
import ai.component.MovesList;
import ai.component.Node;
import ai.type.PseudoAI;
import data.*;
import sound.Sound;
import ui.Gui;
import utilpack.Copier;
import utilpack.Matrix;
import utilpack.Expositor;
import utilpack.Pieces;
import utilpack.Turn;

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
	
	private final Integrator integrator = Integrator.getInstance();

	private Map<String, Integer> game = new HashMap<>();

	private PlayerInfo player;
	private ScoreSheet scoresheet;
	{
		File file = new File("scoresheet.bin");
		if(!file.exists()) {
			try {
				file.createNewFile();
				scoresheet = new ScoreSheet();
			    try(ObjectOutputStream oos = new ObjectOutputStream
				   (new BufferedOutputStream(new FileOutputStream("scoresheet.bin")))){
			           oos.writeUnshared(scoresheet);
			           oos.flush();
			           oos.reset();
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(ObjectInputStream ois = new ObjectInputStream
	 	   (new BufferedInputStream(new FileInputStream("scoresheet.bin")))){
	    	scoresheet = (ScoreSheet) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	private int r, c, r2, c2;
	private String[][] board;
	private int level;
	
	private String[][] undoMove;

	public void setCheckWarning(boolean warn){	
		integrator.setCheckWarning(warn);
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
	
	public void initBoard() {

		board = new String[][]{{"r","k","b"," "," "," "," "," "," "},
							   {" ","p"," "},
							   {" ","P"," "},
							   {"B","K","R"," "," "," "," "," "," "}};
							   
		undoMove = 	Copier.deepCopy(board);
	}
	
	public String refreshBoard(int r, int c) {				
		return board[r][c];
	}
	
	public boolean isPlayerPiece(String probe){		
		return probe.equals("K") || probe.equals("Q") || probe.equals("R")
								 || probe.equals("B") || probe.equals("P");
	}
	
	public boolean isLegalMove(String probe){		
		return probe.equals("k") || probe.equals("q") || probe.equals("r") ||
			   probe.equals("b") || probe.equals("p") || probe.equals(" ");
	}

	private String piece;	
	public void moveFrom(int x, int y, String probe) {		
		r = x;
		c = y;
		piece = probe;
	}	
	public boolean moveTo(int x2, int y2) {
		
		r2 = x2;
		c2 = y2;
		
		if(r==3 & (c>2 & c<9)){
			return true;
		}
		else{
		switch(piece){
			case "P":
				return Pieces.WPAWN.isLegalMove(r, c, r2, c2);
			case "R":
				return Pieces.ROOK.isLegalMove(r, c, r2, c2);
			case "B":
				return Pieces.BISHOP.isLegalMove(r, c, r2, c2);
			case "K":
				return Pieces.KING.isLegalMove(r, c, r2, c2);
			case "Q":
				return Pieces.WQUEEN.isLegalMove(r, c, r2, c2);
			default:
				return false;
			}
		}
	}
	
	private String boardState;
	private String edge;
	private Scribe scribe = Scribe.getInstance();
	
	public void doMove(){
		
		edge = Expositor.getEdge(r, c, r2, c2, board[r][c]);
		scribe.writeGameNote("white", edge);
		
		MovesList.addMove(board, Turn.BLACK);
		
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
		
		MovesList.addMove(board, Turn.WHITE);
		
		boardState = Matrix.makeKey(board);
	}
	
	public void doDrop(){
		
		edge = Expositor.getEdge(r, c, r2, c2, board[r][c]);
		scribe.writeGameNote("white", edge);
		
		MovesList.addMove(board, Turn.BLACK);
		
		board[r2][c2] = board[r][c];
		board[r][c] = " ";
		
		MovesList.addMove(board, Turn.WHITE);
		
		boardState = Matrix.makeKey(board);
	}
	
	public boolean isStartOfGame() {
		return game.isEmpty();
	}
	
	public void undoMove() {
		
		scribe.writeGameNote("undo", null);

		String hash = Matrix.makeKey(undoMove);
		if(game.containsKey(hash)) {
			game.merge(hash, -1, (oldVal, newVal) -> oldVal + newVal);
		}
		hash = Matrix.makeKey(board);		
		if(game.containsKey(hash)) {
			game.merge(hash, -1, (oldVal, newVal) -> oldVal + newVal);
		}
		board = Copier.deepCopy(undoMove);

		if(!isClientActivated) {
			integrator.nextBest(board);
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			}
			catch (InterruptedException exc) {
				exc.printStackTrace();
			}
			Gui.doClick();
		}
		else {
			try {
				compute();
			}
			catch (InterruptedException exc) {
				exc.printStackTrace();
			}
		}
	}
	
	final int cores = Runtime.getRuntime().availableProcessors();
	final AIFactory factory = new AIFactory();
	final Generator generator = new Generator();
	
	public void compute() throws InterruptedException{
		
		undoMove = Copier.deepCopy(board);
		
		Clocks.setTurn(Turn.BLACK);
		if(isEndOfGame(Turn.BLACK)){
			game.clear();
			Gui.lockBoard();
			Clocks.setTurn(Turn.PAUSE);
			return;
		}
		
		if(isClientActivated) {
			sendMoveToClient();
			int[] move = receiveMoveFromClient();
			integrator.activate(board, move);
			TimeUnit.SECONDS.sleep(1);
		}
		else if(!Cache.isEmpty() && Cache.hasPosition(boardState)) {
			integrator.nextBest(board, Cache.getMove(boardState));
		}
		else if(integrator.hasNode(board)) {
			Node node = integrator.getNode(board);
			integrator.nextBest(board, node);
		}		
		else {
			List<Node> legalMoves = generator.generateMoves(board, Turn.BLACK);
			List<Node> nodes = new ArrayList<>(generator.sortMoves(board, legalMoves, Turn.BLACK));
			if(nodes.get(0).getValue() > 999) {
				integrator.nextBest(board, nodes.get(0));
			}
			else {
				switch(level){
				case 0: case 1:
					new PseudoAI(level, board, nodes).run();
					break;
				case 4:
					nodes = generator.filterMoves(board, nodes, Turn.BLACK);
					if(nodes.isEmpty()) {
						nodes = generator.sortMoves(board, legalMoves, Turn.BLACK);
						nodes = generator.filterMoves(nodes, Turn.BLACK);
					}
				case 2: case 3: case 5: case 6: case 7:
					List<Future<Integer>> tasks = new ArrayList<>(nodes.size());
					TaskInterceptor f19 = new TaskInterceptor(tasks);
					ExecutorService es = Executors.newFixedThreadPool(cores);
					for(Node node : nodes) {
						Future<Integer> score = es.submit(factory
								.createAI(level, node, Copier.deepCopy(board)));
						tasks.add(score);
					}
					f19.start();
						es.shutdown();			
						es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
					f19.interrupt();
					break;
				}
				TimeUnit.SECONDS.sleep(1);
				integrator.activate(board, nodes);
			}
		}

		Gui.doClick();
		
		if(isEndOfGame(Turn.WHITE)){
			game.clear();
			Gui.lockBoard();
			Clocks.setTurn(Turn.PAUSE);
			return;
		}		
		Clocks.setTurn(Turn.WHITE);
	}
	
	private boolean isEndOfGame(Turn turn)  {
		
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
		
		if(addToMoveList(Turn.BLACK)){
			scribe.writeGameNote("end", "1/2");
			output("draw");
			chooseVoice("draw");
			return true;
		}
		else if((a+b==2 & turn.equals(Turn.BLACK)) || (a+b==3 & turn.equals(Turn.WHITE)) & 
				(board[0][0].equals("K")||board[0][1].equals("K")||board[0][2].equals("K"))){
			scribe.writeGameNote("end", "1-0");
			output("white");
			chooseVoice("mate");
			return true;
		}
		else if((a+b==1 & turn.equals(Turn.WHITE)) || (a+b==3 & turn.equals(Turn.BLACK)) & 
				(board[3][0].equals("k")||board[3][1].equals("k")||board[3][2].equals("k"))){
			scribe.writeGameNote("end", "0-1");
			output("black");
			chooseVoice("mate");
			return true;
		}		
		return false;
	}
	
	private boolean addToMoveList(Turn turn) {
			
		if(turn.equals(Turn.BLACK)) {
			String hash = Matrix.makeKey(board);		
			game.putIfAbsent(hash, 0);
			game.merge(hash, 1, (oldVal, newVal) -> oldVal + newVal);
			return game.get(hash)==3;
		}
		return false;
	}
	
	private final Sound sound = Sound.getInstance();
	private boolean isMuted;	
	public void setVolumeMute(boolean mute){
		this.isMuted = mute;		
		integrator.setVolumeMute(mute);
	}
	private void chooseVoice(String result) {
		
		if(!isMuted){
			switch(result){
			case "draw":
				sound.playVoice("draw");			
				break;
			case "mate":
				sound.playVoice("mate");				
				break;
			}
		}		
	}
	
	public void newGame() {
		scribe.writeGameNote("new", null);
		game.clear();
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
				new FileOutputStream("save.bin").close();
			}
		    catch (IOException ex) {
				ex.printStackTrace();
			}
			updateScore(10);
			break;
		case "draw":
			Gui.output.setText("Draw. You gain +" + level*5 + " points!");
			try {
				new FileOutputStream("save.bin").close();
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
		
	    try(FileOutputStream fos = new FileOutputStream("save.bin");
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
	
	    try(FileInputStream fis = new FileInputStream("save.bin");
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
		
		List<PlayerInfo> players = scoresheet.getPlayers();
		PlayerInfo newPlayer = new PlayerInfo(name, pass);
		for(PlayerInfo current: players) {
			if(current.getName().equalsIgnoreCase(name)) {
				return false;
			}
		}		
		players.add(newPlayer);
    	player = newPlayer;
	    try(FileOutputStream fos = new FileOutputStream("scoresheet.bin");
	    		ObjectOutputStream oos = new ObjectOutputStream(fos)){
	           oos.writeUnshared(scoresheet);
	           oos.flush();
	           oos.reset();
	    }
	    catch (IOException ex) {
			ex.printStackTrace();
		}
	    return true;
	}
	
	public boolean selectPlayer(String name, String pass) {

		List<PlayerInfo> players = scoresheet.getPlayers();
		for(PlayerInfo current: players) {
			if(current.getName().equalsIgnoreCase(name) & current.getPassword().equals(pass)) {
				player = current;
				return true;
			}
		}
		return false;
	}
	
	public void deletePlayer() {
		
		List<PlayerInfo> players = scoresheet.getPlayers();
		if(players.contains(player)) {
			players.remove(player);
			try(ObjectOutputStream oos = new ObjectOutputStream
			   (new BufferedOutputStream(new FileOutputStream("scoresheet.bin")))){
		           oos.writeUnshared(scoresheet);
		           oos.flush();
		           oos.reset();
			}			
			catch (IOException ex) {
				ex.printStackTrace();
			}
		Gui.output.setText("User profile deleted");
		}
	}
	
	private void updateScore(int scale) {
		
		List<PlayerInfo> players = scoresheet.getPlayers();
		if(players.contains(player)) {
			player.setScore(player.getScore() + level*scale);
			try(ObjectOutputStream oos = new ObjectOutputStream
			   (new BufferedOutputStream(new FileOutputStream("scoresheet.bin")))){
		           oos.writeUnshared(scoresheet);
		           oos.flush();
		           oos.reset();
			}			
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public List<PlayerInfo> getPlayers() {		
		return scoresheet.getPlayers();
	}
	
	private InternalServer server;
	private Mapper driver;
	private boolean isClientActivated = false;
		
	public InternalServer getServer() {
		return server;
	}
	public boolean isClientActive() {
		return isClientActivated;
	}	

	public void activateClient() {
		isClientActivated = true;
	}
	
	public void establishServer() {
		setLevel(0);		
		isClientActivated = true;
		if(driver == null) {
			driver = new Mapper();
		}
		server = new InternalServer();
		server.start();
	}
	
	public void disconnectClient() {
		if(server != null) {
			server.setLine("quit");
		}
		isClientActivated = false;
	}
	
	public void shutdownServer() {
		if(isClientActivated) {
			server.setLine("quit");
			isClientActivated = false;			
		}
		if(server != null) {
			server.interrupt();
			server = null;
		}
	}
	
	public void sendMoveToClient() {
		String line = null;		
		line = driver.output(getBoard());
		server.setLine(line);
	}
	
	public int[] receiveMoveFromClient() {
		String reply = null;
		reply = server.getAnswer();
		return driver.input(reply);		
	}
	
}
