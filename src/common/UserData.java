package common;

import java.io.Serializable;

public class UserData implements Serializable {

	private static final long serialVersionUID = -5988307862561754865L;
	private String name;
	private String password;

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

	@Override
	public String toString() {
		return "UserData [name=" + name + ", password=" + password + "]";
	}

}
