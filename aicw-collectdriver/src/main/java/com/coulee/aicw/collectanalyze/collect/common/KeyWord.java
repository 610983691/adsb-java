package com.coulee.aicw.collectanalyze.collect.common;

import org.apache.commons.lang.StringUtils;

/**
 * Description:关键字类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-3
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class KeyWord {

	/**
	 * 英文冒号
	 */
	public static final String SIGN_COLON_EN = ":";
	
	/**
	 * 中文冒号
	 */
	public static final String SIGN_COLON_CN = "：";
	
	/**
	 * 英文问号
	 */
	public static final String SIGN_QES_EN = "?";
	
	/**
	 * 中文问号
	 */
	public static final String SIGN_QES_CN = "？";
	
	/**
	 * Definition:判断以冒号结尾
	 * @param str
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	public static boolean endsWithColon(String str) {
		if (str == null) {
			return false;
		}
		str = str.trim();
		return (str.endsWith(SIGN_COLON_EN)) || (str.endsWith(SIGN_COLON_CN));
	}
	
	/**
	 * Definition:判断以指定提示符结尾，并返回结尾提示符
	 * @param str
	 * @param defaultPrompts
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-6
	 */
	public static String endsWith(String str, String[] defaultPrompts) {
		for (String prompt : defaultPrompts) {
			if (StringUtils.isNotEmpty(prompt) && str.trim().endsWith(prompt.trim())) {
				return prompt.trim();
			}
		}
		return null;
	}
}
