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
import javax.swing.JButton;
import java.awt.event.ActionListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import common.Notice;
import common.User;

import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JTextArea;

public class NoticeFrame extends JFrame {

	private static final long serialVersionUID = -9036820607979708857L;

	private JPanel contentPane;
	private JTable table;
	private JTabbedPane tabbedPane;
	private int height = 300;
	private int width = 450;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	Vector<Vector<String>> noticesVec;

	public void setState(int i) {
		tabbedPane.setSelectedIndex(i);

	}

	/**
	 * Create the frame.
	 * 
	 * @param usernameComboBox
	 * @param rolesComboBox
	 */
	public NoticeFrame(User user, ClientServer client) {

		output = client.getOutput();
		input = client.getInput();

		setTitle("公告管理界面");
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
		tabbedPane.addTab("新增公告", null, addPanel, null);
		addPanel.setLayout(null);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(15, 15, 400, 150);
		addPanel.add(scrollPane1);

		// noticeArea=new JTextArea();
		// scrollPane.setViewportView(noticeArea);

		JTextArea notice = new JTextArea();
		notice.setBounds(14, 41, 370, 103);
		// addPanel.add(notice);
		notice.setText("<html>xxxx<br/>yyy</html>");

		scrollPane1.setViewportView(notice);

		JButton addButton = new JButton("增加");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String desc = notice.getText();
				if (desc.length() < 1) {
					showMessage("公告不能为空");
					return;
				}
				try {
					if (JOptionPane.showConfirmDialog(null, "您确认要添加吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {

						output.writeObject(ServerMessages.CLIENT_WILL_ADD_NOTICE);
						output.flush();

						output.writeObject(desc);
						output.flush();

						String message = (String) input.readObject();
						if (message.equals(ServerMessages.SERVER_ADD_NOTICE_SUCCESS)) {
							addNoticeToTable();

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
		addButton.setBounds(76, 171, 113, 27);
		addPanel.add(addButton);

		JButton button = new JButton("取消");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button.setBounds(218, 171, 113, 27);
		addPanel.add(button);

		JPanel noticePanel = new JPanel();
		noticePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addNoticeToTable();
			}
		});
		tabbedPane.addTab("公告", null, noticePanel, null);
		noticePanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 389, 151);
		noticePanel.add(scrollPane);

		table = new JTable();
		addNoticeToTable();
		scrollPane.setViewportView(table);

		JButton button_1 = new JButton("查看");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentRow = table.getSelectedRow();
				try {
					String ID = (String) table.getValueAt(currentRow, 0);
					if (JOptionPane.showConfirmDialog(null, "您确认要查看吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {

						for (Vector<String> vector : noticesVec) {
							if (vector.firstElement() == ID) {
								JOptionPane.showMessageDialog(null, vector.lastElement(), "公告",
										JOptionPane.INFORMATION_MESSAGE);
							}

						}

					}
				} catch (Exception e2) {
					showMessage("请选择要查看的公告");
					e2.printStackTrace();
				}
			}
		});
		button_1.setBounds(66, 177, 113, 27);
		noticePanel.add(button_1);

		JButton button_2 = new JButton("删除");
		button_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int currentRow = table.getSelectedRow();
				try {
					String ID = (String) table.getValueAt(currentRow, 0);
					if (JOptionPane.showConfirmDialog(null, "您确认要删除吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {

						output.writeObject(ServerMessages.CLIENT_WILL_DELETE_NOTICE);
						output.flush();

						output.writeObject(ID);
						output.flush();
						String message = (String) input.readObject();

						if (message.equals(ServerMessages.SERVER_DELETE_NOTICE_SUCCESS)) {
							addNoticeToTable();

							showMessage("删除成功");
						} else {
							showMessage("删除失败");
						}

					}
				} catch (Exception e2) {
					showMessage("请选择要删除的公告");
					e2.printStackTrace();
				}
			}

		});
		button_2.setBounds(223, 177, 113, 27);
		noticePanel.add(button_2);

	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "提示", JOptionPane.DEFAULT_OPTION);

	}

	@SuppressWarnings("unchecked")
	private void addNoticeToTable() {
		noticesVec = new Vector<>();
		Vector<String> titleVec = new Vector<>();
		Enumeration<Notice> notices;
		try {
			output.writeObject(ServerMessages.CLIENT_WANT_NOTICES_DATA);
			output.flush();

			String string = (String) input.readObject();
			if (string.equals(ServerMessages.SERVER_GIVE_NOTICES_DATA)) {

				notices = ((Hashtable<String, Notice>) input.readObject()).elements();
				while (notices.hasMoreElements()) {
					Notice notice = notices.nextElement();
					Vector<String> oneNotice = new Vector<>();
					oneNotice.add(notice.getID());
					oneNotice.add(notice.getCreator());
					oneNotice.add(notice.getTimestamp().toString());
					oneNotice.add(notice.getDescription());
					noticesVec.add(oneNotice);
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		titleVec.add("ID");
		titleVec.add("创建者");
		titleVec.add("时间");
		titleVec.add("内容");
		table.setModel(new DefaultTableModel(noticesVec, titleVec));
	}

}
