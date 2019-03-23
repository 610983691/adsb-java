package com.coulee.aicw.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


/**
 * Description: 启动入口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAsync
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class StartSchedulerMain {
	
	private static Logger logger = LoggerFactory.getLogger(StartSchedulerMain.class);
	
	@Bean
    @LoadBalanced
    public RestTemplate cteateRestTemplate(){
        return new RestTemplate();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(StartSchedulerMain.class, args);
		logger.info("satp-system service started ...");
	}

}
