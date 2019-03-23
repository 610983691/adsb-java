package com.coulee.aicw.collectanalyze.collect.driver;

import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.collectanalyze.collect.valueobject.LoginArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.OpenArg;

/**
 * Description:设备类型驱动类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface Driver {
	
	/**
	 * Definition:设置连接协议
	 * @param protocol
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public void setProtocol(Protocol protocol);
	
	/**
	 * Definition:设置数据库jdbc连接协议
	 * @param jdbcProtocol
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public void setJdbcProtocol(JdbcProtocol jdbcProtocol);
	
	/**
	 * Definition:登录设备
	 * @param openArg 连接参数
	 * @param loginArg 登录参数
	 * @return
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public boolean login(OpenArg openArg, LoginArg loginArg) throws DriverException;
	
	/**
	 * Definition:执行跳转登录
	 * @param openArg 本次连接参数
	 * @param loginArg 本次登录参数
	 * @param isLast 是否为最后一跳
	 * @return
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public boolean doJumpLogin(OpenArg openArg, LoginArg loginArg, boolean isLast) throws DriverException;
	
	/**
	 * Definition:执行脚本
	 * @param commandArg 脚本参数对象
	 * @return
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public ExecuteResult executeCmd(CommandArg commandArg) throws DriverException;
	
	/**
	 * Definition:登出设备
	 * 
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public void logout();
}
