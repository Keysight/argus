package com.argus.server.view.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.argus.server.Net;
import com.argus.server.Utils;
import com.argus.shared.CFG;
import com.argus.shared.DbResultsSuite;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbUserData;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;


public class ResultsDetailed  extends RemoteServiceServlet {

	private static final Logger logger = LogManager.getLogger(ResultsDetailed.class.getName());

	//public static List<DbResultsSuite> getDbResultsSuite(String aUser, String aPath, String aRunDate) throws IllegalArgumentException {
	public static List<DbResultsSuite> getDbResultsSuite(Integer aKEY) throws IllegalArgumentException {
		Connection conn     = null;

		List<DbResultsSuite> DetailedPageData = new ArrayList<DbResultsSuite>();

		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			String sql = ""+
					"SELECT suite,"+
					"    COUNT(test_result) AS totalCount,"+
					"    COUNT(CASE WHEN test_result = 'PASS' THEN 1 ELSE null END) AS passCount,"+
					"    COUNT(CASE WHEN test_result = 'FAIL' THEN 1 ELSE null END) AS failCount,"+
					"    COUNT(CASE WHEN test_result = 'SKIP' THEN 1 ELSE null END) AS skipCount,"+
					"    SUM(test_duration) AS duration "+
					"FROM b_tests WHERE run_key=? GROUP BY suite ORDER BY suite ASC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aKEY);
			//pstmt.setString(2, aPath+"%"); //TODO: implement this for when a certain path is selected as a filter
			ResultSet result = pstmt.executeQuery();

			while (result.next()) {
				// SQL
				String suite = result.getString("suite");
				Integer totalCount = result.getInt("totalCount");
				Integer passCount  = result.getInt("passCount");
				Integer failCount  = result.getInt("failCount");
				Integer skipCount  = result.getInt("skipCount");
				Integer duration   = result.getInt("duration");

				// MATH
				String tDuration = Utils.secondsToTime(duration);
				Integer passPercent = Math.round(100 * passCount / totalCount);
				Integer failPercent = Math.round(100 * failCount / totalCount);
				Integer skipPercent = Math.round(100 * skipCount / totalCount);

				DetailedPageData.add(
						new DbResultsSuite(suite, totalCount, passCount, failCount, skipCount, passPercent, failPercent, skipPercent, tDuration)
						);
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			return DbResultsSuite.getDemoFailData1(Objects.toString(e));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}

		return DetailedPageData;
	}


	public static List<DbResultsTestCase> getTestCasePage(Integer aKEY) throws IllegalArgumentException {

		Integer index = 1;
		Connection conn = null;

		List<DbResultsTestCase> TestCasePageData = new ArrayList<DbResultsTestCase>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			String sql = "SELECT * FROM b_tests WHERE run_key=?";
			//if (aUser    != null) { sql = sql + " AND linux_user = ?"; }
			//if (aRunDate != null) { sql = sql + " AND run_date = ?"; }
			sql = sql + " ORDER BY test_start_time ASC";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aKEY);
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {				
				TestCasePageData.add(new DbResultsTestCase(
						index++,
						result.getInt("KEY"),
						result.getInt("run_key"),
						new Date(), //just insert a value to make data structure happy, this is needed in test case history
						result.getString("test_location"),
						result.getString("test_name"),
						result.getString("test_result"),
						result.getString("test_error"),
						result.getString("setup_address"),
						result.getString("setup_ports"),
						Utils.secondsToTime(result.getInt("test_duration")),
						result.getString("test_client_address"),
						result.getString("test_client_port"),
						result.getString("test_topology"),
						result.getString("test_hash_code"),
						result.getString("suite"),
						result.getString("ports_type"),
						result.getString("setup_type"),
						result.getDate("test_start_time"),
						result.getString("setup_sn"),
						result.getString("tc_management"),
						null,
						null
						));
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getTestCasePage", e);
			return DbResultsTestCase.getDemoFailData1(Objects.toString(e));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		return TestCasePageData;
	}

	public static PagingLoadResult<DbResultsTestCase> getTestCasePage(Integer aKEY, FilterPagingLoadConfig config) {

		// get data from SQL database
		List<DbResultsTestCase> tcPageData = getTestCasePage(aKEY);

		ArrayList<DbResultsTestCase> temp = new ArrayList<DbResultsTestCase>();
		ArrayList<DbResultsTestCase> remove = new ArrayList<DbResultsTestCase>();
		for (DbResultsTestCase s : tcPageData) {
			temp.add(s);
		}

		// sort the data
		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(temp, sort.getSortDir().comparator(new Comparator<DbResultsTestCase>() {
						public int compare(DbResultsTestCase p1, DbResultsTestCase p2) {
							if        (sortField.equals("KEY")) {
								return p1.getKEY().compareTo(p2.getKEY());
							} else if (sortField.equals("run_key")) {
								return p1.getRun_key().compareTo(p2.getRun_key());
							} else if (sortField.equals("test_location")) {
								return p1.getTest_location().compareTo(p2.getTest_location());
							} else if (sortField.equals("test_name")) {
								return p1.getTest_name().compareTo(p2.getTest_name());
							} else if (sortField.equals("test_result")) {
								return p1.getTest_result().compareTo(p2.getTest_result());
							} else if (sortField.equals("test_error")) {
								return p1.getTest_error().compareTo(p2.getTest_error());
							} else if (sortField.equals("bug_id")) {
								//return p1.getBug_id().compareTo(p2.getBug_id());
							} else if (sortField.equals("bug_state")) {
								//return p1.getBug_state().compareTo(p2.getBug_state());
							} else if (sortField.equals("setup_address")) {
								return p1.getSetup_address().compareTo(p2.getSetup_address());
							} else if (sortField.equals("setup_ports")) {
								return p1.getSetup_ports().compareTo(p2.getSetup_ports());
							} else if (sortField.equals("test_duration")) {
								return p1.getTest_duration().compareTo(p2.getTest_duration());
							} else if (sortField.equals("test_client_address")) {
								return p1.getTest_client_address().compareTo(p2.getTest_client_address());
							} else if (sortField.equals("test_client_port")) {
								return p1.getTest_client_port().compareTo(p2.getTest_client_port());
							} else if (sortField.equals("test_topology")) {
								return p1.getTest_topology().compareTo(p2.getTest_topology());
							} else if (sortField.equals("test_hash_code")) {
								return p1.getTest_hash_code().compareTo(p2.getTest_hash_code());
							} else if (sortField.equals("suite")) {
								return p1.getSuite().compareTo(p2.getSuite());
							} else if (sortField.equals("ports_type")) {
								return p1.getPorts_type().compareTo(p2.getPorts_type());
							} else if (sortField.equals("setup_type")) {
								return p1.getSetup_type().compareTo(p2.getSetup_type());
							} else if (sortField.equals("test_start_time")) {
								return p1.getTest_start_time().compareTo(p2.getTest_start_time());
							} else if (sortField.equals("setup_sn")) {
								return p1.getSetup_sn().compareTo(p2.getSetup_sn());
							} else if (sortField.equals("ixtracker_tc_id")) {
								return p1.getTc_management().compareTo(p2.getTc_management());
							} else if (sortField.equals("test_aggregated_area")) {
								return p1.getTest_aggregated_area().compareTo(p2.getTest_aggregated_area());
							} else if (sortField.equals("run_owner")) {
								return p1.getRun_owner().compareTo(p2.getRun_owner());
							} else if (sortField.equals("software_builds")) {
								//return p1.getSoftware_builds().compareTo(p2.getSoftware_builds());
							}
							//TODO: software_builds, KEY
							return 0;
						}
					}));
				}
			}
		}

		// filter
		List<FilterConfig> filters = config.getFilters();
		for (FilterConfig f : filters) {
			String type = f.getType();
			String test = f.getValue();
			String prop = f.getField();
			String comparison = f.getComparison();

			String safeTest = test == null ? "" : Objects.toString(test);

			for (DbResultsTestCase tc : tcPageData) {
				String value = tc.getProperty(prop);
				String safeValue = value == null ? null : Objects.toString(value);

				if (safeTest.length() == 0 && (safeValue == null || safeValue.length() == 0)) {
					continue;
				} else if (safeValue == null) {
					remove.add(tc);
					continue;
				}

				if ("string".equals(type)) {
					if (safeValue.toLowerCase().indexOf(safeTest.toLowerCase()) == -1) {
						remove.add(tc);
					}
				} else if ("date".equals(type)) {
					if (ResultsFilters.isDateFiltered(safeTest, comparison, safeValue)) {
						remove.add(tc);
					}
				} else if ("boolean".equals(type)) {
					if (ResultsFilters.isBooleanFiltered(safeTest, comparison, safeValue)) {
						remove.add(tc);
					}
				} else if ("list".equals(type)) {
					if (ResultsFilters.isListFiltered(safeTest, safeValue)) {
						remove.add(tc);
					}
				} else if ("numeric".equals(type)) {
					if (ResultsFilters.isNumberFiltered(safeTest, comparison, safeValue)) {
						remove.add(tc);
					}
				}
			}
		}

		for (DbResultsTestCase s : remove) {
			temp.remove(s);
		}

		int start = config.getOffset();
		int limit = temp.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		return new PagingLoadResultBean<DbResultsTestCase>(new ArrayList<DbResultsTestCase>(temp.subList(start, limit)), temp.size(), start);
	}

	public static List<DbResultsTestCase> getTestCaseHistory(DbUserData loggedUser, String aUser, String aPath) throws IllegalArgumentException {
		Integer index = 1;
		Connection conn     = null;

		List<DbResultsTestCase> TestCasePageData = new ArrayList<DbResultsTestCase>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);

			String sql = ""+
					"SELECT * FROM b_tests " +
					"INNER JOIN " + 
					"	(" + 
					"	SELECT " + 
					"		b_builds.run_key," + 
					"       b_runs.run_owner,";
			for (String product: loggedUser.getProductNames()) {           
				sql = sql + "max(case when product_name = '" + product + "' then product_value end) AS '" + product + "',";
			}
			sql = sql.substring(0, sql.length() - 1); //remove last comma or add a if in loop not to add it
			sql = sql +
					"	FROM b_builds " + 
					"   INNER JOIN b_runs " + 
					"   ON b_runs.KEY = b_builds.run_key " +
					"	GROUP BY run_key " + 
					"	) b_builds_transposed " + 
					"ON b_tests.run_key = b_builds_transposed.run_key AND b_tests.test_location LIKE ? ";
			if (aUser    != null) { sql = sql + " AND b_builds_transposed.run_owner = ? "; }
			sql = sql + " 	INNER JOIN " +
					" b_runs ON b_tests.run_key = b_runs.`KEY`";
			sql = sql + " ORDER BY b_tests.KEY DESC";


			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aPath+"%");
			if (aUser    != null) { pstmt.setString(2, aUser); }

			ResultSet result = pstmt.executeQuery();
			while (result.next()) {

				HashMap<String,String> software_builds = new HashMap<String,String>();
				for (String product: loggedUser.getProductNames()) {           
					software_builds.put(product, result.getString(product));
				}


				TestCasePageData.add(new DbResultsTestCase(
						index++,
						result.getInt("KEY"),
						result.getInt("run_key"),
						result.getTimestamp("run_start_date"),
						result.getString("test_location"),
						result.getString("test_name"),
						result.getString("test_result"),
						result.getString("test_error"),
						result.getString("setup_address"),
						result.getString("setup_ports"),
						Utils.secondsToTime(result.getInt("test_duration")),
						result.getString("test_client_address"),
						result.getString("test_client_port"),
						result.getString("test_topology"),
						result.getString("test_hash_code"),
						result.getString("suite"),
						result.getString("ports_type"),
						result.getString("setup_type"),
						result.getTimestamp("test_start_time"),
						result.getString("setup_sn"),
						result.getString("tc_management"),
						software_builds,
						result.getString("run_owner")
						));
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getTestCaseHistory", e);
			return DbResultsTestCase.getDemoFailData1(Objects.toString(e));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		return TestCasePageData;
	}


	public static String getTestLog(Integer aKEY) throws IllegalArgumentException {
		Connection conn     = null;
		String testLog = "NA" ;
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			String sql = "SELECT `test_log` FROM  `b_tests` WHERE `KEY` = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aKEY);
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				testLog = result.getString("test_log");
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getTestLog", e);
			return Objects.toString(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		return testLog;
	}

	public static String getTestLogDownloadPath(Integer aKEY) throws IllegalArgumentException {
		Connection conn     = null;
		String testLog = null ;
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			String sql = "SELECT `test_log` FROM  `b_tests` WHERE `KEY` = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aKEY);
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				testLog = result.getString("test_log");
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getTestLogDownloadPath", e);
			return Objects.toString(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}

		String machineIP = Net.IpAddr();			
		String link = "http://"+ machineIP + testLog;

		return link;

	}


}
