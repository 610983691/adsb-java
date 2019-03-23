package com.coulee.aicw.scheduler.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description: QuartzTriggerListener<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
public class QuartzTriggerListener implements TriggerListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		logger.info("triggerFired ...");
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		logger.info("vetoJobExecution ...");
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		logger.info("triggerMisfired ...");
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		logger.info("triggerComplete ...");
	}

}
