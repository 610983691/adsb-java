package com.coulee.aicw.collectanalyze.collect.executer;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;

/**
 * Description:执行类创建工厂
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-15
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ExecuterFactory {

	/**
	 * Definition:获取执行类
	 * @param type
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public static Executer getExecuter(String type) throws ExecuterException {
		Executer executer = null;
		if (CollectConstants.SCRIPT_COMMON.equals(type)) {
			executer = new CommonExecuter();
		} else if (CollectConstants.SCRIPT_SHELL.equals(type)) {
			executer = new ShellExecuter();
		} else if (CollectConstants.SCRIPT_SQL.equals(type)) {
			executer = new SqlExecuter();
		} else if (CollectConstants.SCRIPT_INTERACT.equals(type)) {
			executer = new InteractExecuter();
		} else if (CollectConstants.SCRIPT_EXTEND.equals(type)) {
			executer = new ExtendExecuter();
		} else {
			throw new ExecuterException(ErrorCode.NO_SUPPORT_EXECUTER + "");
		}
		return executer;
	}
	
}
