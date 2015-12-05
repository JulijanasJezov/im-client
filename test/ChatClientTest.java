import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChatClientTest {

	private static ChatNetwork chatNetwork;
    private static Server server;
	
	@BeforeClass
	public static void init() throws InterruptedException {
		chatNetwork = ChatNetwork.getInstance();
		assertFalse("checking if the connection is established", chatNetwork.isServerConnectionEstablished());
		
		Thread serverRun = new Thread(new Runnable() {
			@Override
			public void run() {
				server = new Server(9000, false);
			}
	    }, "server");
		serverRun.start();
		
		Thread.sleep(1000); // Allow some time for server to start

		chatNetwork.connectToServer();
	}
	
	@Test
	public void test_connected() {
		assertTrue("check if connected to the server", chatNetwork.isServerConnectionEstablished());
	}
	
	@Test
	public void test_notLoggedIn() {
		assertFalse("checking if the user logged in", chatNetwork.isLoggedIn());
		assertTrue("checking the current user", chatNetwork.getCurrentUser() == null);
	}

	@Test
	public void test_loggedIn() throws IOException, InterruptedException {
		chatNetwork.login("testUser");
		
		assertTrue("checking if the user logged in", chatNetwork.isLoggedIn());
	}
	
}
