package com.coulee.aicw.collectanalyze.collect.executer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.foundations.utils.common.StringTools;

/**
 * Description:SQL脚本执行类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class SqlExecuter extends AbstractExecuter {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public ExecuteResult execute(CommandArg commandArg)
			throws ExecuterException {
		if (StringTools.isEmpty(commandArg.getCmd())) {
			this.throwExecuterException(ErrorCode.EXECUTE_SCRIPT_NULL);
		}
		String[] sqls = commandArg.getCmd().split("\n");
		if (sqls == null || sqls.length == 0) {
			this.throwExecuterException(ErrorCode.EXECUTE_SCRIPT_NULL);
		}
		ExecuteResult result = new ExecuteResult();
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		StringBuffer oriResult = new StringBuffer();
		for (String sql : sqls) {
			sql = sql.trim();
			if (sql.endsWith(";")) {
				sql = sql.substring(0, sql.length() - 1);
			}
			try {
				if (sql.toLowerCase().startsWith("select") || sql.toLowerCase().startsWith("show")) {
					List<Map<String, String>> list = this.jdbcProtocol.executeQuerySql(sql);
					if (list != null && !list.isEmpty()) {
						resultList.addAll(list);
						oriResult.append(this.buildOriSqlResult(list)).append("\r\n\r\n");
					}
				} else {
					boolean b = this.jdbcProtocol.executeSql(sql);
					Map<String, String> resultMap = new HashMap<String, String>();
					resultMap.put("EXECUTE_RESULT", String.valueOf(b));
					List<Map<String, String>> tmpList = new ArrayList<>();
					oriResult.append(this.buildOriSqlResult(tmpList)).append("\r\n\r\n");
					resultList.addAll(tmpList);
				}
			} catch (ProtocolException e) {
				e.printStackTrace();
				logger.error("execute sql error, sql is [{}], error is {}", sql, e.getMessage());
				throw new ExecuterException(e.getMessage());
			}
		}
		if (resultList != null && !resultList.isEmpty()) {
			result.setSelectSqlResult(resultList);
			result.setResult(oriResult.toString());
		}
		return result;
	}

}
