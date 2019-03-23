package com.coulee.aicw.sms.service;

/**
 * Description: 短信发送操作接口<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ISMSSenderService {

	
	/**
	 * Description: 带业务码的短信发送接口<br> 
	 * Created date: 2018年10月29日
	 * @param phone
	 * @param msg
	 * @param businessId
	 * @return
	 * @author oblivion
	 */
	public boolean sendSms(String phone, String msg, String businessId);
}
