package control;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class Mapper {

	public int[] input(String json){
		
		ObjectMapper mapper = new ObjectMapper();
		int[] move = null;

		try {
			move = mapper.readValue(json, int[].class);
		}
		catch (JsonParseException | JsonMappingException exc) {
			exc.printStackTrace();
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}	
	return move;
	}
	
	public String output(String[][] position) {
		
		String reply = null;		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			reply = mapper.writeValueAsString(position);
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
