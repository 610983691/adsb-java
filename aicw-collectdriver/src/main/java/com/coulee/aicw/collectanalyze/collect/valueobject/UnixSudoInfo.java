package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;

/**
 * Description: 类UNIX主机sudo模式信息<br>
 * Create Date: 2017年11月29日<br>
 * Modified By：<br>
 * Modified Date：<br>
 * Why & What is modified：<br> 
 * Copyright (C) 2017 Coulee All Right Reserved.<br>
 * @author LanChao
 * @version 1.0
 */
public class UnixSudoInfo implements Serializable {

	private static final long serialVersionUID = -2992747549378668905L;

	/**
	 * 是否sudo模式
	 */
	private boolean isSudoMode;
	
	/**
	 * sudo用户名
	 */
	private String sudoer;
	
	/**
	 * sudo用户密码
	 */
	private String sudoerPassword;
	
	public UnixSudoInfo(boolean isSudoMode, String sudoer, String sudoerPassword) {
		super();
		this.isSudoMode = isSudoMode;
		this.sudoer = sudoer;
		this.sudoerPassword = sudoerPassword;
	}

	public boolean isSudoMode() {
		return isSudoMode;
	}

	public void setSudoMode(boolean isSudoMode) {
		this.isSudoMode = isSudoMode;
	}

	public String getSudoer() {
		return sudoer;
	}

	public void setSudoer(String sudoer) {
		this.sudoer = sudoer;
	}

	public String getSudoerPassword() {
		return sudoerPassword;
	}

	public void setSudoerPassword(String sudoerPassword) {
		this.sudoerPassword = sudoerPassword;
	}
	
	
}
