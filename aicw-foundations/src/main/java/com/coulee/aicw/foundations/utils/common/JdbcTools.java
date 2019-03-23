package com.coulee.aicw.foundations.utils.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.foundations.utils.page.PageUtils;

/**
 * Description: JDBC查询工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class JdbcTools {

	/**
	 * 驱动类名
	 */
	private String driver;

	/**
	 * JDBC连接URL
	 */
	private String jdbcUrl;

	/**
	 * 数据库用户名
	 */
	private String username;

	/**
	 * 数据库密码
	 */
	private String password;
	
	private Pattern selectColumnsPattern = Pattern.compile("(?is)select\\s(.*?)\\s(?i)from.*");
	
	/**
	 * Description :实例化工具类
	 * @param driver 驱动类名
	 * @param jdbcUrl JDBC连接URL
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 */
	public JdbcTools(String driver, String jdbcUrl, String username, String password) {
		this.driver = driver;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}
	
	@SuppressWarnings("unused")
	private JdbcTools() {
	}
	
	/**
	 * Definition:获取查询SQL内的列名
	 * @param sql
	 * @return
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	private List<String> getColumnsFromSelect(String sql) {
		List<String> colNames = new ArrayList<String>();
		// 取出sql中列名部分
		Matcher m = selectColumnsPattern.matcher(sql.trim());
		String[] tempA = null;
		if (m.matches()) {
			tempA = m.group(1).split(",");
		}
		if (tempA == null) {
			return null;
		}
		String p1 = "(?s)(\\w+)";
		String p2 = "(?:\\w+\\s(\\w+))";
		String p3 = "(?:\\w+\\sas\\s(\\w+))";
		String p4 = "(?:\\w+\\.(\\w+))";
		String p5 = "(?:\\w+\\.\\w+\\s(\\w+))";
		String p6 = "(?:\\w+\\.\\w+\\sas\\s(\\w+))";
		String p7 = "(?:.+\\s(\\w+))";
		String p8 = "(?:.+\\sas\\s(\\w+))";
		Pattern p = Pattern.compile("(?:" + p1 + "||" + p2 + "||" + p3 + "||" + p4
				+ "||" + p5 + "||" + p6 + "||" + p7 + "||" + p8 + ")");
		for (String temp : tempA) {
			m = p.matcher(temp.trim());
			if (!m.matches()) {
				continue;
			}
			for (int i = 1; i <= m.groupCount(); i++) {
				if (m.group(i) == null || "".equals(m.group(i))) {
					continue;
				}
				colNames.add(m.group(i));
			}
		}
		return colNames;
	}
	
	/**
	 * Definition:构造查询总条数SQL
	 * @param sql
	 * @return
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	private String addCountSQL(String sql) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) dataCount from (");
		sb.append(sql);
		sb.append(") as a");
		return sb.toString();
	}
	
	/**
	 * Definition:构造分页查询SQL
	 * @param sql
	 * @param pageon
	 * @param pagesize
	 * @return
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	public String addPagingSQL(String sql, int pageon, int pagesize) {
		int start = pageon;
		int limit = pagesize;
		StringBuffer sb = new StringBuffer();
		if ("com.microsoft.jdbc.sqlserver.SQLServerDviver".equals(this.driver)) {//SQLServer 0.7 2000
			
		} else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(this.driver)) {//SQLServer 2005 2008
			
		} else if ("com.mysql.jdbc.Driver".equals(this.driver)) {//MySQL
			sb.append(sql);
			sb.append(" LIMIT ");
			sb.append(start);
			sb.append(",");
			sb.append(limit);
		} else if ("oracle.jdbc.driver.OracleDriver".equals(this.driver)) {//Oracle8/8i/9i/10g数据库（thin模式）
			List<String> list = getColumnsFromSelect(sql);
			sb.append("select ");
			for (String str : list) {
				sb.append(str).append(", ");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(" from (").append(sql).append(") as a");
			sb.append(" where rownum between ").append(start == 0 ? 1 : start).append(" and ").append(limit);
		} else if ("com.ibm.db2.jdbc.app.DB2Driver".equals(this.driver)) {//DB2
			
		} else if ("com.sybase.jdbc.SybDriver".equals(this.driver)) {//Sybase
			
		} else if ("com.informix.jdbc.IfxDriver".equals(this.driver)) {//Informix
			
		} else if ("org.postgresql.Driver".equals(this.driver)) {//PostgreSQL
			sb.append(sql);
			sb.append(" LIMIT ");
			sb.append(limit);
			sb.append(" OFFSET ");
			sb.append(start);
		}
		return sb.toString();
	}
	
	/**
	 * Definition:将RusultSet对象实例化对象
	 * @param rs
	 * @param sql
	 * @return
	 * @throws Exception
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	private Map<String, Object> instance(ResultSet rs, String sql) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> columns = getColumnsFromSelect(sql);
		for (int i = 0; i < columns.size(); i++) {
			String col = columns.get(i);
			Object v = rs.getObject(i + 1); 
			map.put(col, v);
		}
		return map;
	}
	
	/**
	 * Definition:查询复数的对象
	 * @param param
	 * @param sql
	 * @return
	 * @throws Exception
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	public List<Map<String, Object>> queryPlural(Parameters param, String sql) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			setParameters(stmt, param);
			rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(this.instance(rs, sql));
			}
			return list;
		} finally {
			replease(conn, stmt, rs);
		}
	}
	
	/**
	 * Definition:分页查询复数的对象
	 * @param param
	 * @param sql
	 * @param pageon
	 * @param pagesize
	 * @return
	 * @throws Exception
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	public PageList<Map<String, Object>> queryPluralForPagging(Parameters param, String sql, PageArg pageArg) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			conn = getConnection();
			//查询总行数
			int rows = queryDataCount(param, sql);
			//添加分页代码
			sql = addPagingSQL(sql, pageArg.getStart(), pageArg.getPageSize());
			stmt = conn.prepareStatement(sql);
			setParameters(stmt, param);
			rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(this.instance(rs, sql));
			}
			pageArg.setTotalRow(rows);
			return PageUtils.makeListPage(pageArg, list);
		} finally {
			replease(conn, stmt, rs);
		}
	}
	
	
	/**
	 * Definition:查询单个的对象
	 * @param param
	 * @param sql
	 * @return
	 * @throws Exception
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	public Map<String, Object> querySingular(Parameters param, String sql)
			throws Exception {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstate = null;
		try {
			conn = getConnection();
			pstate = conn.prepareStatement(sql);
			setParameters(pstate, param);
			rs = pstate.executeQuery();
			if (rs.next()) {
				return instance(rs, sql);
			}
		} finally {
			replease(conn, pstate, rs);
		}
		return null;
	}
	
	/**
	 * Definition:查询数据量
	 * @param param
	 * @param sql
	 * @return
	 * @throws Exception
	 * @Author: oblivion
	 * @Created date: 2016-11-29
	 */
	public int queryDataCount(Parameters param, String sql)
			throws Exception {
		int dataCount = 0;
		Connection conn = null;
		PreparedStatement pstate = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			sql = addCountSQL(sql);
			pstate = conn.prepareStatement(sql);
			setParameters(pstate, param);
			rs = pstate.executeQuery();
			if (rs.next()) {
				dataCount = rs.getInt("dataCount");
			}
		} finally {
			replease(conn, pstate, rs);
		}
		return dataCount;
	}
	
	/**
	 * Description: 更新数据<br> 
	 * Created date: 2018年1月11日
	 * @param param
	 * @param sql
	 * @return
	 * @throws Exception
	 * @author oblivion
	 */
	public int update(Parameters param, String sql) throws Exception {
		Connection conn = null;
		PreparedStatement pstate = null;
		try {
			conn = getConnection();
			pstate = conn.prepareStatement(sql);
			setParameters(pstate, param);
			return pstate.executeUpdate();
		} finally {
			replease(conn, pstate, null);
		}
	}
	
	
	/**
	 * 将param中的参数添加到pstate
	 * 
	 * @param pstate
	 * @param param
	 * @throws SQLException
	 */
	private static void setParameters(PreparedStatement pstate, Parameters param)
			throws SQLException {
		if (param != null && pstate != null) {
			Object[] list = param.getParameterList();
			for (int i = 0; i < list.length; i++) {
				Object obj = list[i];
				int j = i + 1;
				if (obj == null) {
					pstate.setString(j, "");
				}
				if (obj instanceof String) {
					pstate.setString(j, (String) obj);
				} else if (obj instanceof Boolean) {
					pstate.setBoolean(j, (Boolean) obj);
				} else if (obj instanceof Date) {
					pstate.setDate(j, (Date) obj);
				} else if (obj instanceof Double) {
					pstate.setDouble(j, (Double) obj);
				} else if (obj instanceof Float) {
					pstate.setFloat(j, (Float) obj);
				} else if (obj instanceof Integer) {
					pstate.setInt(j, (Integer) obj);
				} else if (obj instanceof Long) {
					pstate.setLong(j, (Long) obj);
				} else if (obj instanceof Short) {
					pstate.setShort(j, (Short) obj);
				} else if (obj instanceof Time) {
					pstate.setTime(j, (Time) obj);
				} else if (obj instanceof Timestamp) {
					pstate.setTimestamp(j, (Timestamp) obj);
				} else {
					pstate.setObject(j, obj);
				}
			}
		}

	}


	public static class Parameters {
		private Object[] list = new Object[0];

		public void setBoolean(int parameterIndex, boolean x) {
			addToList(parameterIndex, x);
		}

		public void setDate(int parameterIndex, Date x) {
			addToList(parameterIndex, x);
		}

		public void setDouble(int parameterIndex, double x) {
			addToList(parameterIndex, x);
		}

		public void setFloat(int parameterIndex, float x) {
			addToList(parameterIndex, x);
		}

		public void setInt(int parameterIndex, int x) {
			addToList(parameterIndex, x);
		}

		public void setLong(int parameterIndex, long x) {
			addToList(parameterIndex, x);
		}

		public void setObject(int parameterIndex, Object x) {
			addToList(parameterIndex, x);
		}

		public void setShort(int parameterIndex, short x) {
			addToList(parameterIndex, x);
		}

		public void setString(int parameterIndex, String x) {
			addToList(parameterIndex, x);
		}

		public void setTime(int parameterIndex, Time x) {
			addToList(parameterIndex, x);
		}

		public void setTimestamp(int parameterIndex, Timestamp x) {
			addToList(parameterIndex, x);
		}

		public void add(Object x) {
			Object[] destList = new Object[list.length + 1];
			System.arraycopy(list, 0, destList, 0, list.length);
			list = destList;
			list[list.length - 1] = x;
		}

		private void addToList(int parameterIndex, Object x) {
			if (list.length == 0) {
				list = new Object[1];
			}
			if (parameterIndex > list.length) {
				Object[] destList = new Object[parameterIndex];
				System.arraycopy(list, 0, destList, 0, list.length);
				list = destList;
			}
			list[parameterIndex - 1] = x;
		}

		Object[] getParameterList() {
			return list;
		}

	}
	
	/**
	 * 建立连接
	 * 
	 * @return con Connection
	 * @throws NamingException
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 * @throws Exception
	 */
	private Connection getConnection() throws NamingException,
			SQLException, ClassNotFoundException {
		Class.forName(this.driver);
		Connection conn = DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
		return conn;
	}
	
	/**
	 * 关闭连接
	 * 
	 * @param conn
	 * @param stmt
	 * @param preStmt
	 * @param rs
	 * @throws SQLException
	 */
	private void replease(Connection conn, Statement stmt, ResultSet rs)
			throws SQLException {
		if (rs != null) {
			rs.close();
			rs = null;
		}
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
	
}
