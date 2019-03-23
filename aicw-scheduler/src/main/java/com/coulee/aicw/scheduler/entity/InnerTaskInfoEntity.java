package com.coulee.aicw.scheduler.entity;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.coulee.aicw.scheduler.components.YmlPropertySourceFactory;

/**
 * Description: 内置计划对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
@PropertySource(value = { "classpath:innerjobs.yml" }, factory = YmlPropertySourceFactory.class)
@ConfigurationProperties(prefix="inner")
public class InnerTaskInfoEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 7158397216174223113L;
	
	/**
	 * 所有Job对象集合
	 */
	private List<Job> jobs;
	
	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	/**
	 * Description: Job对象<br>
	 * Create Date: 2018年10月23日<br>
	 * Copyright (C) 2018 Coulee All Right Reserved.<br>
	 * @author oblivion
	 * @version 1.0
	 */
	public static class Job {
		/**
		 * 任务ID
		 */
		private String jobId;
		
		/**
		 * 任务类型
		 */
		private String jobType;
		
		/**
		 * Springcloud应用applicationName或ip:port
		 */
		private String applicationHost;
		
		/**
		 * 任务执行Path
		 */
		private String requestPath;

		/**
		 * 任务描述
		 */
		private String jobDescription;


		/**
		 * 任务Cron表达式 
		 */
		private String cronExpression;

		/**
		 * 任务开始时间
		 */
		private String startTime;

		/**
		 * 周期任务结束时间
		 */
		private String endTime;

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getJobType() {
			return jobType;
		}

		public void setJobType(String jobType) {
			this.jobType = jobType;
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

		public String getJobDescription() {
			return jobDescription;
		}

		public void setJobDescription(String jobDescription) {
			this.jobDescription = jobDescription;
		}

		public String getCronExpression() {
			return cronExpression;
		}

		public void setCronExpression(String cronExpression) {
			this.cronExpression = cronExpression;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		@Override
		public String toString() {
			return "InnerTaskInfoEntity [jobId=" + jobId + ", jobType=" + jobType + ", applicationHost=" + applicationHost
					+ ", requestPath=" + requestPath + ", jobDescription=" + jobDescription + ", cronExpression="
					+ cronExpression + ", startTime=" + startTime + ", endTime=" + endTime + "]";
		}
	}

}
