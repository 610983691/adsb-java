package com.coulee.aicw.foundations.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description: IP操作工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class IPHelper {

	private static Logger logger = LoggerFactory.getLogger(IPHelper.class);

	private static final String LOCALHOST = "127.0.0.1";

	private static final String ANYHOST = "0.0.0.0";

	private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

	private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

	private static volatile InetAddress LOCAL_ADDRESS = null;
	/**
	 * IPv4/IPv6正则表达式
	 */
	private static String REGEX_HTTP_URL = "(^((ht|f)tps?)\\:\\/\\/([^/:]+)(:\\d*)?([^# ]*)$)|(^((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))$)|(^([3-9]\\d{0,1}|1\\d{0,2}|2\\d{0,1}|2[0-4]\\d|25[0-5])\\.([3-9]\\d{0,1}|1\\d{0,2}|2\\d{0,1}|2[0-4]\\d|25[0-5]|0)\\.([3-9]\\d{0,1}|1\\d{0,2}|2\\d{0,1}|2[0-4]\\d|25[0-5]|0)\\.([3-9]\\d{0,1}|1\\d{0,2}|2\\d{0,1}|2[0-4]\\d|25[0-5]|0)$)";

	private static final int RND_PORT_START = 30000;

	private static final int RND_PORT_RANGE = 10000;

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private static boolean isLocalHost(String host) {
		return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0")
				|| host.equals("0:0:0:0:0:0:0:1") || (LOCAL_IP_PATTERN.matcher(host).matches());
	}

	private static boolean isValidAddress(InetAddress address) {
		if (address == null || address.isLoopbackAddress())
			return false;
		String name = address.getHostAddress();
		return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
	}

	private static InetAddress getLocalAddress0() {
		InetAddress localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost();
			if (isValidAddress(localAddress)) {
				return localAddress;
			}
		} catch (Throwable e) {
			logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
		}
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces != null) {
				while (interfaces.hasMoreElements()) {
					try {
						NetworkInterface network = interfaces.nextElement();
						Enumeration<InetAddress> addresses = network.getInetAddresses();
						if (addresses != null) {
							while (addresses.hasMoreElements()) {
								try {
									InetAddress address = addresses.nextElement();
									if (isValidAddress(address)) {
										return address;
									}
								} catch (Throwable e) {
									logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
								}
							}
						}
					} catch (Throwable e) {
						logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
					}
				}
			}
		} catch (Throwable e) {
			logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
		}
		logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
		return localAddress;
	}

	private static InetAddress getLocalAddress() {
		if (LOCAL_ADDRESS != null)
			return LOCAL_ADDRESS;
		InetAddress localAddress = getLocalAddress0();
		LOCAL_ADDRESS = localAddress;
		return localAddress;
	}

	private static String getLocalHost() {
		InetAddress address = getLocalAddress();
		return address == null ? LOCALHOST : address.getHostAddress();
	}

	/**
	 * Definition:获取本机IP地址
	 * 
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年12月1日
	 */
	public static String getLocalIp() {
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
			if (isLocalHost(host)) {
				host = getLocalHost();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return host;
	}

	private static int getRandomPort() {
		return RND_PORT_START + RANDOM.nextInt(RND_PORT_RANGE);
	}

	/**
	 * Definition:获得可用端口
	 * 
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年12月22日
	 */
	public static int getAvailablePort() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket();
			ss.bind(null);
			return ss.getLocalPort();
		} catch (IOException e) {
			return getRandomPort();
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Definition:获取请求IP地址
	 * 
	 * @param request
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年2月1日
	 */
	public static String getRequestIp(HttpServletRequest request) {
		if (request == null) {
			return "request is null ";
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (isLocalHost(ip))
			return LOCALHOST;
		return ip;
	}

	public static void main(String[] args) {
		// boolean isGo = IPHelper.ping("192.168.1.200", 3, 0);
		// System.out.println(isGo);
		// String ss = "Juniper JUNOS路由器";
		// System.out.println(ss.substring(0, 15));
		// System.out.println(ss.length());
		// System.out.println(ss.getBytes().length);
		String ipinfo = "10.213.47.1";
		String mask = "24";
		String netMask = IPHelper.getNetMask(mask);
		String lowIp = IPHelper.getLowAddr(ipinfo, netMask);
		String highIp = IPHelper.getHighAddr(ipinfo, netMask);
		System.out.println("netMask--" + netMask);
		System.out.println("lowIp--" + lowIp);
		System.out.println("highIp--" + highIp);
	}

	/**
	 * Definition:ping一个地址是否网络可达<br>
	 * 目前兼容windows和linux系统
	 * 
	 * @param ip
	 *            IP地址或域名
	 * @param count
	 *            要ping的次数
	 * @param timeout
	 *            超时时间(s)
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年6月3日
	 */
	public static boolean ping(String ip, int count, int timeout) {
		String os = System.getProperties().getProperty("os.name");
		StringBuffer cmdSb = new StringBuffer("ping ");
		if (os.toLowerCase().contains("windows")) {
			cmdSb.append(ip).append(" -n ").append(count).append(" -w ").append(timeout);
		} else {
			cmdSb.append(" -c ").append(count).append(" -w ").append(timeout).append(" ").append(ip);
		}
		logger.info("current os is " + os + ", ping cmd : " + cmdSb.toString());
		BufferedReader in = null;
		Runtime r = Runtime.getRuntime();
		try {
			Process p = r.exec(cmdSb.toString());
			if (p == null)
				return false;
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int okCount = 0;
			String line = null;
			while ((line = in.readLine()) != null)
				okCount += checkPingResult(line);
			return okCount == count;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 增加是否打印日志
	 * 
	 * @param ip
	 * @param count
	 * @param timeout
	 * @param isLog
	 * @return
	 */
	public static boolean ping(String ip, int count, int timeout, boolean isLog) {
		String os = System.getProperties().getProperty("os.name");
		StringBuffer cmdSb = new StringBuffer("ping ");
		if (os.toLowerCase().contains("windows")) {
			cmdSb.append(ip).append(" -n ").append(count).append(" -w ").append(timeout);
		} else {
			cmdSb.append(" -c ").append(count).append(" -w ").append(timeout).append(" ").append(ip);
		}
		if (isLog) {
			logger.info("current os is " + os + ", ping cmd : " + cmdSb.toString());
		}
		BufferedReader in = null;
		Runtime r = Runtime.getRuntime();
		try {
			Process p = r.exec(cmdSb.toString());
			if (p == null)
				return false;
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			int okCount = 0;
			String line = null;
			while ((line = in.readLine()) != null)
				okCount += checkPingResult(line, isLog);
			return okCount == count;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static int checkPingResult(String line, boolean isLog) {
		if (isLog) {
			logger.info("ping result : " + line);
		}

		boolean matched = RegexTools.isMatched(line, "(?i)([\\d\\.]+ms\\s+ttl=\\d+)|(ttl=\\d+\\s+.+=[\\d\\.]+\\s*ms)");
		return matched ? 1 : 0;
	}

	private static int checkPingResult(String line) {
		logger.info("ping result : " + line);
		boolean matched = RegexTools.isMatched(line, "(?i)([\\d\\.]+ms\\s+ttl=\\d+)|(ttl=\\d+\\s+.+=[\\d\\.]+\\s*ms)");
		return matched ? 1 : 0;
	}

	/**
	 * Definition:解析IPv4区间内所有的IP地址
	 * 
	 * @param startIp
	 *            起始IP
	 * @param endIp
	 *            结束IP
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年6月3日
	 */
	public static List<String> parseIpv4Range(String startIp, String endIp) {
		List<String> list = new ArrayList<String>();
		int start = ip2int(startIp);
		int end = ip2int(endIp);
		for (int i = 0; i <= (end - start); i++) {
			list.add(int2ip(start + i));
		}
		return list;
	}

	/**
	 * Definition:将IPv4转为网络字节序
	 * 
	 * @param ip
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年6月3日
	 */
	public static int ip2int(String ip) {
		if (ip == null || "".equals(ip) || !ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))
			return 0;
		try {
			byte[] ips = InetAddress.getByName(ip).getAddress();
			return bytesToInt(ips);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static int bytesToInt(byte[] bytes) {
		int a = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			byte b = bytes[i];
			a += (b & 0xFF) << (8 * (3 - i));
		}
		return a;
	}

	/**
	 * Definition:将网络字节序转换为IP地址
	 * 
	 * @param ip
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年6月3日
	 */
	public static String int2ip(int ip) {
		byte[] bytes = intToBytes(ip);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(bytes[i] & 0xFF);
			if (i < 3) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	private static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	/**
	 * Definition:验证IP区间是否合法，起始IP是否小于结束IP
	 * 
	 * @param startIp
	 *            起始IP
	 * @param endIp
	 *            结束IP
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年6月3日
	 */
	public static boolean validateIpRange(String startIp, String endIp) {
		int start = ip2int(startIp);
		int end = ip2int(endIp);
		return start < end;
	}

	/**
	 * Definition:判断一个IP是否在起始IP和结束IP之间
	 * 
	 * @param startIp
	 * @param endIp
	 * @param validateIp
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016年6月23日
	 */
	public static boolean validateIpInRange(String startIp, String endIp, String validateIp) {
		int start = ip2int(startIp);
		int end = ip2int(endIp);
		int middle = ip2int(validateIp);
		return start < middle && middle < end;
	}

	public static long getIp2long(String ip) {

		ip = ip.trim();

		String[] ips = ip.split("\\.");

		long ip2long = 0L;

		for (int i = 0; i < 4; ++i) {

			ip2long = ip2long << 8 | Integer.parseInt(ips[i]);

		}

		return ip2long;

	}

	public static long getIp2long2(String ip) {

		ip = ip.trim();

		String[] ips = ip.split("\\.");

		long ip1 = Integer.parseInt(ips[0]);

		long ip2 = Integer.parseInt(ips[1]);

		long ip3 = Integer.parseInt(ips[2]);

		long ip4 = Integer.parseInt(ips[3]);

		long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;

		return ip2long;

	}

	private static final String splitIp1 = ",";
	private static final String splitIp2 = "-";
	/*
	 * 掩码结构
	 */
	private static final String splitIp3 = "/";

	/**
	 * add zyj 2017-5-17
	 * 
	 * @param ipStr
	 *            ipStr ip1,ip2,ip3,...
	 * @param split
	 *            分隔符
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> getIpListSplit(String ipStr, String split) {
		if (BaseTools.isNull(ipStr)) {
			return null;
		}
		List<String> ipList = new ArrayList<String>();
		/*
		 * 逗号分隔多个ip
		 */
		if (splitIp1.equals(split)) {

			if (ipStr.indexOf(split) >= 0) {

			} else {
				ipList.add(ipStr);
				return ipList;
			}
			String[] ipMul = ipStr.split(split);
			Map ipMap = new HashMap();
			for (String temp : ipMul) {
				if (BaseTools.isNull(temp)) {
					continue;
				}
				if (ipMap.containsKey(temp)) {
					continue;
				}
				ipMap.put(temp, null);
				ipList.add(temp);
			}

		}
		/*
		 * ip段 从起始到截止
		 */
		else if (splitIp2.equals(split)) {
			int mulInt = ipStr.indexOf(split);
			if (mulInt > 0) {
				String startIP = ipStr.substring(0, mulInt);
				ipList.add(startIP);
				int count = BaseTools.getInt(ipStr.substring(mulInt + 1, ipStr.length()));
				int startInt = IPHelper.ip2int(startIP);
				String tempIP = null;
				for (int i = 1; i < count; i++) {
					tempIP = IPHelper.int2ip(startInt + i);
					ipList.add(tempIP);
				}
			}
		}

		/*
		 * 掩码结构
		 */
		else if (splitIp3.equals(split)) {
			int mulInt = ipStr.indexOf(split);
			if (mulInt > 0) {
				String startIP = ipStr.substring(0, mulInt);

				String mask = ipStr.substring(mulInt + 1, ipStr.length());
				ipList = getIPV4ListByMask(startIP, mask);
			}
		} else {
			ipList.add(ipStr);
		}
		return ipList;
	}

	/**
	 * 
	 * @param ipStr
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> getIpList(String ipStr) {
		if (BaseTools.isNull(ipStr)) {
			return null;
		}
		List<String> ipList = new ArrayList<String>();
		/*
		 * 逗号分隔多个ip
		 */
		if (ipStr.indexOf(splitIp1) >= 0) {

			if (ipStr.indexOf(splitIp1) >= 0) {

			} else {
				ipList.add(ipStr);
				return ipList;
			}
			String[] ipMul = ipStr.split(splitIp1);
			Map ipMap = new HashMap();
			for (String temp : ipMul) {
				if (BaseTools.isNull(temp)) {
					continue;
				}
				if (ipMap.containsKey(temp)) {
					continue;
				}
				ipMap.put(temp, null);
				ipList.add(temp);
			}

		}
		/*
		 * ip段 从起始到截止
		 */
		else if (ipStr.indexOf(splitIp2) >= 0) {
			int mulInt = ipStr.indexOf(splitIp2);
			if (mulInt > 0) {
				String startIP = ipStr.substring(0, mulInt);
				ipList.add(startIP);
				int count = BaseTools.getInt(ipStr.substring(mulInt + 1, ipStr.length()));
				int startInt = IPHelper.ip2int(startIP);
				String tempIP = null;
				for (int i = 1; i < count; i++) {
					tempIP = IPHelper.int2ip(startInt + i);
					ipList.add(tempIP);
				}
			}
		}

		/*
		 * 掩码结构
		 */
		else if (ipStr.indexOf(splitIp3) >= 0) {
			int mulInt = ipStr.indexOf(splitIp3);
			if (mulInt > 0) {
				String startIP = ipStr.substring(0, mulInt);

				String mask = ipStr.substring(mulInt + 1, ipStr.length());
				ipList = getIPV4ListByMask(startIP, mask);
			}
		} else {
			ipList.add(ipStr);
		}
		return ipList;
	}

	/**
	 * add zyj 2017-5-17 根据IP起始和截止范围 获取IP集合
	 * 
	 * @param startIP
	 * @param endIP
	 * @return
	 */
	public static List<String> getIPV4ListByRange(String startIP, String endIP) {
		List<String> ipList = new ArrayList<String>();
		if (BaseTools.isNull(startIP)) {
			return null;
		}
		if (BaseTools.isNull(endIP)) {
			ipList.add(startIP);
			return ipList;
		}
		int startIndex = ip2int(startIP);
		int endIndex = ip2int(endIP);
		// System.out.println("ipstart --" + ipstart);
		// System.out.println(ipend);
		// System.out.println(ipend-ipstart+1);
		// String[] result = new String[ipend - ipstart + 1];
		int length = endIndex - startIndex + 1;
		if (length < 0) {
			return ipList;
		}
		for (int i = 0; i < length; i++) {
			String ip = int2ip(startIndex + i);
			// System.out.println("i --> " + ip);
			ipList.add(ip);
			String[] ipArr = ip.split("\\.");
			if ("0".equals(ipArr[3]) || "255".equals(ipArr[3])) {
				continue;
			}
		}
		return ipList;
	}

	/**
	 * 根据ip和掩码 获取IP集合
	 * 
	 * @param ipInfo
	 * @param mask
	 * @return
	 */
	public static List<String> getIPV4ListByMask(String ipInfo, String mask) {
		List<String> ipList = new ArrayList<String>();
		if (BaseTools.isNull(mask)) {
			logger.error("mask is null ");
			return ipList;
		}
		if (BaseTools.isNull(ipInfo)) {
			logger.error("ipInfo is null ");
			return ipList;
		}
		String netMask = IPHelper.getNetMask(mask);
		if (BaseTools.isNull(netMask)) {
			logger.error("netMask is null ");
			return ipList;
		}
		String startIP = IPHelper.getLowAddr(ipInfo, netMask);
		String endIP = IPHelper.getHighAddr(ipInfo, netMask);
		ipList = IPHelper.getIPV4ListByRange(startIP, endIP);
		return ipList;
	}
	/**
	 * 根据IP获取地理位置
	 * 
	 * @param ip
	 * @return
	 */
	// public static GeoIPModel getGeoIPMdl(String ip) {
	// return GeoIPTools.getGeoIPMdl(ip);
	// }

	/**
	 * 根据掩码位数获取掩码
	 */
	private static String getNetMask(String mask) {
		int inetMask = Integer.parseInt(mask);
		if (inetMask > 32) {
			return null;
		}
		// 子网掩码为1占了几个字节
		int num1 = inetMask / 8;
		// 子网掩码的补位位数
		int num2 = inetMask % 8;
		int array[] = new int[4];
		for (int i = 0; i < num1; i++) {
			array[i] = 255;
		}
		for (int i = num1; i < 4; i++) {
			array[i] = 0;
		}
		for (int i = 0; i < num2; num2--) {
			array[num1] += Math.pow(2, 8 - num2);
		}
		String netMask = array[0] + "." + array[1] + "." + array[2] + "." + array[3];
		return netMask;
	}

	/**
	 * 根据ip地址和掩码获取起始IP
	 * 
	 * @param ipinfo
	 * @param netMask
	 * @return
	 */
	private static String getLowAddr(String ipinfo, String netMask) {
		String lowAddr = "";
		int ipArray[] = new int[4];
		int netMaskArray[] = new int[4];
		if (BaseTools.isNull(netMask) || 4 != ipinfo.split("\\.").length || "" == netMask) {
			return null;
		}
		for (int i = 0; i < 4; i++) {
			try {
				ipArray[i] = Integer.parseInt(ipinfo.split("\\.")[i]);
			} catch (NumberFormatException e) {
				String ip = ipinfo.replaceAll("\n", "");
				ipArray[i] = Integer.parseInt(ip.split("\\.")[i]);
			}
			netMaskArray[i] = Integer.parseInt(netMask.split("\\.")[i]);
			if (ipArray[i] > 255 || ipArray[i] < 0 || netMaskArray[i] > 255 || netMaskArray[i] < 0) {
				return null;
			}
			ipArray[i] = ipArray[i] & netMaskArray[i];
		}
		// 构造最小地址
		for (int i = 0; i < 4; i++) {
			if (i == 3) {
				ipArray[i] = ipArray[i] + 1;
			}
			if ("" == lowAddr) {
				lowAddr += ipArray[i];
			} else {
				lowAddr += "." + ipArray[i];
			}
		}
		return lowAddr;
	}

	/**
	 * 根据ip地址和掩码获取终止IP
	 * 
	 * @param ipinfo
	 * @param netMask
	 * @return
	 */
	private static String getHighAddr(String ipinfo, String netMask) {
		if (BaseTools.isNull(netMask)) {
			logger.error("netMask is null ");
			return null;
		}
		String lowAddr = getLowAddr(ipinfo, netMask);
		if (BaseTools.isNull(lowAddr)) {
			logger.error("lowAddr is null ipinfo--" + ipinfo + ",netMask--" + netMask);
			return null;
		}

		int hostNumber = getHostNumber(netMask);
		if (hostNumber == 0) {
			logger.error("lowAddr is null ipinfo--" + ipinfo + ",netMask--" + netMask + ",hostNumber--" + hostNumber);
			return null;
		}
		int lowAddrArray[] = new int[4];
		for (int i = 0; i < 4; i++) {
			lowAddrArray[i] = Integer.parseInt(lowAddr.split("\\.")[i]);
			if (i == 3) {
				lowAddrArray[i] = lowAddrArray[i] - 1;
			}
		}
		lowAddrArray[3] = lowAddrArray[3] + (hostNumber - 1);
		if (lowAddrArray[3] > 255) {
			int k = lowAddrArray[3] / 256;
			lowAddrArray[3] = lowAddrArray[3] % 256;
			lowAddrArray[2] = lowAddrArray[2] + k;
		}
		if (lowAddrArray[2] > 255) {
			int j = lowAddrArray[2] / 256;
			lowAddrArray[2] = lowAddrArray[2] % 256;
			lowAddrArray[1] = lowAddrArray[1] + j;
			if (lowAddrArray[1] > 255) {
				int k = lowAddrArray[1] / 256;
				lowAddrArray[1] = lowAddrArray[1] % 256;
				lowAddrArray[0] = lowAddrArray[0] + k;
			}
		}
		String highAddr = "";
		for (int i = 0; i < 4; i++) {
			if (i == 3) {
				lowAddrArray[i] = lowAddrArray[i] - 1;
			}
			if ("" == highAddr) {
				highAddr = lowAddrArray[i] + "";
			} else {
				highAddr += "." + lowAddrArray[i];
			}
		}
		return highAddr;
	}

	/**
	 * 实际可用ip数量
	 * 
	 * @param netMask
	 * @return
	 */
	private static int getHostNumber(String netMask) {
		if (BaseTools.isNull(netMask)) {
			logger.error("netMask is null ");
			return 0;
		}
		int hostNumber = 0;
		int netMaskArray[] = new int[4];
		for (int i = 0; i < 4; i++) {
			netMaskArray[i] = Integer.parseInt(netMask.split("\\.")[i]);
			if (netMaskArray[i] < 255) {
				hostNumber = (int) (Math.pow(256, 3 - i) * (256 - netMaskArray[i]));
				break;
			}
		}
		return hostNumber;
	}

	// public static String int2ip(int l) {
	// // System.out.println("l--> " +l);
	// int a = l / (256 * 256 * 256);
	// int b = (l - a * 256 * 256 * 256) / (256 * 256);
	// int c = (l - a * 256 * 256 * 256 - b * 256 * 256) / 256;
	// int d = (l - a * 256 * 256 * 256 - b * 256 * 256 - c * 256);
	// // return l/(256*256*256)+"."+l/(256*256)+"."+l/256+"."+l%256;
	// return a + "." + b + "." + c + "." + d;
	// }
	//
	// public static int ip2int(String ip) {
	// String[] sa = ip.split("\\.");
	// return Integer.parseInt(sa[0]) * (256 * 256 * 256) + Integer.parseInt(sa[1])
	// * (256 * 256)
	// + Integer.parseInt(sa[2]) * 256 + Integer.parseInt(sa[3]);
	// }
	/**
	 * 根据url 截取字符串信息 获取ip
	 * 
	 * @param url
	 * @return
	 */
	public static String getIPByUrl(String url) {
		String ip = null;
		String re = "([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3})";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(re);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		// 若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
		if (matcher.find()) {
			ip = matcher.group();
			return ip;
		}
		return ip;
	}

	/**
	 * 根据url 截取字符串信息 获取port
	 * 
	 * @param url
	 * @return
	 */
	public static int getPortByUrl(String url) {
		int port = 80;
		String re = "([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}):([0-9]{1,100})";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(re);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		// 若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
		if (matcher.find()) {
			String portStr = matcher.group(2);
			if (BaseTools.isNull(portStr)) {
				return port;
			}
			return BaseTools.getIntDef0(portStr);
		}
		return port;
	}

	/**
	 * Definition:校验是否为ipv4
	 * 
	 * @Author: oblivion
	 * @Created date:
	 */
	public static boolean isIpv4(String ipv4) {

		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

		return RegexTools.isMatched(ipv4, regex);
	}

	/**
	 * Definition:校验是否为ipv6
	 * 
	 * @Author: oblivion
	 * @Created date:
	 */
	public static boolean isIpv6(String ipv6) {
		String regex = "^(\\s*" + "((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
				+ "(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%[0-9]*){0,1}"
				+ ")$";

		return RegexTools.isMatched(ipv6, regex);
	}

	public static Pattern httpPattern = Pattern.compile(REGEX_HTTP_URL);

	/**
	 * <b>Description: </b><br>
	 * 校验http url ipv4/ipv6
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isHttpUrl(String url) {
		if (BaseTools.isNull(url)) {
			return false;
		}
		Matcher m = httpPattern.matcher(url);
		if (m.find()) {
			return true;
		} else {
			return false;
		}

	}
}
