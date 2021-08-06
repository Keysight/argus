package com.argus.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.argus.server.view.db.ResultsDetailed;
import com.argus.shared.CFG;
import com.argus.shared.DbResultsTestCase;

public class Export {

	private static final Logger logger = LogManager.getLogger(Install.class.getName());

	public static String exportTestCaseHistory (String aType, Integer aRunKEY) throws IllegalArgumentException {
		logger.debug("exportTestCaseHistory(String, Integer) - start " + aType + Objects.toString(aRunKEY)); //$NON-NLS-1$

		String fRunDate = "asdasdasdasdas";
		String fileName  = fRunDate.replaceAll("/", "-");
		fileName = fileName.replaceAll(" ", "-");
		fileName = fileName.replaceAll(":", "-");
		fileName = "TestCases-" + fileName+"." + aType;

		String link = "exportTestCaseHistoryError";
		switch(aType) { 
		case "xlsx": 
			link =  generateXLSX(fileName, aRunKEY);
			break; 
		case "csv": 
			link = generateCSV(fileName, aRunKEY);
			break; 
		case "zip": 
			link = generateZIP(fileName, aRunKEY);
			break; 
		default: 
			logger.debug("exportTestCaseHistory unknown type <" + fileName + ">");
		} 


		logger.debug("exportTestCaseHistory(String, Integer) - end"); //$NON-NLS-1$

		return link;
	}

	static String generateZIP(String aFileName, Integer aRunKEY) {
		logger.debug("generateZIP(String, Integer) - start"); //$NON-NLS-1$

		generateXLSX(FilenameUtils.getBaseName(aFileName) + ".xlsx", aRunKEY);
		generateCSV(FilenameUtils.getBaseName(aFileName) + ".csv", aRunKEY);
		List<String> filesToZ = new ArrayList<String>();
		filesToZ.add(CFG.APPROOT + "/downloads/xlsx/" + FilenameUtils.getBaseName(aFileName) + ".xlsx" );
		filesToZ.add(CFG.APPROOT + "/downloads/xlsx/" + FilenameUtils.getBaseName(aFileName) + ".csv");
		for (String logPath : getLogFilesForRunKEY(aRunKEY)) {
			if (filesToZ.contains(logPath)) {continue;}
			File lfile = new File(logPath);
			if (lfile.exists()) {
				filesToZ.add(logPath);
			}
		}
		String returnString = generateZIP(aFileName, filesToZ);

		logger.debug("generateZIP(String, Integer) - end"); //$NON-NLS-1$

		return returnString;
	}

	public static String generateZIP (String aFileName, List<String> aFilesToZ) {

		logger.debug("generateZIP(String, List<String>) - start"); //$NON-NLS-1$

		//
		try {
			FileOutputStream fos = new FileOutputStream(CFG.APPROOT + "/downloads/xlsx/"+ aFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			zos.setLevel(9); // max compression
			for (String fileToZ : aFilesToZ) {
				IO.addToZipFile(fileToZ, zos);
			}

			zos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			logger.error("generateZIP(String, List<String>)", e); //$NON-NLS-1$

			logger.error("generateZIP", e);
			return Objects.toString(e);
		} catch (IOException e) {
			logger.error("generateZIP(String, List<String>)", e); //$NON-NLS-1$

			logger.error("generateZIP", e);
			return Objects.toString(e);
		}
		String machineIP = Net.IpAddr();

		String returnString = "http://" + machineIP + ":8080/Argus/downloads/xlsx/" + aFileName;

		logger.debug("generateZIP(String, List<String>) - end"); //$NON-NLS-1$

		return returnString ;
	}

	private static List<String> getLogFilesForRunKEY(Integer aRunKEY) {

		logger.debug("getLogFilesForRunKEY(Integer) - start"); //$NON-NLS-1$


		Connection conn     = null;
		List <String> testLogs = new ArrayList<String>() ;
		try {
			Class.forName(CFG.DRIVER).newInstance();
			conn = DriverManager.getConnection(CFG.URL+CFG.DBNAME+CFG.FLAGS, CFG.LOGIN, CFG.PASS);
			String sql = "SELECT `test_log` FROM  `b_tests` WHERE `run_key` = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, aRunKEY);
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				testLogs.add(result.getString("test_log"));
			}
			pstmt.close();
			result.close();
		} catch (Exception e) {
			logger.error("getLogFilesForRunDate", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.debug(Objects.toString(e));
			}
		}


		logger.debug("getLogFilesForRunKEY(Integer) - end"); //$NON-NLS-1$

		return testLogs;
	}




	public static String generateCSV (String aFileName, Integer aRunKEY) {

		logger.debug("generateCSV(String, Integer) - start", Objects.toString(aRunKEY)); //$NON-NLS-1$


		List<DbResultsTestCase> aTestCasesList = ResultsDetailed.getTestCasePage(aRunKEY);
		ArrayList<ArrayList<String>> csvData = new ArrayList<ArrayList<String>>();
		//fos = new FileOutputStream(CFG.APPROOT + "/downloads/xlsx/"+ aFileName);
		Integer nrOfRows = aTestCasesList.size();
		ArrayList<String> csvLineHeadres = new ArrayList<String>();
		for (String cellName : aTestCasesList.get(0).getPropertyNames()) {
			csvLineHeadres.add(cellName);
		}
		csvData.add(csvLineHeadres);
		for (short rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {
			ArrayList<String> csvLine = new ArrayList<String>();
			DbResultsTestCase dataRow = aTestCasesList.get(rowIndex);
			for (String cellName : aTestCasesList.get(0).getPropertyNames()) {
				csvLine.add(dataRow.getProperty(cellName));
			}
			csvData.add(csvLine);
		}
		try {
			saveCSVFile(CFG.APPROOT + "/downloads/xlsx/"+ aFileName,csvData);
			String machineIP = Net.IpAddr();
			String returnString = "http://" + machineIP + ":8080/Argus/downloads/xlsx/" + aFileName;

			logger.debug("generateCSV(String, Integer) - end"); //$NON-NLS-1$

			return returnString;
		} catch (IOException e) {
			logger.error("generateCSV(String, Integer)", e); //$NON-NLS-1$

			logger.error("generateCSV", e);
			return Objects.toString(e);
		}
	}


	public static String generateXLSX (String aFileName, Integer aRunKEY) {

		logger.debug("generateXLSX(String, Integer) - start"); //$NON-NLS-1$

		List<DbResultsTestCase> aTestCasesList = ResultsDetailed.getTestCasePage(aRunKEY);
		
		Workbook wb = new XSSFWorkbook();
		FileOutputStream fos;
		try {
			File file = new File(CFG.APPROOT + "/downloads/xlsx/"+ aFileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			Sheet sh = wb.createSheet("TestCases");
			Integer nrOfRows = aTestCasesList.size();
			Row xlsxRow = sh.createRow(0);
			short cellIndex = 0;
			for (String cellName : aTestCasesList.get(0).getPropertyNames()) {
				Cell cell = xlsxRow.createCell(cellIndex++);
				cell.setCellValue(cellName);
			}
			for (short rowIndex = 0; rowIndex < nrOfRows; rowIndex++) {
				xlsxRow = sh.createRow(rowIndex+1);
				DbResultsTestCase dataRow = aTestCasesList.get(rowIndex);
				cellIndex = 0;
				for (String cellName : aTestCasesList.get(0).getPropertyNames()) {
					Cell cell = xlsxRow.createCell(cellIndex++);
					cell.setCellValue(dataRow.getProperty(cellName));
				}
			}
			wb.write(fos);
			fos.close();
			wb.close();
		} catch (IOException e) {
			logger.error("generateXLSX(String, Integer)", e); //$NON-NLS-1$
			logger.error("generateXLSX1", e);
			return Objects.toString(e);
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				logger.error("generateXLSX2", e);
			}
		}

		String machineIP = Net.IpAddr();
		String returnString = "http://" + machineIP + ":8080/Argus/downloads/xlsx/" + aFileName;

		logger.debug("generateXLSX(String, Integer) - end"); //$NON-NLS-1$

		return returnString ;
	}


	private static void saveCSVFile(String file,ArrayList<ArrayList<String>> csvData) throws FileNotFoundException, IOException {

		logger.debug("saveCSVFile(String, ArrayList<ArrayList<String>>) - start"); //$NON-NLS-1$


		FileWriter fw = null;
		BufferedWriter bw = null;
		ArrayList<String> line = null;
		StringBuffer buffer = null;
		String csvLineElement = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for(int i = 0; i < csvData.size(); i++) {
				buffer = new StringBuffer();
				line = csvData.get(i);

				for(int j = 0; j < line.size(); j++) {
					csvLineElement = line.get(j);
					buffer.append("\""+csvLineElement+"\"");
					buffer.append(",");
				}

				bw.write(Objects.toString(buffer).trim());
				if(i < (csvData.size() - 1)) {bw.newLine();}
			}
		}
		finally {
			if(bw != null) {
				bw.flush();
				bw.close();
			}
		}


		logger.debug("saveCSVFile(String, ArrayList<ArrayList<String>>) - end"); //$NON-NLS-1$

	}





}
