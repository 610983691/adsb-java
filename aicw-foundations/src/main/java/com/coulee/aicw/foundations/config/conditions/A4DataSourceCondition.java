package com.coulee.aicw.foundations.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.coulee.aicw.foundations.utils.common.BaseTools;
/**
 * 同步4A信息，使用数据源
 * @author zyj
 *
 */
public class A4DataSourceCondition  implements Condition{
	/*
	 * LDAP连接URL common.ldap.url ldap://192.168.0.23:3456/
	 * 
	 * LDAP连接用户名 common.ldap.admin cn=Directory Manager
	 * 
	 * LDAP连接密码 common.ldap.password ••••••••••••••••••••••••••••••••
	 * 
	 * LDAP根节点 common.ldap.basedn ou=boco.com.cn LDAP初始连接数
	 * 
	 * common.ldap.init 1 LDAP最大连接数
	 * 
	 * common.ldap.max
	 */
	public static final String[] propertyMul = new String[] {"common.ldap.url","common.ldap.admin","common.ldap.password"
			,"common.ldap.basedn","common.ldap.init","common.ldap.max"};
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Environment env = context.getEnvironment();
		for(String item : propertyMul) {
			if(BaseTools.isNull(env.getProperty(item))) {
//				return false;
				System.out.println(item+"=="+env.getProperty(item));
				return false;
			}
		}
		return true;
	}

}
