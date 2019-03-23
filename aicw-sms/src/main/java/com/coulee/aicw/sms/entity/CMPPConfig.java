package com.coulee.aicw.sms.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Description: CMPP协议短信网关配置<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Profile({"cmpp2", "cmpp3"})
@Component
@ConfigurationProperties(prefix = "ismg")
public class CMPPConfig {

	/**
	 * ISMG主机地址
	 */
	private String host;

	/**
	 * ISMG主机端口
	 */
	private String port;

	/**
	 * 心跳信息发送间隔时间(单位：秒)
	 */
	private String heartbeat_interval;

	/**
	 * 连接中断时重连间隔时间(单位：秒)
	 */
	private String reconnect_interval;

	/**
	 * 需要重连时，连续发出心跳而没有接收到响应的个数（单位：个)
	 */
	private String heartbeat_noresponseout;

	/**
	 * 操作超时时间(单位：秒)
	 */
	private String transaction_timeout;

	/**
	 * SP…ID(最大为六位字符)
	 */
	private String source_addr;

	/**
	 * 双方协商的版本号(大于0，小于256)
	 */
	private String version;

	/**
	 * shared-secret由中国移动与ICP事先商定 
	 */
	private String shared_secret;

	/**
	 * 是否属于调试状态,true表示属于调试状态，所有的消息被打印输出到屏幕，false表示不属于调试状态，所有的消息不被输出
	 */
	private String debug;

	/**
	 * 服务ID
	 */
	private String service_id;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHeartbeat_interval() {
		return heartbeat_interval;
	}

	public void setHeartbeat_interval(String heartbeat_interval) {
		this.heartbeat_interval = heartbeat_interval;
	}

	public String getReconnect_interval() {
		return reconnect_interval;
	}

	public void setReconnect_interval(String reconnect_interval) {
		this.reconnect_interval = reconnect_interval;
	}

	public String getHeartbeat_noresponseout() {
		return heartbeat_noresponseout;
	}

	public void setHeartbeat_noresponseout(String heartbeat_noresponseout) {
		this.heartbeat_noresponseout = heartbeat_noresponseout;
	}

	public String getTransaction_timeout() {
		return transaction_timeout;
	}

	public void setTransaction_timeout(String transaction_timeout) {
		this.transaction_timeout = transaction_timeout;
	}

	public String getSource_addr() {
		return source_addr;
	}

	public void setSource_addr(String source_addr) {
		this.source_addr = source_addr;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getShared_secret() {
		return shared_secret;
	}

	public void setShared_secret(String shared_secret) {
		this.shared_secret = shared_secret;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	@Override
	public String toString() {
		return "CMPPConfig [host=" + host + ", port=" + port + ", heartbeat_interval=" + heartbeat_interval
				+ ", reconnect_interval=" + reconnect_interval + ", heartbeat_noresponseout=" + heartbeat_noresponseout
				+ ", transaction_timeout=" + transaction_timeout + ", source_addr=" + source_addr + ", version="
				+ version + ", shared_secret=" + shared_secret + ", debug=" + debug + ", service_id=" + service_id
				+ "]";
	}
	
}
