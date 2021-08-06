package com.argus.shared;

import java.io.Serializable;
import java.util.List;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;

public class OsFile implements Serializable, TreeStore.TreeNode<OsFile> {
	
	private static final long serialVersionUID = 7135267948235032980L;
	
	private Integer id;
	private String name;
	private String path;

	protected OsFile() {

	}

	public OsFile(Integer id, String name, String path) {
		this.id = id;
		this.name = name;
		this.path = path;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public OsFile getData() {
		return this;
	}

	@Override
	public List<? extends TreeNode<OsFile>> getChildren() {
		return null;
	}

	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
