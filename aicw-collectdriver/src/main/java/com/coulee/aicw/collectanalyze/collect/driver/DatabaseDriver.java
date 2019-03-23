package com.coulee.aicw.collectanalyze.collect.driver;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.executer.Executer;
import com.coulee.aicw.collectanalyze.collect.executer.ExecuterFactory;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.DatabaseLoginArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.DatabaseOpenArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.collectanalyze.collect.valueobject.LoginArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.OpenArg;

/**
 * Description:数据库驱动类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class DatabaseDriver extends AbstractDriver {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private String ip;
	
	private int port;
	
	private String driverClass;
	
	private String dbName;

	@Override
	public boolean login(OpenArg openArg, LoginArg loginArg)
			throws DriverException {
		if (!(openArg instanceof DatabaseOpenArg) || !(loginArg instanceof DatabaseLoginArg)) {
			this.throwDriverException(ErrorCode.SQL_SCRIPT_TYPE_ERROR);
		}
		try {
			DatabaseOpenArg databaseOpenArg = (DatabaseOpenArg) openArg;
			DatabaseLoginArg databaseLoginArg = (DatabaseLoginArg) loginArg;
			this.ip = databaseOpenArg.getIp();
			this.port = databaseOpenArg.getPort();
			this.driverClass = databaseOpenArg.getJdbcDriver();
			this.dbName = databaseOpenArg.getDbName();
			this.setEncode(databaseOpenArg.getEncode());
			this.checkArgs(databaseOpenArg, databaseLoginArg);
			this.jdbcProtocol.connect(ip, port, driverClass, dbName);
			this.jdbcProtocol.authentication(databaseLoginArg.getUsername(), databaseLoginArg.getPassword());
			logger.info("login database {}:{}/{} successful, driver {}", new Object[] { ip, port, dbName, driverClass });
			return true;
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("login database {}:{}/{} failure, error msg : {}", new Object[]{ip, port, dbName, driverClass, ErrorCode.msg(e.getMessage())});
			throw new DriverException(e.getMessage());
		}
	}

	@Override
	public boolean doJumpLogin(OpenArg openArg, LoginArg loginArg, boolean isLast) throws DriverException {
		throw new DriverException();
	}

	@Override
	public ExecuteResult executeCmd(CommandArg commandArg) throws DriverException {
		try {
			Executer executer = ExecuterFactory.getExecuter(commandArg.getType());
			executer.setJdbcProtocol(jdbcProtocol);
			return executer.execute(commandArg);
		} catch (ExecuterException e) {
			e.printStackTrace();
			throw new DriverException(e.getMessage());
		}
	}

	@Override
	public void logout() {
		logger.info("logout {}:{}/{} ... ", new Object[] { this.ip, this.port, this.dbName });
		try {
			if (this.jdbcProtocol != null) {
				this.jdbcProtocol.close();
				this.jdbcProtocol = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: 判断连接参数和登录参数是否正确<br> 
	 * Created date: 2017年11月28日
	 * @param openArg
	 * @param loginArg
	 * @throws DriverException
	 * @author LanChao
	 */
	private void checkArgs(DatabaseOpenArg databaseOpenArg, DatabaseLoginArg databaseLoginArg) throws DriverException {
		if (StringUtils.isEmpty(this.ip) || this.port == 0) {
			logger.error("connect ip or port is null .");
			this.throwDriverException(ErrorCode.CONNECT_IP_PORT_NULL);
		}
		if (StringUtils.isEmpty(this.driverClass)) {
			logger.error("connect driverclass is null .");
			this.throwDriverException(ErrorCode.CONNECT_DRIVERCLASS_NULL);
		}
		if (StringUtils.isEmpty(this.dbName)) {
			logger.error("connect dbname is null .");
			this.throwDriverException(ErrorCode.CONNECT_DBNAME_NULL);
		}
		if (StringUtils.isEmpty(databaseLoginArg.getUsername()) || StringUtils.isEmpty(databaseLoginArg.getPassword())) {
			logger.error("login username or password is null .");
			this.throwDriverException(ErrorCode.LOGIN_NAME_PASSWORD_NULL);
		}
	}
}
