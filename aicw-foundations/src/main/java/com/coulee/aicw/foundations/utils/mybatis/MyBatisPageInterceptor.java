package com.coulee.aicw.foundations.utils.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.coulee.aicw.foundations.utils.common.RegexTools;
import com.coulee.aicw.foundations.utils.common.ThreadVariableTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: MyBatis拦截器，用于分页操作<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
	@Signature (type=Executor.class,
     method="update",
     args={MappedStatement.class,Object.class}),
	@Signature(type=Executor.class,
    method="query",
    args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class,
            CacheKey.class,BoundSql.class}),
    @Signature(type=Executor.class,
    method="query",
    args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class}),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class MyBatisPageInterceptor implements Interceptor {
	
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    
    /**
     * 分页参数对象
     */
    private final String PAGE_ARG = "pageArg";
    
    /**
     * Oracle数据库配置
     */
    private static final String DBTYPE_ORACLE = "oracle";
    
    /**
     * MySQL数据库
     */
    private static final String DBTYPE_MYSQL = "mysql";
    
    /**
     * Postgre数据库
     */
    private static final String DBTYPE_POSTGRE = "postgresql";
    
    /**
     * DB2数据库
     */
    private static final String DBTYPE_DB2 = "db2";
    
    /**
     * Informix数据库
     */
    private static final String DBTYPE_INFORMIX = "informix";
    
    /**
     * Sybase数据库
     */
    private static final String DBTYPE_SYBASE = "sybase";
    
    /**
     * Sqlite内置数据库
     */
    private static final String DBTYPE_SQLITE = "sqlite";
    
    /**
     * 数据库类型方言，主要构造分页语句使用，默认为Oracle
     */
    private String dialect = DBTYPE_ORACLE;
    
	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof StatementHandler) {
			Connection connection = (Connection) invocation.getArgs()[0];
			this.setDatabaseDialect(connection);
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
			while (metaStatementHandler.hasGetter("h")) {// 可以分离出最原始的的目标类)
				Object object = metaStatementHandler.getValue("h");
				metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			}
			while (metaStatementHandler.hasGetter("target")) {// 分离最后一个代理对象的目标类
				Object object = metaStatementHandler.getValue("target");
				metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
			}
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");  
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");  
	        Object parameterObject = boundSql.getParameterObject();
	        if (parameterObject != null) {
	        	PageArg page = this.getPageArgFromParam(parameterObject, metaStatementHandler); //获取分页参数
				if (page != null) {
					String originalSql = boundSql.getSql(); //原始的SQL
					String pageSql = buildPageSql(originalSql, page); //构造分页SQL
					metaStatementHandler.setValue("delegate.boundSql.sql", pageSql); //用分页查询SQL覆盖原始SQL
					metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);//清除内存分页设置
					metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);//清除内存分页设置
//					Connection connection = (Connection) invocation.getArgs()[0];
					buildPageInfo(originalSql, connection, mappedStatement, boundSql, page);// 重设分页参数里的总条数、总页数等
					ThreadVariableTools.setVariable(PAGE_ARG, page); //将分页对象放入线程变量中
				}
	        }
			return invocation.proceed();
		} else if (invocation.getTarget() instanceof ResultSetHandler) {
			Object result = invocation.proceed();
			/*使用原有getVariable有严重BUG。当分页参数page为null时，如果某线程里曾经存过变量PAGE，这个时候会取到一个错误的PAGE对象，有可能是前一个请求放入其中的。
			 * 因此使用remove方法，获取page的同时，将page从threadlocal里删除
			 * */
			PageArg page = (PageArg) ThreadVariableTools.remove(PAGE_ARG); //从线程变量中取出分页对象
			if (page != null && result instanceof List) {
				return new PageList<Object>((List<Object>) result, page.getCurPage(), page.getPageSize(), page.getTotalRow());
			}
			return result;
		}
		return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler || target instanceof ResultSetHandler || target instanceof Executor|| target instanceof CachingExecutor) {  
	        return Plugin.wrap(target, this);  
	    } else {  
	        return target;  
	    }  
	}

	@Override
	public void setProperties(Properties properties) {
	}
	
	/**
	 * Definition:构造分页信息
	 * @param originalSql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 * @Author: oblivion
	 * @Created date: 2014年12月5日
	 */
	private void buildPageInfo(String originalSql, Connection connection,
			MappedStatement mappedStatement, BoundSql boundSql, PageArg page) {
		String countSql = this.buildCountSql(originalSql);
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),
					countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			int totalCount = 0; // 记录总记录数
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			page.setTotalRow(totalCount);
			int totalPage = totalCount / page.getPageSize()
					+ ((totalCount % page.getPageSize() == 0) ? 0 : 1);
			page.setTotalPage(totalPage);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (countStmt != null) {
					countStmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	  
	/**
	 * Definition:设置参数值
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 * @Author: oblivion
	 * @Created date: 2014年12月5日
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,  
	        Object parameterObject) throws SQLException {
	    ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);  
	    parameterHandler.setParameters(ps);  
	}  

	/**
	 * Definition:从参数中获取分页参数
	 * @param parameterObject
	 * @param metaStatementHandler
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月5日
	 */
	private PageArg getPageArgFromParam(Object parameterObject, MetaObject metaStatementHandler) {
		String params = parameterObject.toString();
		if (params != null && !"".equals(params) && params.contains(PageArg.class.getName() + "@")) {
			String regex = "(\\S+)=" + PageArg.class.getName() + "@\\S+";
			List<String> list = RegexTools.getAllMatchingGroup(params, regex, 1);
			//如果组1为空，则说明该参数对象即为PageArg
			if (list == null || list.isEmpty()) {
				return (PageArg) parameterObject;
			}
			//否则从多个参数中取出PageArg对象
			return (PageArg) metaStatementHandler.getValue("delegate.boundSql.parameterObject." + list.get(0));
		}
		return null;
	}
	
	/**
	 * Definition:构造查询总条数SQL
	 * @param originalSql
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月5日
	 */
	private String buildCountSql(String originalSql) {
		StringBuffer sb = new StringBuffer("select count(*) as count from ");
		sb.append("(").append(originalSql).append(")");
		if (DBTYPE_POSTGRE.equals(dialect) || DBTYPE_MYSQL.equals(dialect)) {
			sb.append(" as a ");
		}
		return sb.toString();
	}
	
	/**
	 * Definition: 构造Oracle分页查询语句
	 * @param originalSql
	 * @param pageArg
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月5日
	 */
	private String buildPageSql(String originalSql, PageArg pageArg) {
		StringBuffer sb = new StringBuffer();
		int start = pageArg.getStart();
		int limit = pageArg.getPageSize();
		if (DBTYPE_ORACLE.equals(dialect)) {
			limit += start;
			start++;
			sb.append("select * from(");
			sb.append("select rownum num, a.*");
			sb.append(" from (").append(originalSql).append(") a where rownum <= ").append(limit);
			sb.append(") where num >= ").append(start);
		} else if (DBTYPE_MYSQL.equals(dialect)) {
			sb.append(originalSql);
			sb.append(" LIMIT ");
			sb.append(start);
			sb.append(",");
			sb.append(limit);
		} else if (DBTYPE_POSTGRE.equals(dialect) || DBTYPE_SQLITE.equals(dialect)) {
			sb.append(originalSql);
			sb.append(" LIMIT ");
			sb.append(limit);
			sb.append(" OFFSET ");
			sb.append(start);
		} else if (DBTYPE_DB2.equals(dialect)) {
			
		} else if (DBTYPE_SYBASE.equals(dialect)) {
			
		} else if (DBTYPE_INFORMIX.equals(dialect)) {
			
		}
		return sb.toString();
	}
	
	/**
	 * Definition:设置数据库方言
	 * @param connection 数据库连接对象
	 * @throws SQLException
	 * @Author: oblivion
	 * @Created date: 2015年6月29日
	 */
	private void setDatabaseDialect(Connection connection) throws SQLException {
		String dbTypeName = connection.getMetaData().getDatabaseProductName(); //数据库名称
		if (dbTypeName == null || "".equals(dbTypeName)) {
			this.dialect = DBTYPE_ORACLE;
		}
		dbTypeName = dbTypeName.toLowerCase();
		this.dialect = dbTypeName;
	}
}