package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;

/**
 * Description:登录参数
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-11-28
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class LoginArg implements Serializable {

	private static final long serialVersionUID = 7963767830329659355L;

	/**
	 * 登录用户名
	 */
	private String username;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 登录提示符
	 */
	private String prompt;

	/**
	 * 部分设备跳转登录指令
	 */
	private String switchCommand;

	/**
	 * 管理员用户名
	 */
	private String adminUsername;

	/**
	 * 管理员密码
	 */
	private String adminPassword;

	/**
	 * 管理员登录提示符
	 */
	private String adminPrompt;
	
	/**
	 * 是否需要切入特权模式
	 */
	private boolean isSwitch;
	
	/**
	 * 跳转指令
	 */
	private String jumpCommand;
	
	/**
	 * 是否sudo模式
	 */
	private boolean isSudoMode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getSwitchCommand() {
		return switchCommand;
	}

	public void setSwitchCommand(String switchCommand) {
		this.switchCommand = switchCommand;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminPrompt() {
		return adminPrompt;
	}

	public void setAdminPrompt(String adminPrompt) {
		this.adminPrompt = adminPrompt;
	}

	public boolean isSwitch() {
		return isSwitch;
	}

	public void setSwitch(boolean isSwitch) {
		this.isSwitch = isSwitch;
	}

	public String getJumpCommand() {
		return jumpCommand;
	}

	public void setJumpCommand(String jumpCommand) {
		this.jumpCommand = jumpCommand;
	}

	public boolean isSudoMode() {
		return isSudoMode;
	}

	public void setSudoMode(boolean isSudoMode) {
		this.isSudoMode = isSudoMode;
	}


}
