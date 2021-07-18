package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoreSheet implements Serializable {
	
	private List<Player> players = new ArrayList<>();
	
	public List<Player> getList() {
		return players;
	}
}
