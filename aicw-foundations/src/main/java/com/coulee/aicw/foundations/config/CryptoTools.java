package com.coulee.aicw.foundations.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.coulee.aicw.foundations.config.conditions.CryptoToolsCondition;
import com.coulee.aicw.foundations.utils.crypt.Crypto;
import com.coulee.aicw.foundations.utils.crypt.SecetKeyUtils;

/**
 * Description: 加解密工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Component
@RefreshScope
@Conditional(CryptoToolsCondition.class)
public class CryptoTools {

	/**
	 * 配置中心配置的加解密方式
	 */
	@Value("${crypto.type}")
	private String cryptType;
	
	/**
	 * 配置中心配置的加解密密钥
	 */
	@Value("${crypto.key}")
	private String key;
	

	/**
	 * Description: 使用配置中心配置的加解密算法及密钥加密<br> 
	 * Created date: 2017年12月14日
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public String encode(String ori) {
		//对密钥进行解密
		String decodeKey = SecetKeyUtils.decodeKey(key);
		return encode(cryptType, decodeKey, ori);
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
		String decodeKey = SecetKeyUtils.decodeKey(key);
		return decode(cryptType, decodeKey, ori);
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
	
}
