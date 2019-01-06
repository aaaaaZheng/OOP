package common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import dao.DataProcessing;

public class Operator extends User {

	private static final long serialVersionUID = -2485862989295609328L;

	public Operator(String name, String password, String role) {
		super(name, password, role);
	}

	/**
	 * 
	 * @param fileName为绝对路径
	 * @param fileNum
	 * @param fileDescription
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public boolean uploadFile(String absolutePath, String fileNum, String fileDescription)
			throws IOException, SQLException {

		Properties p = new Properties();

		String pPath = Operator.class.getClassLoader().getResource("path.properties").getPath();
		FileInputStream in = new FileInputStream(pPath);
		p.load(in);
		String fileHouse = p.getProperty("fileHouse");

		// String fileHouse = "F:\\OOP\\fileHouse";

		// 截取文件名
		String fileName = absolutePath.substring(absolutePath.lastIndexOf("\\") + 1);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		DataProcessing.insertDoc(fileNum, this.getName(), timestamp, fileDescription, fileName);

		// 要上传的文件
		File file = new File(absolutePath);
		byte[] bytes = new byte[(int) file.length()];
		// 上传到House的文件
		File resultFile = new File(fileHouse + "\\" + fileName);

		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(resultFile));

		bufferedInputStream.read(bytes);
		bufferedOutputStream.write(bytes);
		bufferedInputStream.close();
		bufferedOutputStream.close();
		return true;
	}

	/***
	 * 
	 * @param fileName        文件名
	 * @param fileNum
	 * @param fileDescription
	 * @param bytes
	 * @param fileHouse
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public boolean uploadFile(String fileName, String fileNum, String fileDescription, byte[] bytes, String fileHouse)
			throws IOException, SQLException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		DataProcessing.insertDoc(fileNum, this.getName(), timestamp, fileDescription, fileName);

		// 上传到House的文件
		File resultFile = new File(fileHouse + "\\" + fileName);

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(resultFile));

		bufferedOutputStream.write(bytes);
		bufferedOutputStream.close();
		return true;
	}

}
