package control;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import data.Board;

public class Driver {

	public String[][] input(){
	
		ObjectMapper mapper = new ObjectMapper();
	
		Board board = new Board();

		try {
			board = mapper.readValue(new File("input.json"), Board.class);
		}
		catch (JsonParseException | JsonMappingException exc) {
			exc.printStackTrace();
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}	
		return board.getBoard();
	}
	
	public void output(String[][] pos) {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Board board = new Board(pos);
		
		try {
			mapper.writeValue(new File("output.json"), board);
		}
		catch (JsonGenerationException | JsonMappingException exc) {
			exc.printStackTrace();
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
}
