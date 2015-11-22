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
	private boolean serverConnectionEstablished;
	private ChatClient chatClient;
	private static LoginSwing frame;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public LoginSwing() {
		chatClient = ChatClient.getInstance();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!chatClient.isLoggedIn()) {
					chatClient.quit();
					System.exit(0);
				}
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtFieldUsername = new JTextField();
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
		btnLogin.setBounds(148, 107, 128, 34);
		contentPane.add(btnLogin);
		
		final JLabel lblFailedToConnect = new JLabel("Failed to connect to the server");
		lblFailedToConnect.setName("lblFailed");
		lblFailedToConnect.setVisible(false);
		lblFailedToConnect.setForeground(Color.RED);
		lblFailedToConnect.setVerticalAlignment(SwingConstants.BOTTOM);
		lblFailedToConnect.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFailedToConnect.setFont(new Font("Corbel", Font.PLAIN, 12));
		lblFailedToConnect.setBounds(224, 191, 200, 20);
		contentPane.add(lblFailedToConnect);
		

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!chatClient.isServerConnectionEstablished()) {
					lblFailedToConnect.setVisible(true);
					chatClient.connectToServer();
				} else {
					lblFailedToConnect.setVisible(false);
					boolean loggedIn = chatClient.login(txtFieldUsername.getText());
					
					if (loggedIn) {
						MessengerSwing messenger = new MessengerSwing();
						frame.dispose();
						messenger.setVisible(true);
					}
				}
			}
		});
	}
}
