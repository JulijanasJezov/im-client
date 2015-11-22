import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	private static ChatClient instance = null;
	
	private boolean serverConnection;
	private static BufferedReader in;
	private static PrintWriter out;
	private static Socket socket;
	final static int PORT = 9000;
	final static String HOST = "localhost";
	
	private boolean isLoggedIn;
	
	protected ChatClient() { 
		isLoggedIn = false;
		connectToServer();
	}
	   
	public static ChatClient getInstance() {
		if(instance == null) {
			instance = new ChatClient();
	    }
	    return instance;
	}
	
	public boolean login(String username) {
		if (username == null || username.trim().equals("")) {
			return false;
		}
		
		out.println("IDEN " + username);
		String response = readLine();
		
		if (response == null) {
			return false;
		} 
		
		isLoggedIn = true;
		return true;
	}
	
	public void quit() {
		out.println("QUIT");
		readLine();
	}
	
	public boolean isServerConnectionEstablished() {
		return serverConnection;
	}
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	private String readLine() {
		try {
			return in.readLine();
		} catch (IOException e) {
			serverConnection = false;
			return null;
		}
	}
	
	public void connectToServer() {
		serverConnection = false;
		try {
			socket = new Socket("localhost", PORT);
	        System.out.println("Connected to the server on port " + PORT);
	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			serverConnection = true;
		} catch (UnknownHostException uhe) {
			System.out.println("Failed to connect to the server at " + HOST + PORT);
		} catch (IOException ioe) {
			System.out.println("Failed to connect to the server at " + HOST + PORT);
		}
	}
}
