package com.coulee.aicw.sms.entity;

/**
 * Description: 短信内容实体类<br>
 * Create Date: 2018年10月29日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class SMSContentEntity {

	/**
	 * 应用别名
	 */
	private String appAlias;
	
	/**
	 * 外网IP
	 */
	private String netIp;
	
	/**
	 * 申请时长：分钟
	 */
	private String applyDuration;
	
	/**
	 * 申请人
	 */
	private String applyUser;
	
	/**
	 * 申请人手机
	 */
	private String applyPhone;
	
	/**
	 * 错误信息
	 */
	private String errorMsg;

	public String getAppAlias() {
		return appAlias;
	}

	public void setAppAlias(String appAlias) {
		this.appAlias = appAlias;
	}

	public String getNetIp() {
		return netIp;
	}

	public void setNetIp(String netIp) {
		this.netIp = netIp;
	}

	public String getApplyDuration() {
		return applyDuration;
	}

	public void setApplyDuration(String applyDuration) {
		this.applyDuration = applyDuration;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getApplyPhone() {
		return applyPhone;
	}

	public void setApplyPhone(String applyPhone) {
		this.applyPhone = applyPhone;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
