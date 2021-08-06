package com.argus.shared;

import java.io.Serializable;
import java.util.List;

public class ResultsCompare implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int ID = 0;
	private String test_location;
	private String test_name;
	private String topology;
	private List<String> resultList;
	
	private int id;

	public ResultsCompare() {
		this.id = ID++;
	}
	
	public ResultsCompare(String test_location, String test_name, List<String> resultList) {
		this();
		this.setTest_location(test_location);
		this.setTest_name(test_name);
		this.setResultList(resultList);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setResultList(List<String> kResultList) {
		resultList = kResultList;	
	}

	public List<String> getResultList() {
		return resultList;
	}
	
	private void setTest_name(String kTest_name) {
		test_name = kTest_name;	
	}
	
	public String getTest_name() {
		if (test_name != null) {
			return test_name;
		} else {
			return "NULL";
		}
	}
	
	public Integer getResultListSize() {
		return resultList.size();
	}

	public String getTest_location() {
		return test_location;
	}

	public void setTest_location(String test_location) {
		this.test_location = test_location;
	}

	public String getTopology() {
		return this.topology;
	}	
	public void setTopology(String topology) {
		this.topology = topology;
	}

}
