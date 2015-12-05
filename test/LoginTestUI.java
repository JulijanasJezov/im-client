import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.junit.Test;

import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import junit.extensions.abbot.ComponentTestFixture;

public class LoginTestUI extends ComponentTestFixture {
	private LoginSwing login;

	@SuppressWarnings("deprecation")
	public void setUp() {
		login = new LoginSwing();
        login.show();
	}

	@Test
	public void test_initialWindow() throws ComponentNotFoundException, MultipleComponentsFoundException {
		JTextField textField = (JTextField)getFinder().find(new Matcher() {
					@Override
					public boolean matches(Component c) {
						return c instanceof JTextField;
					}
		        });
		assertTrue("checking the message textbox is empty", textField.getText().equals(""));
		
		JButton btnLogin = (JButton)getFinder().find(new Matcher() {
	        public boolean matches(Component c) {
	            return c instanceof JButton && ((JButton)c).getName().equals("btnLogin");
	        }
	    });
		assertEquals("checking the send button text", "Login", btnLogin.getText());
		
		JLabel lblVal = (JLabel)getFinder().find(new Matcher() {
			@Override
			public boolean matches(Component c) {
				return c instanceof JLabel && ((JLabel)c).getName().equals("lblValUsername");
			}
        });
		
		assertFalse("checking if validation label is hidden", lblVal.isVisible());
		
		JLabel lblFailed = (JLabel)getFinder().find(new Matcher() {
			@Override
			public boolean matches(Component c) {
				return c instanceof JLabel && ((JLabel)c).getName().equals("lblFailed");
			}
        });
		
		assertFalse("checking if failed connection label is hidden", lblFailed.isVisible());
		
		JLabel lblUsername = (JLabel)getFinder().find(new Matcher() {
			@Override
			public boolean matches(Component c) {
				return c instanceof JLabel && ((JLabel)c).getName().equals("lblUsername");
			}
        });
		
		assertTrue("checking if username label is shown", lblUsername.isVisible());
		
		assertTrue("checking the text of username label", lblUsername.getText().equals("Username"));
	}
	  
	@Test
	public void test_noUsername() throws ComponentNotFoundException, MultipleComponentsFoundException {
		JButton btnLogin = (JButton)getFinder().find(new Matcher() {
	        public boolean matches(Component c) {
	            return c instanceof JButton && ((JButton)c).getName().equals("btnLogin");
	        }
	    });
		
		btnLogin.doClick();
		
		JLabel lblVal = (JLabel)getFinder().find(new Matcher() {
			@Override
			public boolean matches(Component c) {
				return c instanceof JLabel && ((JLabel)c).getName().equals("lblValUsername");
			}
        });
		
		assertTrue("checking if label became visible", lblVal.isVisible());
	}
	
	@Test
	public void test_noConnection() throws ComponentNotFoundException, MultipleComponentsFoundException {
		JTextField textField = (JTextField)getFinder().find(new Matcher() {
			@Override
			public boolean matches(Component c) {
				return c instanceof JTextField;
			}
        });
		
		textField.setText("testUser");
		
		JButton btnLogin = (JButton)getFinder().find(new Matcher() {
	        public boolean matches(Component c) {
	            return c instanceof JButton && ((JButton)c).getName().equals("btnLogin");
	        }
	    });
		
		btnLogin.doClick();
		
		JLabel lblFailed = (JLabel)getFinder().find(new Matcher() {
			@Override
			public boolean matches(Component c) {
				return c instanceof JLabel && ((JLabel)c).getName().equals("lblFailed");
			}
        });
		
		assertTrue("checking if failed connection label has shown", lblFailed.isVisible());
	}
	
	public void tearDown() {
		login.dispose();
	}
}
