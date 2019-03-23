package com.coulee.aicw.collectanalyze.collect.executer;

import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;

/**
 * Description:脚本执行接口
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-15
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface Executer {

	/**
	 * Definition:设置非JDBC连接协议
	 * @param protocol
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public void setProtocol(Protocol protocol);
	
	/**
	 * Definition:设置JDBC连接协议
	 * @param jdbcProtocol
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public void setJdbcProtocol(JdbcProtocol jdbcProtocol);
	
	/**
	 * Definition:设置当前提示符及提示符行
	 * @param prompt
	 * @param promptLine
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public void setCurrentPrompt(String prompt, String promptLine);
	
	/**
	 * Definition:执行脚本
	 * @param commandArg 脚本参数
	 * @return
	 * @throws ExecuterException
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public ExecuteResult execute(CommandArg commandArg) throws ExecuterException;
}
