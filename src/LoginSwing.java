import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginSwing extends JFrame {

	private JPanel contentPane;
	private JTextField txtFieldUsername;
	private ChatNetwork chatClient;
	private static LoginSwing frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new LoginSwing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginSwing() {
		chatClient = ChatNetwork.getInstance();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!chatClient.isLoggedIn()) {
					chatClient.quit();
				}
			}
		});
		
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtFieldUsername = new JTextField();
		txtFieldUsername.setName("txtFieldUsername");
		txtFieldUsername.setToolTipText("");
		txtFieldUsername.setBounds(148, 76, 128, 20);
		contentPane.add(txtFieldUsername);
		txtFieldUsername.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setName("lblUsername");
		lblUsername.setLabelFor(txtFieldUsername);
		lblUsername.setFont(new Font("Corbel", Font.PLAIN, 18));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(148, 29, 128, 36);
		contentPane.add(lblUsername);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setName("btnLogin");
		btnLogin.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnLogin.setBounds(148, 104, 128, 34);
		contentPane.add(btnLogin);
		
		final JLabel lblFailedToConnect = new JLabel("Failed to connect to the server");
		lblFailedToConnect.setName("lblFailed");
		lblFailedToConnect.setVisible(false);
		lblFailedToConnect.setForeground(Color.RED);
		lblFailedToConnect.setVerticalAlignment(SwingConstants.BOTTOM);
		lblFailedToConnect.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFailedToConnect.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblFailedToConnect.setBounds(234, 190, 200, 20);
		contentPane.add(lblFailedToConnect);
		
		final JLabel lblValUsername = new JLabel("Username is required");
		lblValUsername.setName("lblValUsername");
		lblValUsername.setVisible(false);
		lblValUsername.setForeground(Color.RED);
		lblValUsername.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblValUsername.setBounds(286, 80, 118, 14);
		contentPane.add(lblValUsername);
		

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtFieldUsername.getText();
				if (username == null || username.trim().equals("")) {
					lblValUsername.setVisible(true);
					return;
				}
				
				if (!chatClient.isServerConnectionEstablished()) {
					lblFailedToConnect.setVisible(true);
					chatClient.connectToServer();
				}
				
				lblValUsername.setVisible(false);
				
				boolean loggedIn = chatClient.login(username);
					
				if (loggedIn) {
					MessengerSwing messenger = new MessengerSwing();
					frame.dispose();
					messenger.setVisible(true);
				}
			}
		});
	}
}
