package com.coulee.foundations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Description: 配置中心启动入口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@SpringBootApplication
@ServletComponentScan
public class ConfigServerMain {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigServerMain.class);

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerMain.class, args);
		logger.info("config server started ...");
	}

}
