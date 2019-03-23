package com.coulee.aicw.foundations.utils.mybatis;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
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
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

/**
 * Description: MyBatis拦截器，用于like查询中%和_的处理<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class MyBatisSqlEscapeInterceptor implements Interceptor {
	
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof StatementHandler) {
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
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");  
			String sql = boundSql.getSql();
			if (sql.toLowerCase().contains("like")) {
				MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");  
				MetaObject metaMappedStatement = MetaObject.forObject(mappedStatement, DEFAULT_OBJECT_FACTORY,DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
				SqlNode sqlNode = (SqlNode) metaMappedStatement.getValue("sqlSource.rootSqlNode");
		        boundSql = this.getBoundSql(mappedStatement.getConfiguration(), boundSql.getParameterObject(), sqlNode);
		        metaStatementHandler.setValue("delegate.boundSql", boundSql);
			}
			return invocation.proceed();
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {  
	        return Plugin.wrap(target, this);  
	    } else {  
	        return target;  
	    }  
	}

	@Override
	public void setProperties(Properties properties) {
	}
	
	/**
	 * Definition:构造新的BoundSql
	 * @param configuration
	 * @param parameterObject
	 * @param sqlNode
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年7月16日
	 */
	private BoundSql getBoundSql(Configuration configuration, Object parameterObject, SqlNode sqlNode) {
		DynamicContext context = new DynamicContext(configuration, parameterObject);
		sqlNode.apply(context);
		String originalSql = context.getSql();
		System.out.println("originalSql == "+originalSql);
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
		String sql = this.modifyLikeSql(originalSql, parameterObject);
		SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
			boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}
		return boundSql;
	}
	
	/**
	 * Definition:修改SQL和参数
	 * @param sql
	 * @param parameterObject
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年7月16日
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String modifyLikeSql(String sql, Object parameterObject) {
		if ((parameterObject instanceof HashMap) == false) {
			return sql;
		}
		if (!sql.toLowerCase().contains("like")) {
			return sql;
		}
		String reg = "(?i)like\\s+'%'\\s*\\|\\|\\s*#\\{(\\w+\\.)?(\\w+)\\}\\s*\\|\\|\\s*'%'";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		List<String> replaceEscape = new ArrayList<String>();
		List<String> replaceFiled = new ArrayList<String>();
		while (matcher.find()) {
			replaceEscape.add(matcher.group());
			replaceFiled.add(matcher.group(2));
		}
		for (String s : replaceEscape) { //修改SQL，加入转义
			sql = sql.replace(s, s + " ESCAPE '\\' ");
		}
		// 修改参数
		Object param1 = ((HashMap) parameterObject).get("param1");
		//如果为String类型参数
		if (param1 instanceof String) {
			if (param1 != null && (param1.toString().contains("%") || param1.toString().contains("_"))) {
				param1 = param1.toString().replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
				((HashMap) parameterObject).put("param1", param1);
				((HashMap) parameterObject).put("params", param1);
			}
		} else { //如果为HashMap类型参数
			HashMap paramMab = (HashMap) ((HashMap) parameterObject).get("param1");
			for (String key : replaceFiled) {
				Object val = paramMab.get(key);
				if (val != null && val instanceof String && (val.toString().contains("%") || val.toString().contains("_"))) {
					val = val.toString().replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
					paramMab.put(key, val);
				}
			}
		}
		return sql;
	}
	
}