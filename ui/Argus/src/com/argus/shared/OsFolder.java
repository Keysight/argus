package com.argus.shared;

import java.util.List;

public class OsFolder extends OsFile {

	private static final long serialVersionUID = 521119528959400509L;
	
	private List<OsFile> children;
	private Integer filesCount;

	protected OsFolder() {

	}

	public OsFolder(Integer id, String name, String path, Integer filesCount) {
		super(id, name, path);
		this.filesCount = filesCount;
	}
	
	public OsFolder(Integer id, String name, String path) {
		super(id, name, path);
		this.filesCount = 0;
	}

	public List<OsFile> getChildren() {
		return children;
	}

	public void setChildren(List<OsFile> children) {
		this.children = children;
	}

	public void addChild(OsFile child) {
		getChildren().add(child);
	}

	public Integer getFilesCount() {
		return filesCount;
	}

	public void setFilesCount(Integer filesCount) {
		this.filesCount = filesCount;
	}
}
