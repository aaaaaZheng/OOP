package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import socket.ClientServer;
import socket.ServerMessages;

import javax.swing.JTabbedPane;

import javax.swing.JOptionPane;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import common.Administrator;
import common.User;

import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserFrame extends JFrame {

	private static final long serialVersionUID = -9036820607979708857L;
	private Administrator administrator;
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTable table;
	private JPasswordField passwordField_1;
	private JComboBox<String> usernameComboBox;
	private JComboBox<String> rolesComboBox;
	private JTabbedPane tabbedPane;
	private int height = 300;
	private int width = 450;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public void setState(int i) {
		tabbedPane.setSelectedIndex(i);

	}

	/**
	 * Create the frame.
	 * 
	 * @param usernameComboBox
	 * @param rolesComboBox
	 */
	public UserFrame(User user, ClientServer client) {

		output = client.getOutput();
		input = client.getInput();

		administrator = (Administrator) user;

		setTitle("用户管理界面");
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds((int) (dimension.getWidth() - width) / 2, (int) (dimension.getHeight() - height) / 2, width, height);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel addPanel = new JPanel();
		tabbedPane.addTab("增加用户", null, addPanel, null);
		addPanel.setLayout(null);

		JLabel label = new JLabel("用户名");
		label.setBounds(79, 16, 72, 18);
		addPanel.add(label);

		JLabel label_1 = new JLabel("密码");
		label_1.setBounds(79, 57, 72, 18);
		addPanel.add(label_1);

		JLabel label_2 = new JLabel("角色");
		label_2.setBounds(79, 96, 72, 18);
		addPanel.add(label_2);

		usernameField = new JTextField();
		usernameField.setBounds(165, 13, 140, 24);
		addPanel.add(usernameField);
		usernameField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(165, 54, 140, 24);
		addPanel.add(passwordField);

		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.addItem("administrator");
		comboBox.addItem("browser");
		comboBox.addItem("operator");
		comboBox.setBounds(165, 93, 140, 24);
		addPanel.add(comboBox);

		JButton addButton = new JButton("增加");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String addName = usernameField.getText();
				String addPwd = new String(passwordField.getPassword());
				String addRole = (String) comboBox.getSelectedItem();
				if (addPwd.length() < 1) {
					showMessage("密码不能为空");
					return;
				}
				try {
					if (JOptionPane.showConfirmDialog(null, "您确认要添加吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {

						output.writeObject(ServerMessages.CLIENT_WILL_ADD_USER);
						output.flush();

						String[] userdata = { addName, addPwd, addRole };

						output.writeObject(userdata);
						output.flush();
						String message = (String) input.readObject();
						if (message.equals(ServerMessages.SERVER_ADD_USER_SUCCESS)) {
							addUserToTable();
							addMessageToPannel2();
							showMessage("添加成功");
						} else {
							showMessage("添加失败");
						}

					}
				} catch (Exception e1) {
					showMessage("添加失败");
					e1.printStackTrace();
				}
			}
		});
		addButton.setBounds(78, 148, 113, 27);
		addPanel.add(addButton);

		JButton button = new JButton("取消");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button.setBounds(217, 148, 113, 27);
		addPanel.add(button);

		JPanel changePanel = new JPanel();
		tabbedPane.addTab("修改用户", null, changePanel, null);
		changePanel.setLayout(null);

		JLabel label_3 = new JLabel("用户名");
		label_3.setBounds(79, 16, 72, 18);
		changePanel.add(label_3);

		JLabel label_4 = new JLabel("密码");
		label_4.setBounds(79, 57, 72, 18);
		changePanel.add(label_4);

		JLabel label_5 = new JLabel("角色");
		label_5.setBounds(79, 96, 72, 18);
		changePanel.add(label_5);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(165, 54, 140, 24);
		changePanel.add(passwordField_1);

		usernameComboBox = new JComboBox<String>();
		usernameComboBox.setBounds(165, 13, 133, 24);
		changePanel.add(usernameComboBox);

		rolesComboBox = new JComboBox<String>();
		rolesComboBox.setBounds(165, 93, 140, 24);
		changePanel.add(rolesComboBox);

		addMessageToPannel2();

		JButton button_3 = new JButton("修改");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String changedUserName = (String) usernameComboBox.getSelectedItem();
				String changedpwd = new String(passwordField_1.getPassword());
				String changedRole = (String) rolesComboBox.getSelectedItem();
				if (changedpwd.length() < 1) {
					showMessage("密码不能为空");
					return;
				}
				try {
					if (JOptionPane.showConfirmDialog(null, "您确认要修改吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {

						output.writeObject(ServerMessages.CLIENT_WILL_CHANGE_USER);
						output.flush();

						String[] userdata = { changedUserName, changedpwd, changedRole };

						output.writeObject(userdata);
						output.flush();
						String message = (String) input.readObject();
						if (message.equals(ServerMessages.SERVER_CHANGE_USER_SUCCESS)) {
							addUserToTable();
							addMessageToPannel2();
							showMessage("修改成功");
						} else {
							showMessage("修改失败");
						}

						// administrator.changeUserInfo(changedUserName, changedpwd, changedRole);

					}
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

			}
		});
		button_3.setBounds(78, 148, 113, 27);
		changePanel.add(button_3);

		JButton button_4 = new JButton("取消");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_4.setBounds(217, 148, 113, 27);
		changePanel.add(button_4);

		JPanel delPanel = new JPanel();
		delPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addUserToTable();
			}
		});
		tabbedPane.addTab("删除用户", null, delPanel, null);
		delPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 389, 151);
		delPanel.add(scrollPane);

		table = new JTable();
		addUserToTable();
		scrollPane.setViewportView(table);

		JButton button_1 = new JButton("删除");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentRow = table.getSelectedRow();
				try {
					String Name = (String) table.getValueAt(currentRow, 0);
					if (JOptionPane.showConfirmDialog(null, "您确认要删除吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {

						output.writeObject(ServerMessages.CLIENT_WILL_DELETE_USER);
						output.flush();

						output.writeObject(Name);
						output.flush();
						String message = (String) input.readObject();
						if (message.equals(ServerMessages.SERVER_DELETE_USER_SUCCESS)) {
							addUserToTable();
							addMessageToPannel2();
							showMessage("删除成功");
						} else {
							showMessage("删除失败");
						}

						administrator.delUser(Name);

					}
				} catch (Exception e2) {
					showMessage("请选择要删除的用户");
					e2.printStackTrace();
				}
			}
		});
		button_1.setBounds(66, 177, 113, 27);
		delPanel.add(button_1);

		JButton button_2 = new JButton("取消");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_2.setBounds(223, 177, 113, 27);
		delPanel.add(button_2);
	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "提示", JOptionPane.DEFAULT_OPTION);

	}

	@SuppressWarnings("unchecked")
	private void addUserToTable() {
		Vector<Vector<String>> usersVec = new Vector<>();
		Vector<String> titleVec = new Vector<>();
		Enumeration<User> users;
		try {
			output.writeObject(ServerMessages.CLIENT_WANT_USERS_DATA);
			output.flush();

			String string = (String) input.readObject();
			if (string.equals(ServerMessages.SERVER_GIVE_USERS_DATA)) {

				users = ((Hashtable<String, User>) input.readObject()).elements();
				while (users.hasMoreElements()) {
					User user = users.nextElement();
					Vector<String> oneUser = new Vector<>();
					oneUser.add(user.getName());
					oneUser.add(user.getPassword());
					oneUser.add(user.getRole());
					usersVec.add(oneUser);
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		titleVec.add("用户名");
		titleVec.add("密码");
		titleVec.add("角色");
		table.setModel(new DefaultTableModel(usersVec, titleVec));
	}

	@SuppressWarnings("unchecked")
	private void addMessageToPannel2() {
		Enumeration<User> users;
		usernameComboBox.removeAllItems();
		rolesComboBox.removeAllItems();
		try {

			output.writeObject(ServerMessages.CLIENT_WANT_USERS_DATA);
			output.flush();

			String string = (String) input.readObject();
			if (string.equals(ServerMessages.SERVER_GIVE_USERS_DATA)) {

				users = ((Hashtable<String, User>) input.readObject()).elements();
				while (users.hasMoreElements()) {
					User tuser = users.nextElement();

					usernameComboBox.addItem(tuser.getName());
				}
				rolesComboBox.addItem("administrator");
				rolesComboBox.addItem("browser");
				rolesComboBox.addItem("operator");
			}

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
