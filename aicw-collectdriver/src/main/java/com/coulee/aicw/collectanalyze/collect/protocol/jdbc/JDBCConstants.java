package com.coulee.aicw.collectanalyze.collect.protocol.jdbc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:JDBC协议类常量
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class JDBCConstants {
	
	/**
	 * SQLSERVER2000驱动
	 */
	public static final String DRIVER_SQLSERVER_2000 = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	
	/**
	 * SQLSERVER驱动
	 */
	public static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	/**
	 * MYSQL驱动
	 */
	public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	
	/**
	 * ORACLE驱动
	 */
	public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * DB2驱动
	 */
	public static final String DRIVER_DB2 = "com.ibm.db2.jcc.DB2Driver";
	
	/**
	 * SYBASE驱动
	 */
	public static final String DRIVER_SYBASE = "com.sybase.jdbc4.jdbc.SybDriver";
	
	/**
	 * INFORMIX驱动
	 */
	public static final String DRIVER_INFORMIX = "com.informix.jdbc.IfxDriver";
	
	/**
	 * POSTGRE驱动
	 */
	public static final String DRIVER_POSTGRE = "org.postgresql.Driver";
	
	/**
	 * 数据库驱动与JDBC连接URL模板映射表
	 */
	public static Map<String, String> DRIVER_URL_MAPPER = new HashMap<String, String>();
	
	static {
		DRIVER_URL_MAPPER.put(DRIVER_SQLSERVER_2000, "jdbc:microsoft:sqlserver://{0}:{1};DatabaseName={2}");
		DRIVER_URL_MAPPER.put(DRIVER_SQLSERVER, "jdbc:sqlserver://{0}:{1};DatabaseName={2}");
		DRIVER_URL_MAPPER.put(DRIVER_MYSQL, "jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding={3}");
		DRIVER_URL_MAPPER.put(DRIVER_ORACLE, "jdbc:oracle:thin:@{0}:{1}:{2}");
		DRIVER_URL_MAPPER.put(DRIVER_DB2, "jdbc:db2://{0}:{1}/{2}");
		DRIVER_URL_MAPPER.put(DRIVER_SYBASE, "jdbc:sybase:Tds:{0}:{1}/{2}");
		DRIVER_URL_MAPPER.put(DRIVER_INFORMIX, "jdbc:informix-sqli://{0}:{1}/{2}:INFORMIXSERVER={3}");
		DRIVER_URL_MAPPER.put(DRIVER_POSTGRE, "jdbc:postgresql://{0}:{1}/{2}");
	}
	
	/**
	 * JDBC数据类型与JAVA类型映射
	 */
	public static final Map<Integer, Class<?>> JDBCJAVAMAP = new HashMap<Integer, Class<?>>();
	
	static {
		JDBCJAVAMAP.put(java.sql.Types.CHAR, String.class);
		JDBCJAVAMAP.put(java.sql.Types.VARCHAR, String.class);
		JDBCJAVAMAP.put(java.sql.Types.LONGVARCHAR, String.class);
		JDBCJAVAMAP.put(java.sql.Types.NUMERIC, BigDecimal.class);
		JDBCJAVAMAP.put(java.sql.Types.DECIMAL, BigDecimal.class);
		JDBCJAVAMAP.put(java.sql.Types.BIT, Boolean.class);
		JDBCJAVAMAP.put(java.sql.Types.TINYINT, byte.class);
		JDBCJAVAMAP.put(java.sql.Types.SMALLINT, Short.class);
		JDBCJAVAMAP.put(java.sql.Types.INTEGER, Integer.class);
		JDBCJAVAMAP.put(java.sql.Types.BIGINT, Long.class);
		JDBCJAVAMAP.put(java.sql.Types.REAL, Float.class);
		JDBCJAVAMAP.put(java.sql.Types.FLOAT, Double.class);
		JDBCJAVAMAP.put(java.sql.Types.DOUBLE, Double.class);
		JDBCJAVAMAP.put(java.sql.Types.BINARY, byte[].class);
		JDBCJAVAMAP.put(java.sql.Types.VARBINARY, byte[].class);
		JDBCJAVAMAP.put(java.sql.Types.LONGVARBINARY, byte[].class);
		JDBCJAVAMAP.put(java.sql.Types.DATE, java.sql.Date.class);
		JDBCJAVAMAP.put(java.sql.Types.TIME, java.sql.Time.class);
		JDBCJAVAMAP.put(java.sql.Types.TIMESTAMP, java.sql.Timestamp.class);
	}
}
