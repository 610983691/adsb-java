package com.coulee.aicw.foundations.config.apidoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description: Swagger2 API文档生成配置<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Configuration
@EnableSwagger2
@ConditionalOnWebApplication
public class Swagger2Config {
	
	@Autowired
	private Environment env;

	/**
	 * Description: API信息<br> 
	 * Created date: 2018年2月6日
	 * @return
	 * @author oblivion
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any()).build();
	}

	/**
	 * Description: API基本信息<br> 
	 * Created date: 2018年2月6日
	 * @return
	 * @author oblivion
	 */
	private ApiInfo apiInfo() {
		String appName = env.getProperty("spring.application.name");
		String version = env.getProperty("info.version", "1.0");
		return new ApiInfoBuilder().title(appName + " 服务的RESTful APIs")// API 标题
//				.description(appName + " 服务的RESTful APIs")// API描述
				.contact(new Contact("coulee", "http://www.coulee.com", "abc@coulee.com"))// 联系人
				.version(version)// 版本号
				.build();
	}

}
