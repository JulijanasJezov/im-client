import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;

import org.junit.Test;

import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import junit.extensions.abbot.ComponentTestFixture;

public class MessengerTestUI extends ComponentTestFixture {

	private MessengerSwing messenger;
	private static Server server;
	private static ChatClient chatClient;
	private Thread serverRun;

	@SuppressWarnings("deprecation")
	  public void setUp() {
		  messenger = new MessengerSwing();
		  messenger.show();
	  }

	  @Test
	  public void test_initialWindow() throws ComponentNotFoundException, MultipleComponentsFoundException, InterruptedException {
		  runServer();
		  
		  chatClient.connectToServer();
		  Thread.sleep(1000); // Allow some time for server to start
			
		  chatClient.login("testUser");
		  
		  JTextField messangeField = (JTextField)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JTextField;
				}
	      });
		  assertTrue("checking the message textbox is empty", messangeField.getText().equals(""));
		  
		  JButton btnSend = (JButton)getFinder().find(new Matcher() {
		        public boolean matches(Component c) {
		            return c instanceof JButton && ((JButton)c).getName().equals("btnSend");
		        }
		  });
		  assertEquals("checking the send button text", "Send", btnSend.getText());
		  
		  JLabel lblUsers = (JLabel)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JLabel && ((JLabel)c).getName().equals("lblUsers");
				}
	      });
		  assertEquals("checking the username label text", "Users", lblUsers.getText());
		  
		  JLabel lblMessage = (JLabel)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JLabel && ((JLabel)c).getName().equals("lblMessage");
				}
	      });
		  assertEquals("checking the message label text", "Message", lblMessage.getText());
		  
		  JLabel lblLogged = (JLabel)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JLabel && ((JLabel)c).getName().equals("lblLogged");
				}
	      });
		  
		  assertEquals("checking the logged in label text", "Logged in as: testUser", lblLogged.getText());
		  
		  JLabel lblChat = (JLabel)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JLabel && ((JLabel)c).getName().equals("lblChat");
				}
	      });
		  assertEquals("checking the chat label text", "Chat", lblChat.getText());
		  
		  @SuppressWarnings("unchecked")
		  JList<String> listUsers = (JList<String>)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JList && ((JList<String>)c).getName().equals("listUsers");
				}
	      });
		  
		  ListModel<String> listModel = listUsers.getModel();
		  
		  assertTrue("checking the list is empty", listModel.getSize() == 1);
		  
		  JTextArea textAreaChat = (JTextArea)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JTextArea && ((JTextArea)c).getName().equals("textAreaChat");
				}
	      });
		  assertTrue("checking the chat is empty", textAreaChat.getText().equals(""));
	  }
	  
	  @SuppressWarnings("deprecation")
	@Test
	  public void test_list() throws InterruptedException, ComponentNotFoundException, MultipleComponentsFoundException {
		  runServer();
		 
		  chatClient.connectToServer();
		  Thread.sleep(1000); // Allow some time for server to start
			
		  chatClient.login("testUser");
		  Thread.sleep(1000); // wait for UI list to be updated
		  
		  @SuppressWarnings("unchecked")
		  JList<String> listUsers = (JList<String>)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JList && ((JList<String>)c).getName().equals("listUsers");
				}
	      });
		 
		  ListModel<String> listModel = listUsers.getModel();
		  
		  String username = listModel.getElementAt(0);
		  
		  assertEquals("checking if contains new user", "testUser", username);
		  
	  }
	  
	  @SuppressWarnings("deprecation")
	@Test
	  public void test_sendingMessage() throws InterruptedException, ComponentNotFoundException, MultipleComponentsFoundException {
		  runServer();
		  
		  chatClient.connectToServer();
		  Thread.sleep(1000); // Allow some time for server to start
			
		  chatClient.login("testUser");
		  Thread.sleep(1000);
		  JTextField messageField = (JTextField)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JTextField;
				}
	      });
		  
		  JButton btnSend = (JButton)getFinder().find(new Matcher() {
		        public boolean matches(Component c) {
		            return c instanceof JButton && ((JButton)c).getName().equals("btnSend");
		        }
		  });
		  assertTrue(chatClient.isLoggedIn());
		  messageField.setText("Hello");
		  
		  btnSend.doClick();
		  Thread.sleep(1000); // wait for UI text area to be updated
		  
		  JTextArea textAreaChat = (JTextArea)getFinder().find(new Matcher() {
				@Override
				public boolean matches(Component c) {
					return c instanceof JTextArea && ((JTextArea)c).getName().equals("textAreaChat");
				}
	      });
		  assertEquals("checking the message is broadcasted", "Broadcast from testUser: Hello" + System.lineSeparator(), textAreaChat.getText());
		  
	  }
	  
	  private void runServer() {
		  chatClient = ChatClient.getInstance();
		  if (chatClient.isServerConnectionEstablished()) return;
		  serverRun = new Thread(new Runnable() {
			  @Override
			  public void run() {
				  server = new Server(9000, false);
			  }
		  }, "server");
		  serverRun.start();
	  }
	  
	  public void tearDown() {
	    messenger.dispose();
	  }

}
