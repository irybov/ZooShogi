package control;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import data.Board;
import ui.Gui;

class Driver {

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
		Gui.output.setText(board.getMessage());
		long score = board.getScore();
		Gui.score.setText(score > 0 ? "+" + Long.toString(score) : Long.toString(score));		
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
		return reply;
	}
	
}
