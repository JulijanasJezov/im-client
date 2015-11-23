import static org.junit.Assert.*;

import java.awt.Color;

import static org.assertj.swing.core.MouseButton.LEFT_BUTTON;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.ColorFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginTestUI {
	
	private FrameFixture window;

	  @BeforeClass
	  public static void initialise() {
	    FailOnThreadViolationRepaintManager.install();
	  }

	  @Before
	  public void openWindow() {
	    LoginSwing frame = GuiActionRunner.execute(new GuiQuery<LoginSwing>() {
	      protected LoginSwing executeInEDT() {
	        return new LoginSwing();
	      }
	    });
	    
	    window = new FrameFixture(frame);
	  }

	  @Test
	  public void test_initialWindow() {
		  String result = window.textBox("txtFieldUsername").text();
		  assertTrue("checking the username textbox is empty", result.equals(""));
		  assertEquals("checking the login button text", "Login", window.button("btnLogin").text());
		  assertEquals("checking the username label text", "Username", window.label("lblUsername").text());
	  }
	  
	  @After
	  public void closeWindow() {
	    window.cleanUp();
	  }
	
}
