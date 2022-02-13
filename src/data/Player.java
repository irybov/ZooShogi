package data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Player implements Externalizable, Comparable<Player> {

	private String name;
	private String pass;
	private Integer score = 0;
	private String date = setRegistrationDate();
		
	public Player(String name, String pass) {
		this.name = name;
		this.pass = pass;
	}
	
	public Player() {}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return pass;
	}
	public Integer getScore() {
		return score;
	}
	private String setRegistrationDate() {		
		LocalDate now = LocalDate.now();
		return now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	public String getRegistrationDate() {
		return date;
	}
	
	@Override
	public int compareTo(Player another) {
		return score.compareTo(another.score);
	}	
	@Override
	public String toString() {
		return "Player [name=" + name + ", date=" + date + ", score=" + score + "]";
	}	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		return true;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

	       out.writeObject(this.getName());
	       out.writeObject(this.encryptString(this.getPassword()));
	       out.writeObject(this.getScore());
	       out.writeObject(this.getRegistrationDate());
	}
	private Object encryptString(String pass2) {
		return Base64.getEncoder().encodeToString(pass2.getBytes());
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

	       name = (String) in.readObject();
	       pass = (String) this.decryptString((String) in.readObject());
	       score = (Integer) in.readObject();
	       date = (String) in.readObject();
	}
	private String decryptString(String pass2) {
		return new String(Base64.getDecoder().decode(pass2));
	}
	
}
