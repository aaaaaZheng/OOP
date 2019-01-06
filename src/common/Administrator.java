package common;

import java.sql.SQLException;
import java.sql.Timestamp;

import dao.DataProcessing;

public class Administrator extends User {

	private static final long serialVersionUID = 366340120754113628L;

	public Administrator(String name, String password, String role) {
		super(name, password, role);
	}

	public boolean changeUserInfo(String name, String pwd, String role) throws SQLException {
		DataProcessing.updateUser(name, pwd, role);
		return true;

	}

	public boolean delUser(String name) throws SQLException {
		DataProcessing.deleteUser(name);
		return true;
	}

	public boolean addUser(String name, String pwd, String role) throws SQLException {
		if (DataProcessing.insertUser(name, pwd, role))
			return true;
		else
			return false;
	}

	public boolean addNotice(String desc) throws SQLException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (DataProcessing.insertNotice(this.getName(), timestamp, desc))
			return true;
		else
			return false;
	}

	public Notice SearchNotice(String ID) throws SQLException {
		return DataProcessing.searchNotice(ID);
	}

	public boolean delNotice(String ID) throws SQLException {
		DataProcessing.deleteNotice(ID);
		return true;
	}

}
