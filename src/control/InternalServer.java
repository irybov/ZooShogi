package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InternalServer extends Thread{
	
	private static InternalServer INSTANCE = new InternalServer();
	
	public static InternalServer getInstance(){
		return INSTANCE;
	}
	
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

	public void run() {
		
		ServerSocket server = null;
		
		try {
			try {
				InetAddress ia = InetAddress.getByName("localhost");			
				server = new ServerSocket(1980, 1, ia);
				System.out.println("Server established");
				
				while(true) {
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

		System.out.println("Client connected");
		
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
				line = bq.take();				
				dos.writeUTF(line);
				dos.flush();
				System.out.println("Sended from server" + line);
			    if(line.equals("quit")) {
					line = null;
					socket.close();
			    	break;
			    }
			    reply = dis.readUTF();
				bq.put(reply);			    
				System.out.println("Received by server" + reply);
			}		
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
}
