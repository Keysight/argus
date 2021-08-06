package com.argus.server.view.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.argus.server.Utils;
import com.argus.shared.CFG;
import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbUserData;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class ResultsAggregated extends RemoteServiceServlet {

	private static final Logger logger = LogManager.getLogger(ResultsAggregated.class.getName());

	public static String hideRegressionRun(Integer aRunKey) throws IllegalArgumentException {
		logger.debug("hideRegressionRun");
		Connection conn = null;
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE `ARGUS`.`b_runs` SET `visible`=0 WHERE `KEY`= ?");
			pstmt.setInt(1, aRunKey);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			logger.debug(Objects.toString(e));
			return Objects.toString(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		return "Done";
	}

	private static List<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser) {
		logger.debug("getDbResultsAggregated-1");
		Integer index = 1;

		Connection conn = null;
		String log = new String();
		log += "0";

		List<DbResultsAggregated> summaryPageData = new ArrayList<DbResultsAggregated>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			//conn.createStatement().execute("SET SESSION group_concat_max_len = 1 * 1024 * 1024;");
			String sql = ""+
					"SELECT * FROM " + 
					"	ARGUS.b_runs " + 
					"INNER JOIN " + 
					"	(" + 
					"	SELECT " + 
					"		b_builds.run_key," + 
					"        b_runs.run_owner,";
			//"		max(case when product_name = 'SONiC' then product_value end) AS 'SONiC'," + 
			//"		max(case when product_name = 'DENT'  then product_value end) AS 'DENT'";
			for (String product: aUser.getProductNames()) {           
				sql = sql + "max(case when product_name = '" + product + "' then product_value end) AS '" + product + "',";
			}
			sql = sql.substring(0, sql.length() - 1); //remove last comma or add a if in loop not to add it
			sql = sql +
					"	FROM " + 
					"       `ARGUS`.`b_builds` " + 
					"   INNER JOIN " + 
					"       `ARGUS`.`b_runs` " + 
					"   ON b_runs.KEY = b_builds.run_key AND b_runs.run_owner=? " + 
					"	GROUP BY run_key " + 
					"	) b_builds_transposed " + 
					"ON b_runs.KEY = b_builds_transposed.run_key AND b_runs.run_owner=? AND b_runs.visible=1 " +
					"ORDER BY b_runs.run_start_date DESC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aUser.getRunOwner());
			pstmt.setString(2, aUser.getRunOwner());

			ResultSet result = pstmt.executeQuery();

			while (result.next()) {

				HashMap<String,String> software_builds = new HashMap<String,String>();
				for (String product: aUser.getProductNames()) {           
					//software_builds.put(product, result.getString(product));
					String[] parts = result.getString(product).split("-");
					software_builds.put(product, parts[0]);
				}

				summaryPageData.add( new DbResultsAggregated(
						index++,
						result.getInt("KEY"),
						result.getTimestamp("run_start_date"),
						result.getTimestamp("run_end_date"),
						result.getString("run_name"),
						result.getInt("total_count_end"),
						result.getInt("total_count_now"),
						result.getInt("pass_count"),
						result.getInt("fail_count"),
						result.getInt("skip_count"),
						(double) result.getInt("pass_percent"),
						(double) result.getInt("fail_percent"),
						(double) result.getInt("skip_percent"),
						Utils.secondsToTime(result.getInt("run_duration")),
						result.getString("run_comment"),
						software_builds
						));
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.debug(Objects.toString(e));
			return DbResultsAggregated.getDemoData(log + Objects.toString(e));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}

		// long stopTime = System.currentTimeMillis();
		// long elapsedTime = stopTime - startTime;
		// setLog("argus","PERF", "total> "+elapsedTime);

		return summaryPageData;

	}
	
	public static List<DbResultsAggregated> getDbResultsHistoryAggregated(DbUserData aUser, Integer aStartingPoint) throws IllegalArgumentException {
		logger.debug("getDbResultsAggregated-1");
		Integer index = 1;
		
		Integer dataSize = 25;
		Integer startPoint = aStartingPoint - dataSize;
		if (startPoint < 0) {
			startPoint = 0;
		}

		Connection conn = null;
		String log = new String();
		log += "0";

		List<DbResultsAggregated> summaryPageData = new ArrayList<DbResultsAggregated>();
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			//conn.createStatement().execute("SET SESSION group_concat_max_len = 1 * 1024 * 1024;");
			String sql = ""+
					"SELECT * FROM " + 
					"	ARGUS.b_runs " + 
					"INNER JOIN " + 
					"	(" + 
					"	SELECT " + 
					"		b_builds.run_key," + 
					"        b_runs.run_owner,";
			//"		max(case when product_name = 'SONiC' then product_value end) AS 'SONiC'," + 
			//"		max(case when product_name = 'DENT'  then product_value end) AS 'DENT'";
			for (String product: aUser.getProductNames()) {           
				sql = sql + "max(case when product_name = '" + product + "' then product_value end) AS '" + product + "',";
			}
			sql = sql.substring(0, sql.length() - 1); //remove last comma or add a if in loop not to add it
			sql = sql +
					"	FROM " + 
					"       `ARGUS`.`b_builds` " + 
					"   INNER JOIN " + 
					"       `ARGUS`.`b_runs` " + 
					"   ON b_runs.KEY = b_builds.run_key AND b_runs.run_owner=? " + 
					"	GROUP BY run_key " + 
					"	) b_builds_transposed " + 
					"ON b_runs.KEY = b_builds_transposed.run_key AND b_runs.run_owner=? AND b_runs.visible=1 " +
					"ORDER BY b_runs.run_start_date DESC LIMIT ?,?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aUser.getRunOwner());
			pstmt.setString(2, aUser.getRunOwner());
			pstmt.setInt(3, startPoint);
			pstmt.setInt(4, dataSize);

			ResultSet result = pstmt.executeQuery();

			while (result.next()) {

				HashMap<String,String> software_builds = new HashMap<String,String>();
				for (String product: aUser.getProductNames()) {           
					//software_builds.put(product, result.getString(product));
					String[] parts = result.getString(product).split("-");
					software_builds.put(product, parts[0]);
					
				}

				summaryPageData.add( new DbResultsAggregated(
						index++,
						result.getInt("KEY"),
						result.getTimestamp("run_start_date"),
						result.getTimestamp("run_end_date"),
						result.getString("run_name"),
						result.getInt("total_count_end"),
						result.getInt("total_count_now"),
						result.getInt("pass_count"),
						result.getInt("fail_count"),
						result.getInt("skip_count"),
						(double) result.getInt("pass_percent"),
						(double) result.getInt("fail_percent"),
						(double) result.getInt("skip_percent"),
						Utils.secondsToTime(result.getInt("run_duration")),
						result.getString("run_comment"),
						software_builds
						));
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.debug(Objects.toString(e));
			return DbResultsAggregated.getDemoData(log + Objects.toString(e));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}

		// long stopTime = System.currentTimeMillis();
		// long elapsedTime = stopTime - startTime;
		// setLog("argus","PERF", "total> "+elapsedTime);

		return summaryPageData;

	}


	public static PagingLoadResult<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser, FilterPagingLoadConfig config) {

		// get data from SQL database
		List<DbResultsAggregated> summaryPageData = getDbResultsAggregated(aUser);

		ArrayList<DbResultsAggregated> temp = new ArrayList<DbResultsAggregated>();
		ArrayList<DbResultsAggregated> remove = new ArrayList<DbResultsAggregated>();
		for (DbResultsAggregated s : summaryPageData) {
			temp.add(s);
		}

		// sort the data
		if (config.getSortInfo().size() > 0) {
			SortInfo sort = config.getSortInfo().get(0);
			if (sort.getSortField() != null) {
				final String sortField = sort.getSortField();
				if (sortField != null) {
					Collections.sort(temp, sort.getSortDir().comparator(new Comparator<DbResultsAggregated>() {
						public int compare(DbResultsAggregated p1, DbResultsAggregated p2) {
							if        (sortField.equals("run_start_date")) {
								return p1.getRun_start_date().compareTo(p2.getRun_start_date());
							} else if (sortField.equals("run_end_date")) {
								return p1.getRun_end_date().compareTo(p2.getRun_end_date());
							} else if (sortField.equals("run_name")) {
								return p1.getRun_name().compareTo(p2.getRun_name());
							} else if (sortField.equals("total_count_end")) {
								return p1.getTotal_count_end().compareTo(p2.getTotal_count_end());
							} else if (sortField.equals("total_count_now")) {
								return p1.getTotal_count_now().compareTo(p2.getTotal_count_now());
							} else if (sortField.equals("pass")) {
								return p1.getPass().compareTo(p2.getPass());
							} else if (sortField.equals("fail")) {
								return p1.getFail().compareTo(p2.getFail());
							} else if (sortField.equals("skip")) {
								return p1.getSkip().compareTo(p2.getSkip());
							} else if (sortField.equals("pass_percent")) {
								return p1.getPass_percent().compareTo(p2.getPass_percent());
							} else if (sortField.equals("fail_percent")) {
								return p1.getFail_percent().compareTo(p2.getFail_percent());
							} else if (sortField.equals("skip_percent")) {
								return p1.getSkip_percent().compareTo(p2.getSkip_percent());
							} else if (sortField.equals("run_duration")) {
								return p1.getRun_duration().compareTo(p2.getRun_duration());
							} else if (sortField.equals("run_comment")) {
								return p1.getRun_comment().compareTo(p2.getRun_comment());
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

			for (DbResultsAggregated s : summaryPageData) {
				String value = s.getProperty(prop);
				String safeValue = value == null ? null : Objects.toString(value);

				if (safeTest.length() == 0 && (safeValue == null || safeValue.length() == 0)) {
					continue;
				} else if (safeValue == null) {
					remove.add(s);
					continue;
				}

				if ("string".equals(type)) {
					if (safeValue.toLowerCase().indexOf(safeTest.toLowerCase()) == -1) {
						remove.add(s);
					}
				} else if ("date".equals(type)) {
					if (ResultsFilters.isDateFiltered(safeTest, comparison, safeValue)) {
						remove.add(s);
					}
				} else if ("boolean".equals(type)) {
					if (ResultsFilters.isBooleanFiltered(safeTest, comparison, safeValue)) {
						remove.add(s);
					}
				} else if ("list".equals(type)) {
					if (ResultsFilters.isListFiltered(safeTest, safeValue)) {
						remove.add(s);
					}
				} else if ("numeric".equals(type)) {
					if (ResultsFilters.isNumberFiltered(safeTest, comparison, safeValue)) {
						remove.add(s);
					}
				}
			}
		}

		for (DbResultsAggregated s : remove) {
			temp.remove(s);
		}

		int start = config.getOffset();
		int limit = temp.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		return new PagingLoadResultBean<DbResultsAggregated>(new ArrayList<DbResultsAggregated>(temp.subList(start, limit)), temp.size(), start);
	}



	public static List<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser, String aPath) throws IllegalArgumentException {
		logger.debug("getDbResultsAggregated-2");
		// optimized code, we have a table b_runs that has the proper info for each user
		if (aPath == null) {
			return getDbResultsAggregated(aUser);
		}

		try {
			//		SummaryPageData.add( new DbResultsAggregated(
			//				result.getInt("KEY"),
			//				result.getDate("run_start_date"),
			//				result.getDate("run_end_date"),
			//				result.getString("run_name"),
			//				result.getInt("total_count_end"),
			//				result.getInt("total_count_now"),
			//				result.getInt("pass_count"),
			//				result.getInt("fail_count"),
			//				result.getInt("skip_count"),
			//				(double) result.getInt("pass_percent"),
			//				(double) result.getInt("fail_percent"),
			//				(double) result.getInt("skip_percent"),
			//				Utils.secondsToTime(result.getInt("run_duration")),
			//				result.getString("run_comment"),
			//				software_builds,
			//				"NA"
			//				));

		} catch (Exception e) {
			logger.debug(Objects.toString(e));
			return DbResultsAggregated.getDemoData(Objects.toString(e));
		}
		return DbResultsAggregated.getDemoData("implement me");
		//return SummaryPageData;
	}

	public static String updateRun(DbResultsAggregated aRunRow) throws IllegalArgumentException {
		logger.debug("updateRun");
		Connection conn = null;
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			PreparedStatement pstmt = conn.prepareStatement("UPDATE `ARGUS`.`b_runs` SET `run_comment`=? WHERE `KEY`= ?");
			pstmt.setString(1, aRunRow.getRun_comment());
			pstmt.setInt(2, aRunRow.getKEY());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			logger.debug(Objects.toString(e));
			return Objects.toString(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		return "Done";
	}

	public static Integer getDatasetSize(DbUserData aUser) {
		Connection conn = null;
		Integer dataSize = -1;

		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			//conn.createStatement().execute("SET SESSION group_concat_max_len = 1 * 1024 * 1024;");
			String sql = "SELECT COUNT(*) as data_size FROM ARGUS.b_runs WHERE b_runs.run_owner=? AND b_runs.visible=1";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aUser.getRunOwner());
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				dataSize = result.getInt("data_size");
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.debug(Objects.toString(e));
			return dataSize;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}
		return dataSize;
	}

}