package com.coulee.aicw.vo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.common.BaseTools;

import io.swagger.annotations.ApiModelProperty;
/**
 * 操控防火墙的调用接口参数模型
 * 暴露一个接口，根据防火墙ID，策略类型（增加、移除），外网IP，应用信息等参数，内部根据这些参数信息操作防火墙进行策略操作
 * Description:
 * Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月27日
 * author：zyj
 * @version 1.0
 */
public class OperateFirewallParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 防火墙ID
	 */
	private String fwId ;
	/*
	 * 拨测
	 */
	private boolean testLogin;
	/*
	 * 策略类型
	 * 增加 add
	 * 移除  remove 
	 */
	private String operateType;
	/*
	 * 外网IP
	 */
	private String netIP;
	/*
	 * 打开端口号
	 */
	private Integer openPort;
	/*
	 * 连接内网ip
	 */
	private String inIp;
	/*
	 * 应用信息 id
	 */
	private String appliationId;
	 
	/**
	 * 使用模型前调用方法进行检查
	 * @return
	 */
	public Message checkParam() {
		Message mes = Message.newSuccessMessage(null);
		if(BaseTools.isNull(this.fwId)) {
			return Message.newFailureMessage("防火墙ID不能为空");
		}
		 
		return mes;
	}
	
	/********************************************************/
	public String getFwId() {
		return fwId;
	}
	public void setFwId(String fwId) {
		this.fwId = fwId;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getNetIP() {
		return netIP;
	}
	public void setNetIP(String netIP) {
		this.netIP = netIP;
	}
	public String getAppliationId() {
		return appliationId;
	}
	public void setAppliationId(String appliationId) {
		this.appliationId = appliationId;
	}

	public Integer getOpenPort() {
		return openPort;
	}

	public void setOpenPort(Integer openPort) {
		this.openPort = openPort;
	}

	public String getInIp() {
		return inIp;
	}

	public void setInIp(String inIp) {
		this.inIp = inIp;
	}

	public boolean isTestLogin() {
		return testLogin;
	}

	public void setTestLogin(boolean testLogin) {
		this.testLogin = testLogin;
	}
}
