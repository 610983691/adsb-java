package com.coulee.aicw.collectanalyze.collect.protocol;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;

/**
 * Description:JDBC协议操作接口
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface JdbcProtocol {
	
	/**
	 * 缺省字符集
	 */
	public static final String DEFAULT_ENCODE = "UTF-8";

	/**
	 * Definition:设置编码
	 * @param encode
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void setEncode(String encode);
	
	/**
	 * Definition:Informix数据库设置service名称
	 * @param serverName
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public void setServerName(String serverName);
	
	/**
	 * Definition:连接数据库
	 * @param ip 数据库IP
	 * @param port 数据库端口
	 * @param driverClass 数据库驱动
	 * @param db 数据库名称
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public void connect(String ip, int port, String driverClass, String db) throws ProtocolException;
	
	/**
	 * Definition:登录数据库
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public boolean authentication(String username, String password) throws ProtocolException;
	
	/***
	 * Definition:执行查询SQL语句
	 * @param sql 要执行的SQL
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public List<Map<String, String>> executeQuerySql(String sql) throws ProtocolException;
	
	/**
	 * Definition:执行非查询SQL语句
	 * @param sql
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-10
	 */
	public boolean executeSql(String sql) throws ProtocolException;
	
	/**
	 * Definition:获取已经建立的数据库连接
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public Connection getConnection() throws ProtocolException;
	
	/**
	 * Definition:关闭所有连接
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void close() throws ProtocolException;
}
