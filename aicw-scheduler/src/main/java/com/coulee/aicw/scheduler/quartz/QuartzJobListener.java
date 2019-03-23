package com.coulee.aicw.scheduler.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: QuartzJobListener<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
public class QuartzJobListener implements JobListener {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		logger.info("jobToBeExecuted ...");
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("jobExecutionVetoed ...");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		logger.info("jobWasExecuted ...");
	}

}
