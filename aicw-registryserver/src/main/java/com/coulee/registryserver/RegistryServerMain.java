package com.coulee.registryserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Description: 注册中心启动入口<br>
 * Create Date: 2017年11月24日<br>
 * Modified By：<br>
 * Modified Date：<br>
 * Why & What is modified：<br> 
 * Copyright (C) 2017 coulee All Right Reserved.<br>
 * @author LanChao
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class RegistryServerMain {
	private static Logger logger = LoggerFactory.getLogger(RegistryServerMain.class);

	public static void main(String[] args) {
		SpringApplication.run(RegistryServerMain.class, args);
		logger.info("registry server started ...");
	}

}
