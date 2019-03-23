package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description:执行结果
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-15
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ExecuteResult implements Serializable {

	private static final long serialVersionUID = -5408650879053119404L;

	/**
	 * 普通命令结果
	 */
	private String result;
	
	/**
	 * 查询SQL结果
	 */
	private List<Map<String, String>> selectSqlResult;
	
	public ExecuteResult() {
	}
	
	public ExecuteResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<Map<String, String>> getSelectSqlResult() {
		return selectSqlResult;
	}

	public void setSelectSqlResult(List<Map<String, String>> selectSqlResult) {
		this.selectSqlResult = selectSqlResult;
	}

}
