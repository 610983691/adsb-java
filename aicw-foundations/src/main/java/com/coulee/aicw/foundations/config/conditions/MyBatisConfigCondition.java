package com.coulee.aicw.foundations.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * Description: MyBatis配置加载条件<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class MyBatisConfigCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		Environment env = context.getEnvironment();
		String mapperScan = env.getProperty("mybatis.mapper-scan");
		String mapperLocations = env.getProperty("mybatis.mapper-locations");
		if (StringUtils.isEmpty(mapperScan) || StringUtils.isEmpty(mapperLocations)) {
			return false;
		}
		return true;
	}

}
