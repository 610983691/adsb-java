package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;

/**
 * Description:交互脚本对象
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class InteractiveCmd implements Serializable {

	private static final long serialVersionUID = -7413705261913182168L;
	
	/**
	 * 脚本内容
	 */
	private String cmd;
	
	/**
	 * 期待回显值信息
	 */
	private String prompt;
	
	/**
	 * 期待错误回显值信息
	 */
	private String errorPrompt;

	public String getCmd() {
		return cmd;
	}

	public String getPrompt() {
		return prompt;
	}
	
	public String getErrorPrompt() {
		return errorPrompt;
	}

	public InteractiveCmd(String cmd, String prompt, String errorPrompt) {
		this.cmd = cmd;
		this.prompt = prompt;
		this.errorPrompt = errorPrompt;
	}
	
	@SuppressWarnings("unused")
	private InteractiveCmd() {
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

}
