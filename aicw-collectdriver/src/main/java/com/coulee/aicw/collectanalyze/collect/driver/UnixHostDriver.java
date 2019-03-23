package com.coulee.aicw.collectanalyze.collect.driver;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
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
import com.coulee.aicw.collectanalyze.collect.valueobject.InteractiveCmd;
import com.coulee.aicw.collectanalyze.collect.valueobject.LoginArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.OpenArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.UnixSudoInfo;
import com.coulee.aicw.foundations.utils.common.StringTools;

/**
 * Description:Unix主机驱动类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class UnixHostDriver extends AbstractDriver {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	/**
	 * 自定义提示符
	 */
	private final String FINAL_PROMPT = "#AICW-PROMPT#";
	
	/**
	 * 默认切特权命令
	 */
	private final String DEFAULT_SU = "su -";
	
	/**
	 * 默认特权用户
	 */
	private final String DEFAULT_ADMIN = "root";
	
	/**
	 * 是否sudo模式
	 */
	private boolean isSudoMode = false;
	
	/**
	 * 测试sudo命令是否输入密码
	 */
	private final String SUDO_TEST_CMD = "sudo -v";
	
	/**
	 * sudo用户名
	 */
	private String sudoer = null;
	
	/**
	 * sudo用户密码
	 */
	private String sudoerPassword = null;
	
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
			if (loginArg.isSudoMode()) {
				this.isSudoMode = loginArg.isSudoMode();
				this.sudoer = loginArg.getUsername();
				this.sudoerPassword = loginArg.getPassword();
			}
			this.protocol.connect(ip, port, openArg.getConnectTimeout(), openArg.getEchoTimeout());
			this.promptLine = this.protocol.authentication(loginArg.getUsername(), loginArg.getPassword(), 
					new String[] { loginArg.getPrompt() });
			logger.info("login {}:{} successful, user promptline is {} ", new Object[] { ip, port, promptLine });
			if (!loginArg.isSwitch()) { //不需切入root
				this.changeEvn();
				this.changePrompt();
				return true;
			}
			this.changeEvn();
			if(su(loginArg)) { //su操作
				this.changePrompt();
				logger.info("login {}:{} with su mode successful, user promptline is {} ", new Object[] { ip, port, promptLine });
				return true;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("login {}:{} failure, error msg : {}", new Object[]{ip, port, ErrorCode.msg(e.getMessage())});
			throw new DriverException(e.getMessage());
		}
		return false;
	}
	
	/**
	 * Definition:执行su操作
	 * @param loginArg
	 * @return
	 * @throws ProtocolException
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	private boolean su(LoginArg loginArg) throws ProtocolException, DriverException {
		String suCmd = this.getSuCmd(loginArg);
		this.protocol.sendCommand(suCmd, Protocol.CR);
		String echo = this.protocol.read2KeyWordEcho(suCmd, KeyWord.SIGN_COLON_EN, KeyWord.SIGN_COLON_CN, 
				this.prompt, this.promptLine, loginArg.getAdminPrompt());
		if (!this.isPasswordPrompt(echo)) {
			if (userNotExists(echo, loginArg.getAdminUsername())) {
				logger.error("su failed because of su user [{}] does not exist", loginArg.getAdminUsername());
				this.throwDriverException(ErrorCode.SU_USER_NOT_EXIST);
			}
			String prompt = KeyWord.endsWith(echo, new String[] { loginArg.getAdminPrompt() });
			if (prompt == null) {
				this.throwDriverException(ErrorCode.SU_PROMPT_ERROR);
			}
			this.promptLine = checkPrompt(echo, prompt);
			return true; //不需密码直接切入特权
		}
		this.protocol.sendPassword(loginArg.getAdminPassword(), Protocol.CR);
		this.sleep(2000);
		int count = 0;
		echo = "";
		while(true) {
			String temp = this.protocol.readEcho();
			echo += temp;
	        if (userNotExists(echo, loginArg.getAdminUsername())) {
				logger.error("su failed because of su user [{}] does not exist", loginArg.getAdminUsername());
				this.throwDriverException(ErrorCode.SU_USER_NOT_EXIST);
			}
			String prompt = KeyWord.endsWith(echo.trim(), new String[] { KeyWord.SIGN_COLON_EN, KeyWord.SIGN_COLON_CN, this.prompt,
					this.promptLine, loginArg.getAdminPrompt() });
			if (prompt != null) {
				if (this.isPasswordPrompt(echo)) { //密码错误
					logger.error("su failed because of su password error");
					this.throwDriverException(ErrorCode.USER_PWD_ERROR);
				}
				String promptLine = checkPrompt(echo, prompt);
				if (promptLine.trim().equals(this.promptLine.trim()) && (echo.contains("su:")
								|| echo.contains("3004-300") || echo.contains("[compat]:")
								|| echo.contains("Authentication is denied") || echo.contains("3004-501"))) {
					//如果截取的提示符行与su之前提示符行相同，且包含密码错误信息
					logger.info("su failed because of su password error");
					this.throwDriverException(ErrorCode.USER_PWD_ERROR);
				}
				if (prompt.equals(loginArg.getAdminPrompt())) {
					this.prompt = prompt;
					this.promptLine = checkPrompt(echo, prompt);
					break;
				}
				logger.error("su failed because of su prompt error, receive prompt is {}", prompt);
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
	 * Definition:构造su命令
	 * @param loginArg
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	private String getSuCmd(LoginArg loginArg) {
		String suCmd = loginArg.getSwitchCommand();
		if (StringUtils.isEmpty(suCmd)) {
			suCmd = DEFAULT_SU;
		}
		String suUser = loginArg.getAdminUsername();
		if (StringUtils.isEmpty(suUser)) {
			suUser = DEFAULT_ADMIN;
		}
		return suCmd + " " + suUser;
	}
	
	/**
	 * Definition:切换当前环境：shell切换至bash、语言环境切换至英文
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	private void changeEvn() throws ProtocolException {
		this.protocol.sendCommand("/bin/bash", Protocol.CR);
		this.protocol.read2KeyWordEcho("/bin/bash", prompt, this.promptLine);
		this.protocol.sendCommand("LANG=c", Protocol.CR);
		this.protocol.read2KeyWordEcho("LANG=c", prompt, this.promptLine);
	}
	
	/**
	 * Definition:提示符切换至#AICW-PROMPT#
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-14
	 */
	private void changePrompt() throws ProtocolException {
		this.protocol.sendCommand("PS1=" + FINAL_PROMPT, Protocol.CR);
		String echo = this.protocol.read2KeyWordEcho("PS1=" + FINAL_PROMPT, FINAL_PROMPT);
		if (echo.trim().endsWith(FINAL_PROMPT)) {
			logger.info("change {}:{} prompt from {} to {}", new Object[]{ ip, port, this.promptLine, FINAL_PROMPT });
			this.prompt = FINAL_PROMPT;
			this.promptLine = FINAL_PROMPT;
		}
	}
	
	/**
	 * Definition:判断用户不存在
	 * @param echo 
	 * @param suUser
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	private boolean userNotExists(String echo, String suUser) {
		if (echo == null || "".equals(echo.trim()) || suUser == null || "".equals(suUser)) {
			return false;
		}
		String[] lines = echo.toLowerCase().split("\n");
		suUser = suUser.toLowerCase();
		for (String line : lines) {
			if ((line.contains(suUser) && line.contains("不存在"))
					|| (line.contains(suUser) && line.contains("does not exist"))
					|| (line.contains(suUser) && line.contains("未知的 id"))
					|| (line.contains(suUser) && line.contains("unknown id"))
					|| (line.contains("su:") && line.contains("不存在"))
					|| (line.contains("su:") && line.contains("does not exist"))
					|| (line.contains("su:") && line.contains("未知的 id"))
					|| (line.contains("su:") && line.contains("unknown id"))
					|| (line.contains("3004-500") && line.contains("does not exist"))) {
				return true;
			}
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
			if (loginArg.isSwitch()) {
				this.changeEvn();
				this.su(loginArg);
				logger.info("jump login {}:{} with su mode successful, user promptline is {} ", new Object[] { openArg.getIp(), openArg.getPort(), promptLine });
			}
			if (isLast) { //如果此跳为登录目标设备
				this.changePrompt();
			} else {
				this.changeEvn();
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
			if (this.isSudoMode) {
				this.processSudoMode(commandArg);
			}
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
	 * Definition:针对unix的sudo模式特殊处理<br>
	 * 在普通脚本和交互脚本命令前加入sudo，shell类型脚本则由ShellExecuter自行处理<br>
	 * 脚本处理完毕后，进行sudo -v命令测试是否需要输入密码
	 * @param commandArg
	 * @throws DriverException 
	 * @Author: LanChao
	 * @Created date: 2017年11月17日
	 */
	private void processSudoMode(CommandArg commandArg) throws DriverException {
		commandArg.setUnixSudoInfo(new UnixSudoInfo(isSudoMode, sudoer, sudoerPassword));
		String type = commandArg.getType();
		if (CollectConstants.SCRIPT_COMMON.equals(type)) { //普通脚本命令
			if (StringTools.isNotEmpty(commandArg.getCmd())) {
				StringBuffer newCmd = new StringBuffer();
				String[] cmds = commandArg.getCmd().split("\n");
				for (int i = 0; i < cmds.length; i++) {
					newCmd.append("sudo ").append(cmds[i]).append("\n");
				}
				commandArg.setCmd(newCmd.toString());
			}
		} else if (CollectConstants.SCRIPT_SHELL.equals(type)) { //shell类型
//			ThreadVariableTools.setVariable("isSudoMode", this.isSudoMode);
		} else if (CollectConstants.SCRIPT_INTERACT.equals(type)) { //交互类型
			String interactiveType = commandArg.getInteractiveType();
			List<InteractiveCmd> cmds = commandArg.getCmds();
			if (!CollectConstants.SCRIPT_COMMON.equals(interactiveType) || cmds == null || cmds.isEmpty()) {
				return; //如果交互脚本类型不是普通脚本命令，或者脚本内容为空
			}
			for (int i = 0; i < cmds.size(); i++) {
				InteractiveCmd ic = cmds.get(i);
				String cmd = ic.getCmd();
				if (!"".equals(cmd.replaceAll("\\{(\\d+)\\}", "").trim())) {
					cmd = "sudo " + cmd;
					ic.setCmd(cmd);
				}
				cmds.set(i, ic);
			}
		} else {
			return;
		}
		this.processSudoModeLogin();
	}
	
	
	/**
	 * Definition:初次以sudo模式执行命令，如未配置免密方式，则需登录一次
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2017年11月17日
	 */
	private void processSudoModeLogin() throws DriverException {
		String[] keywords = new String[] { this.sudoer + KeyWord.SIGN_COLON_EN, this.sudoer + KeyWord.SIGN_COLON_CN,
				this.prompt, this.promptLine };
		try {
			this.protocol.sendCommand(SUDO_TEST_CMD, Protocol.CR);
			String echo = this.protocol.read2KeyWordEcho(SUDO_TEST_CMD, keywords);
			if (this.sudoModePassword(echo)) {
				this.protocol.sendPassword(this.sudoerPassword, Protocol.CR);
				echo = this.protocol.read2KeyWordEcho(SUDO_TEST_CMD, keywords);
			}
			logger.info("sudo test cmd result : {}", echo);
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new DriverException(e.getMessage());
		}
	}
	
	
	/**
	 * Definition:判断sudo命令是否需要输入密码
	 * @param echo 执行sudo命令后的回显值
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017年11月16日
	 */
	private boolean sudoModePassword(String echo) {
		if (echo == null || "".equals(echo)) {
			return false;
		}
		if (echo.trim().endsWith(this.sudoer + KeyWord.SIGN_COLON_EN)
				|| echo.trim().endsWith(this.sudoer + KeyWord.SIGN_COLON_CN)) {
			return true;
		}
		return false;
	}
}
