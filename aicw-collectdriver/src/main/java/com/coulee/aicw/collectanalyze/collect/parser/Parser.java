package com.coulee.aicw.collectanalyze.collect.parser;

import java.util.List;
import java.util.Map;

import com.coulee.aicw.collectanalyze.collect.exception.ParserException;

/**
 * Description:解析接口
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface Parser {
	
	/**
	 * Definition:解析数据
	 * @param original 原始数据
	 * @param regexOrscript 正则或解析脚本
	 * @return
	 * @throws ParserException
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public List<Map<String, String>> parse (String original, String regexOrscript) throws ParserException;
}
