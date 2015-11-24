import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class MessengerSwing extends JFrame {

	private JPanel contentPane;
	private ChatClient chatClient;
	private JTextField txtFieldMessage;
	private static JList<String> listUsers;
	private static DefaultListModel<String> listModel;
	private static JTextArea textAreaChat;
	private String pmUser;

	public MessengerSwing() {
		listModel = new DefaultListModel<String>();
		chatClient = ChatClient.getInstance();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chatClient.quit();
				System.exit(0);
			}
		});
		
		setTitle("Messenger");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 575, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtFieldMessage = new JTextField();
		txtFieldMessage.setName("txtFieldMessage");
		txtFieldMessage.setBounds(163, 399, 311, 26);
		contentPane.add(txtFieldMessage);
		txtFieldMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setName("btnSend");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = txtFieldMessage.getText();
				if (message != null && !message.trim().equals("")) {
					txtFieldMessage.setText("");
					if (message.startsWith("PM ")) {
						addBroadcastMessage(message);
						message = message.substring(message.indexOf(':') + 1);
						chatClient.mesg(pmUser, message);
					} else {
						chatClient.hail(message);
					}
				}
			}
		});
		btnSend.setFont(new Font("Corbel", Font.PLAIN, 12));
		btnSend.setBounds(484, 399, 65, 26);
		contentPane.add(btnSend);
		
		getRootPane().setDefaultButton(btnSend);
		
		JLabel lblUsers = new JLabel("Users");
		lblUsers.setName("lblUsers");
		lblUsers.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblUsers.setBounds(10, 11, 46, 14);
		contentPane.add(lblUsers);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setName("lblMessage");
		lblMessage.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblMessage.setBounds(163, 374, 46, 14);
		contentPane.add(lblMessage);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setName("lblChat");
		lblChat.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblChat.setBounds(163, 11, 46, 14);
		contentPane.add(lblChat);
		
		JScrollPane scrollPaneChat = new JScrollPane();
		scrollPaneChat.setBounds(163, 34, 386, 329);
		contentPane.add(scrollPaneChat);
		
		textAreaChat = new JTextArea();
		textAreaChat.setLineWrap(true);
		textAreaChat.setName("textAreaChat");
		DefaultCaret caret = (DefaultCaret)textAreaChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPaneChat.setViewportView(textAreaChat);
		
		JScrollPane scrollPaneUsers = new JScrollPane();
		scrollPaneUsers.setBounds(10, 34, 143, 391);
		contentPane.add(scrollPaneUsers);
		
		listUsers = new JList<String>();
		listUsers.setName("listUsers");
		scrollPaneUsers.setViewportView(listUsers);
		listUsers.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		listUsers.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
            	int selectionIndex = listUsers.getSelectedIndex();
                if (selectionIndex != -1) {
                	pmUser = listUsers.getSelectedValue().toString();
                	txtFieldMessage.setText("PM " + pmUser +": ");
                }
                listUsers.clearSelection();
            }
        });
		
		Thread manageUsers = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					chatClient.list();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	    }, "manage users");
		manageUsers.start();
	}
	
	public static void addUsersToList(String[] users) {
		DefaultListModel<String> tempListModel = new DefaultListModel<String>();
		if (users != null) {
			for(String user : users) {
				tempListModel.addElement(user);
			}
			if (!listModel.contains(tempListModel)) 
			{
				listModel = tempListModel;
				listUsers.setModel(listModel);
			}
		}
	}
	
	public static void addBroadcastMessage(String message) {
		textAreaChat.append(message + System.lineSeparator());
		textAreaChat.setCaretPosition(textAreaChat.getDocument().getLength());
	}
}
