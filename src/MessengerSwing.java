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

public class MessengerSwing extends JFrame {

	private JPanel contentPane;
	private ChatClient chatClient;
	private JTextField txtFieldMessage;
	/**
	 * Create the frame.
	 */
	public MessengerSwing() {
		chatClient = ChatClient.getInstance();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chatClient.quit();
				System.exit(0);
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 575, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JList<String> listUsers = new JList<String>();
		listUsers.setBounds(10, 34, 143, 391);
		listUsers.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(listUsers);
		
		txtFieldMessage = new JTextField();
		txtFieldMessage.setBounds(163, 399, 311, 26);
		contentPane.add(txtFieldMessage);
		txtFieldMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
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
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(163, 34, 386, 329);
		contentPane.add(textArea);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblChat.setBounds(163, 11, 46, 14);
		contentPane.add(lblChat);
		
		Thread manageUsers = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					DefaultListModel<String> listModel = new DefaultListModel<String>();
					String[] users = chatClient.list();
					if (users != null) {
						for(String user : users) {
							listModel.addElement(user);
						}
						listUsers.setModel(listModel);
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	    }, "manage users");
		manageUsers.start();
	}
}
