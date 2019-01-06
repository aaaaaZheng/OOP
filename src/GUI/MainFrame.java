package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import common.Administrator;
import common.Browser;
import common.Notice;
import common.Operator;
import common.User;
import socket.ClientServer;
import socket.ServerMessages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JLabel;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -4753361557127386777L;
	private User user = null;
	private int height = 600;
	private int width = 1200;
	private ClientServer client = null;

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private JLabel notice;
	private JLabel timeNotice;
	private JLabel timeManNotice;

	/**
	 * Create the frame.
	 * 
	 * @param login
	 */
	public MainFrame(User user, ClientServer client, Login login) {
		this.client = client;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {

					output.writeObject(ServerMessages.CLIENT_TERMINATE);
					output.flush();
					client.closeConnection();
					login.dispose();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

			}
		});
		this.user = user;

		output = client.getOutput();

		try {
			output.writeObject(ServerMessages.CLIENT_LOGIN_SUCCESS);
			output.flush();
		} catch (IOException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
		}

		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds((int) (dimension.getWidth() - width) / 2, (int) (dimension.getHeight() - height) / 2, width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu userManage = new JMenu("用户管理");
		menuBar.add(userManage);

		JMenuItem addUser = new JMenuItem("新增用户");
		addUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!(user instanceof Administrator)) {
					return;
				}
				UserFrame userFrame = new UserFrame(user, client);
				userFrame.setState(0);
				userFrame.setVisible(true);
			}
		});
		userManage.add(addUser);

		JMenuItem changeUser = new JMenuItem("修改用户");
		changeUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!(user instanceof Administrator)) {
					return;
				}
				UserFrame userFrame = new UserFrame(user, client);
				userFrame.setState(1);
				userFrame.setVisible(true);
			}
		});
		userManage.add(changeUser);

		JMenuItem deleteUser = new JMenuItem("删除用户");
		deleteUser.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (!(user instanceof Administrator)) {
					return;
				}
				UserFrame userFrame = new UserFrame(user, client);
				userFrame.setState(2);
				userFrame.setVisible(true);
			}
		});
		userManage.add(deleteUser);

		JMenu fileManage = new JMenu("档案管理");
		menuBar.add(fileManage);

		JMenuItem fileUp = new JMenuItem("档案上传");
		fileUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (user instanceof Administrator || user instanceof Browser) {
					return;
				}
				FileFrame fileFrame = new FileFrame(user, client);
				fileFrame.setState(1);
				fileFrame.setVisible(true);
			}
		});
		fileManage.add(fileUp);

		JMenuItem fileDown = new JMenuItem("档案下载");
		fileDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				FileFrame fileFrame = new FileFrame(user, client);
				fileFrame.setState(0);
				fileFrame.setVisible(true);
			}
		});
		fileManage.add(fileDown);

		JMenu selfManage = new JMenu("个人信息管理");
		menuBar.add(selfManage);

		JMenuItem infoChange = new JMenuItem("信息修改");
		infoChange.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SelfFrame selfFrame = new SelfFrame(user, client);
				if (user != null)
					selfFrame.setUser(user);
				selfFrame.setVisible(true);
			}
		});

		selfManage.add(infoChange);

		JMenu noticeMenu = new JMenu("公告管理");
		menuBar.add(noticeMenu);

		JMenuItem addNotice = new JMenuItem("新增公告");
		addNotice.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				NoticeFrame noticeFrame = new NoticeFrame(user, client);
				noticeFrame.setVisible(true);

			}
		});
		noticeMenu.add(addNotice);

		JMenuItem lookOrDelNotice = new JMenuItem("查看或删除公告");
		lookOrDelNotice.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				NoticeFrame noticeFrame = new NoticeFrame(user, client);
				noticeFrame.setState(1);
				noticeFrame.setVisible(true);
			}
		});
		noticeMenu.add(lookOrDelNotice);

		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel() {

			private static final long serialVersionUID = -1427588039847579574L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon icon = new ImageIcon("mainPicture.png");
				g.drawImage(icon.getImage(), 0, 0, null);
			}

		};
		panel.setLayout(null);
		JLabel lblNewLabel = new JLabel("欢迎   " + user.getName() + "   使用档案管理系统");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 40));
		lblNewLabel.setBounds(100, 350, 1000, 80);
		panel.add(lblNewLabel);

		JLabel noticeLabel = new JLabel("公告");
		noticeLabel.setFont(new Font("宋体", Font.BOLD, 30));
		noticeLabel.setBounds(750, 35, 70, 50);
		panel.add(noticeLabel);

		JButton flush = new JButton("刷新");
		flush.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setNotice();
			}
		});
		flush.setBounds(1063, 35, 87, 30);
		panel.add(flush);

		JLabel updateTime = new JLabel("更新时间");
		updateTime.setFont(new Font("宋体", Font.BOLD, 15));
		updateTime.setBounds(950, 32, 120, 43);
		panel.add(updateTime);

		timeNotice = new JLabel();
		timeNotice.setFont(new Font("宋体", Font.BOLD, 15));
		timeNotice.setBounds(950, 57, 240, 43);
		panel.add(timeNotice);

		JLabel noticeManLabel = new JLabel("发布人");
		noticeManLabel.setFont(new Font("宋体", Font.BOLD, 15));
		noticeManLabel.setBounds(850, 32, 120, 43);
		panel.add(noticeManLabel);

		timeManNotice = new JLabel();
		timeManNotice.setFont(new Font("宋体", Font.BOLD, 15));
		timeManNotice.setBounds(850, 57, 240, 43);
		panel.add(timeManNotice);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(750, 90, 400, 300);
		panel.add(scrollPane);

		// noticeArea=new JTextArea();
		// scrollPane.setViewportView(noticeArea);
		notice = new JLabel();
		notice.setFont(new Font("宋体", Font.BOLD, 20));
		notice.setBounds(0, 0, 400, 300);
		scrollPane.setViewportView(notice);

		setNotice();

		// 根据用户role做出调整
		this.user = user;
		getContentPane().add(panel, BorderLayout.CENTER);
		if (user instanceof Administrator) {
			setTitle("管理员管理员界面");
			fileUp.setEnabled(false);
		} else if (user instanceof Operator) {
			setTitle("档案录入员界面");
			addUser.setEnabled(false);
			changeUser.setEnabled(false);
			deleteUser.setEnabled(false);
			noticeMenu.setEnabled(false);
		} else if (user instanceof Browser) {
			setTitle("档案浏览员界面");
			fileUp.setEnabled(false);
			addUser.setEnabled(false);
			changeUser.setEnabled(false);
			deleteUser.setEnabled(false);
			noticeMenu.setEnabled(false);
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	private void setNotice() {

		output = client.getOutput();
		input = client.getInput();

		Vector<Vector<String>> noticesVec = new Vector<>();
		Enumeration<Notice> notices;
		try {
			output.writeObject(ServerMessages.CLIENT_WANT_NOTICES_DATA);
			output.flush();

			String string = (String) input.readObject();
			if (string.equals(ServerMessages.SERVER_GIVE_NOTICES_DATA)) {

				notices = ((Hashtable<String, Notice>) input.readObject()).elements();
				int max = -1;
				while (notices.hasMoreElements()) {
					Notice notice = notices.nextElement();
					Vector<String> oneNotice = new Vector<>();
					if (max < Integer.parseInt(notice.getID()))
						max = Integer.parseInt(notice.getID());
					oneNotice.add(notice.getID());
					oneNotice.add(notice.getCreator());
					oneNotice.add(notice.getTimestamp().toString());
					oneNotice.add(notice.getDescription());
					noticesVec.add(oneNotice);
				}

				int cnt = -1;
				for (Vector<String> vector : noticesVec) {
					String IDS = vector.get(0);
					int ID = Integer.parseInt(IDS);
					cnt++;
					if (max == ID) {
						break;
					}

				}

				String desc = noticesVec.get(cnt).lastElement();
				String timeDesc = noticesVec.get(cnt).elementAt(2);
				String timeMan = noticesVec.get(cnt).elementAt(1);
				notice.setText(desc);
				timeNotice.setText(timeDesc);
				timeManNotice.setText(timeMan);

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
