package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoreSheet implements Serializable {
	
	private List<Player> players = new ArrayList<>();
	
	public List<Player> getList() {
		return players;
	}

	public boolean create(Player player) {

		if(!players.contains(player)) {
			players.add(player);
			return true;
		}
		return false;
	}
	
	public Player select(String name, String pass) {

		for(Player player: players) {	
			if(player.getName().equalsIgnoreCase(name) & player.getPass().equals(pass))
			return player;
		}
		return null;
	}
	
	public boolean delete(Player player) {

		if(players.contains(player)) {
			players.remove(player);
			return true;
		}
		return false;
	}
	
	public void update(Player player, int score) {
		
		if(players.contains(player)) {
		player.setScore(player.getScore() + score);
		}		
	}
	
}
