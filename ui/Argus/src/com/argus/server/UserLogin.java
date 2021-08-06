package com.argus.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.argus.shared.CFG;
import com.argus.shared.DbUserData;

public class UserLogin extends RemoteServiceServlet  {

	private static final Logger logger = LogManager.getLogger(UserLogin.class.getName());

	public static List<DbUserData> getUsers() throws IllegalArgumentException {

		logger.debug("getUsers() - start");

		Connection conn     = null;

		List<DbUserData> dbUserDataList = new ArrayList<DbUserData>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);	
//			Statement stmt = conn.createStatement(); 
//			String select = "select * from ui_users order by user_name ASC";
//			ResultSet result = stmt.executeQuery(select);
			ResultSet result = conn.prepareStatement("SELECT * FROM ui_users ORDER BY user_name ASC").executeQuery();
			while (result.next()){
				dbUserDataList.add(new DbUserData(
						result.getInt("KEY"),
						result.getString("user_name"),
						result.getString("email_address"),
						result.getString("account_type"),
						result.getString("run_owner"),
						result.getString("home_folder")
						));
			}
			result.close();
		} catch (Exception e) {
			logger.error("getUsers", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				logger.debug(Objects.toString(e));
			}
		}

		logger.debug("getUsers() - end");
		return dbUserDataList;
	}


	public static DbUserData updateUserData(DbUserData aUser) {

		logger.debug("updateUserData(DbUserData) - start");


		aUser.setUserProperties(getUserProperties(aUser.getKEY()));
		aUser.setProductNames(getProductNames(aUser.getRunOwner()));


		logger.debug("updateUserData(DbUserData) - end");

		return aUser;
	}


	private static List<String> getProductNames(String aUserName) {

		logger.debug("getProductNames(String) - start");


		Connection conn = null;
		List<String> productNames = new ArrayList<String>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			String sql = "" +
					"SELECT " + 
					"    DISTINCT product_name " + 
					"FROM `ARGUS`.`b_builds` " + 
					"    INNER JOIN " + 
					"`ARGUS`.`b_runs` " + 
					"ON b_runs.KEY = b_builds.run_key AND b_runs.run_owner=?  ORDER BY `product_name` ASC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aUserName);
			ResultSet result = pstmt.executeQuery();

			while (result.next()){
				productNames.add(result.getString("product_name"));
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getProductNames", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}

		logger.debug("getProductNames(String) - end");
		return productNames;
	}


	private static HashMap<String, String> getUserProperties(Integer aUserKey) throws IllegalArgumentException {

		logger.debug("getUserProperties(Integer) - start");


		Connection conn     = null;

		HashMap<String, String> dbUserProperties = new HashMap<String, String>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM `ui_persistence` WHERE `user_key`=? ORDER BY `property_name` ASC");
			pstmt.setInt(1, aUserKey);
			ResultSet result = pstmt.executeQuery();

			while (result.next()){
				dbUserProperties.put(
						result.getString("property_name"),
						result.getString("property_value")
						);
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getUserProperties", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}


		logger.debug("getUserProperties(Integer) - end");

		return dbUserProperties;
	}


	public static String saveUserProperty(Integer aUserKey, String aPropertyName, String aPropertyValue) throws IllegalArgumentException {

		logger.debug("saveUserProperty(Integer, String, String) - start");


		Connection conn     = null;
		String result       = new String();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO `ui_persistence` (`user_key`, `property_name`, `property_value`) VALUES (?, ?, ?) " +
							"ON DUPLICATE KEY UPDATE `property_name`=?, `property_value`=?"
					);
			pstmt.setInt(1, aUserKey);
			pstmt.setString(2, aPropertyName);
			pstmt.setString(3, aPropertyValue);
			pstmt.setString(4, aPropertyName);
			pstmt.setString(5, aPropertyValue);

			Boolean resultSet = pstmt.execute();
			result = Objects.toString(resultSet);
			pstmt.close();
		} catch (Exception e) {
			result = Objects.toString(e);
			logger.error("saveUserProperty", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		String returnString = result + "\n";

		logger.debug("saveUserProperty(Integer, String, String) - end");

		return returnString;
	}

	public static String saveUserDb(DbUserData aUser) throws IllegalArgumentException {

		logger.debug("saveUserDb(DbUserData) - start");


		Connection conn     = null;
		String result       = new String();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO  `ui_users` (`user_name`, `email_address`, `account_type`, `run_owner`, `home_folder`) VALUES (?,?,?,?,?)" 
//			+
//							"ON DUPLICATE KEY UPDATE `user_name`=?, `email_address`=?, `account_type`=?"
					);

			pstmt.setString(1, aUser.getName());
			pstmt.setString(2, aUser.getEmailAddress());
			pstmt.setString(3, aUser.getAccountType());
			pstmt.setString(4, aUser.getRunOwner());
			pstmt.setString(5, aUser.getHomeFolder());
//			pstmt.setString(6, aUser.getName());
//			pstmt.setString(7, aUser.getEmailAddress());
//			pstmt.setString(8, aUser.getAccountType());

			Boolean resultSet = pstmt.execute();
			result = Objects.toString(resultSet);
			pstmt.close();
		} catch (Exception e) {
			result = Objects.toString(e);
			logger.error("saveUserDb", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}

		logger.debug("savePytharUserDb(DbUserData) - end");
		return result;
	}






}

