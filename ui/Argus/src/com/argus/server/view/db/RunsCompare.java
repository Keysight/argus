package com.argus.server.view.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.argus.shared.CFG;
import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.ResultsCompare;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RunsCompare extends RemoteServiceServlet {

	//private static final Logger logger = LogManager.getLogger(RunCompare.class.getName());

	public static List<ResultsCompare> compareRuns(List<Integer> runsKeys) throws IllegalArgumentException {
		//logger.debug("compareRuns");
		List<ResultsCompare> resultsCompareList = new ArrayList<ResultsCompare>();
		List<String> errors = new ArrayList<String>();
		errors.add("DUMMY");
		ResultsCompare rc_temp ;

		try {
			Integer runIndex = 0;
			for (Integer runKey: runsKeys) {
				List<DbResultsTestCase> dbResultsTestCaseList = getDatafromDb(runKey);
				if (dbResultsTestCaseList.size() < 1) {
					resultsCompareList.add(new ResultsCompare("TL","TC list size is 0", errors));
				}
				for (DbResultsTestCase testCase : dbResultsTestCaseList) {
					boolean found = false;
					for (ResultsCompare resultCompare: resultsCompareList) {
						if (resultCompare.getTest_location().equals(testCase.getTest_location())) {
							resultCompare.getResultList().add(testCase.getProperty("test_result"));
							found = true;
							break;
						}
					}
					if (found == false) {
						List<String> pivotList = new ArrayList<String>();
						for (Integer i = 0; i < runIndex; i++ ) {
							pivotList.add("NA");
						}
						pivotList.add(testCase.getProperty("test_result"));
						rc_temp = new ResultsCompare(testCase.getTest_location(),testCase.getTest_name(), pivotList);
						resultsCompareList.add(rc_temp);
					}

				}

				Integer maxLength = -1;
				for (ResultsCompare resultCompare: resultsCompareList) {
					if (maxLength < resultCompare.getResultListSize()) {
						maxLength = resultCompare.getResultListSize();
					}
				}

				for (ResultsCompare resultCompare: resultsCompareList) {
					for (Integer i = resultCompare.getResultListSize(); i < maxLength; i++) {
						resultCompare.getResultList().add("NA");
					}
				}

				runIndex++;

			}
			if (resultsCompareList.size() < 1) {
				resultsCompareList.add(new ResultsCompare("TL","RC list size is 0", errors));
			}
		} catch (Exception e) {
			//logger.debug(Objects.toString(e));
			resultsCompareList.add(new ResultsCompare("TL", e.toString(), errors));
		} 
		return resultsCompareList;
	}


	public static List<DbResultsTestCase> getDatafromDb(Integer aRunKey) throws IllegalArgumentException {
		//logger.debug("getDatafromDb");
		Connection conn = null;
		List<DbResultsTestCase> testCaseList = new ArrayList<DbResultsTestCase>();
		List<String> errors = new ArrayList<String>();

		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			PreparedStatement pstmt = conn.prepareStatement("SELECT `KEY`, test_location, test_name, test_result FROM ARGUS.b_tests WHERE run_key=?");
			pstmt.setInt(1, aRunKey);
			ResultSet result = pstmt.executeQuery();

			while (result.next()) {
				testCaseList.add(new DbResultsTestCase(
						result.getString("test_location"),
						result.getString("test_name"),
						result.getString("test_result")
						));
			}
			pstmt.close();

		} catch (Exception e) {
			//logger.debug(Objects.toString(e));
			testCaseList.add(new DbResultsTestCase("TL", e.toString()));
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				//logger.debug(Objects.toString(e));
			}
		}
		return testCaseList;
	}




}
