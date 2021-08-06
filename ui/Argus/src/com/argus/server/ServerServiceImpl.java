package com.argus.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.argus.client.ArgusService;
import com.argus.server.view.db.ResultsAggregated;
import com.argus.server.view.db.ResultsDetailed;
import com.argus.server.view.db.RunsCompare;
import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbUserData;
import com.argus.shared.ResultsCompare;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbResultsSuite;
import com.argus.shared.OsFileContent;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.argus.shared.CFG;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServerServiceImpl extends RemoteServiceServlet implements ArgusService {


	public String loadprops() throws IllegalArgumentException {

		Map<String, String> env = System.getenv();
		String ENV_IP   = env.get("MYSQL_CONTAINER_IP");
		String ENV_PASS = env.get("MYSQL_ROOT_PASSWORD");
		if (ENV_IP != null) {CFG.IP   = ENV_IP;}
		if (ENV_PASS != null) {CFG.PASS = ENV_PASS;}

		return CFG.IP;
	}


	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// UserLoginServer.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////


	public List<DbUserData> getUsers() throws IllegalArgumentException {
		return UserLogin.getUsers();
	}

	public DbUserData updateUserData(DbUserData aUser) throws IllegalArgumentException {
		return UserLogin.updateUserData(aUser);
	}



	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// ViewResultsDetailed.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////



	public String getTestLog(Integer aKEY) throws IllegalArgumentException {
		return ResultsDetailed.getTestLog(aKEY);
	}
	public String getTestLogDownloadPath(Integer aKEY) throws IllegalArgumentException {
		return ResultsDetailed.getTestLogDownloadPath(aKEY);
	}


	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// ViewResultsAggregated.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////

	public PagingLoadResult<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser, FilterPagingLoadConfig config) {
		return ResultsAggregated.getDbResultsAggregated(aUser, config);
	}

	public List<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser, String aPath) throws IllegalArgumentException {
		return ResultsAggregated.getDbResultsAggregated(aUser, aPath);
	}

	public Integer getDatasetSize(DbUserData user) throws IllegalArgumentException {
		return ResultsAggregated.getDatasetSize(user);
	}

	public List<DbResultsAggregated> getDbResultsHistoryAggregated(DbUserData aUser, Integer startPoint) throws IllegalArgumentException {
		return ResultsAggregated.getDbResultsHistoryAggregated(aUser, startPoint);
	}

	public List<DbResultsTestCase> getTestCaseHistory(DbUserData loggedUser, String aUser, String aPath) throws IllegalArgumentException {
		return ResultsDetailed.getTestCaseHistory(loggedUser, aUser, aPath);
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// RunCompare.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////

	public List<ResultsCompare> compareRuns(List<Integer> runsKeys) throws IllegalArgumentException {
		return RunsCompare.compareRuns(runsKeys);
	}
	public List<DbResultsSuite> getDbResultsSuite(Integer aKEY) throws IllegalArgumentException {
		return ResultsDetailed.getDbResultsSuite(aKEY);
	}

	public List<DbResultsTestCase> getTestCasePage(Integer aKEY) throws IllegalArgumentException {
		return ResultsDetailed.getTestCasePage(aKEY);
	}

	public PagingLoadResult<DbResultsTestCase> getTestCasePage(Integer aKEY, FilterPagingLoadConfig config) throws IllegalArgumentException {
		return ResultsDetailed.getTestCasePage(aKEY, config);
	}

	public String exportTestCaseHistory (String aType, Integer aRunKEY) {
		return Export.exportTestCaseHistory (aType, aRunKEY);
	}


	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// IO.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////


	public OsFileContent readFileFromServer(String filePath) throws IllegalArgumentException {
		return IO.readFileFromServer(filePath);
	}

	public OsFileContent getTestLogFromZipFile(String logFile) throws IllegalArgumentException {
		return IO.getTestLogFromZipFile(logFile);
	}
	public String writeFileToServer(String filePath, String contents) throws IllegalArgumentException {
		return IO.writeFileToServer(filePath, contents);
	}

	public String writeFileToServer(String filePath, String contents, String user) throws IllegalArgumentException {
		return IO.writeFileToServer(user, filePath, contents);
	}

}
