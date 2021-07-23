package data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class ScoreSheet implements Externalizable {
	
	private List<PlayerStats> players = new ArrayList<>();
	
	public List<PlayerStats> getList() {
		return players;
	}
	
	public ScoreSheet() {}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
	       out.writeObject(this.getList());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	       players = (List<PlayerStats>) in.readObject();
	}
	
}