package control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class InternalServer {

	public void establish() {
		
		ServerSocket server = null;
		
		try {
			try {
				InetAddress ia = InetAddress.getByName("localhost");			
				server = new ServerSocket(1980, 0, ia);
				
				while(true) {
					Socket socket = server.accept();
					operate(socket);
				}
				
			}
			catch(Exception exc) {
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
	
	public void operate(Socket socket) {
		
		try {
			InputStream  sin  = socket.getInputStream();
			OutputStream sout = socket.getOutputStream();
			Driver driver = null;
			String line = null;
			DataInputStream  dis = new DataInputStream (sin );
			DataOutputStream dos = new DataOutputStream(sout);
		
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
}
