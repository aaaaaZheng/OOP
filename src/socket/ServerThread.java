package socket;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Hashtable;

import common.Administrator;
import common.Doc;
import common.Notice;
import common.Operator;
import common.User;
import common.UserData;
import dao.DataProcessing;

public class ServerThread extends Thread {
	// 用于传输Doc和User
	private ObjectOutputStream output; // output stream to client
	private ObjectInputStream input; // input stream from client

	private Socket connection; // connection to client
	private static int counter = 0; // counter of number of connections

	private String fileHouse = "F:\\\\OOP\\\\fileHouse";

	public ServerThread(Socket connection) {
		this.connection = connection;
		counter++;

		System.out.println("当前连接数为：" + counter);

		System.out.println("连接来自" + connection.getInetAddress().getHostAddress());

		try {
			getStreams();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		try {
			processConnection();
		} finally {
			closeConnection();
			counter--;
		}

	}

	private void getStreams() throws IOException {

		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();

		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("获得输入流。。。");
	}

	private void processConnection() {
		User user = null;

		String message;
		UserData userData;
		String message1;

		try {

			do {

				message = (String) input.readObject();
				System.out.println(message);
				// 登陆
				if (message.equals(ServerMessages.CLIENT_WILL_LOGIN)) {

					// 获得登录信息
					userData = (UserData) input.readObject();
					// 处理
					user = DataProcessing.searchUser(userData.getName(), userData.getPassword());

					System.out.println(userData.getName());

					message1 = ServerMessages.SERVER_LOGIN_SUCCESS;
					sendData(message1);

					output.writeObject(user);
					output.flush();

					// 登陆成功
				}
				// 修改自己信息
				if (message.equals(ServerMessages.CLIENT_WILL_CHANGE_SELF_PASSWORD)) {

					User auser = (User) input.readObject();
					user.changeSelfInfo(auser.getPassword());
					message1 = ServerMessages.SERVER_CHANGE_SELF_PASSWORD_SUCCESS;
					sendData(message1);
				}
				// 添加用户
				if (message.equals(ServerMessages.CLIENT_WILL_ADD_USER)) {

					String[] strings = (String[]) input.readObject();

					if (((Administrator) user).addUser(strings[0], strings[1], strings[2])) {
						message1 = ServerMessages.SERVER_ADD_USER_SUCCESS;
						sendData(message1);
					} else {
						message1 = ServerMessages.SERVER_ADD_USER_FALSE;
						sendData(message1);
					}
				}
				// 修改用户
				if (message.equals(ServerMessages.CLIENT_WILL_CHANGE_USER)) {

					String[] strings = (String[]) input.readObject();

					if (((Administrator) user).changeUserInfo(strings[0], strings[1], strings[2])) {
						message1 = ServerMessages.SERVER_CHANGE_USER_SUCCESS;
						sendData(message1);
					} else {
						message1 = ServerMessages.SERVER_CHANGE_USER_FALSE;
						sendData(message1);
					}
				}
				// 删除用户
				if (message.equals(ServerMessages.CLIENT_WILL_DELETE_USER)) {

					String name = (String) input.readObject();

					if (((Administrator) user).delUser(name)) {
						message1 = ServerMessages.SERVER_DELETE_USER_SUCCESS;
						sendData(message1);
					} else {
						message1 = ServerMessages.SERVER_DELETE_USER_FALSE;
						sendData(message1);
					}
				}
				// 下载文件
				if (message.equals(ServerMessages.CLIENT_WILL_DOWNLOAD_FILE)) {
					String ID = (String) input.readObject();

					Doc doc = DataProcessing.searchDoc(ID);
					if (doc != null) {
						String path = fileHouse + "\\" + doc.getFilename();
						File file = new File(path);
						BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
						byte[] bytes = new byte[(int) file.length()];

						bufferedInputStream.read(bytes);

						System.out.println("----开始传出文件<" + doc.getFilename() + ">,文件大小为<" + file.length() + ">----");

						message1 = ServerMessages.SERVER_WILL_SEND_FILE;
						sendData(message1);

						output.writeObject((int) file.length());
						output.flush();

						OutputStream outputStream = connection.getOutputStream();

						for (int i = 0; i < file.length(); i++) {
							outputStream.write(bytes[i]);
						}

						output.flush();

						System.out.println("传出文件进度100%");
						message1 = ServerMessages.SERVER_DOWNLOAD_FILE_SUCCESS;
						sendData(message1);

						bufferedInputStream.close();

					} else {
						message1 = ServerMessages.CLIENT_WILL_DOWNLOAD_FILE;
						sendData(message1);
					}

				}

				// 上传文件
				if (message.equals(ServerMessages.CLIENT_WILL_UPLOAD_FILE)) {

					String[] upFile = (String[]) input.readObject();

					int fileLength = (int) input.readObject();

					InputStream inputStream = connection.getInputStream();
					byte[] bytes = new byte[fileLength];

					System.out.println("----开始接受文件<" + upFile[0] + ">,文件大小为<" + fileLength + ">----");

					inputStream.read(bytes);

					if (((Operator) user).uploadFile(upFile[0], upFile[1], upFile[2], bytes, fileHouse)) {
						System.out.println("接受文件进度100%");
						message1 = ServerMessages.SERVER_UPLOAD_FILE_SUCCESS;
						sendData(message1);
					} else {
						message1 = ServerMessages.SERVER_UPLOAD_FILE_FALSE;
						sendData(message1);
					}
				}

				// 登陆成功
				if (message.equals(ServerMessages.CLIENT_LOGIN_SUCCESS)) {
					continue;
				}
				// 获取用户信息
				if (message.equals(ServerMessages.CLIENT_WANT_USERS_DATA)) {
					message1 = ServerMessages.SERVER_GIVE_USERS_DATA;
					sendData(message1);

					Hashtable<String, User> users = DataProcessing.getAllUser_Hash();

					output.writeObject(users);
					output.flush();
				}
				// 获取档案信息
				if (message.equals(ServerMessages.CLIENT_WANT_DOCS_DATA)) {
					message1 = ServerMessages.SERVER_GIVE_DOCS_DATA;
					sendData(message1);

					Hashtable<String, Doc> docs = DataProcessing.getAllDocs_Hash();

					output.writeObject(docs);
					output.flush();
				}
				// 获取公告信息
				if (message.equals(ServerMessages.CLIENT_WANT_NOTICES_DATA)) {
					message1 = ServerMessages.SERVER_GIVE_NOTICES_DATA;
					sendData(message1);

					Hashtable<String, Notice> notices = DataProcessing.getAllNotices_Hash();

					output.writeObject(notices);
					output.flush();

				}
				// 添加公告
				if (message.equals(ServerMessages.CLIENT_WILL_ADD_NOTICE)) {

					String desc = (String) input.readObject();

					((Administrator) user).addNotice(desc);
					message1 = ServerMessages.SERVER_ADD_NOTICE_SUCCESS;
					sendData(message1);

				}
				// 删除公告
				if (message.equals(ServerMessages.CLIENT_WILL_DELETE_NOTICE)) {

					String ID = (String) input.readObject();

					if (((Administrator) user).delNotice(ID)) {
						message1 = ServerMessages.SERVER_DELETE_NOTICE_SUCCESS;
						sendData(message1);
					} else {
						message1 = ServerMessages.SERVER_DELETE_NOTICE_FALSE;
						sendData(message1);
					}
				}

			} while (!message.equals(ServerMessages.CLIENT_TERMINATE));

		} catch (ClassNotFoundException e1) {

			// e.printStackTrace();
			System.out.println("接收到了未知信息");
		} catch (SQLException e) {
			message = ServerMessages.SERVER_SQL_FAILE;
			sendData(message);

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	private void closeConnection() {
		System.out.println("关闭连接");
		try {

			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	private void sendData(String message) {
		try {
			output.writeObject(message);
			output.flush();
			System.out.println(message);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
