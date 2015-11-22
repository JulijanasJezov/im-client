import static org.junit.Assert.*;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginTest {

	private FrameFixture window;
	
	@BeforeClass
	public static void innitialize() {
	  FailOnThreadViolationRepaintManager.install();
	}
	
	@Before
	public void openFrame() {
	  LoginSwing frame = GuiActionRunner.execute(new GuiQuery<LoginSwing>() {
	    protected LoginSwing executeInEDT() {
	      return new LoginSwing();
	    }
	  });
	  window = new FrameFixture(frame);
	  window.show();
	}
	
	@Test
	public void test_fake() {
		
	}
	
	@After
	public void closeFrame() {
	  window.cleanUp();
	}
}
