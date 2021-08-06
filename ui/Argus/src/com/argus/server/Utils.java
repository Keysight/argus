package com.argus.server;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.concurrent.TimeUnit;
import com.argus.server.Utils;

public class Utils extends RemoteServiceServlet  {

	private static final Logger logger = LogManager.getLogger(Utils.class.getName());

	public static String secondsToTime (int sec) {
		long seconds = sec % 60;
		long minutes = TimeUnit.SECONDS.toMinutes(sec) % 60;
		long hours   = TimeUnit.SECONDS.toHours(sec) % 24;
		long days    = TimeUnit.SECONDS.toDays(sec);

		if (days == 0) {
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			return String.format("%d-%02d:%02d:%02d", days, hours, minutes, seconds);
		}
	}


	public static String getCommonLocation (String aLocation, String aPath) {
		Integer lastSlashIndex = aLocation.lastIndexOf("/");
		String location = aLocation.substring(0, lastSlashIndex);
		if (!aPath.contains(location)) {
			location = getCommonLocation(location, aPath);
		}
		return location;
	}


}