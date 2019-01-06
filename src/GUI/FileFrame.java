package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import socket.ClientServer;
import socket.ServerMessages;

import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import common.Doc;
import common.Operator;
import common.User;

import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FileFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6639154371962147206L;
	private JPanel contentPane;
	private JTable table;
	private JTextField filenum;
	private JTextField filedesc;
	private JTextField filename;
	private JTabbedPane tabbedPane;
	private int height = 563;
	private int width = 884;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public void setState(int i) {
		tabbedPane.setSelectedIndex(i);

	}

	/**
	 * Create the frame.
	 */
	public FileFrame(User user, ClientServer client) {

		output = client.getOutput();
		input = client.getInput();

		setTitle("文件管理界面");
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds((int) (dimension.getWidth() - width) / 2, (int) (dimension.getHeight() - height) / 2, width, height);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// setBounds(100, 100, 884, 536);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JLayeredPane downLayeredPane = new JLayeredPane();
		tabbedPane.addTab("下载文件", null, downLayeredPane, null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 20, 820, 351);
		downLayeredPane.add(scrollPane);

		table = new JTable();
		addFileToTable();

		scrollPane.setViewportView(table);

		JButton button = new JButton("下载");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "请选择要下载的文件", "提示", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				String ID = (String) table.getValueAt(table.getSelectedRow(), 0);
				if (ID == null)
					JOptionPane.showMessageDialog(null, "请选择要下载的文件", "提示", JOptionPane.PLAIN_MESSAGE);
				try {
					if (ID != null) {
						if (JOptionPane.showConfirmDialog(null, "您确认要下载吗", "提示", JOptionPane.YES_NO_OPTION,
								JOptionPane.NO_OPTION) == 0) {

							FileDialog fDialog = new FileDialog(FileFrame.this, "请选择要下载文件的地址", FileDialog.SAVE);
							fDialog.setFile((String) table.getValueAt(table.getSelectedRow(), 3));
							fDialog.setVisible(true);
							if (fDialog.getFile() != null) {
								filename.setText(fDialog.getDirectory());

								String downPath = filename.getText();

								output.writeObject(ServerMessages.CLIENT_WILL_DOWNLOAD_FILE);
								output.flush();

								output.writeObject(ID);
								output.flush();

								// 接收
								String message = (String) input.readObject();
								if (message.equals(ServerMessages.SERVER_WILL_SEND_FILE)) {
									int length = (int) input.readObject();
									byte[] bytes = new byte[length];

									InputStream outputStream = client.getInputStream();
									for (int i = 0; i < length; i++) {
										bytes[i] = (byte) outputStream.read();
									}

									user.downloadFile(bytes, bytes.length, fDialog.getFile(), downPath);
									addFileToTable();
									JOptionPane.showMessageDialog(null, "下载成功", "提示", JOptionPane.PLAIN_MESSAGE);

								} else {
									JOptionPane.showMessageDialog(null, "下载失败", "提示", JOptionPane.PLAIN_MESSAGE);

								}

							}

						}
					}

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "系统找不到指定路径", "提示", JOptionPane.PLAIN_MESSAGE);

					e1.printStackTrace();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "连接数据库失败", "提示", JOptionPane.PLAIN_MESSAGE);

					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

			}
		});
		button.setBounds(145, 384, 113, 27);
		downLayeredPane.add(button);

		JButton button_1 = new JButton("返回");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		button_1.setBounds(584, 384, 113, 27);
		downLayeredPane.add(button_1);

		JLayeredPane upLayeredPane = new JLayeredPane();
		tabbedPane.addTab("上传文件", null, upLayeredPane, null);

		JLabel label = new JLabel("档案号");
		label.setBounds(161, 104, 72, 18);
		upLayeredPane.add(label);

		JLabel label_1 = new JLabel("档案描述");
		label_1.setBounds(161, 149, 72, 18);
		upLayeredPane.add(label_1);

		JLabel label_2 = new JLabel("档案名");
		label_2.setBounds(161, 275, 83, 18);
		upLayeredPane.add(label_2);

		filenum = new JTextField();
		filenum.setBounds(281, 101, 216, 24);
		upLayeredPane.add(filenum);
		filenum.setColumns(10);

		filedesc = new JTextField();
		filedesc.setBounds(281, 149, 216, 97);
		upLayeredPane.add(filedesc);
		filedesc.setColumns(10);

		filename = new JTextField();
		filename.setBounds(281, 272, 216, 24);
		upLayeredPane.add(filename);
		filename.setColumns(10);

		JButton btnNewButton = new JButton("上传");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (JOptionPane.showConfirmDialog(null, "您确认要上传吗", "提示", JOptionPane.YES_NO_OPTION,
							JOptionPane.NO_OPTION) == 0) {
						String absolutePath = filename.getText();
						String upFileNum = filenum.getText();
						String upFileDesc = filedesc.getText();
						if (upFileNum.isEmpty() || absolutePath.isEmpty() || upFileDesc.isEmpty()) {
							JOptionPane.showMessageDialog(null, "请将信息填写完整", "提示", JOptionPane.PLAIN_MESSAGE);
							return;
						}
						for (int i = 0; i < upFileNum.length(); i++) {
							char c = upFileNum.charAt(i);
							if (!(c >= '0' && c <= '9')) {
								JOptionPane.showMessageDialog(null, "档案号必须是数字", "提示", JOptionPane.PLAIN_MESSAGE);
								return;
							}
						}
						String upFileName = absolutePath.substring(absolutePath.lastIndexOf("\\") + 1);

						output.writeObject(ServerMessages.CLIENT_WILL_UPLOAD_FILE);
						output.flush();

						String[] strings = { upFileName, upFileNum.trim(), upFileDesc };
						output.writeObject(strings);
						output.flush();

						// 要上传的文件
						File file = new File(absolutePath);

						// 文件大小
						output.writeObject((int) file.length());
						output.flush();

						byte[] bytes = new byte[(int) file.length()];
						BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
						bufferedInputStream.read(bytes);

						OutputStream outputStream = client.getOutputStream();
						outputStream.write(bytes);
						outputStream.flush();
						bufferedInputStream.close();

						String string = (String) input.readObject();
						if (string.equals(ServerMessages.SERVER_UPLOAD_FILE_SUCCESS)) {
							JOptionPane.showMessageDialog(null, "上传成功", "提示", JOptionPane.PLAIN_MESSAGE);
							addFileToTable();
						} else
							JOptionPane.showMessageDialog(null, "上传文件失败", "提示", JOptionPane.PLAIN_MESSAGE);

					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "上传文件失败", "提示", JOptionPane.PLAIN_MESSAGE);
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} finally {

				}

			}
		});
		btnNewButton.setBounds(161, 361, 113, 27);
		upLayeredPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("取消");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(487, 361, 113, 27);
		upLayeredPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("打开");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				FileDialog fDialog = new FileDialog(FileFrame.this, "请选择要上传的文件", FileDialog.LOAD);
				fDialog.setVisible(true);
				if (fDialog.getFile() != null) {
					filename.setText(fDialog.getDirectory() + fDialog.getFile());
				}

			}
		});
		btnNewButton_2.setBounds(525, 271, 113, 27);
		upLayeredPane.add(btnNewButton_2);

		if (!(user instanceof Operator)) {
			tabbedPane.setEnabledAt(1, false);
		}

	}

	@SuppressWarnings("unchecked")
	private void addFileToTable() {
		Vector<Vector<String>> docVec = new Vector<Vector<String>>();
		Vector<String> titleVec = new Vector<>();
		Doc doc = null;
		Enumeration<Doc> docs;
		titleVec.add("档案号");
		titleVec.add("创建者");
		titleVec.add("时间");
		titleVec.add("文件名");
		titleVec.add("描述");

		// 读取数据
		try {
			output.writeObject(ServerMessages.CLIENT_WANT_DOCS_DATA);
			output.flush();

			String string = (String) input.readObject();
			if (string.equals(ServerMessages.SERVER_GIVE_DOCS_DATA)) {

				docs = ((Hashtable<String, Doc>) input.readObject()).elements();
				while (docs.hasMoreElements()) {
					doc = docs.nextElement();
					Vector<String> temp = new Vector<>();
					temp.add(doc.getID());
					temp.add(doc.getCreator());
					temp.add(doc.getTimestamp().toString());
					temp.add(doc.getFilename());
					temp.add(doc.getDescription());
					docVec.add(temp);

					table.setModel(new DefaultTableModel(docVec, titleVec));
				}
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
