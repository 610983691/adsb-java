package com.coulee.aicw.collectanalyze.collect.driver;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.common.KeyWord;
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
 * Description:网络设备驱动类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class NetdeviceDriver extends AbstractDriver {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private final String DEFAULT_EN = "enable";
	
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
			this.setEncode(openArg.getEncode());
			this.protocol.connect(ip, port, openArg.getConnectTimeout(), openArg.getEchoTimeout());
			this.promptLine = this.protocol.authentication(loginArg.getUsername(), loginArg.getPassword(), 
					new String[] { loginArg.getPrompt() });
			logger.info("login {}:{} successful, user promptline is {} ", new Object[] { ip, port, promptLine });
			if (!loginArg.isSwitch()) { //不需切入特权
				return true;
			}
			if (this.enable(loginArg)) {
				logger.info("login {}:{} with enable mode successful, user promptline is {} ", new Object[] { ip, port, promptLine });
				return true;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("login {}:{} failure, error msg : {}", new Object[]{ip, port, ErrorCode.msg(e.getMessage())});
			throw new DriverException(e.getMessage());
		}
		return false;
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
			if (isLast && loginArg.isSwitch()) {
				return this.enable(loginArg);
			}
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
	
	/**
	 * Definition:切入特权模式
	 * @param loginArg
	 * @return
	 * @throws ProtocolException 
	 * @throws DriverException 
	 * @Author: LanChao
	 * @Created date: 2016-12-23
	 */
	private boolean enable(LoginArg loginArg) throws ProtocolException, DriverException {
		String enCmd = this.getEnCmd(loginArg);
		this.protocol.sendCommand(enCmd, Protocol.CR);
		String echo = this.protocol.read2KeyWordEcho(enCmd, KeyWord.SIGN_COLON_EN, KeyWord.SIGN_COLON_CN, 
				this.prompt, this.promptLine, loginArg.getAdminPrompt());
		this.protocol.sendPassword(loginArg.getAdminPassword(), Protocol.CR);
		this.sleep(2000);
		int count = 0;
		echo = "";
		while(true) {
			String temp = this.protocol.readEcho();
			echo += temp;
			String prompt = KeyWord.endsWith(echo.trim(), new String[] { KeyWord.SIGN_COLON_EN, KeyWord.SIGN_COLON_CN, this.prompt,
					this.promptLine, loginArg.getAdminPrompt() });
			if (prompt != null) {
				if (this.isPasswordPrompt(echo)) { //密码错误
					logger.error("enable failed because of en password error");
					this.throwDriverException(ErrorCode.USER_PWD_ERROR);
				}
				if (prompt.equals(loginArg.getAdminPrompt())) {
					this.prompt = prompt;
					this.promptLine = checkPrompt(echo, prompt);
					break;
				}
				logger.error("enable failed because of en prompt error, receive prompt is {}", prompt);
				this.throwDriverException(ErrorCode.SU_PROMPT_ERROR);
			}
			if (temp.trim().equals("")) {
				count++;
				logger.debug("empty echo count is:" + count);
				if (count > 50) {
					logger.error("Reading echo by prompt quited forcibly...");
					this.throwDriverException(ErrorCode.SU_PROMPT_ERROR);
				}
				this.sleep(100);
			} else {
				count = 0;
			}
	    }
		return true;
	}
	
	/**
	 * Definition:获取切换命令
	 * @param loginArg
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-23
	 */
	private String getEnCmd(LoginArg loginArg) {
		String enCmd = loginArg.getSwitchCommand();
		if (StringUtils.isEmpty(enCmd)) {
			enCmd = DEFAULT_EN;
		}
		return enCmd;
	}

}
