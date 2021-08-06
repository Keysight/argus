package com.argus.shared;

import java.io.Serializable;

public class OsFileContent implements Serializable {

	private static final long serialVersionUID = -8954290963096188841L;

	private static int ID = 0;

	private String content;
	private String type;
	private int id;
	
	public OsFileContent() {
		this.setId(ID++);
		this.setContent("NA");
		this.setType("NA");
	}
	
	public OsFileContent(String content, String type){
		this();
		this.setContent(content);
		this.setType(type);
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
