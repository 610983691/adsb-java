package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;

/**
 * Description:连接参数
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-11-28
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class OpenArg implements Serializable {

	private static final long serialVersionUID = -8638382008269285126L;
	
	public OpenArg() {
	}
	
	/**
	 * Description : 实例化连接参数
	 * @param ip IP
	 * @param port 端口
	 * @param protocol 协议
	 */
	public OpenArg(String ip, int port, String protocol) {
		this.ip = ip;
		this.port = port;
		this.protocol = protocol;
	}

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 协议端口
	 */
	private int port;

	/**
	 * 协议类型
	 */
	private String protocol;
	
	/**
	 * 连接超时时间，单位：秒
	 */
	private int connectTimeout;
	
	/**
	 * 读取回显超时时间，单位：秒
	 */
	private int echoTimeout;
	
	/**
	 * 编码
	 */
	private String encode;
	
	/**
	 * Windows传输文件协议类型<br>
	 * 目前支持RDP、SAMBA
	 */
	private String windowsTransferMode;
	
	/**
	 * Windows传输文件协议端口<br>
	 * RDP默认为3389
	 * SAMBA无端口
	 */
	private int windowsTransferPort;
	
	/**
	 * Definition:设置Windows传输文件协议信息
	 * @param mode 协议
	 * @param port 端口
	 * @Author: LanChao
	 * @Created date: 2017-1-18
	 */
	public void setWindowsTransfer(String mode, int port) {
		this.windowsTransferMode = mode;
		this.windowsTransferPort = port;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getEchoTimeout() {
		return echoTimeout;
	}

	public void setEchoTimeout(int echoTimeout) {
		this.echoTimeout = echoTimeout;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getWindowsTransferMode() {
		return windowsTransferMode;
	}

	public int getWindowsTransferPort() {
		return windowsTransferPort;
	}

}
