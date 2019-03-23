package com.coulee.aicw.collectanalyze.collect.driver;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.common.KeyWord;
import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.LoginArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.OpenArg;

/**
 * Description:设备驱动抽象类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public abstract class AbstractDriver implements Driver {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	protected Protocol protocol;
	
	protected JdbcProtocol jdbcProtocol;
	
	/**
	 * 提示符
	 * 如 #
	 */
	protected String prompt = "";
	
	/**
	 * 提示符行
	 * 如 C:\Users\XXXX>
	 */
	protected String promptLine = "";
	
	@Override
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public void setJdbcProtocol(JdbcProtocol jdbcProtocol) {
		this.jdbcProtocol = jdbcProtocol;
	}

	/**
	 * Definition:构造跳转指令
	 * @param openArg 设备连接参数
	 * @param loginArg 设备登录参数
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	protected String buildJumpCmd(OpenArg openArg, LoginArg loginArg) throws DriverException {
		if (StringUtils.isNotEmpty(loginArg.getJumpCommand())) {
			return loginArg.getJumpCommand();
		}
		String protocol = openArg.getProtocol();
		StringBuffer sb = new StringBuffer();
		if (CollectConstants.SSH1.equals(protocol)) { //ssh -1 -l name -p port ip
			sb.append("ssh -1 -l ").append(loginArg.getUsername())
					.append(" -p ").append(openArg.getPort()).append(" ").append(openArg.getIp());
		} else if (CollectConstants.SSH2.equals(protocol) || CollectConstants.WIN_SSH2.equals(protocol)) { //ssh -l name -p port ip
			sb.append("ssh -l ").append(loginArg.getUsername())
					.append(" -p ").append(openArg.getPort()).append(" ").append(openArg.getIp());
		} else if (CollectConstants.TELNET.equals(protocol)) { //telnet -l name ip port
			sb.append("telnet -l ").append(loginArg.getUsername())
					.append(" ").append(openArg.getIp()).append(" ").append(openArg.getPort());
		} else {
			this.throwDriverException(ErrorCode.UNSUPPORT_JUMP_PROTOCOL);
		}
		return sb.toString();
	}
	
	
	/**
	 * Definition:抛出设备驱动层异常
	 * @param errorCode 错误码
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void throwDriverException(int errorCode) throws DriverException {
		throw new DriverException(String.valueOf(errorCode));
	}
	
	/**
	 * Definition:抛出设备驱动层异常
	 * @param errorCode 错误码
	 * @param e 异常
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void throwDriverException(int errorCode, Exception e) throws DriverException {
		throw new DriverException(String.valueOf(errorCode), e);
	}

	/**
	 * Definition:线程等待
	 * @param millis 毫秒数
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Definition:设置编码
	 * @param encode
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	protected void setEncode(String encode) {
		if (encode != null && !"".equals(encode)) {
			if (this.protocol != null) {
				this.protocol.setEncode(encode);
			}
			if (this.jdbcProtocol != null) {
				this.jdbcProtocol.setEncode(encode);
			}
		}
	}
	
	/**
	 * Definition:输入ssh或telnet命令后，等待输入密码提示
	 * @throws ProtocolException
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	protected void read2PasswordPrompt() throws ProtocolException, DriverException {
		String echo = "";
		int count = 0;
		this.sleep(3000);
		while(true) {
			String temp = this.protocol.readEcho();
			echo += temp;
			//网络连接异常
			if (echo.toLowerCase().contains("connection refused")
					|| echo.toLowerCase().contains("no route to host")
					|| echo.toLowerCase().contains("connection timed out")
					|| echo.toLowerCase().contains("bad file number")
					|| echo.toLowerCase().contains("连接失败")) {
				this.throwDriverException(ErrorCode.JUMP_LOGIN_CONNECTION_ERROR);
			}
			//SSH提示 保存RSA指纹 Are you sure you want to continue connecting (yes/no)?
			if (echo.toLowerCase().trim().endsWith("(yes/no)?")
					|| echo.toLowerCase().trim().endsWith("(yes/no)？")) {
				this.protocol.sendCommand("yes", Protocol.CR);
			}
			//SSH目标设备RSA公钥失效
			if (echo.contains("Host key verification failed")) {
				this.throwDriverException(ErrorCode.RSA_HOST_KEY_ERROR);
			}
			//Telnet提示 您将要把您的密码信息送到 Internet 区域内的一台远程计算机上。这可能不安全。您想发送吗(y/n):
			if (echo.toLowerCase().trim().endsWith("(y/n):")
					|| echo.toLowerCase().trim().endsWith("(y/n)：")) {
				this.protocol.sendCommand("y", Protocol.CR);
			}
			String colon = KeyWord.endsWith(echo.trim(), new String[] { KeyWord.SIGN_COLON_EN, KeyWord.SIGN_COLON_CN });
			if (colon != null) {
				break;
			}
			if (temp.trim().equals("")) {
				count++;
				logger.debug("empty echo count is:" + count);
				if (count > 50) {
					logger.error("Reading echo by prompt quited forcibly...");
					this.throwDriverException(ErrorCode.JUMP_LOGIN_RECEIVE_ECHO_ERROR);
				}
				this.sleep(100);
			} else {
				count = 0;
			}
		}
	}
	
	/**
	 * Definition:等待成功登录提示符
	 * @param prompt
	 * @throws ProtocolException
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	protected void read2SuccessPrompt(String prompt) throws ProtocolException, DriverException {
		String echo = "";
		int count = 0;
		this.sleep(3000);
		while(true) {
			String temp = this.protocol.readEcho();
			echo += temp;
			//密码错误
			if (echo.toLowerCase().contains("permission denied")
					|| echo.toLowerCase().contains("please try again")
					|| echo.toLowerCase().contains("login failed")) {
				this.throwDriverException(ErrorCode.JUMP_LOGIN_USER_PWD_ERROR);
			}
			//密码过期
			if (echo.toLowerCase().contains("your password has expired")) {
				logger.error("login password is expired. ");
				this.throwDriverException(ErrorCode.LOGIN_PASSWORD_EXPIRED);
			}
			//提示修改密码
			if (echo.trim().endsWith("The password needs to be changed. Change now? [Y/N]:")) {
				this.protocol.sendCommand("N", Protocol.CR);
			}
			String colon = KeyWord.endsWith(echo.trim(), new String[] { prompt });
			if (colon != null) {
				break;
			}
			if (temp.trim().equals("")) {
				count++;
				logger.debug("empty echo count is:" + count);
				if (count > 50) {
					logger.error("Reading echo by prompt quited forcibly...");
					this.throwDriverException(ErrorCode.JUMP_LOGIN_RECEIVE_ECHO_ERROR);
				}
				this.sleep(100);
			} else {
				count = 0;
			}
		}
		this.prompt = prompt;
		this.promptLine = this.checkPrompt(echo, prompt);
	}
	
	/**
	 * Definition:截取提示行
	 * @param echoResult
	 * @param prompt
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	protected String checkPrompt(String echoResult, String prompt) {
		String promptLine = null;
		String[] lineArray = echoResult.split("\n");
		for (int i = lineArray.length - 1; i >= 0; i--) {
			if (StringUtils.isEmpty(lineArray[i].trim())) {
				continue;
			}
			logger.debug("last not empty line is {}", lineArray[i]);
			if (!lineArray[i].trim().endsWith(prompt)) {
				break;
			}
			if (lineArray[i].trim().indexOf(prompt) == lineArray[i].trim().lastIndexOf(prompt)) {
				promptLine = lineArray[i].trim();
				break;
			}
			String[] promptLineArray = lineArray[i].trim().split(prompt);
			if ((promptLineArray.length == 2) && (promptLineArray[0].trim().equals(promptLineArray[1].trim()))) {
				promptLine = promptLineArray[0].trim() + this.prompt;
				logger.debug("two prompt line, lastLine is {}, promptLine is {}", lineArray[i], promptLine);
			}
			break;
		}
		if (promptLine == null) {
			promptLine = prompt;
		}
		return promptLine;
	}
	
	/**
	 * Definition:是否为输入密码提示
	 * @param echo
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-23
	 */
	protected boolean isPasswordPrompt(String echo) {
		if (!echo.trim().toLowerCase().endsWith("令:")
					&& !echo.trim().toLowerCase().endsWith("令：")
					&& !echo.trim().toLowerCase().endsWith("码:")
					&& !echo.trim().toLowerCase().endsWith("码：")
					&& !echo.trim().toLowerCase().endsWith("password:")
					&& !echo.trim().toLowerCase().endsWith("password：")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Description: 判断连接参数和登录参数是否正确<br> 
	 * Created date: 2017年11月28日
	 * @param openArg
	 * @param loginArg
	 * @throws DriverException
	 * @author LanChao
	 */
	protected void checkArgs(OpenArg openArg, LoginArg loginArg) throws DriverException {
		String ip = openArg.getIp();
		int port = openArg.getPort();
		if (StringUtils.isEmpty(ip) || port == 0) {
			logger.error("connect ip or port is null .");
			this.throwDriverException(ErrorCode.CONNECT_IP_PORT_NULL);
		}
		String username = loginArg.getUsername();
		String password = loginArg.getPassword();
		String prompt = loginArg.getPrompt();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			logger.error("login username or password is null .");
			this.throwDriverException(ErrorCode.LOGIN_NAME_PASSWORD_NULL);
		}
		if (StringUtils.isEmpty(prompt)) {
			logger.error("login prompt is null .");
			this.throwDriverException(ErrorCode.LOGIN_PROMPT_NULL);
		}
		if (loginArg.isSwitch()) {
			String adminPassword = loginArg.getAdminPassword();
			String adminPrompt = loginArg.getAdminPrompt();
			if (StringUtils.isEmpty(adminPassword)) {
				logger.error("admin login password is null .");
				this.throwDriverException(ErrorCode.ADMIN_LOGIN_PASSWORD_NULL);
			}
			if (StringUtils.isEmpty(adminPrompt)) {
				logger.error("admin login prompt is null .");
				this.throwDriverException(ErrorCode.ADMIN_LOGIN_PROMPT_NULL);
			}
		}
	}
}
