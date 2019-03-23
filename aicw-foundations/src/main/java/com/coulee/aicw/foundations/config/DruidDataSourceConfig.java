package com.coulee.aicw.foundations.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.coulee.aicw.foundations.config.conditions.DruidDataSourceCondition;

/**
 * Description: 数据源配置<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Configuration
@Conditional(DruidDataSourceCondition.class)
public class DruidDataSourceConfig {
	
	/**
	 * 加解密工具类
	 */
	@Autowired
	private CryptoTools cryptoTools;

	
	/**
	 * POSTGRE驱动
	 */
	private final String DRIVER_POSTGRE = "org.postgresql.Driver";
	
	/**
	 * MYSQL驱动
	 */
	private final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	
	/**
	 * ORACLE驱动
	 */
	private final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * SQLSERVER驱动
	 */
	private final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	/**
	 * POSTGRE
	 */
	private final String DB_POSTGRE = "postgresql";

	/**
	 * MYSQL
	 */
	private final String DB_MYSQL = "mysql";

	/**
	 * ORACLE
	 */
	private final String DB_ORACLE = "oracle";
	
	/**
	 * SQL-SERVER
	 */
	private final String DB_SQLSERVER = "sqlserver";

	@Autowired
	private Environment env;
	
	@Bean
	public StatFilter statFilter() {
		StatFilter statFilter = new StatFilter();
		statFilter.setSlowSqlMillis(10000);
		statFilter.setLogSlowSql(true);
		statFilter.setMergeSql(true);
		return statFilter;
	}
	
	@Bean
	public Slf4jLogFilter slf4jLogFilter() {
		Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
		slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
		return slf4jLogFilter;
	}

	@Bean
	public WallFilter wallFilter() {
		String driverClass = env.getProperty("spring.datasource.driver-class-name");
		WallFilter wallFilter = new WallFilter();
		if (driverClass.toLowerCase().contains(DB_POSTGRE)) {
			wallFilter.setDbType(DB_POSTGRE);
		} else if (driverClass.toLowerCase().contains(DB_ORACLE)) {
			wallFilter.setDbType(DB_ORACLE);
		} else if (driverClass.toLowerCase().contains(DB_MYSQL)) {
			wallFilter.setDbType(DB_MYSQL);
		} else if (driverClass.toLowerCase().contains(DB_SQLSERVER)) {
			wallFilter.setDbType(DB_SQLSERVER);
		} else {
			wallFilter.setDbType(DB_ORACLE);
		}
		WallConfig wallConfig = new WallConfig();
		if (DB_POSTGRE.equals(wallFilter.getDbType())) {
			wallConfig.setDir("META-INF/druid/wall/postgres");
		} else {
			wallConfig.setDir("META-INF/druid/wall/" + wallFilter.getDbType());
		}
		wallConfig.setStrictSyntaxCheck(false);
		wallConfig.setFunctionCheck(false);
		wallConfig.init();
		wallFilter.setConfig(wallConfig);
		return wallFilter;
	}

	@Bean
	public List<Filter> druidFilters() {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(this.statFilter());
		filters.add(this.slf4jLogFilter());
		filters.add(this.wallFilter());
		return filters;
	}
	
	@Bean(initMethod = "init")
	public DruidDataSource dataSourceMaster() throws SQLException {
		DruidDataSource d = new DruidDataSource();
		this.fillDataSource(d);
		this.fillDataSourceDefault(d);
		return d;
	}
	
	/**
	 * Definition:填充数据源配置信息
	 * @param ds
	 * @Author: oblivion
	 * @Created date: 2017-7-31
	 */
	private void fillDataSource(DruidDataSource ds) {
		ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		ds.setUrl(env.getProperty("spring.datasource.url"));
		ds.setUsername(env.getProperty("spring.datasource.username"));
		String cryptPwd = env.getProperty("spring.datasource.password");
		ds.setPassword(this.cryptoTools.decode(cryptPwd));
		ds.setValidationQuery(this.getValidateQuery());
		String init = env.getProperty("spring.datasource.initial-size");
		String max = env.getProperty("spring.datasource.max-active");
		if (init != null && Integer.parseInt(init) > 0) {
			ds.setInitialSize(Integer.parseInt(init));
		}
		if (max != null && Integer.parseInt(max) > 0) {
			ds.setMaxActive(Integer.parseInt(max));
		}
	}

	/**
	 * Definition:填充数据源默认属性
	 * @param ds
	 * @return
	 * @Author: oblivion
	 * @Created date: 2017-7-26
	 */
	private void fillDataSourceDefault(DruidDataSource ds) {
		ds.setMinIdle(1);
		ds.setMaxWait(60000);
		ds.setTimeBetweenEvictionRunsMillis(60000);
		ds.setMinEvictableIdleTimeMillis(300000);
		ds.setValidationQuery(this.getValidateQuery());
		ds.setValidationQueryTimeout(5);
		ds.setTestWhileIdle(true);
		ds.setTestOnBorrow(true);
		ds.setTestOnReturn(true);
		ds.setPoolPreparedStatements(true);
		ds.setMaxPoolPreparedStatementPerConnectionSize(50);
		ds.setProxyFilters(this.druidFilters());
	}
	
	/**
	 * Definition:根据driverClass获取数据源验证查询语句
	 * @return
	 * @Author: oblivion
	 * @Created date: 2017-7-26
	 */
	private String getValidateQuery() {
		String driverClass = env.getProperty("spring.datasource.driver-class-name");
		if (DRIVER_POSTGRE.equals(driverClass)) {
			return "SELECT 'x'";
		} else if (DRIVER_ORACLE.equals(driverClass)) {
			return "SELECT 'x' FROM DUAL";
		} else if (DRIVER_MYSQL.equals(driverClass)) {
			return "SELECT 'x'";
		} else if (DRIVER_SQLSERVER.equals(driverClass)) {
			return "SELECT 'x'";
		} else {
			return "SELECT 'x'";
		}
	}
}
