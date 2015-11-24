import static org.junit.Assert.*;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessengerTestUI {

	private FrameFixture window;
	private static Server server;
	private static ChatClient chatClient;
	
	  @BeforeClass
	  public static void initialise() throws InterruptedException {
	    FailOnThreadViolationRepaintManager.install();
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
	  }

	  @Before
	  public void openWindow() {
	    MessengerSwing frame = GuiActionRunner.execute(new GuiQuery<MessengerSwing>() {
	      protected MessengerSwing executeInEDT() {
	        return new MessengerSwing();
	      }
	    });
	    
	    window = new FrameFixture(frame);
	    window.show();
	  }

	  @Test
	  public void test_initialWindow() {
		  String result = window.textBox("txtFieldMessage").text();
		  assertTrue("checking the username textbox is empty", result.equals(""));
		  assertEquals("checking the send button text", "Send", window.button("btnSend").text());
		  assertEquals("checking the username label text", "Users", window.label("lblUsers").text());
		  assertEquals("checking the message label text", "Message", window.label("lblMessage").text());
		  assertEquals("checking the logged in label text", "Logged in as: null", window.label("lblLogged").text());
		  assertEquals("checking the chat label text", "Chat", window.label("lblChat").text());
		  String[] usersArray = window.list("listUsers").contents();
		  assertTrue("checking the list is empty", usersArray.length == 0);
		  assertTrue("checking the chat is empty", window.textBox("textAreaChat").text().equals(""));
	  }
	  
	  @Test
	  public void test_listUsers() throws InterruptedException {
		  chatClient.connectToServer();
		  chatClient.login("testUser");
		  Thread.sleep(1500);
		  String[] usersArray = window.list("listUsers").contents();
		  assertEquals("checking if contains new user", "testUser", usersArray[0]);
		  chatClient.disconnect();
	  }
	  
	  @After
	  public void closeWindow() {
	    window.cleanUp();
	  }

}
