package com.coulee.aicw.collectanalyze.collect.parser;

import java.util.List;
import java.util.Map;

/**
 * Description:脚本解析接口
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface IScriptParser {

	/**
	 * Definition:解析
	 * @param content
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public List<Map<String, String>> parse (String content);
}
