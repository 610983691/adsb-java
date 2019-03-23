package com.coulee.aicw.service;

import com.coulee.aicw.foundations.service.IBaseService;
/**
 * 短信接收后的业务处理接口
 * Description:
 * Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月29日上午9:48:03
 * author：HongZhang
 * @version 1.0
 */
public interface ISMSService extends IBaseService {
	/**
	 * 接收申请短信接口
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日上午9:51:22
	* @param telphone  发送的手机号码
	*        msg    短信内容
	*        smsnumber   短信码
	* @return
	 */
	public void applyReceptionSMS(String telphone,String msg,String smsnumber);
	
	/**
	 * 接收审批短信的接口
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日下午12:43:37
	* @param telphone  发送的手机号码
	*        msg    短信内容
	*        smsnumber   短信码
	* @param 
	* @return
	 */
	public void approvalReceptionSMS(String telphone,String msg,String smsnumber);
}
