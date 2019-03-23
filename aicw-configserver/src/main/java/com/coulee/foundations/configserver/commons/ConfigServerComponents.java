package com.coulee.foundations.configserver.commons;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.coulee.aicw.foundations.utils.zookeeper.ZookeeperTools;
import com.coulee.foundations.ConfigServerConstants;

/**
 * Description: 配置中心常用bean组件实例化类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
public class ConfigServerComponents {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * Description: sqlite数据源<br>
	 * Created date: 2017年12月12日
	 * 
	 * @return
	 * @author oblivion
	 */
	@Bean
	@Primary
	public DataSource dataSource() {
		//使用druid数据源操作sqlite报错……
		org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
		String dbFilePath = ConfigServerUtils.getDbFilePath();
		logger.info("init sqlite db file {}", dbFilePath);
		datasource.setUrl("jdbc:sqlite:" + dbFilePath);
		datasource.setUsername("");
		datasource.setPassword("");
		datasource.setDriverClassName("org.sqlite.JDBC");
		datasource.setInitialSize(5);
		datasource.setMaxActive(15);
		datasource.setMinIdle(5);
		datasource.setMaxIdle(10);
		datasource.setMaxWait(60000);
		datasource.setTimeBetweenEvictionRunsMillis(60000);
		datasource.setMinEvictableIdleTimeMillis(300000);
		datasource.setValidationQuery("select 1");
		datasource.setTestWhileIdle(true);
		datasource.setTestOnBorrow(true);
		datasource.setTestOnReturn(true);
		return datasource;
	}

}
