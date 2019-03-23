package com.coulee.aicw.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coulee.aicw.client.fallback.InvokeTaskServiceClientError;
import com.coulee.aicw.entity.TaskInfoEntity;
import com.coulee.aicw.foundations.entity.Message;

import io.swagger.annotations.ApiOperation;

@Service
@FeignClient(name = "aicw-scheduler", fallbackFactory = InvokeTaskServiceClientError.class)
public interface InvokeTaskServiceClient {
	/**
	 * Description: 增加调度<br> 
	 * Created date: 2018年9月27日
	 * @param taskInfo
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "增加调度任务", notes = "增加调度任务，返回操作结果")
	@RequestMapping(value = "/scheduler/add", method = RequestMethod.POST)
	public Message add(@RequestBody TaskInfoEntity taskInfo);
}
