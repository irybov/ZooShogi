package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoreSheet implements Serializable {
	
	private List<PlayerStats> players = new ArrayList<>();
	
	public List<PlayerStats> getList() {
		return players;
	}
	
}
