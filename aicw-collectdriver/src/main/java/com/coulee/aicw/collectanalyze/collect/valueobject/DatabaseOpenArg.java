package com.coulee.aicw.collectanalyze.collect.valueobject;

/**
 * Description:数据库连接对象
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-15
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class DatabaseOpenArg extends OpenArg {

	private static final long serialVersionUID = -846530521643789350L;

	/**
	 * 数据库JDBC驱动类名
	 */
	private String jdbcDriver;
	
	/**
	 * 数据库名称
	 */
	private String dbName;
	
	/**
	 * informix数据库server服务名
	 */
	private String ifxServerName;

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getIfxServerName() {
		return ifxServerName;
	}

	public void setIfxServerName(String ifxServerName) {
		this.ifxServerName = ifxServerName;
	}

	
}
