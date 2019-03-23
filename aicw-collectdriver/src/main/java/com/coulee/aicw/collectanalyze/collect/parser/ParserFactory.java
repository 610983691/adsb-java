package com.coulee.aicw.collectanalyze.collect.parser;

/**
 * Description:创建解析类工厂
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ParserFactory {

	/**
	 * 正则解析方式
	 */
	public static final String REGEX_RULE = "DIC_PARSER_REGEX";
	
	/**
	 * 脚本解析方式
	 */
	public static final String SCRIPT_RULE = "DIC_PARSER_SCRIPT";
	
	/**
	 * Definition:创建解析对象
	 * @param rule
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public static Parser createParser(String rule) {
		if (REGEX_RULE.equals(rule)) {
			return new RegexParser();
		} else if (SCRIPT_RULE.equals(rule)) {
			return new ScriptParser();
		} else {
			return new RegexParser();
		}
	}
}
