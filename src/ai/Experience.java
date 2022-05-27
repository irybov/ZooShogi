package ai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

class Experience {

	private Set<String> negativeExp;
	private Map<String, Node> positiveExp;
	{
		File negative = new File("exp/negative.bin");
		if(!negative.exists()) {
			try {
				negative.createNewFile();
				negativeExp = new HashSet<>();
			    try(ObjectOutputStream oos = new ObjectOutputStream
			       (new BufferedOutputStream(new FileOutputStream("exp/negative.bin")))){
			           oos.writeUnshared(negativeExp);
			           oos.flush();
			           oos.reset();
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	    try(ObjectInputStream ois = new ObjectInputStream
	       (new BufferedInputStream(new FileInputStream("exp/negative.bin")))){
	    	negativeExp = (Set<String>) ois.readObject();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	    
		File positive = new File("exp/positive.zip");
		if(!positive.exists()) {
			try {
				positive.createNewFile();
				positiveExp = new HashMap<>();
			    try(FileOutputStream fos = new FileOutputStream("exp/positive.zip");
				    BufferedOutputStream bos = new BufferedOutputStream(fos);
			    	ZipOutputStream zos = new ZipOutputStream(bos)){
			    	
			        // Convert Map to byte array
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        ObjectOutputStream oos = new ObjectOutputStream(baos);
			        oos.writeObject(positiveExp);
			        oos.flush();
			        byte[] data = baos.toByteArray();

		            ZipEntry entry = new ZipEntry("positive.bin");
		            zos.putNextEntry(entry);
		            ByteArrayInputStream bais = new ByteArrayInputStream(data);
		            byte[] buffer = new byte[8192];
		            int length;
		            while ((length = bais.read(buffer)) > 0) {
		                zos.write(buffer, 0, length);
		                zos.flush();
		            }
		            zos.closeEntry();
		            oos.close();
		            baos.close();
		            bais.close();
			    }			
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}

    	ZipFile zipped = null;
		try {
			zipped = new ZipFile("exp/positive.zip");
		}
		catch (IOException exc) {
			exc.printStackTrace();
		}
    	ZipEntry entry = zipped.getEntry("positive.bin");   	
	    try(InputStream fis = zipped.getInputStream(entry);
	    	BufferedInputStream bis = new BufferedInputStream(fis)){

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	int count;
	        byte[] buffer = new byte[8192];
	        while ((count = bis.read(buffer, 0, 8192)) > 0) {
	            baos.write(buffer, 0, count);
	            baos.flush();
	        }  
	        // Parse byte array to Map
	        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	        ObjectInputStream ois = new ObjectInputStream(bais);
	    	positiveExp = (Map<String, Node>) ois.readObject();
	    	zipped.close();
            ois.close();
            baos.close();
            bais.close();
	    }
	    catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	boolean bingo(String note) {
		return negativeExp.contains(note);
	}
	
	void learn(String note) {
		negativeExp.add(note);
	    try(ObjectOutputStream oos = new ObjectOutputStream
		   (new BufferedOutputStream(new FileOutputStream("exp/negative.bin")))){
	           oos.writeUnshared(negativeExp);
	           oos.flush();
	           oos.reset();
	    }			
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	boolean hasNode(String note) {
		return positiveExp.containsKey(note);		
	}
	Node getNode(String note) {
		return positiveExp.get(note);		
	}
	
	void learn(String note, Node node) {
		positiveExp.putIfAbsent(note, node);
	    try(FileOutputStream fos = new FileOutputStream("exp/positive.zip");
		    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    	ZipOutputStream zos = new ZipOutputStream(bos)){
	    	
	        // Convert Map to byte array
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(baos);
	        oos.writeObject(positiveExp);
	        oos.flush();
	        byte[] data = baos.toByteArray();

            ZipEntry entry = new ZipEntry("positive.bin");
            zos.putNextEntry(entry);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bais.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
		        zos.flush();
            }
            zos.closeEntry();
            oos.close();
            baos.close();
            bais.close();
	    }	    			
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
