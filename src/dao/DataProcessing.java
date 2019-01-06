package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;

import common.Administrator;
import common.Browser;
import common.Doc;
import common.Notice;
import common.Operator;
import common.User;

public class DataProcessing {

	// private static boolean connectToDB = false;

	private static String driverName = "com.mysql.cj.jdbc.Driver"; // 加载数据库驱动类
	private static String url = "jdbc:mysql://localhost:3306/oop?useSSL=false&serverTimezone=Asia/Shanghai"; // 声明数据库的URL'
	private static String user = "root"; // 数据库用户
	private static String password = "123456";
	private static boolean connectedToDatabase = false;

	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;

	private static Hashtable<String, User> users;
	private static Hashtable<String, Doc> docs;
	private static Hashtable<String, Notice> notices;

	public static void connectToDatabase(String aDriverName, String aUrl, String aUser, String aPassword) {

		try {
			Class.forName(aDriverName);
			connection = DriverManager.getConnection(aUrl, aUser, aPassword); // 建立数据库连接
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			connectedToDatabase = true;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static void disconnectFromDatabase() {
		if (connectedToDatabase) {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} finally {
				connectedToDatabase = false;
			}
		}

	}

	public static Doc searchDoc(String fileNum) throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from doc_info where id='" + fileNum + "'";
		resultSet = statement.executeQuery(sql);
		resultSet.next();
		String ID = resultSet.getString("id");
		String creator = resultSet.getString("creator");
		Timestamp timestamp = resultSet.getTimestamp("timestamp");
		String description = resultSet.getString("description");
		String filename = resultSet.getString("filename");

		Doc doc = new Doc(ID, creator, timestamp, description, filename);
		disconnectFromDatabase();
		return doc;

	}

	public static Notice searchNotice(String aID) throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from notice_info where id='" + aID + "'";
		resultSet = statement.executeQuery(sql);
		resultSet.next();
		String ID = resultSet.getString("id");
		String creator = resultSet.getString("creator");
		Timestamp timestamp = resultSet.getTimestamp("timestamp");
		String description = resultSet.getString("description");

		Notice notice = new Notice(ID, creator, timestamp, description);
		disconnectFromDatabase();
		return notice;

	}

	public static Enumeration<Doc> getAllDocs() throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from doc_info";
		resultSet = statement.executeQuery(sql);
		docs = new Hashtable<String, Doc>();
		while (resultSet.next()) {
			String ID = resultSet.getString("ID");
			String creator = resultSet.getString("creator");
			Timestamp timestamp = resultSet.getTimestamp("timestamp");
			String description = resultSet.getString("description");
			String filename = resultSet.getString("filename");

			Doc doc = new Doc(ID, creator, timestamp, description, filename);
			docs.put(ID, doc);
		}
		disconnectFromDatabase();
		return docs.elements();

	}

	public static Hashtable<String, Doc> getAllDocs_Hash() throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from doc_info";
		resultSet = statement.executeQuery(sql);
		docs = new Hashtable<String, Doc>();
		while (resultSet.next()) {
			String ID = resultSet.getString("ID");
			String creator = resultSet.getString("creator");
			Timestamp timestamp = resultSet.getTimestamp("timestamp");
			String description = resultSet.getString("description");
			String filename = resultSet.getString("filename");

			Doc doc = new Doc(ID, creator, timestamp, description, filename);
			docs.put(ID, doc);
		}
		disconnectFromDatabase();
		return docs;

	}

	public static Enumeration<Notice> getAllNotices() throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from notice_info";
		resultSet = statement.executeQuery(sql);
		notices = new Hashtable<String, Notice>();
		while (resultSet.next()) {
			String ID = resultSet.getString("ID");
			String creator = resultSet.getString("creator");
			Timestamp timestamp = resultSet.getTimestamp("timestamp");
			String description = resultSet.getString("description");

			Notice notice = new Notice(ID, creator, timestamp, description);

			notices.put(ID, notice);
		}
		disconnectFromDatabase();
		return notices.elements();

	}

	public static Hashtable<String, Notice> getAllNotices_Hash() throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from notice_info";
		resultSet = statement.executeQuery(sql);
		notices = new Hashtable<String, Notice>();
		while (resultSet.next()) {
			String ID = resultSet.getString("ID");
			String creator = resultSet.getString("creator");
			Timestamp timestamp = resultSet.getTimestamp("timestamp");
			String description = resultSet.getString("description");

			Notice notice = new Notice(ID, creator, timestamp, description);

			notices.put(ID, notice);
		}
		disconnectFromDatabase();
		return notices;

	}

	public static boolean insertNotice(String creator, Timestamp timestamp, String description) throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "insert into notice_info (creator,timestamp,description) values(?,?,?)";
		preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setString(1, creator);
		preparedStatement.setTimestamp(2, timestamp);
		;
		preparedStatement.setString(3, description);

		preparedStatement.executeUpdate();
		disconnectFromDatabase();
		return true;
	}

	public static boolean deleteNotice(String ID) throws SQLException {

		connectToDatabase(driverName, url, user, password);

		String sql = "delete from notice_info where Id='" + ID + "'";
		statement.executeUpdate(sql);

		disconnectFromDatabase();
		return true;

	}

	public static boolean insertDoc(String ID, String creator, Timestamp timestamp, String description, String filename)
			throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "insert into doc_info (id,creator,timestamp,description,filename) values(?,?,?,?,?)";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, ID);
		preparedStatement.setString(2, creator);
		preparedStatement.setTimestamp(3, timestamp);
		;
		preparedStatement.setString(4, description);
		preparedStatement.setString(5, filename);

		preparedStatement.executeUpdate();
		disconnectFromDatabase();
		return true;
	}

	public static User searchUser(String name) throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from user_info where username='" + name + "'";
		resultSet = statement.executeQuery(sql);
		resultSet.next();
		String username = resultSet.getString("username");
		String password = resultSet.getString("password");
		String role = resultSet.getString("role");
		User aUser = null;
		if (role.equals("operator")) {
			aUser = new Operator(username, password, role);
		} else if (role.equals("administrator")) {
			aUser = new Administrator(username, password, role);
		} else {
			aUser = new Browser(username, password, role);
		}

		disconnectFromDatabase();
		return aUser;

	}

	public static User searchUser(String name, String password) throws SQLException {

		connectToDatabase(driverName, url, DataProcessing.user, DataProcessing.password);
		String sql = "select * from user_info where username='" + name + "'" + "AND password='" + password + "'";
		resultSet = statement.executeQuery(sql);
		resultSet.next();
		String username = resultSet.getString("username");
		String pwd = resultSet.getString("password");
		String role = resultSet.getString("role");
		User aUser = null;
		if (role.equals("operator")) {
			aUser = new Operator(username, pwd, role);
		} else if (role.equals("administrator")) {
			aUser = new Administrator(username, pwd, role);
		} else {
			aUser = new Browser(username, pwd, role);
		}

		disconnectFromDatabase();
		return aUser;
	}

	public static Enumeration<User> getAllUser() throws SQLException {
		connectToDatabase(driverName, url, user, password);
		String sql = "select * from user_info";
		resultSet = statement.executeQuery(sql);
		users = new Hashtable<String, User>();
		while (resultSet.next()) {

			String name = resultSet.getString("username");
			String password = resultSet.getString("password");
			String role = resultSet.getString("role");
			User aUser;
			if (role.equals("operator")) {
				aUser = new Operator(name, password, role);
			} else if (role.equals("administrator")) {
				aUser = new Administrator(name, password, role);
			} else {
				aUser = new Browser(name, password, role);
			}
			users.put(name, aUser);

		}
		disconnectFromDatabase();
		return users.elements();
	}

	public static Hashtable<String, User> getAllUser_Hash() throws SQLException {

		connectToDatabase(driverName, url, user, password);
		String sql = "select * from user_info";
		resultSet = statement.executeQuery(sql);
		users = new Hashtable<String, User>();
		while (resultSet.next()) {

			String name = resultSet.getString("username");
			String password = resultSet.getString("password");
			String role = resultSet.getString("role");
			User aUser;
			if (role.equals("operator")) {
				aUser = new Operator(name, password, role);
			} else if (role.equals("administrator")) {
				aUser = new Administrator(name, password, role);
			} else {
				aUser = new Browser(name, password, role);
			}
			users.put(name, aUser);

		}
		disconnectFromDatabase();
		return users;
	}

	public static boolean updateUser(String name, String password, String role) throws SQLException {
		connectToDatabase(driverName, url, DataProcessing.user, DataProcessing.password);
		String sql = "update user_info set password = ?,role = ? where username = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, password);
		preparedStatement.setString(2, role);
		preparedStatement.setString(3, name);
		preparedStatement.executeUpdate();
		disconnectFromDatabase();
		return true;
	}

	public static boolean insertUser(String name, String password, String role) throws SQLException {

		connectToDatabase(driverName, url, DataProcessing.user, DataProcessing.password);
		String sql = "insert into user_info (username,password,role) values(?,?,?)";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, password);
		preparedStatement.setString(3, role);
		preparedStatement.executeUpdate();
		disconnectFromDatabase();
		return true;
	}

	public static boolean deleteUser(String name) throws SQLException {

		connectToDatabase(driverName, url, user, password);

		String sql = "delete from user_info where username='" + name + "'";
		statement.executeUpdate(sql);

		disconnectFromDatabase();
		return true;

	}

	public static void main(String[] args) {
		try {
			Notice notice = searchNotice("1");
			System.out.println(notice);

		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
