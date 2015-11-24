import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.filechooser.FileSystemView;

import org.junit.BeforeClass;
import org.junit.Test;

public class ChatClientTest {

	private static ChatClient chatClient;
	
	@BeforeClass
	public static void init() {
		chatClient = ChatClient.getInstance();
	}
	
	@Test
	public void test_notConnected() {
		assertFalse("checking if the connection is established", chatClient.isServerConnectionEstablished());
		assertFalse("checking if the user logged in", chatClient.isLoggedIn());
		assertTrue("checking the current user", chatClient.getCurrentUser() == null);
	}

	@Test
	public void test_loggedIn() throws IOException, InterruptedException {
		Path currentRelativePath = Paths.get(""); 
		String s = currentRelativePath.toAbsolutePath().toString(); 
		System.out.println("Current relative path is: " + s);
		Process proc = Runtime.getRuntime().exec("java -jar " + s + "/chatServer.jar");
		chatClient.connectToServer();
		
		chatClient.login("testUser");
		
		assertTrue("checking if the user logged in", chatClient.isLoggedIn());
		
		proc.destroy();
	}
	
}
