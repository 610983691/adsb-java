package com.coulee.aicw.collectanalyze.collect.protocol.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;

/**
 * Description:数据库JDBC协议操作类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class DatabaseJdbc implements JdbcProtocol {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	/**
	 * 字符编码
	 */
	private String encode;
	
	/**
	 * informix数据库服务名
	 */
	private String serverName;
	
	private String ip;
	
	private int port;
	
	private String driverClass;
	
	private String dbName;
	
	private Connection conn;
	
	/**
	 * Definition:获取字符编码，主要为MySql使用
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	private String getEncode() {
		if (this.encode == null || "".equals(this.encode)) {
			return JdbcProtocol.DEFAULT_ENCODE;
		}
		return this.encode;
	}

	@Override
	public void setEncode(String encode) {
		this.encode = encode;
	}

	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public void connect(String ip, int port, String driverClass, String db)
			throws ProtocolException {
		logger.debug("connect to ip [{}] port [{}] with jdbc protocol, driver is [{}]",
				new Object[] { ip, port, driverClass });
		this.ip = ip;
		this.port = port;
		this.driverClass = driverClass;
		this.dbName = db;
		this.checkArgs();
	}

	@Override
	public boolean authentication(String username, String password)
			throws ProtocolException {
		this.checkArgs();
		String url = this.buildUrl();
		logger.debug("login database [{}] with username [{}]", this.dbName, username);
		try {
			Class.forName(this.driverClass).newInstance();
			this.conn = DriverManager.getConnection(url, username, password);
			return true;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			if (e.getMessage().contains("The Network Adapter could not establish the connection")) {
				logger.error("login database faild, network is not ready. ");
				this.throwProtocolException(ErrorCode.CON_RES_CONNECTION, e);
			} else {
				logger.error("login database faild, username/password error. ");
				this.throwProtocolException(ErrorCode.CON_RES_AUTHENTICATION, e);
			}
		}
		return false;
	}

	@Override
	public List<Map<String, String>> executeQuerySql(String sql) throws ProtocolException {
		this.testSession();
		logger.debug("execute sql {}", sql);
		if (StringUtils.isEmpty(sql)) {
			this.throwProtocolException(ErrorCode.PARAMS_NULL);
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
		    int columnCount = rsmd.getColumnCount();
		    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		    while (rs.next()) {
		    	Map<String, String> map = new HashMap<String, String>();
		    	for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnName(i), this.objectToString(rs.getObject(i)));
		    	}
		    	list.add(map);
		    }
		    return list;
		} catch (SQLException e) {
			logger.error("execute sql error. ");
			this.throwProtocolException(ErrorCode.EXECUTE_QUERY_SQL_ERROR, e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public boolean executeSql(String sql) throws ProtocolException {
		this.testSession();
		logger.debug("execute sql {}", sql);
		if (StringUtils.isEmpty(sql)) {
			this.throwProtocolException(ErrorCode.PARAMS_NULL);
		}
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			return true;
		} catch (SQLException e) {
			logger.error("execute sql error. ");
			this.throwProtocolException(ErrorCode.EXECUTE_SQL_ERROR, e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void close() throws ProtocolException {
		try {
			if (conn != null) {
				conn.close();
			}
			logger.debug("close connection for ip [{}], port [{}]", this.ip, this.port);
		} catch (SQLException e) {
			logger.error("close connection for ip [{}], port [{}] error. ", this.ip, this.port);
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Definition:检查参数
	 * @throws ProtocolException 
	 * 
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	private void checkArgs() throws ProtocolException {
		if (StringUtils.isEmpty(this.driverClass)) {
			this.throwProtocolException(ErrorCode.PARAMS_NULL_DRIVERCLASS);
		}
		if (StringUtils.isEmpty(this.dbName)) {
			this.throwProtocolException(ErrorCode.PARAMS_NULL_DBNAME);
		}
		if (JDBCConstants.DRIVER_INFORMIX.equals(this.driverClass) && StringUtils.isEmpty(this.serverName)) {
			this.throwProtocolException(ErrorCode.PARAMS_NULL_DBSERVER);
		}
	}
	
	/**
	 * Definition:构造jdbc连接url
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	private String buildUrl() throws ProtocolException {
		String template = JDBCConstants.DRIVER_URL_MAPPER.get(driverClass);
		if (template == null) {
			this.throwProtocolException(ErrorCode.UNSUPPORT_DATABASE_TYPE);
		}
		MessageFormat mf = new MessageFormat(template);
		if (JDBCConstants.DRIVER_INFORMIX.equals(this.driverClass)) {
			return mf.format(new Object[] {this.ip, String.valueOf(this.port), this.dbName, this.serverName});
		} else if (JDBCConstants.DRIVER_MYSQL.equals(this.driverClass)) {
			return mf.format(new Object[] {this.ip, String.valueOf(this.port), this.dbName, this.getEncode()});
		} else {
			return mf.format(new Object[] {this.ip, String.valueOf(this.port), this.dbName});
		}
	}
	
	/**
	 * Definition:抛出协议层异常
	 * @param errorCode 错误码
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	private void throwProtocolException(int errorCode) throws ProtocolException {
		throw new ProtocolException(String.valueOf(errorCode));
	}
	
	/**
	 * Definition:抛出协议层异常
	 * @param errorCode 错误码
	 * @param e 异常
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	private void throwProtocolException(int errorCode, Exception e) throws ProtocolException {
		throw new ProtocolException(String.valueOf(errorCode), e);
	}
	
	/**
	 * Definition:测试数据库连接
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	private void testSession() throws ProtocolException {
		if (this.conn == null) {
			logger.error("session doesn`t created or disconnected");
			this.throwProtocolException(ErrorCode.SESSION_NOT_CREATE);
		}
	}
	
	/**
	 * Definition:将
	 * @param o
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-10
	 */
	private String objectToString(Object o) {
		if (o == null) {
			return "";
		}
		if (o instanceof Byte) {
			Byte b = (Byte) o;
			return Byte.toString(b);
		} else if (o instanceof Short) {
			Short s = (Short) o;
			return Short.toString(s);
		} else if (o instanceof Integer) {
			Integer i = (Integer) o;
			return Integer.toString(i);
		} else if (o instanceof Long) {
			Long l = (Long) o;
			return Long.toString(l);
		} else if (o instanceof Float) {
			Float f = (Float) o;
			return Float.toString(f);
		} else if (o instanceof Double) {
			Double d = (Double) o;
			return Double.toString(d);
		} else if (o instanceof Byte[]) {
			Byte[] bs = (Byte[]) o;
			byte[] s = new byte[bs.length];
			for (int i = 0; i < bs.length; i++) {
				s[i] = bs[i].byteValue();
			}
			return new String(s);
		} else if (o instanceof java.sql.Date) {
			java.sql.Date sqlDate = (java.sql.Date) o;
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(sqlDate.getTime()));
		} else if (o instanceof java.sql.Time) {
			java.sql.Time time = (java.sql.Time) o;
			return new SimpleDateFormat("HH:mm:ss").format(time);
		} else if (o instanceof java.sql.Timestamp) {
			java.sql.Timestamp time = (java.sql.Timestamp) o;
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(time);
		} else if (o instanceof Clob) {
			Clob clob = (Clob) o;
			return this.clobToString(clob);
		} else if (o instanceof Blob) {
			Blob blob = (Blob) o;
			try {
				return new String(blob.getBytes((long)1, (int)blob.length()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "";
		}
		return o.toString();
	}
	
	/**
	 * Definition:Clob to String
	 * @param clob
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-10
	 */
	private String clobToString(Clob clob) {
		if (clob == null) {
			return "";
		}
		try {
			Reader inStreamDoc = clob.getCharacterStream();
			char[] tempDoc = new char[(int) clob.length()];
			inStreamDoc.read(tempDoc);
			inStreamDoc.close();
			return new String(tempDoc);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException es) {
			es.printStackTrace();
		}
		return "";
	}


	@Override
	public Connection getConnection() throws ProtocolException {
		if (this.conn != null) {
			return conn;
		} else {
			this.throwProtocolException(ErrorCode.UNAUTHENTICATION_CONNECTION);
		}
		return conn;
	}
	
	public static void main(String[] args) {
		DatabaseJdbc jdbc = new DatabaseJdbc();
		try {
			jdbc.connect("192.168.1.16", 1521, "oracle.jdbc.driver.OracleDriver", "orcl");
			jdbc.authentication("vuln", "1234567890");
			List<Map<String, String>> list = jdbc.executeQuerySql("select count(*) from CA_RUNNING_LOG_COLLECTOR");
			for (Map<String, String> map : list) {
				for (String key : map.keySet()) {
					System.out.print(key + " == " + map.get(key));
					System.out.print(" ");
				}
				System.out.println("===================");
			}
			jdbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
