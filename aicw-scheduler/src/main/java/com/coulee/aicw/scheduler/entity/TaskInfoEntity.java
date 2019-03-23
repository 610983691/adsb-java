package com.coulee.aicw.scheduler.entity;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 调度任务数据对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@ApiModel(value ="TaskInfoEntity", description = "调度任务数据对象")
public class TaskInfoEntity implements java.io.Serializable {

	private static final long serialVersionUID = 397574239741068793L;
	
	/**
	 * 任务ID
	 */
	@ApiModelProperty(value = "任务ID", required = true)
	private String jobId;
	
	/**
	 * 任务类型
	 */
	@ApiModelProperty(value = "任务类型", required = true)
	private String jobType;
	
	/**
	 * Springcloud应用applicationName或ip:port
	 */
	@ApiModelProperty(value = "Springcloud应用applicationName或ip:port")
	private String applicationHost;
	
	/**
	 * 任务执行Path
	 */
	@ApiModelProperty(value = "任务执行Path", required = true)
	private String requestPath;

	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称", hidden = true)
	private String jobName;

	/**
	 * 任务分组
	 */
	@ApiModelProperty(value = "任务分组", hidden = true)
	private String jobGroup;

	/**
	 * 任务描述
	 */
	@ApiModelProperty(value = "任务描述")
	private String jobDescription;

	/**
	 * 任务状态
	 */
	@ApiModelProperty(value = "任务状态", hidden = true)
	private String jobStatus;

	/**
	 * 任务Cron表达式 
	 */
	@ApiModelProperty(value = "任务Cron表达式 ")
	private String cronExpression;

	/**
	 * 任务Cron表达式 详情
	 */
	@ApiModelProperty(value = "任务Cron表达式 详情", hidden = true)
	private String cronExpressionSummary;

	/**
	 * 任务开始时间
	 */
	@ApiModelProperty(value = "任务开始时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**
	 * 周期任务结束时间
	 */
	@ApiModelProperty(value = "周期任务结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 上一次任务触发时间
	 */
	@ApiModelProperty(value = "上一次任务触发时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date previousFireTime;

	/**
	 * 下一次任务触发时间
	 */
	@ApiModelProperty(value = "下一次任务触发时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nextFireTime;
	
	/**
	 * 任务触发回调时的header头信息
	 */
	@ApiModelProperty(value = "任务触发回调时的header头信息")
	private Map<String, String> headers;
	
	/**
	 * 任务触发回调时需要缓存的数据
	 */
	@ApiModelProperty(value = "任务触发回调时需要缓存的数据")
	private Map<String, Object> contents;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getCronExpressionSummary() {
		return cronExpressionSummary;
	}

	public void setCronExpressionSummary(String cronExpressionSummary) {
		this.cronExpressionSummary = cronExpressionSummary;
	}

	public String getApplicationHost() {
		return applicationHost;
	}

	public void setApplicationHost(String applicationHost) {
		this.applicationHost = applicationHost;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "TaskInfoEntity [jobId=" + jobId + ", jobType=" + jobType + ", applicationHost=" + applicationHost
				+ ", requestPath=" + requestPath + ", jobName=" + jobName + ", jobGroup=" + jobGroup
				+ ", jobDescription=" + jobDescription + ", jobStatus=" + jobStatus + ", cronExpression="
				+ cronExpression + ", cronExpressionSummary=" + cronExpressionSummary + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", previousFireTime=" + previousFireTime + ", nextFireTime=" + nextFireTime
				+ "]";
	}

}
