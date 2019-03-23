package com.coulee.aicw.foundations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.coulee.aicw.foundations.config.conditions.A4DataSourceCondition;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.crypt.Crypto;
import com.coulee.aicw.foundations.utils.crypt.SecetKeyUtils;
import com.coulee.aicw.foundations.utils.ldap.LDAPEnv;

@Component
/*
 *@RefreshScope 是解决资源文件与javaConfig的同步问题的 当boot环境中properties值改变 SpringCloud触发 Apollo触发
 * 需要对boot的java config进行刷新处理 因为config初始化之后不会再初始化了
 */
@RefreshScope
/*
 * @Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，并用于构建bean定义，初始化Spring容器
 */
@Configuration
@Conditional(A4DataSourceCondition.class)
public class A4DataSourceConfig {
	/**
	 * 加解密工具类
	 */
	@Autowired
	private CryptoTools cryptoTools;
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
	/**
	 * LDAP连接URL
	 */
	@Value("${common.ldap.url}")
	private String ldapUrl;
	
	/**
	 * LDAP连接用户名
	 */
	@Value("${common.ldap.admin}")
	private String ldapAdmin;
	/**
	 * LDAP连接密码
	 */
	@Value("${common.ldap.password}")
	private String ldapPassword;
	/**
	 * LDAP根节点
	 */
	@Value("${common.ldap.basedn}")
	private String ldapBaseDn;
	/**
	 * LDAP初始连接数
	 */
	@Value("${common.ldap.init}")
	private Integer ldapInit;
	/**
	 * LDAP最大连接数 
	 */
	@Value("${common.ldap.max}")
	private Integer ldapMax;
	
	/**
	 * LDAP里数据的密码，加密方式
	 */
	@Value("${common.ldap.crypto.type}")
	private String cryptoType;
	
	/**
	 * LDAP里数据的密码，加密方式
	 */
	@Value("${common.ldap.crypto.key}")
	private String cryptoKey;
	public LDAPEnv getLDAPEnv() {
		LDAPEnv ldapEnv = new LDAPEnv();
		ldapEnv.setUrl(ldapUrl);
		ldapEnv.setAdminUID(ldapAdmin);
		ldapEnv.setBaseDn(ldapBaseDn);
		if(!BaseTools.isNull(ldapPassword)) {
			String pwd = cryptoTools.decode(ldapPassword);
			ldapEnv.setAdminPWD(pwd);
		}
		
		return ldapEnv;
	}
	
	/**
	 * Description: 使用配置中心配置的加解密算法及密钥加密<br> 
	 * Created date: 2017年12月14日
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public String encode(String ori) {
		//对密钥进行解密
		/*
		 * 使用产品 公共解密方式 ，对 该秘钥进行解密
		 */
		String decodeKey = cryptoTools.decode(cryptoKey);
//		String decodeKey = SecetKeyUtils.decodeKey(cryptoKey);
		return encode(cryptoType, decodeKey, ori);
	}
	
	/**
	 * Description: 使用配置中心配置的加解密算法及密钥解密<br> 
	 * Created date: 2017年12月14日
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public String decode(String ori) {
		//对密钥进行解密
		/*
		 * 使用产品 公共解密方式 ，对 该秘钥进行解密
		 */
		String decodeKey = cryptoTools.decode(cryptoKey);
//		String decodeKey = SecetKeyUtils.decodeKey(cryptoKey);
		return decode(cryptoType, decodeKey, ori);
	}
	
	/**
	 * Description: 使用指定的加解密算法及密钥加密<br> 
	 * Created date: 2017年12月14日
	 * @param cryptType
	 * @param key
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public String encode(String cryptType, String key, String ori) {
		Crypto c = Crypto.getInstance(cryptType);
		return c.encrypt(ori, key);
	}
	
	/**
	 * Description: 使用指定的加解密算法及密钥解密<br> 
	 * Created date: 2017年12月14日
	 * @param cryptType
	 * @param key
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public String decode(String cryptType, String key, String ori) {
		Crypto c = Crypto.getInstance(cryptType);
		return c.decrypt(ori, key);
	}
	public String getLdapBaseDn() {
		return ldapBaseDn;
	}
}
