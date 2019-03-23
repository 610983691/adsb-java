package com.coulee.aicw.foundations.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * Description: 加解密工具类初始化条件<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class CryptoToolsCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Environment env = context.getEnvironment();
		String type = env.getProperty("crypto.type");
		String key = env.getProperty("crypto.key");
		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(key)) {
			return false;
		}
		return true;
	}

}
