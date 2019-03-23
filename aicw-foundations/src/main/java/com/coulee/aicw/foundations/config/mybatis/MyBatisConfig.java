package com.coulee.aicw.foundations.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.coulee.aicw.foundations.config.conditions.MyBatisConfigCondition;
import com.coulee.aicw.foundations.utils.mybatis.MyBatisPageInterceptor;
import com.coulee.aicw.foundations.utils.mybatis.MyBatisSqlEscapeInterceptor;

/**
 * Description: Mybatis配置<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Configuration
@MyMapperScan
@Conditional(MyBatisConfigCondition.class)
public class MyBatisConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Autowired(required = false)
	private Interceptor[] interceptors;

	@Autowired(required = false)
	private DatabaseIdProvider databaseIdProvider;

	/**
	 * Definition:分页插件
	 * @return
	 * @Author: oblivion
	 * @Created date: 2017-7-26
	 */
	@Bean
	public MyBatisPageInterceptor myBatisPageInterceptor() {
		return new MyBatisPageInterceptor();
	}

	/**
	 * Definition:SQL特殊字符转义插件
	 * @return
	 * @Author: oblivion
	 * @Created date: 2017-7-26
	 */
	@Bean
	public MyBatisSqlEscapeInterceptor myBatisSqlEscapeInterceptor() {
		return new MyBatisSqlEscapeInterceptor();
	}
	
	/**
	 * Definition:MyBatis多数据库支持
	 * @return
	 * @Author: oblivion
	 * @Created date: 2017-7-26
	 */
	@Bean
	public VendorDatabaseIdProvider vendorDatabaseIdProvider() {
		VendorDatabaseIdProvider v = new VendorDatabaseIdProvider();
		Properties properties = new Properties();
		properties.put("Oracle", "oracle");
		properties.put("PostgreSQL", "postgresql");
		properties.put("MySQL", "mysql");
		v.setProperties(properties);
		return v;
	}

	@Bean
	public SqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setVfs(SpringBootVFS.class);
		if (!StringUtils.isEmpty(this.properties.getConfigLocation())) {
			sqlSessionFactory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		sqlSessionFactory.setConfiguration(properties.getConfiguration());
		if (this.interceptors != null && this.interceptors.length > 0) {
			sqlSessionFactory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			sqlSessionFactory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (!StringUtils.isEmpty(this.properties.getTypeAliasesPackage())) {
			sqlSessionFactory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (!StringUtils.isEmpty(this.properties.getTypeHandlersPackage())) {
			sqlSessionFactory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (this.properties.resolveMapperLocations() != null && this.properties.resolveMapperLocations().length > 0) {
			sqlSessionFactory.setMapperLocations(this.properties.resolveMapperLocations());
		}
		return sqlSessionFactory;
	}
}
