package com.coulee.aicw.scheduler.components;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.common.DateTools;
import com.coulee.aicw.scheduler.entity.InnerTaskInfoEntity;
import com.coulee.aicw.scheduler.entity.InnerTaskInfoEntity.Job;
import com.coulee.aicw.scheduler.entity.TaskInfoEntity;
import com.coulee.aicw.scheduler.quartz.SchedulerManager;

/**
 * Description: 服务启动后执行<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SchedulerManager schedulerManager;
	
	@Autowired
	private InnerTaskInfoEntity innerTaskInfoEntity;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.initInnerJobs();
	}
	
	/**
	 * Description: 加载内部作业计划<br> 
	 * Created date: 2018年9月28日
	 * @author oblivion
	 */
	private void initInnerJobs() {
		if (innerTaskInfoEntity == null) {
			logger.info("none inner jobs to load .");
			return;
		}
		List<Job> jobs = this.innerTaskInfoEntity.getJobs();
		if (jobs == null || jobs.isEmpty()) {
			logger.info("none inner jobs to load .");
			return;
		}
		for (Job job : jobs) {
			TaskInfoEntity taskInfo = this.convert(job);
			if (this.schedulerManager.checkExists(taskInfo.getJobId(), taskInfo.getJobType())) {
				Message message = this.schedulerManager.updateTask(taskInfo);
				logger.info("inner job {} do update result: {}", job.toString(), message.getMsg());
			} else {
				Message message = this.schedulerManager.addTask(taskInfo);
				logger.info("inner job {} do add result : {}", job.toString(), message.getMsg());
			}
		}
	}

	/**
	 * Description: 对象属性转化<br> 
	 * Created date: 2018年9月28日
	 * @param job
	 * @return
	 * @author oblivion
	 */
	private TaskInfoEntity convert(Job job) {
		TaskInfoEntity task = new TaskInfoEntity();
		task.setJobId(job.getJobId());
		task.setJobType(job.getJobType());
		task.setJobDescription(job.getJobDescription());
		task.setApplicationHost(job.getApplicationHost());
		task.setRequestPath(job.getRequestPath());
		task.setCronExpression(job.getCronExpression());
		task.setStartTime(DateTools.parser(job.getStartTime(), DateTools.COMMON_DATE_FORMAT));
		if (!StringUtils.isEmpty(job.getEndTime())) {
			task.setEndTime(DateTools.parser(job.getEndTime(), DateTools.COMMON_DATE_FORMAT));
		}
		return task;
	}

}
