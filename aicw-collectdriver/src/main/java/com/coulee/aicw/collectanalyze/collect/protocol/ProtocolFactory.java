package com.coulee.aicw.collectanalyze.collect.protocol;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.protocol.jdbc.DatabaseJdbc;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh1.SSH1;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh2.SSH2;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh2.WinSSH2;
import com.coulee.aicw.collectanalyze.collect.protocol.telnet.Telnet;

/**
 * Description:协议创建工厂
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ProtocolFactory {

	/**
	 * Definition:根据协议类型创建具体协议实例
	 * @param type
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public static Protocol createProtocol(String type) {
		if (CollectConstants.SSH1.equals(type)) {
			return new SSH1();
		} else if (CollectConstants.SSH2.equals(type)) {
			return new SSH2();
		} else if (CollectConstants.WIN_SSH2.equals(type)) {
			return new WinSSH2();
		} else if (CollectConstants.TELNET.equals(type)) {
			return new Telnet();
		} else {
			return null;
		}
	}
	
	/**
	 * Definition:创建JDBC协议实例
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public static JdbcProtocol createJdbcProtocol() {
		return new DatabaseJdbc();
	}
}
