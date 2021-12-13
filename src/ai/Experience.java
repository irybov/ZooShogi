package ai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Experience {

	private Set<String> neg;
	private Map<String, Node> pos;
	{
		File negative = new File("exp/negative.bin");
		if(!negative.exists()) {
			try {
				negative.createNewFile();
				neg = new HashSet<>();
			    try(ObjectOutputStream oos = new ObjectOutputStream
			       (new BufferedOutputStream(new FileOutputStream("exp/negative.bin")))){
			           oos.writeObject(neg);
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(ObjectInputStream ois = new ObjectInputStream
	       (new BufferedInputStream(new FileInputStream("exp/negative.bin")))){
	    	neg = (Set<String>) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	    
		File positive = new File("exp/positive.bin");
		if(!positive.exists()) {
			try {
				positive.createNewFile();
				pos = new HashMap<>();
			    try(ObjectOutputStream oos = new ObjectOutputStream
			       (new BufferedOutputStream(new FileOutputStream("exp/positive.bin")))){
			           oos.writeObject(pos);
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(ObjectInputStream ois = new ObjectInputStream
	       (new BufferedInputStream(new FileInputStream("exp/positive.bin")))){
	    	pos = (Map<String, Node>) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	boolean bingo(String note) {
		return neg.contains(note);
	}
	
	void learn(String note) {
		neg.add(note);
	    try(ObjectOutputStream oos = new ObjectOutputStream
		   (new BufferedOutputStream(new FileOutputStream("exp/negative.bin")))){
	           oos.writeUnshared(neg);
	           oos.flush();
	           oos.reset();
	    }			
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	boolean has(String note) {
		return pos.containsKey(note);		
	}
	Node get(String note) {
		return pos.get(note);		
	}
	
	void learn(String note, Node node) {
		pos.putIfAbsent(note, node);
	    try(ObjectOutputStream oos = new ObjectOutputStream
		   (new BufferedOutputStream(new FileOutputStream("exp/positive.bin")))){
	           oos.writeUnshared(pos);
	           oos.flush();
	           oos.reset();
	    }			
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
