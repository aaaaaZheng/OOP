package common;

import java.io.Serializable;
import java.sql.Timestamp;

public class Doc implements Serializable {

	private static final long serialVersionUID = 63101692249796367L;
	private String ID;
	private String creator;
	private Timestamp timestamp;
	private String description;
	private String filename;

	@Override
	public String toString() {
		return "Doc [ID=" + ID + ", creator=" + creator + ", timestamp=" + timestamp + ", description=" + description
				+ ", filename=" + filename + "]";
	}

	public Doc(String ID, String creator, Timestamp timestamp, String description, String filename) {
		super();
		this.ID = ID;
		this.creator = creator;
		this.timestamp = timestamp;
		this.description = description;
		this.filename = filename;
	}

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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}