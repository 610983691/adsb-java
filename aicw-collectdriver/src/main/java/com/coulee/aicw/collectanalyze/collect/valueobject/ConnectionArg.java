package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:连接设备参数对象
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-11-28
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ConnectionArg implements Serializable {

	private static final long serialVersionUID = -1147229485988844392L;
	
	/**
	 * 连接对象
	 */
	private OpenArg openArg;
	
	/**
	 * 登录对象
	 */
	private LoginArg loginArg;
	
	/**
	 * 数据连接对象
	 */
	private DatabaseOpenArg databaseOpenArg;
	
	/**
	 * 数据库登录对象
	 */
	private DatabaseLoginArg databaseLoginArg;
	
	/**
	 * 设备类型字典值
	 */
	private String deviceType;
	
	/**
	 * 中间件、数据库所在主机的设备类型字典值
	 */
	private String belongDeviceType;
	
	/**
	 * 设备驱动类
	 */
	private String driverClass;
	
	/**
	 * 设备ID
	 */
	private String deviceId;
	
	/**
	 * 跳转机
	 */
	private List<ConnectionArg> jumps;
	
	/**
	 * 异步调用时需要冗余缓存并返回的信息<br>
	 * value为实现了java.io.Serializable接口的对象
	 */
	private Map<String, Object> context = new HashMap<String, Object>();
	
	/**
	 * Definition:设备信息
	 * @param deviceType 设备类型字典值
	 * @param deviceId 设备ID
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	public ConnectionArg setDeviceInfo(String deviceType, String deviceId){
		this.deviceType = deviceType;
		this.deviceId = deviceId;
		return this;
	}
	
	/**
	 * Definition:设置连接参数
	 * @param ip IP地址
	 * @param port 端口
	 * @param protocol 协议
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setOpenArg(String ip, int port, String protocol) {
		if (openArg == null) {
			openArg = new OpenArg(ip, port, protocol);
		}
		openArg.setIp(ip);
		openArg.setPort(port);
		openArg.setProtocol(protocol);
		return this;
	}
	
	/**
	 * Definition:设置编码格式
	 * @param encode
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setEncode(String encode) {
		if (openArg == null) {
			openArg = new OpenArg();
		}
		openArg.setEncode(encode);
		return this;
	}
	
	/**
	 * Definition:设置超时时间
	 * @param connectTimeout 连接超时时间，单位：秒
	 * @param echoTimeout 读取回显超时时间，单位：秒
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setTimeout(int connectTimeout, int echoTimeout) {
		if (openArg == null) {
			openArg = new OpenArg();
		}
		openArg.setConnectTimeout(connectTimeout);
		openArg.setEchoTimeout(echoTimeout);
		return this;
	}
	
	/**
	 * Definition:设置登录参数
	 * @param username 用户名
	 * @param password 密码
	 * @param prompt 提示符
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setLoginArg(String username, String password, String prompt) {
		if (loginArg == null) {
			loginArg = new LoginArg();
		}
		loginArg.setUsername(username);
		loginArg.setPassword(password);
		loginArg.setPrompt(prompt);
		return this;
	}
	
	/**
	 * Definition:设置管理员登录参数
	 * @param adminUsername 管理员用户名
	 * @param adminPassword 管理员密码
	 * @param adminPrompt 管理员提示符
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setAdminLoginArg(String adminUsername, String adminPassword, String adminPrompt) {
		if (loginArg == null) {
			loginArg = new LoginArg();
		}
		loginArg.setAdminUsername(adminUsername);
		loginArg.setAdminPassword(adminPassword);
		loginArg.setAdminPrompt(adminPrompt);
		return this;
	}
	
	/**
	 * Definition:设置数据库连接参数
	 * @param ip IP地址
	 * @param port 端口
	 * @param jdbcDriver JDBC Type4驱动
	 * @param dbName 数据库名
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public ConnectionArg setDatabaseOpenArg(String ip, int port, String jdbcDriver, String dbName) {
		if (databaseOpenArg == null) {
			databaseOpenArg = new DatabaseOpenArg();
		}
		databaseOpenArg.setIp(ip);
		databaseOpenArg.setPort(port);
		databaseOpenArg.setJdbcDriver(jdbcDriver);
		databaseOpenArg.setDbName(dbName);
		return this;
	}
	
	/**
	 * Definition:设置数据库特殊属性
	 * @param encode MySQL数据库编码 ，非MySQL传空
	 * @param ifxServerName informix数据库server服务名，非informix传空
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public ConnectionArg setDatabaseSpecialArg(String encode, String ifxServerName) {
		if (databaseOpenArg == null) {
			databaseOpenArg = new DatabaseOpenArg();
		}
		databaseOpenArg.setEncode(encode);
		databaseOpenArg.setIfxServerName(ifxServerName);
		return this;
	}
	
	/**
	 * Definition:设置数据库登录参数
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public ConnectionArg setDatabaseLoginArg(String username, String password) {
		if (databaseLoginArg == null) {
			databaseLoginArg = new DatabaseLoginArg();
		}
		databaseLoginArg.setUsername(username);
		databaseLoginArg.setPassword(password);
		return this;
	}
	
	/**
	 * Definition:设置跳转指令
	 * @param jumpCommand
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setJumpCommand(String jumpCommand) {
		if (loginArg == null) {
			loginArg = new LoginArg();
		}
		loginArg.setJumpCommand(jumpCommand);
		return this;
	}
	
	/**
	 * Definition:设置跳转机，顺序为从第一台跳转机直至目标机
	 * @param jumps
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ConnectionArg setJumps(List<ConnectionArg> jumps) {
		this.jumps = jumps;
		return this;
	}
	
	/**
	 * Definition:设置异步采集时需要冗余缓存的数据
	 * @param key
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-1-17
	 */
	public ConnectionArg addContext(String key, java.io.Serializable value) {
		if (key != null && !"".equals(key) && value != null) {
			this.context.put(key, value);
		}
		return this;
	}
	
	/**
	 * Definition:当Windows执行的命令存在依赖文件时，需进行设置<br>
	 * 目前支持RDP和SAMBA
	 * @param mode 方式
	 * @param port 端口：RDP时需传入，如不传递默认为3389；SAMBA时不需要传
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-1-17
	 */
	public ConnectionArg setWindowsTransferMode(String mode, int port) {
		if (openArg == null) {
			openArg = new OpenArg();
		}
		openArg.setWindowsTransfer(mode, port);
		return this;
	}
	
	/**
	 * Definition:设置类Unix主机是否sudo模式执行命令
	 * @param isSudoMode 是否sudo模式
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017年11月16日
	 */
	public ConnectionArg setUnixSudoMode(boolean isSudoMode) {
		if (loginArg == null) {
			loginArg = new LoginArg();
		}
		loginArg.setSudoMode(isSudoMode);
		return this;
	}

	public OpenArg getOpenArg() {
		return openArg;
	}

	public LoginArg getLoginArg() {
		return loginArg;
	}
	
	public DatabaseOpenArg getDatabaseOpenArg() {
		return databaseOpenArg;
	}

	public DatabaseLoginArg getDatabaseLoginArg() {
		return databaseLoginArg;
	}

	public List<ConnectionArg> getJumps() {
		return jumps;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public String getBelongDeviceType() {
		return belongDeviceType;
	}

	public ConnectionArg setBelongDeviceType(String belongDeviceType) {
		this.belongDeviceType = belongDeviceType;
		return this;
	}

	
}
