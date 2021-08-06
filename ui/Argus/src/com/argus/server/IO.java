package com.argus.server;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.argus.shared.OsFile;
import com.argus.shared.OsFileContent;
import com.argus.shared.OsFolder;


public class IO extends RemoteServiceServlet {

	private static final Logger logger = LogManager.getLogger(IO.class.getName());


	public static OsFileContent readFileFromServer(String aFilePath) {
		StringBuilder contents = new StringBuilder();
		try {
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			BufferedReader input =  new BufferedReader(new FileReader(aFilePath));
			try {
				String line = null; //not declared within while loop
				/*
				 * readLine is a bit quirky :
				 * it returns the content of a line MINUS the newline.
				 * it returns null only for the END of the stream.
				 * it returns an empty String if two newlines appear in a row.
				 */
				while (( line = input.readLine()) != null){
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			finally {
				//fr.close();
				input.close();
			}
		}
		catch (IOException e){
			logger.error("readFileFromServer", e);
			return new OsFileContent(Objects.toString(e), "log");
		}
		return new OsFileContent(Objects.toString(contents), getExtension(aFilePath));
	}

	public static String writeFileToServer(String aFilePath, String aContents) {
		return writeFileToServer(aFilePath, aContents, "root");
	}

	public static String writeFileToServer(String aFilePath, String aContents, String aUser) {

		File file 	= new File(aFilePath);
		executeLinuxCommand("/usr/bin/sudo -u " + aUser + " mkdir -p " + file.getParent());
		//use buffering

		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(aFilePath));
		} catch (IOException e) {
			logger.error("writeFileToServer1", e);
			return Objects.toString(e);
		}

		try {
			//FileWriter always assumes default encoding is OK!
			try {
				output.write( aContents );
			} catch (IOException e) {
				logger.error("writeFileToServer2", e);
				return Objects.toString(e);
			}
		}
		finally {
			try {
				output.close();
			} catch (IOException e) {
				logger.error("writeFileToServer3", e);
				return Objects.toString(e);
			}
		}

		chmodOnFiles(aFilePath);

		return "done";
	}

	public static final void writeFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	public static boolean createPath(File aPath) {
		try {
			if (!aPath.isDirectory()) {
			  boolean success = aPath.mkdirs();
			  if (success) {
			    logger.debug("Created path: " + aPath.getPath());
			  } else {
			    logger.debug("Could not create path: " + aPath.getPath());
			    return false;
			  }
			} else {
			  logger.debug("Path exists: " + aPath.getPath());
			}
			return true;
		} catch (Exception e) {
			logger.error("createPath", e);
			return false;
		}
	}
		
	public static String copyDirOnServer(String sourceDirPath, String destDirPath) throws IllegalArgumentException {
		try {
			File sourceDirFile = new File(sourceDirPath);
			File destDirFile = new File(destDirPath);
			
			if (!createPath(destDirFile)) {
				return "Could not create path";
			}
			
			FileUtils.copyDirectory(sourceDirFile, destDirFile);
			
		} catch (Exception e) {
			logger.error("copyDirOnServer", e);
			return Objects.toString(e);
		}
		return "OK";
	}
	
	public static String removeDirFromServer(String dirPath, String userHome) throws IllegalArgumentException {
		try {
			
			File dirFile = new File(dirPath);		
			FileUtils.deleteDirectory(dirFile);
			while (true) {
				String parentDir = dirFile.getParent();
				if ( parentDir.equals(userHome) ) {
					break;
				}
				File parentDirFile = new File(parentDir);
				if (parentDirFile.listFiles().length == 0) {
					FileUtils.deleteDirectory(parentDirFile);
				}
			}
			
		} catch (Exception e) {
			logger.error("removeDirFromServer", e);
			return Objects.toString(e);
		}
		return "OK";
	}
	
	public static String appendToFileOnServer(String aFilePath, String aContents) {

		String current_file_contents	= readFileFromServer(aFilePath).getContent();
		String contents_to_write		= current_file_contents + aContents;

		return writeFileToServer(aFilePath, contents_to_write );

	}

	public static String appendToFileOnServer(String aFilePath, String aContents, String user) {

		String current_file_contents	= readFileFromServer(aFilePath).getContent();
		String contents_to_write		= current_file_contents + aContents;

		return writeFileToServer(user, aFilePath, contents_to_write );

	}

	public static String findAndReplaceInFile(String filePath, String lookFor, String replacement) throws IllegalArgumentException {
		try {
			Path path = Paths.get(filePath);
			Charset charset = StandardCharsets.US_ASCII;
			String content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll(lookFor, replacement);
			Files.write(path, content.getBytes(charset));
		} catch (Exception e) {
			logger.error("findAndReplaceInFile", e);
			return Objects.toString(e);
		}
		return "OK";
	}

	public static String chmodOnFiles(String aPath) {
		String line;
		String text = new String();
		Runtime run = Runtime.getRuntime();
		try {
			Process pp=run.exec("/usr/bin/chmod -R 777 " + aPath);

			BufferedReader in1 =new BufferedReader(new InputStreamReader(pp.getErrorStream()));
			while ((line = in1.readLine()) != null) {
				text = text.concat("PyT> "+line+"\n");
			}

			BufferedReader in2 =new BufferedReader(new InputStreamReader(pp.getInputStream()));
			while ((line = in2.readLine()) != null) {
				text = text.concat("PyT> "+line+"\n");
			}
			pp.waitFor();
			return text;
		} catch (Exception e) {
			logger.error("chmodOnFiles", e);
			return Objects.toString(e);
		}
	}

	public static List<String> getTestCasesLists(String folder_location) {
		List<String> result = new ArrayList<String>();
		try {
			File folder = new File(folder_location);
			if (folder.exists()) {
				if (folder.length()>0) {
					File[] files_list = folder.listFiles();
					Arrays.sort(files_list);
					for (int i= 0; i<files_list.length; i++) {
						if (files_list[i].getName().endsWith(".list") || files_list[i].getName().endsWith(".csv")) {	result.add(files_list[i].getName());	}
					}
				} else {
					result.add("No Test Lists Found...");
				}
			} else {
				result.add("No TC_Lists folder Found...");
			}
		} catch (Exception e) {
			result.add(Objects.toString(e));
		}

		return result;
	}

	public static String executeLinuxCommand(String aCmd) {
		logger.debug(aCmd);
		String line;
		String text = new String();
		Runtime run = Runtime.getRuntime();
		try {
			Process pp=run.exec(aCmd);

			BufferedReader in1 =new BufferedReader(new InputStreamReader(pp.getErrorStream()));
			while ((line = in1.readLine()) != null) {
				text = text.concat("PyT> "+line+"\n");
			}

			BufferedReader in2 =new BufferedReader(new InputStreamReader(pp.getInputStream()));
			while ((line = in2.readLine()) != null) {
				text = text.concat("PyT> "+line+"\n");
			}
			pp.waitFor();
			logger.debug(text);
			return text;
		} catch (Exception e) {
			logger.error("executeLinuxCommand", e);
			logger.debug(text);
			return Objects.toString(e);
		}
	}

	public static void saveCSVFile(String file,ArrayList<ArrayList<String>> csvData) throws FileNotFoundException, IOException {
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
					buffer.append(csvLineElement);
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
	}

	private static int ID = 0;
	public static OsFolder SearchInFoldersFiltered(String path, String filter) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		String name = folder.getName();
		Integer filesCount = 0;
		OsFolder child = new OsFolder(ID++, name, path, filesCount);
		child.setChildren((List<OsFile>) new ArrayList<OsFile>());
		for (File sub : listOfFiles) {
			if (sub.isDirectory()) {
				if(!sub.getName().contains("utils")){ // filter out utils folders
					OsFolder directory = (OsFolder) SearchInFoldersFiltered(sub.getAbsolutePath(), filter);
					if (sub.getName().toLowerCase().contains(filter) || directory.getChildren().size() > 0) {
						child.addChild(directory);
					}
				}
			} else if (sub.isFile()){
				String file_name = sub.getName();
				if (isPyTharTestScript(file_name)) {
					file_name = file_name.substring(file_name.indexOf(".")+1, file_name.length());
					if (file_name.toLowerCase().contains(filter)) {
						child.addChild((OsFile) new OsFile(ID++, file_name, sub.getAbsolutePath()));
					}

				} else if (file_name.endsWith(".list")) {
					if (file_name.toLowerCase().contains(filter)) {
						child.addChild((OsFile) new OsFile(ID++, file_name, sub.getAbsolutePath()));
					}
				}
			}
		}
		return child;
	}

	// TODO : what is this used for ? we have now a async browser
	public static OsFolder SearchInFolders(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		String name = path.substring(path.lastIndexOf("/")+1, path.length());
		Integer filesCount = 0;
		OsFolder child = new OsFolder(ID++, name, path, 0);
		child.setChildren((List<OsFile>) new ArrayList<OsFile>());
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				OsFolder directory = (OsFolder) SearchInFolders(Objects.toString(listOfFiles[i]));
				child.addChild(directory);
				filesCount += directory.getFilesCount();
			} else if (listOfFiles[i].isFile()){
				String file_name = Objects.toString(listOfFiles[i]).substring(Objects.toString(listOfFiles[i]).lastIndexOf("/")+1, Objects.toString(listOfFiles[i]).length());
				if (isPyTharTestScript(file_name)) {
					file_name = file_name.substring(file_name.indexOf(".")+1, file_name.length());
					child.addChild((OsFile) new OsFile(ID++, file_name, listOfFiles[i].getAbsolutePath()));
					filesCount++;
				}
			}
		}
		child.setFilesCount(filesCount);
		child.setName(name + " [" + Objects.toString(filesCount)+ "]");
		return child;
	}

	public static boolean isPyTharTestScript (String scriptName) {
		if (scriptName.startsWith("test.") && (
				scriptName.endsWith(".tcl") || 
				scriptName.endsWith(".py") || 
				scriptName.endsWith(".pl") || 
				scriptName.endsWith(".rb") || 
				scriptName.endsWith(".prt"))
				) {
			return true;
		} else {
			return false;
		}
	}

	public static int getTestCount(File file) {
		File[] files = file.listFiles();
		int count = 0;
		for (File f : files) {
			if (f.isDirectory()) {
				count += getTestCount(f);
			} else if (f.isFile()){
				String file_name = f.getName();
				if (isPyTharTestScript(file_name)) {
					count++;
				}
			}
		}
		return count;
	}

	private static int TSID = 2;
	public static List<OsFile> getScriptsTreeDataAsync(OsFolder folder) {
		//		if (folder == null) {
		//			// TODO: find a way to display home folder and the common location.
		//			String startPath = "/regression/test-cases";
		//			folder = new OsFolder(TSID++, "[root]", startPath, getTestCount(new File(startPath)));
		//		}
		File f = new File(folder.getPath());
		List<OsFile> children = new ArrayList<OsFile>();

		// the file list is not returned sorted, code to sort the list of files
		File[] listOfFiles = f.listFiles();
		Arrays.sort(listOfFiles);

		for (File sub : listOfFiles) {
			if (sub.isDirectory()) {
				if(!sub.getName().contains("utils")){ // filter out utils folders
					Integer filesCount = getTestCount(sub);
					String folder_name = sub.getName() + " [" + Objects.toString(filesCount)+ "]";
					children.add(new OsFolder(TSID++, folder_name, sub.getAbsolutePath(), filesCount));
				}
			} else if (sub.isFile()){
				String file_name = sub.getName();
				if (isPyTharTestScript(file_name)) {
					file_name = file_name.substring(file_name.indexOf(".")+1, file_name.length());
					children.add(new OsFile(TSID++, file_name, sub.getAbsolutePath()));
				} else if (file_name.endsWith(".list")) {
					children.add(new OsFile(TSID++, file_name, sub.getAbsolutePath()));
				}
			}
		}
		return children;
	}

	public static OsFolder getScriptsTreeDataFiltered(List<OsFile> folders, String filter) {
		OsFolder root  = new OsFolder(ID++, "ABC", "ABC", 0);
		root.setChildren((List<OsFile>) new ArrayList<OsFile>());
		for (OsFile folder: folders) {
			String path = folder.getPath();

			OsFolder localRoot = new OsFolder(ID++, folder.getName(), path, 0);
			localRoot.setChildren((List<OsFile>) new ArrayList<OsFile>());

			File f = new File(path);

			File[] listOfFiles = f.listFiles();
			Arrays.sort(listOfFiles);

			for (File sub : listOfFiles) {
				if (sub.isDirectory()) {
					if(!sub.getName().contains("utils")){ // filter out utils folders
						OsFolder directory = (OsFolder) SearchInFoldersFiltered(sub.getAbsolutePath(), filter);
						if (sub.getName().toLowerCase().contains(filter) || directory.getChildren().size() > 0) {
							localRoot.addChild(directory);
						}
					}
				} else if (sub.isFile()){
					String file_name = sub.getName();
					if (isPyTharTestScript(file_name)) {
						file_name = file_name.substring(file_name.indexOf(".")+1, file_name.length());
						if (file_name.toLowerCase().contains(filter)) {
							localRoot.addChild((OsFile) new OsFile(ID++, file_name, sub.getAbsolutePath()));
						}

					} else if (file_name.endsWith(".list")) {
						if (file_name.toLowerCase().contains(filter)) {
							localRoot.addChild((OsFile) new OsFile(ID++, file_name, sub.getAbsolutePath()));
						}
					}
				}
			}

			root.addChild(localRoot);
		}
		return root;
	}

	public static OsFileContent getTestLogFromZip(String aZipFile){
		Random generator = new Random();
		Integer r = generator.nextInt();
		String directory = "/tmp/" + Objects.toString(r) + "/";
		String logFilePath = unzipMyZip(aZipFile, directory);
		OsFileContent logFileContent = readFileFromServer(directory+"/"+logFilePath);
		try {
			File f = new File(directory+"/"+logFilePath);
			f.delete();
			f = new File(directory);
			f.delete();
		} catch (Exception e) { 
			return new OsFileContent(Objects.toString(e), "txt");
		}
		return logFileContent;
	}

	private static String unzipMyZip(String zipFileName, String directoryToExtractTo) {
		Enumeration<?> entriesEnum;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(zipFileName);
			entriesEnum = zipFile.entries();

			File directory= new File(directoryToExtractTo);

			if(!directory.exists()) {
				new File(directoryToExtractTo).mkdir();
			}
			while (entriesEnum.hasMoreElements()) {
				try {
					ZipEntry entry = (ZipEntry) entriesEnum.nextElement();

					if (entry.isDirectory()) {
						// Getting rid of the whole dir structure
					} else {
						// extracting just the file
						int index = 0;
						String name = entry.getName();
						index = entry.getName().lastIndexOf("/");
						if (index > 0 && index != name.length()) {
							name = entry.getName().substring(index + 1);
						}
						writeFile(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(directoryToExtractTo + name)));
						zipFile.close();
						return name; //it will extract only the first file from the ZIP but we expect only one so it should be ok
					}
				} catch (Exception e) {
					zipFile.close();
					return Objects.toString(e);
				}
			}
			zipFile.close();
		} catch (IOException e) {
		    logger.error("unzipMyZip", e);
			return Objects.toString(e);
		}
		return "zip archive was empty";
	}

	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		logger.debug("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	private static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i+1).toLowerCase();

		if(ext == null)
			return "";
		return ext;
	}

	private static String getExtension(String filePath) {
		File f = new File(filePath);
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i+1).toLowerCase();

		if(ext == null)
			return "";
		return ext;
	}

	static OsFileContent getTestLogFromZipFile(String testLog) {
		File zipFile = new File(testLog);
		if (zipFile.exists()) {
			if (testLog.endsWith(".zip")) {
				return IO.getTestLogFromZip(testLog);
			} else {
				// TODO - if the file exists but is not a zip file, we should have a better code for this
				return new OsFileContent(testLog, "txt");
			}
		} else {
			return new OsFileContent(testLog, "txt");
		}
	}

}
