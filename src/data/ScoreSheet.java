package data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class ScoreSheet implements Externalizable {
	
	private List<PlayerInfo> players = new ArrayList<>();
	
	public List<PlayerInfo> getPlayers() {
		return players;
	}
	
	public ScoreSheet() {}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
	       out.writeObject(this.getPlayers());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	       players = (List<PlayerInfo>) in.readObject();
	}
	
}
