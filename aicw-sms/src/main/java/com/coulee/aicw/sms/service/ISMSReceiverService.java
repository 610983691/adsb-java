package com.coulee.aicw.sms.service;

import com.coulee.aicw.sms.entity.SMSReceiveContentEntity;

/**
 * Description: 接收短信后，业务处理接口<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ISMSReceiverService {

	/**
	 * Description: 接收短信<br> 
	 * Created date: 2018年10月30日
	 * @param content
	 * @author oblivion
	 */
	public void receive(SMSReceiveContentEntity content);
}
