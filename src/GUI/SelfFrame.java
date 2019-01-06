package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.User;
import socket.ClientServer;
import socket.ServerMessages;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelfFrame extends JFrame {

	private static final long serialVersionUID = -8908033228226580826L;
	private User user = null;
	private JPanel contentPane;
	private JTextField username;
	private JTextField role;
	private JPasswordField pwd2;
	private JPasswordField pwd3;
	private JPasswordField pwd1;
	private int height = 300;
	private int width = 450;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	/**
	 * Create the frame.
	 */
	public SelfFrame(User user, ClientServer client) {

		output = client.getOutput();
		input = client.getInput();

		this.user = user;
		setTitle("个人信息管理");
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds((int) (dimension.getWidth() - width) / 2, (int) (dimension.getHeight() - height) / 2, width, height);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setToolTipText("个人信息管理");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("用户名");
		label.setBounds(85, 34, 45, 18);
		contentPane.add(label);

		JLabel label_1 = new JLabel("原密码");
		label_1.setBounds(85, 65, 72, 18);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("新密码");
		label_2.setBounds(85, 100, 72, 18);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("确认密码");
		label_3.setBounds(85, 131, 72, 18);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("身份");
		label_4.setBounds(85, 162, 72, 18);
		contentPane.add(label_4);

		username = new JTextField();
		username.setEditable(false);
		username.setBounds(162, 31, 149, 24);
		if (user != null)

			username.setText(user.getName());
		contentPane.add(username);
		username.setColumns(10);

		role = new JTextField();
		role.setEditable(false);
		role.setBounds(162, 159, 149, 24);
		if (user != null)
			role.setText(user.getRole());
		contentPane.add(role);
		role.setColumns(10);

		JButton button = new JButton("修改");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String newpwd1 = new String(pwd2.getPassword());
				String newpwd2 = new String(pwd3.getPassword());
				String oldpwd = new String(pwd1.getPassword());
				if (JOptionPane.showConfirmDialog(null, "您确认要修改吗", "提示", JOptionPane.YES_NO_OPTION,
						JOptionPane.NO_OPTION) == 0) {
					if (!oldpwd.equals(user.getPassword())) {
						JOptionPane.showMessageDialog(null, "您输入的原密码错误", "提示", JOptionPane.PLAIN_MESSAGE);
						return;
					}
					if (!newpwd1.equals(newpwd2)) {
						JOptionPane.showMessageDialog(null, "您输入的两个新密码不一样", "提示", JOptionPane.PLAIN_MESSAGE);
						return;
					}

					user.setPassword(newpwd1);
					try {

						output.writeObject(ServerMessages.CLIENT_WILL_CHANGE_SELF_PASSWORD);
						output.flush();

						output.writeObject(user);
						output.flush();
						String message = (String) input.readObject();

						if (message.equals(ServerMessages.SERVER_CHANGE_SELF_PASSWORD_SUCCESS))
							JOptionPane.showMessageDialog(null, "修改密码成功", "提示", JOptionPane.PLAIN_MESSAGE);
						else
							JOptionPane.showMessageDialog(null, "修改密码失败", "提示", JOptionPane.PLAIN_MESSAGE);

					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "连接服务器失败", "提示", JOptionPane.PLAIN_MESSAGE);
						e.printStackTrace();
					} catch (ClassNotFoundException e) {

						e.printStackTrace();
					}
				}
			}
		});
		button.setBounds(51, 210, 113, 27);
		contentPane.add(button);

		JButton button_1 = new JButton("返回");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		button_1.setBounds(239, 210, 113, 27);
		contentPane.add(button_1);

		pwd1 = new JPasswordField();
		pwd1.setBounds(162, 65, 149, 24);
		contentPane.add(pwd1);

		pwd2 = new JPasswordField();
		pwd2.setBounds(162, 100, 149, 24);
		contentPane.add(pwd2);

		pwd3 = new JPasswordField();
		pwd3.setBounds(162, 128, 149, 24);
		contentPane.add(pwd3);

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
