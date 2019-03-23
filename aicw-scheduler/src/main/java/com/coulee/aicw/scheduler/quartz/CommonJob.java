package com.coulee.aicw.scheduler.quartz;

import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.common.JWTUtils;
import com.coulee.aicw.foundations.utils.common.ObjTransTools;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.scheduler.entity.TaskInfoEntity;

/**
 * Description: CommonJob<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class CommonJob implements Job {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings({"unchecked", "rawtypes" })
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jDataMap = context.getMergedJobDataMap();
		Object taskInfoObj = jDataMap.get(SchedulerManager.JOB_DATA_MAP_TASKINFO);
		Map tempMap = ObjTransTools.entity2map(taskInfoObj);
		TaskInfoEntity taskInfo = ObjTransTools.map2entity(tempMap, TaskInfoEntity.class);
		logger.info("execute task : {}", taskInfo.toString());
		taskInfo.setPreviousFireTime(context.getPreviousFireTime());
		taskInfo.setNextFireTime(context.getNextFireTime());
		if (context.getTrigger() instanceof CronTrigger) {
			CronTrigger cronTrigger = (CronTrigger) context.getTrigger();
			taskInfo.setCronExpression(cronTrigger.getCronExpression());
			taskInfo.setCronExpressionSummary(cronTrigger.getExpressionSummary());
		}
		String host = taskInfo.getApplicationHost();
		String path = taskInfo.getRequestPath();
		String url = "http://" + host + path;
		this.execute(url, taskInfo);
	}
	
	@Async
	private void execute(String url, TaskInfoEntity taskInfo) {
		try {
			HttpEntity<TaskInfoEntity> requestEntity = null;
			Map<String, String> headersMap = taskInfo.getHeaders();
			if (headersMap != null && !headersMap.isEmpty()) {
				HttpHeaders requestHeaders = new HttpHeaders();
				for (Map.Entry<String, String> entry : headersMap.entrySet()) {
			        requestHeaders.add(entry.getKey(), entry.getValue());
				}
		        requestEntity = new HttpEntity<TaskInfoEntity>(taskInfo, requestHeaders);
			} else {
				requestEntity = new HttpEntity<TaskInfoEntity>(taskInfo, null);
			}
			ResponseEntity<Message> message = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Message.class);
			logger.info("{} 调用成功，结果：{}", url, message.getBody().getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} 调用失败：{}", url, e.getMessage());
		}
//		try {
//			HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
//			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
//			String taskInfoJson = JsonTools.obj2json(taskInfo);
//			paramMap.add("taskInfoJson", taskInfoJson);
//			Map<String, String> headersMap = taskInfo.getHeaders();
//			if (headersMap != null && !headersMap.isEmpty()) {
//				HttpHeaders requestHeaders = new HttpHeaders();
//				for (Map.Entry<String, String> entry : headersMap.entrySet()) {
//			        requestHeaders.add(entry.getKey(), entry.getValue());
//			        paramMap.add(entry.getKey(), entry.getValue());
//				}
//				long expirationMill = taskInfo.getStartTime().getTime() + 10*1000;
//				String token = JWTUtils.create("FW_ACL_TYPE_REMOVE_TASK", expirationMill);
//				paramMap.add("token", "JWT "+token);
//		        requestEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,requestHeaders);
//			}else {
//				HttpHeaders headers = new HttpHeaders();
//				
//				long expirationMill = taskInfo.getStartTime().getTime() + 10*1000;
//				String token = JWTUtils.create("FW_ACL_TYPE_REMOVE_TASK", expirationMill);
//				paramMap.add("token", "JWT "+token);
//				requestEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
//			}
//
//	        ResponseEntity<Message> message = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Message.class, taskInfo);
//			logger.info("{} 调用成功，结果：{}", url, message.getBody().getMsg());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("{} 调用失败：{}", url, e.getMessage());
//		}
	}

}
