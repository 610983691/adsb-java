package com.coulee.aicw.collectanalyze.collect.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ParserException;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.common.RegexTools;

/**
 * Description:正则解析方式
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class RegexParser implements Parser {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public List<Map<String, String>> parse(String original, String regexOrscript) throws ParserException {
		if (BaseTools.isNull(regexOrscript)) {
			logger.error("SCRIPT_PARSER_REGEX_NULL");
			throw new ParserException(String.valueOf(ErrorCode.SCRIPT_PARSER_REGEX_NULL));
		}
		if (BaseTools.isNull(original)) {
			return null;
		}
		try {
			if (RegexTools.isGroupRegex(regexOrscript)) {
				return RegexTools.getMatchingByAlias(original, regexOrscript);
			} else {
				Pattern pattern = Pattern.compile(regexOrscript);
				Matcher matcher = pattern.matcher(original);
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				while (matcher.find()) {
					Map<String, String> map = new TreeMap<String, String>();
					for (int i = 0; i <= matcher.groupCount(); i++) {
						map.put("GROUP" + i, matcher.group(i).trim());
					}
					list.add(map);
				}
				return list;
			}
		} catch (Exception e) {
			logger.error("REGEX_PARSER_ERROR : " + e.getMessage());
			throw new ParserException(String.valueOf(ErrorCode.REGEX_PARSER_ERROR));
		}
	}
	
}
