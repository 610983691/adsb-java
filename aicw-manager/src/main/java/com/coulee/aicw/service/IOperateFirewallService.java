package com.coulee.aicw.service;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.aicw.vo.OperateFirewallParam;

/**
 * 操控防火墙的接口类
 * Description:
 * Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月27日
 * author：zyj
 * @version 1.0
 */
public interface IOperateFirewallService  extends IBaseService{

	/**
	 * 暴露一个接口，根据防火墙ID，策略类型（增加、移除），外网IP，应用信息等参数，内部根据这些参数信息操作防火墙进行策略操作
	 * @param paramVo
	 * @return
	 */
	public Message operateFw(OperateFirewallParam paramVo);
	
	
	/***
	 * 防火墙网络状态拨测
	 * @param paramVo
	 * @return
	 */
	Message testFwStatus() ;
}
