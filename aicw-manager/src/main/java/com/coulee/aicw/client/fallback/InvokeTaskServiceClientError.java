package com.coulee.aicw.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.coulee.aicw.client.InvokeTaskServiceClient;
import com.coulee.aicw.entity.TaskInfoEntity;
import com.coulee.aicw.foundations.entity.Message;

import feign.hystrix.FallbackFactory;
@Component
public class InvokeTaskServiceClientError  implements FallbackFactory<InvokeTaskServiceClient> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public InvokeTaskServiceClient create(Throwable throwable) {
		logger.error(throwable.getMessage());
		return new InvokeTaskServiceClient() {

			@Override
			public Message add(TaskInfoEntity taskInfo) {
				return Message.newFailureMessage("调用新增计划失败,请检查计划服务是否启动");
			}
			
		};
	}

}
