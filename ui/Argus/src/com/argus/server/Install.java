package com.argus.server;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.argus.shared.CFG;
import com.argus.shared.DbUserData;

public class Install extends RemoteServiceServlet  {

	private static final Logger logger = LogManager.getLogger(Install.class.getName());

	public static String installPyThar() throws IllegalArgumentException {
		String result = "";
		DbUserData demoUser = new DbUserData();
		demoUser.setUserName("Demo User");
		demoUser.setEmailAddress("demo123@keysight.com");
		demoUser.setAccountType("shared");
		//demoUser.setLinuxUser("demoargus");
		demoUser.setHomeFolder("/home/demoargus/argus");
		//result += UserLogin.savePytharUser(demoUser);
		//result += UserLogin.createLinuxUser(demoUser);

		// if all went good create this file to mark the installation
		result += IO.executeLinuxCommand("/usr/bin/touch "+CFG.INSTALLPATH+"/.isinstalled");
		
		return result;
//		String text;
//		String ret = new String();
//		text = IO.chmodOnFiles(CFG.APPROOT + "/install/install.sh");
//		ret.concat(text);
//		text = IO.executeLinuxCommand(CFG.APPROOT + "/install/install.sh");
//		ret.concat(text);
//		text = IO.executeLinuxCommand("/usr/bin/touch " + CFG.APPROOT + "/.isinstalled");
//		ret.concat(text);
//		IO.writeFileToServer("/usr/share/tomcat/logs/argus_install.log", ret);
//		return ret;
	}


	public static Boolean isInstalled() throws IllegalArgumentException {
		File lfile = new File(CFG.INSTALLPATH + "/.isinstalled");
		return lfile.exists();
	}

}
