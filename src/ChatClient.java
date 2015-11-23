import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatClient {
	private static ChatClient instance = null;
	
	private boolean serverConnection;
	private static BufferedReader in;
	private static PrintWriter out;
	private static Socket socket;
	final static int PORT = 9000;
	final static String HOST = "localhost";
	private Thread listener;
	
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
		checkServerConnection();
		if (!serverConnection) return false;
		
		if (username == null || username.trim().equals("")) {
			return false;
		}
		
		out.println("IDEN " + username);
		String response = readLine();
		
		if (response == null) {
			return false;
		} 
		
		isLoggedIn = true;
		setupListener();
		return true;
	}
	
	private void setupListener() {
		listener = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					String message = readLine();
					if (message != null && message.indexOf(",") != -1) {
						message = message.substring(message.indexOf("OK") + 3);
						String [] users = message.split(", ");
						MessengerSwing.addUsersToList(users);
					}
					
					if (message != null && message.indexOf("Broadcast from") != -1) {
						MessengerSwing.addBroadcastMessage(message);
					} 
					
					if (message != null && message.startsWith("PM from")) {
						MessengerSwing.addBroadcastMessage(message);
					}
				}
			}
	    }, "listener");
		listener.start();
	}
	
	public void quit() {
		checkServerConnection();
		if (!serverConnection) return;
		
		out.println("QUIT");
		readLine();
	}
	
	public void list() {
		String[] users = null;
		checkServerConnection();
		if (!serverConnection) return;
		
		out.println("LIST");
	}
	
	public void mesg(String user, String message) {
		checkServerConnection();
		if (!serverConnection) return;
		
		out.println("MESG " + user + " " + message);
	}
	
	public void hail(String message) {
		checkServerConnection();
		if (!serverConnection) return;
		
		out.println("HAIL " + message);
	}
	
	public boolean isServerConnectionEstablished() {
		return serverConnection;
	}
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public String readLine() {
		try {
			return in.readLine();
		} catch (IOException e) {
			serverConnection = false;
			return null;
		}
	}
	
	private void checkServerConnection() {
		if (out == null || in == null) {
			serverConnection = false;
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
			readLine();
		} catch (UnknownHostException uhe) {
			System.out.println("Failed to connect to the server at " + HOST + PORT);
		} catch (IOException ioe) {
			System.out.println("Failed to connect to the server at " + HOST + PORT);
		}
	}
}
