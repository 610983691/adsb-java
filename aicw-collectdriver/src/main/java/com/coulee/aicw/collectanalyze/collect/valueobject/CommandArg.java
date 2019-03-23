package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:执行命令参数对象
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class CommandArg implements Serializable {

	private static final long serialVersionUID = 218259153403050288L;

	/**
	 * 脚本类型
	 */
	private String type;
	
	/**
	 * 非交互脚本内容
	 */
	private String cmd;
	
	/**
	 * 交互脚本类型
	 */
	private String interactiveType;
	
	/**
	 * 交互脚本集合
	 */
	private List<InteractiveCmd> cmds = new ArrayList<InteractiveCmd>(5);
	
	/**
	 * 脚本参数集合
	 */
	private List<String> args = new ArrayList<String>(5);
	
	/**
	 * 所有脚本字符串形式
	 */
	private String cmdStr = "";
	
	/**
	 * 类unix主机sudo信息
	 */
	private UnixSudoInfo unixSudoInfo;
	
	/**
	 * Description : 实例化非交互脚本对象
	 * @param type 脚本类型，普通、Shell、SQL
	 * @param cmd 脚本内容
	 */
	public CommandArg(String type, String cmd) {
		this.type = type;
		this.cmd = cmd;
		this.cmdStr = cmd == null ? "" : cmd;
	}
	
	/**
	 * Description : 实例化脚本对象
	 * @param type 脚本类型
	 */
	public CommandArg(String type) {
		this.type = type;
	}
	
	@SuppressWarnings("unused")
	private CommandArg() {
	}
	
	/**
	 * Definition: 设置交互脚本类型
	 * @param type 脚本类型：普通、SQL
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public CommandArg setInteractiveType(String type) {
		this.interactiveType = type;
		return this;
	}
	
	/**
	 * Definition: 设置脚本参数
	 * @param args 脚本参数
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public CommandArg addCmdArgs(String... args) {
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				this.args.add(args[i]);
			}
		}
		return this;
	}
	
	/**
	 * Definition:设置交互脚本内容
	 * @param cmd 脚本内容
	 * @param prompt 期待回显信息
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public CommandArg addInteractiveCmd(String cmd, String prompt, String errorPrompt) {
		if (cmd == null) {
			return this;
		}
		this.cmds.add(new InteractiveCmd(cmd, prompt, errorPrompt));
		this.cmdStr = this.cmdStr + cmd + "\r\n";
		return this;
	}
	
	public String getType() {
		return type;
	}

	public String getCmd() {
		return cmd;
	}

	public String getInteractiveType() {
		return interactiveType;
	}

	public List<InteractiveCmd> getCmds() {
		return cmds;
	}

	public List<String> getArgs() {
		return args;
	}

	public String getCmdStr() {
		return cmdStr.trim();
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public UnixSudoInfo getUnixSudoInfo() {
		return unixSudoInfo;
	}

	public void setUnixSudoInfo(UnixSudoInfo unixSudoInfo) {
		this.unixSudoInfo = unixSudoInfo;
	}

}
