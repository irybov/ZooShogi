package control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

class InternalServer extends Thread{
	
	private String line = null;
	private String reply = null;
//	private BlockingQueue<String> bq = new ArrayBlockingQueue<>(1, true);
	private BlockingQueue<String> bq = new SynchronousQueue<>();
		
	public void setLine(String line) {
		reply = null;
		this.line = line;
		try {
			bq.put(line);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
	}
	public String getAnswer() {
		line = null;		
		try {
			reply = bq.take();
		} catch (InterruptedException exc) {
			exc.printStackTrace();
		}
		return reply;
	}
	
    private Properties props;
    private int PORT;
    private String URL;
	{
		try (Reader config = new BufferedReader(new FileReader("config.txt"))) {	
	        props = new Properties();
	        props.load(config);
	        PORT = Integer.parseInt(props.getProperty("port"));
	        URL = props.getProperty("url");	
	    }
		catch (IOException ex) {
	        ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		ServerSocket server = null;
		
		try {
			try {
				InetAddress ia = InetAddress.getByName(URL);			
				server = new ServerSocket(PORT, 1, ia);
				
				while(!Thread.interrupted()) {
					Socket socket = server.accept();
					operate(socket);
				}
				
			}
			catch(IOException exc) {
				exc.printStackTrace();
			}
		}
		finally {
			try {
				if(server != null) {				
					server.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void operate(Socket socket) {
		
		InputStream sin = null;
		OutputStream sout = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
   		DataInputStream dis = null;
   		DataOutputStream dos = null;
		
		try {
			sin  = socket.getInputStream();
			sout = socket.getOutputStream();
			bis = new BufferedInputStream(sin);
			bos = new BufferedOutputStream(sout);
			dis = new DataInputStream (bis);
			dos = new DataOutputStream(bos);
						
			while(true) {
				try {
					line = bq.take();
				}
				catch (InterruptedException exc) {
					exc.printStackTrace();
				}				
				dos.writeUTF(line);
				dos.flush();
			    if(line.equals("quit")) {		    	
					line = null;
			    	break;
			    }
			    reply = dis.readUTF();
				try {
					bq.put(reply);
				}
				catch (InterruptedException exc) {
					exc.printStackTrace();
				}			    
			}		
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}
		finally {
			try {
				dis.close();
				dos.close();
				socket.close();
			}
			catch (IOException exc) {
				exc.printStackTrace();
			}			
		}
	}
	
}
