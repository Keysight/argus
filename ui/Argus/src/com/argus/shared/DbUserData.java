package com.argus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DbUserData implements Serializable {
	
	private static final long serialVersionUID = -668370386320228989L;

	private static int ID = 0;

	private Integer KEY;
	private String UserName;
	private String EmailAddress;
	private String AccountType;
	private String RunOwner;
	private String HomeFolder;
	private HashMap<String, String> UserProperties;
	private List<String> productNames;
	private int id;
	

	public DbUserData(){
		this.id = ID++;
		this.KEY = -1;
	}

	public DbUserData(Integer KEY, String UserName, String EmailAddress, String AccountType, String RunOwner, String HomeFolder) {
		this();
		this.KEY = KEY;
		this.UserName = UserName;
		this.EmailAddress = EmailAddress;
		this.AccountType = AccountType;
		this.RunOwner = RunOwner;
		this.HomeFolder = HomeFolder;
		this.UserProperties = null;
	}

	public static List<DbUserData> getDemoFailData1(String err) {
		List<DbUserData> Results = new ArrayList<DbUserData>();
		Results.add(new DbUserData(0,err,"","","",""));		
		return Results;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getKEY() {
		return KEY;
	}

	public void setKEY(Integer kEY) {
		KEY = kEY;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		this.UserName = userName;
	}

	
	public String getName() {
		return UserName;
	}
	
	public String getEmail() {
		return EmailAddress;
	}

	
	public String getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.EmailAddress = emailAddress;
	}

	public String getAccountType() {
		return AccountType;
	}

	public void setAccountType(String accountType) {
		AccountType = accountType;
	}

	public String getRunOwner() {
		return RunOwner;
	}

	public void setRunOwner(String runOwner) {
		RunOwner = runOwner;
	}

	public String getHomeFolder() {
		return HomeFolder;
	}

	public void setHomeFolder(String homeFolder) {
		HomeFolder = homeFolder;
	}

	public HashMap<String, String> getUserProperties() {
		return UserProperties;
	}

	public String getUserProperty(String propertyName) {
		if (UserProperties.containsKey(propertyName)) {
			return UserProperties.get(propertyName);
		} else {
			return "";
		}
	}

	public void setUserProperties(HashMap<String, String> userProperties) {
		UserProperties = userProperties;
	}

	public List<String> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<String> productNames) {
		this.productNames = productNames;
	}
}