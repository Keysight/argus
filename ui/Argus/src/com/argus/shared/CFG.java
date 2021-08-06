package com.argus.shared;


// EDIT \WEB-INF\CFG.properties

public class CFG {
	public static String VERSION	  = "0.0.0.25";
	public static String IP           = "127.0.0.1"; // EDIT \WEB-INF\CFG.properties
	public static String URL 	      = "jdbc:mysql://"+ IP +":3306/";
	public static String DBNAME       = "ARGUS";
	public static String DRIVER       = "com.mysql.cj.jdbc.Driver";
	public static String LOGIN        = "root"; 
	public static String PASS         = "wrinkle#B52"; // EDIT \WEB-INF\CFG.properties
	public static String FLAGS       = "?allowPublicKeyRetrieval=true&useSSL=false";
		
	// PATHS
	public static String FILEROOT = "/regression/";
	public static String INSTALLPATH = "/opt/argus/";
	public static String APPROOT = "/usr/share/tomcat8/webapps/Argus"; //getServletContext()
	
}
