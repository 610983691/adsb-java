package com.coulee.aicw.collectanalyze.collect.driver;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.executer.Executer;
import com.coulee.aicw.collectanalyze.collect.executer.ExecuterFactory;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.collectanalyze.collect.valueobject.LoginArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.OpenArg;

/**
 * Description:Windows主机驱动类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class WinHostDriver extends AbstractDriver {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private String ip;
	
	private int port;
	
	@Override
	public boolean login(OpenArg openArg, LoginArg loginArg)
			throws DriverException {
		this.checkArgs(openArg, loginArg);
		try {
			this.ip = openArg.getIp();
			this.port = openArg.getPort();
			this.prompt = loginArg.getPrompt();
			if (StringUtils.isEmpty(openArg.getEncode())) {
				this.setEncode("GBK"); //windows默认GBK编码
			} else {
				this.setEncode(openArg.getEncode());
			}
			this.protocol.connect(ip, port, openArg.getConnectTimeout(), openArg.getEchoTimeout());
			this.promptLine = this.protocol.authentication(loginArg.getUsername(), loginArg.getPassword(), 
					new String[] { loginArg.getPrompt() });
			logger.info("login {}:{} successful, user promptline is {} ", new Object[] { ip, port, promptLine });
			return true;
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("login {}:{} failure, error msg : {}", new Object[]{ip, port, ErrorCode.msg(e.getMessage())});
			throw new DriverException(e.getMessage());
		}
	}

	@Override
	public boolean doJumpLogin(OpenArg openArg, LoginArg loginArg, boolean isLast) throws DriverException {
		this.ip = openArg.getIp();
		this.port = openArg.getPort();
		String cmd = this.buildJumpCmd(openArg, loginArg);
		try {
			this.protocol.sendCommand(cmd, Protocol.CR);
			this.read2PasswordPrompt();
			this.protocol.sendPassword(loginArg.getPassword(), Protocol.CR);
			this.read2SuccessPrompt(loginArg.getPrompt());
			logger.info("jump login {}:{} successful, user promptline is {} ", new Object[] { openArg.getIp(), openArg.getPort(), promptLine });
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("jump login {}:{} failure, error msg : {}", new Object[]{ openArg.getIp(), openArg.getPort(), ErrorCode.msg(e.getMessage())});
			throw new DriverException(e.getMessage());
		}
		return true;
	}
	
	@Override
	public ExecuteResult executeCmd(CommandArg commandArg) throws DriverException {
		try {
			Executer executer = ExecuterFactory.getExecuter(commandArg.getType());
			executer.setProtocol(protocol);
			executer.setCurrentPrompt(prompt, promptLine);
			return executer.execute(commandArg);
		} catch (ExecuterException e) {
			e.printStackTrace();
			throw new DriverException(e.getMessage());
		}
	}

	@Override
	public void logout() {
		logger.info("logout {}:{} ... ", this.ip, this.port);
		try {
			if (this.protocol != null) {
				this.protocol.sendCommand("exit", Protocol.CR);
				this.protocol.close();
				this.protocol = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
