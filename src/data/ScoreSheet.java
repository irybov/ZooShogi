package data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class ScoreSheet implements Externalizable {
	
	private List<Player> players = new ArrayList<>();
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public ScoreSheet() {}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
	       out.writeObject(this.getPlayers());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	       players = (List<Player>) in.readObject();
	}
	
}
