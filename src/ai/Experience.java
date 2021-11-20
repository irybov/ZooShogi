package ai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Experience {

	private Set<String> exp;	
	{
		File file = new File("experience.bin");
		if(!file.exists()) {
			try {
				file.createNewFile();
				exp = new CopyOnWriteArraySet<>();
			    try(ObjectOutputStream oos = new ObjectOutputStream
			       (new BufferedOutputStream(new FileOutputStream("experience.bin")))){
			           oos.writeObject(exp);
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(ObjectInputStream ois = new ObjectInputStream
	       (new BufferedInputStream(new FileInputStream("experience.bin")))){
	    	exp = (Set<String>) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	boolean bingo(String note) {
		return exp.contains(note);		
	}
	
	void learn(String note) {
		exp.add(note);
	    try(ObjectOutputStream oos = new ObjectOutputStream
		   (new BufferedOutputStream(new FileOutputStream("experience.bin")))){
	           oos.writeUnshared(exp);
	           oos.flush();
	           oos.reset();
	    }			
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
