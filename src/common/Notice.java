package common;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable {

	private static final long serialVersionUID = 3273884059556185720L;
	private String ID;
	private String creator;
	private Timestamp timestamp;
	private String description;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Notice [ID=" + ID + ", creator=" + creator + ", timestamp=" + timestamp + ", description=" + description
				+ "]";
	}

	public Notice(String iD, String creator, Timestamp timestamp, String description) {
		super();
		ID = iD;
		this.creator = creator;
		this.timestamp = timestamp;
		this.description = description;
	}

	public Notice() {
		super();
		// TODO 自动生成的构造函数存根
	}

}
