package com.argus.client;

import java.util.List;

import com.argus.shared.DbResultsAggregated;
import com.argus.shared.DbResultsTestCase;
import com.argus.shared.DbResultsSuite;
import com.argus.shared.DbUserData;
import com.argus.shared.ResultsCompare;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.argus.shared.OsFileContent;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("Argus")
public interface ArgusService extends RemoteService {
	
	String loadprops() throws IllegalArgumentException;
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// UserLogin.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	List<DbUserData> getUsers() throws IllegalArgumentException;
	DbUserData updateUserData(DbUserData aUser) throws IllegalArgumentException;
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// ViewResultsAggregated.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	
	PagingLoadResult<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser, FilterPagingLoadConfig config);
	List<DbResultsAggregated> getDbResultsAggregated(DbUserData aUser, String aPath) throws IllegalArgumentException;
	List<DbResultsAggregated> getDbResultsHistoryAggregated(DbUserData aUser, Integer startPoint) throws IllegalArgumentException;
	Integer getDatasetSize(DbUserData aUser) throws IllegalArgumentException;
	String getTestLog(Integer KEY) throws IllegalArgumentException;
	String getTestLogDownloadPath(Integer KEY) throws IllegalArgumentException;
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// RunCompareContentPanel.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	List<ResultsCompare> compareRuns(List<Integer> runsKeys) throws IllegalArgumentException;
	List<DbResultsTestCase> getTestCaseHistory(DbUserData loggedUser, String aUser, String aPath) throws IllegalArgumentException;
	PagingLoadResult<DbResultsTestCase> getTestCasePage(Integer aKEY, FilterPagingLoadConfig config) throws IllegalArgumentException;
	List<DbResultsTestCase> getTestCasePage(Integer aKEY) throws IllegalArgumentException;
	List<DbResultsSuite> getDbResultsSuite(Integer aKEY) throws IllegalArgumentException;
	String exportTestCaseHistory(String aType, Integer aRunKEY) throws IllegalArgumentException;

	/////////////////////////////////////////////////////////////////////////////////////////
	////
	//// IO.java
	////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	OsFileContent readFileFromServer (String filePath) throws IllegalArgumentException;
	String writeFileToServer (String filePath, String contents) throws IllegalArgumentException;
	String writeFileToServer (String filePath, String contents, String user) throws IllegalArgumentException;
	OsFileContent getTestLogFromZipFile(String logFile) throws IllegalArgumentException;

	


}
