package com.coulee.aicw.collectanalyze.collect.executer;

import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;

/**
 * Description:自定义扩展脚本
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-15
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface ICustomScript {

	/**
	 * Definition:执行非JDBC协议脚本
	 * @param protocol 已经登录的连接协议
	 * @param prompt 已经登录设备提示符
	 * @param promptLine 已经登录设备的提示符行
	 * @param commandArg 脚本信息
	 * @return
	 * @throws Exception
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public ExecuteResult execute(Protocol protocol, String prompt, String promptLine, CommandArg commandArg) throws Exception;
	
	/**
	 * Definition:执行JDBC协议脚本
	 * @param protocol 已经登录的连接协议
	 * @param commandArg 脚本信息
	 * @return
	 * @throws Exception
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public ExecuteResult execute(JdbcProtocol protocol, CommandArg commandArg) throws Exception;
}
