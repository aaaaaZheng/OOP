package common;

import java.sql.SQLException;
import java.util.Properties;

import dao.DataProcessing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public abstract class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7003985540932825851L;
	private String name;
	private String password;
	private String role;

	User(String name, String password, String role) {
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public boolean changeSelfInfo(String password) throws SQLException {
		// 写用户信息到存储
		if (DataProcessing.updateUser(name, password, role)) {
			this.password = password;
			return true;
		} else
			return false;
	}

	public boolean downloadFile(String ID) throws IOException, SQLException {
		// double ranValue = Math.random();
		// if (ranValue > 0.5)
		// throw new IOException("Error in accessing file");

		Properties p = new Properties();
		String pPath = Operator.class.getClassLoader().getResource("path.properties").getPath();
		FileInputStream in = new FileInputStream(pPath);
		p.load(in);
		String fileHouse = p.getProperty("fileHouse");
		String downloadPath = p.getProperty("downloadPath");

		// String downloadPath="F:\\OOP\\downloadFiles";
		// String fileHouse = "F:\\OOP\\fileHouse";

		Doc doc = DataProcessing.searchDoc(ID);
		if (doc != null) {
			String path = fileHouse + "\\" + doc.getFilename();
			String downPath = downloadPath + "\\" + doc.getFilename();
			File file = new File(path);
			File downloadFile = new File(downPath);

			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));

			byte[] bytes = new byte[(int) file.length()];
			bufferedInputStream.read(bytes);
			bufferedOutputStream.write(bytes);
			bufferedInputStream.close();
			bufferedOutputStream.close();
			return true;
		}
		return false;

	}

	public boolean downloadFile(String ID, String downloadPath) throws IOException, SQLException {
		// double ranValue = Math.random();
		// if (ranValue > 0.5)
		// throw new IOException("Error in accessing file");

		Properties p = new Properties();
		String pPath = Operator.class.getClassLoader().getResource("path.properties").getPath();
		FileInputStream in = new FileInputStream(pPath);
		p.load(in);
		String fileHouse = p.getProperty("fileHouse");

		// String downloadPath="F:\\OOP\\downloadFiles";
		// String fileHouse = "F:\\OOP\\fileHouse";

		Doc doc = DataProcessing.searchDoc(ID);
		if (doc != null) {
			String path = fileHouse + "\\" + doc.getFilename();
			String downPath = downloadPath + "\\" + doc.getFilename();
			File file = new File(path);
			File downloadFile = new File(downPath);

			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));

			byte[] bytes = new byte[(int) file.length()];
			bufferedInputStream.read(bytes);
			bufferedOutputStream.write(bytes);
			bufferedInputStream.close();
			bufferedOutputStream.close();
			return true;
		}
		return false;

	}

	public boolean downloadFile(byte[] bytes, int length, String fileName, String downloadPath)
			throws IOException, SQLException {

		String downPath = downloadPath + "\\" + fileName;

		File downloadFile = new File(downPath);

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));

		bufferedOutputStream.write(bytes);

		bufferedOutputStream.close();
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
