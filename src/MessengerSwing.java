import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class MessengerSwing extends JFrame {

	private JPanel contentPane;
	private ChatClient chatClient;
	private JTextField txtFieldMessage;
	private static JList<String> listUsers;
	private static JList<String> listChat;
	private static DefaultListModel<String> chatListModel;

	public MessengerSwing() {
		chatListModel = new DefaultListModel<String>();
		chatClient = ChatClient.getInstance();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chatClient.quit();
				System.exit(0);
			}
		});
		
		setTitle("Messenger");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 575, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		listUsers = new JList<String>();
		listUsers.setBounds(10, 34, 143, 391);
		listUsers.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(listUsers);
		
		txtFieldMessage = new JTextField();
		txtFieldMessage.setBounds(163, 399, 311, 26);
		contentPane.add(txtFieldMessage);
		txtFieldMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = txtFieldMessage.getText();
				if (message != null && !message.trim().equals("")) {
					chatClient.hail(message);
				}
			}
		});
		btnSend.setFont(new Font("Corbel", Font.PLAIN, 12));
		btnSend.setBounds(484, 399, 65, 26);
		contentPane.add(btnSend);
		
		JLabel lblUsers = new JLabel("Users");
		lblUsers.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblUsers.setBounds(10, 11, 46, 14);
		contentPane.add(lblUsers);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblMessage.setBounds(163, 374, 46, 14);
		contentPane.add(lblMessage);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblChat.setBounds(163, 11, 46, 14);
		contentPane.add(lblChat);
		
		listChat = new JList<String>();
		listChat.setBounds(163, 34, 386, 334);
		listChat.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		contentPane.add(listChat);
		
		Thread manageUsers = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					chatClient.list();
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	    }, "manage users");
		manageUsers.start();
		
		/*Thread listenForBroadcast = new Thread(new Runnable() {
			@Override
			public void run() {
				DefaultListModel<String> chatListModel = new DefaultListModel<String>();
				while(true) {
					String message = chatClient.readLine();
					
					if (message != null && message.indexOf("Broadcast from") != -1) {
						String newMessage = message.substring(message.indexOf("from " + 5));
						chatListModel.addElement(newMessage);
						listChat.setModel(chatListModel);
					} 
				}
			}
	    }, "listen for messages");
		listenForBroadcast.start();*/
	}
	
	public static void addUsersToList(String[] users) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		if (users != null) {
			for(String user : users) {
				listModel.addElement(user);
			}
			listUsers.setModel(listModel);
		}
	}
	
	public static void addBroadcastMessage(String message) {
		chatListModel.addElement(message);
		listChat.setModel(chatListModel);
	}
}
