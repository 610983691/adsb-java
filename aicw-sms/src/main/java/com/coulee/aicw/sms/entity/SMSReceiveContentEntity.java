package com.coulee.aicw.sms.entity;

/**
 * Description: 接收到的短信内容<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class SMSReceiveContentEntity {

	/**
	 * 短信内容
	 */
	private String content;
	
	/**
	 * 短信业务ID
	 */
	private String businessId;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 错误信息
	 */
	private String error;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "SMSReceiveContentEntity [content=" + content + ", businessId=" + businessId + ", phone=" + phone
				+ ", error=" + error + "]";
	}
	
}
