package com.coulee.aicw.collectanalyze.collect.parser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coulee.aicw.foundations.utils.compiler.ICompiler;
import com.coulee.aicw.foundations.utils.compiler.impl.JdkCompiler;


/**
 * Description:动态编译工具类
 * Copyright (C) 2017 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2017-1-5
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class CompilerUtils {
	
	private static ICompiler compiler = new JdkCompiler();
	
	private static Map<String, Class<?>> map = new ConcurrentHashMap<String, Class<?>>();
	
	private static Pattern PATTERN = Pattern.compile("(?s)parse\\(String content\\)\\s+\\{(.+)\\}.+\\}");
	
	/**
	 * Definition:获得编译好的类
	 * @param classStr
	 * @return
	 * @throws IllegalStateException
	 * @Author: LanChao
	 * @Created date: 2017-1-5
	 */
	public synchronized static Class<?> compiler(String classStr) throws IllegalStateException {
		Matcher matcher = PATTERN.matcher(classStr);
		Class<?> c = null;
		if (matcher.find()) {
			String methodStr = matcher.group(1).trim();
			c = map.get(methodStr);
			if (c == null) {
				c = compiler.compile(classStr);
				map.put(methodStr, c);
			}
		} else {
			c = compiler.compile(classStr);
		}
		return c;
	}
}
