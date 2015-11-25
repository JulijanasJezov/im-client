import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChatClientTest {

	private static ChatClient chatClient;
    private static Server server;
	
	@BeforeClass
	public static void init() throws InterruptedException {
		chatClient = ChatClient.getInstance();
		assertFalse("checking if the connection is established", chatClient.isServerConnectionEstablished());
		
		Thread serverRun = new Thread(new Runnable() {
			@Override
			public void run() {
				server = new Server(9000, false);
			}
	    }, "manage users");
		serverRun.start();
		
		Thread.sleep(1000); // Allow some time for server to start

		chatClient.connectToServer();
	}
	
	@Test
	public void test_connected() {
		assertTrue("check if connected to the server", chatClient.isServerConnectionEstablished());
	}
	
	@Test
	public void test_notLoggedIn() {
		assertFalse("checking if the user logged in", chatClient.isLoggedIn());
		assertTrue("checking the current user", chatClient.getCurrentUser() == null);
	}

	@Test
	public void test_loggedIn() throws IOException, InterruptedException {
		chatClient.login("testUser");
		
		assertTrue("checking if the user logged in", chatClient.isLoggedIn());
	}
	
}
