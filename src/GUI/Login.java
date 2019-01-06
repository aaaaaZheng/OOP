package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.User;
import common.UserData;
import socket.ClientServer;
import socket.ServerMessages;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 4738726928182334907L;
	private JPanel contentPane;
	private JTextField textUsernamer;
	private JPasswordField passwordField;
	private int height = 300;
	private int width = 450;

	private ClientServer client;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	private User user = null;

	/**
	 * Create the frame.
	 */
	public Login() {

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				if (client != null)
					try {
						output.writeObject(ServerMessages.CLIENT_TERMINATE);
						output.flush();
						client.closeConnection();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				if (client != null)
					try {
						output.writeObject(ServerMessages.CLIENT_TERMINATE);
						output.flush();
						client.closeConnection();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
			}
		});
		setTitle("登陆");

		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds((int) (dimension.getWidth() - width) / 2, (int) (dimension.getHeight() - height) / 2, width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("账号");
		lblNewLabel_1.setBounds(89, 76, 30, 18);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("欢迎使用档案管理系统", JLabel.CENTER);
		lblNewLabel.setBounds(134, 13, 150, 18);
		panel.add(lblNewLabel);

		JLabel label = new JLabel("密码");
		label.setBounds(89, 125, 30, 18);
		panel.add(label);

		textUsernamer = new JTextField();
		textUsernamer.setToolTipText("请输入账号");
		textUsernamer.setBounds(147, 73, 154, 24);
		textUsernamer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					login();
				}

			}
		});

		panel.add(textUsernamer);
		textUsernamer.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					login();
				}

			}
		});
		passwordField.setToolTipText("请输入密码");
		passwordField.setBounds(147, 122, 154, 24);
		panel.add(passwordField);

		JButton button = new JButton("登陆");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		button.setBounds(73, 170, 113, 27);
		panel.add(button);

		JButton button_1 = new JButton("退出");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				dispose();
			}
		});
		button_1.setBounds(239, 170, 113, 27);
		panel.add(button_1);

		JLabel lblBy = new JLabel("by郑荣涛");
		lblBy.setBounds(296, 210, 72, 18);
		panel.add(lblBy);

	}

	private void connectServer() {
		client = new ClientServer("127.0.0.1");

		client.runClient();

		input = client.getInput();

		output = client.getOutput();

	}

	private void login() {

		connectServer();

		// 连接服务器

		try {
			output.writeObject(ServerMessages.CLIENT_WILL_LOGIN);
		} catch (IOException e3) {
			e3.printStackTrace();
		}

		String username = textUsernamer.getText();
		String pwd = new String(passwordField.getPassword());
		try {

			UserData userData = new UserData();
			userData.setName(username);
			userData.setPassword(pwd);
			// 验证账号密码
			output = client.getOutput();
			input = client.getInput();

			output.writeObject(userData);
			output.flush();

			String message = (String) input.readObject();
			if (message.equals(ServerMessages.SERVER_LOGIN_SUCCESS)) {
				User user = (User) input.readObject();

				MainFrame mainFrame = new MainFrame(user, client, Login.this);
				mainFrame.setVisible(true);
				setVisible(false);
			} else {
				JOptionPane.showMessageDialog(null, "您输入的账号或密码错误", "您输入的账号或密码错误", JOptionPane.PLAIN_MESSAGE);

			}

		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "您输入的账号或密码错误", "您输入的账号或密码错误", JOptionPane.PLAIN_MESSAGE);

			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}

	}

	public JTextField getTxtUsernamer() {
		return textUsernamer;
	}

	public void setTxtUsernamer(JTextField txtUsernamer) {
		this.textUsernamer = txtUsernamer;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
