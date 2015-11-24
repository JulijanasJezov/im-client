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
	
	@BeforeClass
	  public static void initialise() {
	    FailOnThreadViolationRepaintManager.install();
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
	  
	  @After
	  public void closeWindow() {
	    window.cleanUp();
	  }

}
