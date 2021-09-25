package control;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import data.Board;

public class Driver {
	
	private static Driver INSTANCE = new Driver();
	
	public static Driver getInstance(){
		System.out.println("Driver loaded");
		return INSTANCE;
	}

	public String[][] input(String json){
		
		ObjectMapper mapper = new ObjectMapper();

		Board board = new Board();

		try {
//			board = mapper.readValue(new File("input.json"), Board.class);
			board = mapper.readValue(json, Board.class);
		}
		catch (JsonParseException | JsonMappingException exc) {
			exc.printStackTrace();
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}
		System.out.println("Received by server driver: " + board.getBoard());
		return board.getBoard();
	}
	
	public String output(String[][] position) {
		
		String reply = null;
		
		ObjectMapper mapper = new ObjectMapper();
		
		Board board = new Board(position);
		
		try {
//			mapper.writeValue(Paths.get("output.json").toFile(), board);
			reply = mapper.writeValueAsString(board);
		}
		catch (JsonGenerationException | JsonMappingException exc) {
			exc.printStackTrace();
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}
		System.out.println("Sended by server driver: " + reply);
		return reply;
	}
	
}
