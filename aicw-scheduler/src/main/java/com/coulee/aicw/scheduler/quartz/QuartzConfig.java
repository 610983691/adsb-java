package com.coulee.aicw.scheduler.quartz;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Description: QuartzConfig<br>
 * Create Date: 2018年9月27日<br>
 * Modified By：<br>
 * Modified Date：<br>
 * Why & What is modified：<br> 
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author LanChao
 * @version 1.0
 */
@Configuration
public class QuartzConfig {

	@Autowired
	private DruidDataSource dataSource;
	
	@Autowired
	private QuartzJobListener jobListener;
	
	@Autowired
	private QuartzTriggerListener triggerListener;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(QuartzJobFactory jobFactory) throws IOException {
		Properties properties = this.getQuartzProperties();
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(jobFactory);
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		schedulerFactoryBean.setStartupDelay(15);// 延迟15s，防止程序未完全启动
		schedulerFactoryBean.setQuartzProperties(properties);
		schedulerFactoryBean.setDataSource(this.dataSource);
		schedulerFactoryBean.setSchedulerName(properties.getProperty("org.quartz.scheduler.instanceName"));
//		schedulerFactoryBean.setAutoStartup(true);
		return schedulerFactoryBean;
	}

	public Properties getQuartzProperties() throws IOException {
		PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
		factoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	@Bean
	public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException, IOException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.getListenerManager().addJobListener(this.jobListener);
		scheduler.getListenerManager().addTriggerListener(this.triggerListener);
		scheduler.start();
		return scheduler;
	}

}
