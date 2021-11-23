package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class LocalServer extends Thread{
	
	private String line = null;
	private String reply = null;
	private BlockingQueue<String> bq = new ArrayBlockingQueue<>(1, true);
		
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
	
    Properties prop;
    int PORT;
    String URL;
	{
		try (InputStream input = new FileInputStream("config.txt")) {
	
	        prop = new Properties();
	        prop.load(input);
	        PORT = Integer.parseInt(prop.getProperty("port"));
	        URL = prop.getProperty("url");
	
	    }
		catch (IOException ex) {
	        ex.printStackTrace();
		}
	}

	public void run() {
		
		ServerSocket server = null;
		
		try {
			try {
				InetAddress ia = InetAddress.getByName(URL);			
				server = new ServerSocket(PORT, 1, ia);
//				System.out.println("Server established");
				
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

//		System.out.println("Client connected");
		
		InputStream sin = null;
		OutputStream sout = null;
   		DataInputStream dis = null;
   		DataOutputStream dos = null;
		
		try {
			sin  = socket.getInputStream();
			sout = socket.getOutputStream();
			dis = new DataInputStream (sin);
			dos = new DataOutputStream(sout);
						
			while(true) {
				try {
					line = bq.take();
				}
				catch (InterruptedException exc) {
					exc.printStackTrace();
				}				
				dos.writeUTF(line);
				dos.flush();
//				System.out.println("Sended from server: " + line);
			    if(line.equals("quit")) {
//					System.out.println("Client disconnected from server");			    	
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
//				System.out.println("Received by server: " + reply);
			}		
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}
		finally {
			try {
				socket.close();
			}
			catch (IOException exc) {
				exc.printStackTrace();
			}			
		}
	}
	
}
