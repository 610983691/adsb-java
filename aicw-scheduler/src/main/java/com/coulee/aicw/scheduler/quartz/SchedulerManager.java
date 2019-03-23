package com.coulee.aicw.scheduler.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.scheduler.entity.TaskInfoEntity;

/**
 * Description: SchedulerManager<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
public class SchedulerManager {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String JOB_DATA_MAP_TASKINFO = "JOB_DATA_MAP_TASKINFO";

	private final String JOBNAME_PREFIX = "jobName_";

	private final String JOBGROUP_PREFIX = "jobGroup_";

	/**
	 * 任务调度器
	 */
	@Autowired
	private Scheduler scheduler;

	/**
	 * Description: 构造任务名称<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskId
	 * @return
	 * @author oblivion
	 */
	private String getJobName(String taskId) {
		return JOBNAME_PREFIX + taskId;
	}

	/**
	 * Description: 构造任务组名称<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param group
	 * @return
	 * @author oblivion
	 */
	private String getJobGroup(String group) {
		return JOBGROUP_PREFIX + group;
	}

	/**
	 * Description: 构造JobKey<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 * @author oblivion
	 */
	private JobKey getJobKey(String jobName, String jobGroup) {
		return JobKey.jobKey(getJobName(jobName), getJobGroup(jobGroup));
	}

	/**
	 * Description: 构造TriggerKey<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 * @author oblivion
	 */
	private TriggerKey getTriggerKey(String jobName, String jobGroup) {
		return TriggerKey.triggerKey(getJobName(jobName), getJobGroup(jobGroup));
	}

	/**
	 * Description: 查询指定类型的计划<br> 
	 * Created date: 2018年9月28日
	 * @param jobType
	 * @return
	 * @author oblivion
	 */
	public List<TaskInfoEntity> listTask(String jobType) {
		List<TaskInfoEntity> list = new ArrayList<>();
		try {
			for (String groupJob : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
					if (!jobKey.getGroup().equals(jobType)) {
						continue;
					}
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
						JobDetail jobDetail = scheduler.getJobDetail(jobKey);
						TaskInfoEntity info = new TaskInfoEntity();
						info.setJobId(jobKey.getName().replace(this.JOBNAME_PREFIX, ""));
						info.setJobGroup(jobKey.getGroup());
						info.setJobName(jobKey.getName());
						info.setJobDescription(jobDetail.getDescription());
						info.setJobStatus(triggerState.name());
						info.setStartTime(trigger.getStartTime());
						info.setEndTime(trigger.getEndTime());
						info.setPreviousFireTime(trigger.getPreviousFireTime());
						info.setNextFireTime(trigger.getNextFireTime());
						if (trigger instanceof CronTrigger) {
							CronTrigger cronTrigger = (CronTrigger) trigger;
							info.setCronExpression(cronTrigger.getCronExpression());
							info.setCronExpressionSummary(cronTrigger.getExpressionSummary());
						}
						list.add(info);
					}
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Description: 注册任务调度<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskInfo
	 * @return
	 * @author oblivion
	 */
	public Message addTask(TaskInfoEntity taskInfo) {
		if (taskInfo == null || StringUtils.isEmpty(taskInfo.getJobId())
				|| StringUtils.isEmpty(taskInfo.getJobType())
				|| StringUtils.isEmpty(taskInfo.getApplicationHost())
				|| StringUtils.isEmpty(taskInfo.getRequestPath())) {
			logger.error("增加失败，计划调度基本信息不完整！{}", taskInfo.toString());
			return Message.newFailureMessage("增加失败，计划调度基本信息不完整！");
		}
		String taskId = taskInfo.getJobId();
		if (this.checkExists(taskId, taskInfo.getJobType())) {
			logger.error("增加失败，该计划调度任务已存在！{}", taskInfo.toString());
			return Message.newFailureMessage("增加失败，该计划调度任务已存在！");
		}
		String cronExpression = taskInfo.getCronExpression(); // cron表达式
		Date startDate = taskInfo.getStartTime(); // 开始执行时间
		Date endDate = taskInfo.getEndTime(); // 结束执行时间
		if (StringUtils.isEmpty(cronExpression) && startDate == null) {
			logger.error("增加失败，计划调度Cron表达式或开始时间为空！{}", taskInfo.toString());
			return Message.newFailureMessage("增加失败，计划调度Cron表达式或开始时间为空！");
		}
		TriggerKey triggerKey = this.getTriggerKey(taskId, taskInfo.getJobType());
		TriggerBuilder<?> builder;
		Trigger trigger;
		if (!StringUtils.isEmpty(cronExpression)) { // 使用Cron表达式进行周期调度
			if (!CronExpression.isValidExpression(cronExpression)) {
				logger.error("增加失败，计划调度Cron表达式不合法！{}", taskInfo.toString());
				return Message.newFailureMessage("增加失败，计划调度Cron表达式不合法！");
			}
			builder = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(
					CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing());
			if (startDate != null) {
				builder.startAt(startDate);
			}
			if (endDate != null) {
				builder.endAt(endDate);
			}
		} else { // 指定简单的执行日期，仅执行一次
			builder = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withSchedule(SimpleScheduleBuilder.simpleSchedule());
			if (startDate.before(new Date())) {
				builder.startNow();
			} else {
				builder.startAt(startDate);
			}
		}
		trigger = builder.build();
		JobDetail jobDetail = JobBuilder.newJob(CommonJob.class).withIdentity(getJobKey(taskId, taskInfo.getJobType()))
				.withDescription(taskInfo.getJobDescription()).build();
		jobDetail.getJobDataMap().put(JOB_DATA_MAP_TASKINFO, taskInfo);
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("增加失败！{}", taskInfo.toString());
			return Message.newFailureMessage("增加失败！" + e.getMessage(), taskInfo);
		}
		logger.info("增加成功！{}", taskInfo.toString());
		return Message.newSuccessMessage("增加成功！", taskInfo);
	}

	/**
	 * Description: 检测调度任务是否存在<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskId
	 * @param taskType
	 * @return
	 * @author oblivion
	 */
	public boolean checkExists(String taskId, String taskType) {
		JobKey jobKey = this.getJobKey(taskId, taskType);
		try {
			return this.scheduler.checkExists(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Description: 暂停任务调度<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskType
	 * @param taskId
	 * @return
	 * @author oblivion
	 */
	public Message pauseTask(String taskType, String taskId) {
		if (this.checkExists(taskId, taskType)) {
			try {
				this.scheduler.pauseTrigger(this.getTriggerKey(taskId, taskType));
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("暂停失败！任务ID：{}", taskId);
				return Message.newFailureMessage("暂停失败！");
			}
			logger.info("暂停成功！任务ID：{}", taskId);
			return Message.newSuccessMessage("暂停成功！");
		} else {
			logger.error("暂停失败，任务不存在！任务ID：{}", taskId);
			return Message.newFailureMessage("暂停失败，任务不存在！");
		}
	}

	/**
	 * Description: 恢复任务调度<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskType
	 * @param taskId
	 * @return
	 * @author oblivion
	 */
	public Message resumeTask(String taskType, String taskId) {
		if (this.checkExists(taskId, taskType)) {
			try {
				this.scheduler.resumeTrigger(this.getTriggerKey(taskId, taskType));
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("恢复失败！任务ID：{}", taskId);
				return Message.newFailureMessage("恢复失败！");
			}
			logger.info("恢复成功！任务ID：{}", taskId);
			return Message.newSuccessMessage("恢复成功！");
		} else {
			logger.error("恢复失败，任务不存在！任务ID：{}", taskId);
			return Message.newFailureMessage("恢复失败，任务不存在！");
		}
	}

	/**
	 * Description: 删除任务调度<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskType
	 * @param taskId
	 * @return
	 * @author oblivion
	 */
	public Message deleteTask(String taskType, String taskId) {
		if (this.checkExists(taskId, taskType)) {
			TriggerKey triggerKey = this.getTriggerKey(taskId, taskType);
			try {
				this.scheduler.pauseTrigger(triggerKey);
				this.scheduler.unscheduleJob(triggerKey);
				this.scheduler.deleteJob(this.getJobKey(taskId, taskType));
				logger.info("删除成功！任务ID：{}", taskId);
				return Message.newSuccessMessage("删除成功！");
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error("删除失败！任务ID：{}", taskId);
				return Message.newSuccessMessage("删除失败！");
			}
		} else {
			logger.error("删除失败，任务不存在！任务ID：{}", taskId);
			return Message.newFailureMessage("删除失败，任务不存在！");
		}
	}

	/**
	 * Description: 更新计划调度<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskInfo
	 * @return
	 * @author oblivion
	 */
	public Message updateTask(TaskInfoEntity taskInfo) {
		if (taskInfo == null || StringUtils.isEmpty(taskInfo.getJobId())
				|| StringUtils.isEmpty(taskInfo.getJobType())
				|| StringUtils.isEmpty(taskInfo.getApplicationHost())
				|| StringUtils.isEmpty(taskInfo.getRequestPath())) {
			logger.error("修改失败，计划调度基本信息不完整！{}", taskInfo.toString());
			return Message.newFailureMessage("修改失败，计划调度基本信息不完整！");
		}
		String taskId = taskInfo.getJobId();
		if (!this.checkExists(taskId, taskInfo.getJobType())) {
			logger.error("修改失败，计划调度任务不存在！{}", taskInfo.toString());
			return Message.newFailureMessage("修改失败，计划调度任务不存在！");
		}
		String cronExpression = taskInfo.getCronExpression(); // cron表达式
		Date startDate = taskInfo.getStartTime(); // 开始执行时间
		Date endDate = taskInfo.getEndTime(); // 结束执行时间
		if (StringUtils.isEmpty(cronExpression) && startDate == null) {
			logger.error("修改失败，计划调度Cron表达式或开始时间为空！{}", taskInfo.toString());
			return Message.newFailureMessage("修改失败，计划调度Cron表达式或开始时间为空！");
		}
		TriggerKey triggerKey = this.getTriggerKey(taskId, taskInfo.getJobType());
		TriggerBuilder<?> builder;
		Trigger trigger;
		if (!StringUtils.isEmpty(cronExpression)) { // 使用Cron表达式进行周期调度
			if (!CronExpression.isValidExpression(cronExpression)) {
				logger.error("修改失败，计划调度Cron表达式不合法！{}", taskInfo.toString());
				return Message.newFailureMessage("修改失败，计划调度Cron表达式不合法！");
			}
			builder = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(
					CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing());
			if (startDate != null) {
				builder.startAt(startDate);
			}
			if (endDate != null) {
				builder.endAt(endDate);
			}
		} else { // 指定简单的执行日期，仅执行一次
			builder = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withSchedule(SimpleScheduleBuilder.simpleSchedule());
			if (startDate.before(new Date())) {
				builder.startNow();
			} else {
				builder.startAt(startDate);
			}
		}
		trigger = builder.build();
		JobDetail jobDetail = JobBuilder.newJob(CommonJob.class).withIdentity(getJobKey(taskId, taskInfo.getJobType()))
				.withDescription(taskInfo.getJobDescription()).build();
		jobDetail.getJobDataMap().put(JOB_DATA_MAP_TASKINFO, taskInfo);
		Set<Trigger> triggerSet = new HashSet<>();
		triggerSet.add(trigger);
		try {
			scheduler.scheduleJob(jobDetail, triggerSet, true);
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("修改失败！{}", taskInfo.toString());
			return Message.newFailureMessage("修改失败！" + e.getMessage(), taskInfo);
		}
		logger.info("修改成功！{}", taskInfo.toString());
		return Message.newSuccessMessage("修改成功！", taskInfo);
	}

	/**
	 * Definition:获取任务状态<br>
	 * NONE：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除<br>
	 * NORMAL:正常状态<br>
	 * PAUSED：暂停状态<br>
	 * COMPLETE：触发器完成，但是任务可能还正在执行中<br>
	 * BLOCKED：线程阻塞状态<br>
	 * ERROR：出现错误<br>
	 * Created date: 2018年9月27日
	 * 
	 * @param taskType
	 * @param taskId
	 * @return
	 * @author oblivion
	 */
	public String getTaskStatus(String taskType, String taskId) {
		try {
			TriggerState state = this.scheduler.getTriggerState(getTriggerKey(taskId, taskType));
			return state.name();
		} catch (SchedulerException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

}
