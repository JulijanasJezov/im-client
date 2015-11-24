import static org.junit.Assert.*;

import java.io.IOException;

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
		Process proc = Runtime.getRuntime().exec("java -jar chatServer.jar");
		chatClient.connectToServer();
		
		chatClient.login("testUser");
		
		assertTrue("checking if the user logged in", chatClient.isLoggedIn());
		
		proc.destroy();
	}
	
}
