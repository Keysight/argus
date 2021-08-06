package com.argus.server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class Net extends RemoteServiceServlet {

	private static final Logger logger = LogManager.getLogger(Net.class.getName());

	public static String IpAddr() throws IllegalArgumentException {
		// still not perfect
		// at this moment it only return IPv4 address, it does not return IPv6, but should be fine to do it.
		// best will be to detect the default gateway and chose the ip address that is in the same network with the gateway. route -n
		try {
			Enumeration<NetworkInterface> netIntEnum = NetworkInterface.getNetworkInterfaces();
			String ipAddr = "";
			for (; netIntEnum.hasMoreElements();) {
				NetworkInterface netInt = netIntEnum.nextElement();
				//logger.debug(netInt.getIndex() + "INT:" + netInt.getName() + " L:" + netInt.isLoopback() + " P:" + netInt.isPointToPoint() + " U:" + netInt.isUp() + " V:" + netInt.isVirtual());		
				if ( netInt.isUp() && !netInt.isLoopback() && !netInt.getName().contains("docker")) {
					Enumeration<InetAddress> addrEnum = netInt.getInetAddresses();
					for (; addrEnum.hasMoreElements();) {
						InetAddress addr = addrEnum.nextElement();
						//logger.debug("IP:" + addr.getHostAddress() + " LL:" + addr.isLinkLocalAddress() + " L:" + addr.isLoopbackAddress() + " M:" + addr.isMulticastAddress());
						if (addr instanceof Inet4Address && !addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && !addr.isMulticastAddress()) {
							ipAddr += addr.getHostAddress();
						}
					}
				}
			}
			//logger.debug("RETURN: " + ipAddr);
			return ipAddr;
		} catch (Exception e) {
			logger.error("IpAddr", e);
			return "IpAddr error: " + Objects.toString(e);
		}
	}
}
