package com.argus.client;

import java.util.List;

import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbResultsSuite;
import com.argus.shared.DbUserData;
import com.argus.shared.ResultsCompare;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.argus.shared.OsFileContent;

/**
 * The async counterpart of <code>ArgusService</code>.
 */
public interface ArgusServiceAsync {
	
	void loadprops(AsyncCallback<String> asyncCallback);
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// UserLogin.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////

	void getUsers(AsyncCallback<List<DbUserData>> asyncCallback);
	void updateUserData(DbUserData loggedInUser, AsyncCallback<DbUserData> asyncCallback);
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// ViewResultsAggregated.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
		
	void getDbResultsAggregated(DbUserData aUser, FilterPagingLoadConfig config, AsyncCallback<PagingLoadResult<DbResultsAggregated>> asyncCallback);
	void getDbResultsAggregated(DbUserData aUser, String aPath, AsyncCallback<List<DbResultsAggregated>> asyncCallback);
	void getDbResultsHistoryAggregated(DbUserData aUser, Integer startPoint, AsyncCallback<List<DbResultsAggregated>> asyncCallback);
	
	void getDatasetSize(DbUserData aUser, AsyncCallback<Integer> asyncCallback);
	void getTestLog(Integer KEY, AsyncCallback<String> callback);
	void getTestLogDownloadPath(Integer KEY, AsyncCallback<String> callback);
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// RunCompareContentPanel.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	void compareRuns(List<Integer> runsKeys, AsyncCallback<List<ResultsCompare>> asyncCallback);

	void getTestCaseHistory(DbUserData loggedUser, String aUser, String APath, AsyncCallback<List<DbResultsTestCase>> callback);
	void getTestCasePage(Integer aKEY, AsyncCallback<List<DbResultsTestCase>> callback);
	void getTestCasePage(Integer aKEY, FilterPagingLoadConfig config, AsyncCallback<PagingLoadResult<DbResultsTestCase>> callback);
	void getDbResultsSuite(Integer aKEY, AsyncCallback<List<DbResultsSuite>> callback);
	void exportTestCaseHistory(String aType, Integer aRunKEY, AsyncCallback<String> callback);

	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// IO.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	void readFileFromServer (String filePath, AsyncCallback<OsFileContent> callback);
	void writeFileToServer (String filePath, String contents, AsyncCallback<String> callback);
	void writeFileToServer (String filePath, String contents, String user, AsyncCallback<String> callback);
	void getTestLogFromZipFile(String logFile, AsyncCallback<OsFileContent> callback);
	
}
