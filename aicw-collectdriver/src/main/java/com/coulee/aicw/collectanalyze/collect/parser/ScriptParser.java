package com.coulee.aicw.collectanalyze.collect.parser;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ParserException;
import com.coulee.aicw.foundations.utils.common.StringTools;

/**
 * Description:动态编译解析方式，为提升效率弃用Groovy脚本解析
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-9
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ScriptParser implements Parser {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public List<Map<String, String>> parse(String original, String regexOrscript) throws ParserException {
		if (StringTools.isEmpty(regexOrscript)) {
			logger.error("SCRIPT_PARSER_CODE_NULL");
			throw new ParserException(String.valueOf(ErrorCode.SCRIPT_PARSER_CODE_NULL));
		}
		if (StringTools.isEmpty(original)) {
			return null;
		}
		try {
			Class<?> c = CompilerUtils.compiler(regexOrscript);
			IScriptParser parser = (IScriptParser) c.newInstance();
			return parser.parse(original);
		} catch (InstantiationException e) { //实例化错误
			logger.error("SCRIPT_PARSER_ERROR : " + e.getMessage());
			throw new ParserException(String.valueOf(ErrorCode.SCRIPT_PARSER_INSTANCE_ERROR));
		} catch (IllegalAccessException e) { //实例化错误
			logger.error("SCRIPT_PARSER_ERROR : " + e.getMessage());
			throw new ParserException(String.valueOf(ErrorCode.SCRIPT_PARSER_INSTANCE_ERROR));
		} catch (IllegalStateException e) { //编译错误
			logger.error("SCRIPT_PARSER_ERROR : Compilation failed : \r\n" + regexOrscript);
			throw new ParserException(String.valueOf(ErrorCode.SCRIPT_PARSER_INSTANCE_ERROR));
		}
	}

}
