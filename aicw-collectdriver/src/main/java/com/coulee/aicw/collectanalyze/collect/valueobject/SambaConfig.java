package com.coulee.aicw.collectanalyze.collect.valueobject;

import java.io.Serializable;

/**
 * Description:SAMBA协议配置参数
 * Copyright (C) 2017 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2017-1-18
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class SambaConfig implements Serializable {

	private static final long serialVersionUID = -1672244076540474852L;
	
	/**
	 * samba用户名
	 */
	private String sambaUsername;
	
	/**
	 * samba密码
	 */
	private String sambaPassword;
	
	/**
	 * samba服务ip
	 */
	private String sambaIp;
	
	/**
	 * samba共享路径别名
	 */
	private String sharePathAlias = "aicwshare";
	
	/**
	 * Description : 实例化SAMBA配置类
	 * @param sambaIp samba服务ip
	 * @param sambaUsername samba用户名
	 * @param sambaPassword samba密码
	 * @param sharePathAlias samba共享路径别名
	 */
	public SambaConfig(String sambaIp, String sambaUsername, String sambaPassword, String sharePathAlias) {
		this.sambaIp = sambaIp;
		this.sambaUsername = sambaUsername;
		this.sambaPassword = sambaPassword;
		if (sharePathAlias != null && !"".equals(sharePathAlias)) {
			this.sharePathAlias = sharePathAlias;
		}
	}
	
	public String getSambaUsername() {
		return sambaUsername;
	}

	public void setSambaUsername(String sambaUsername) {
		this.sambaUsername = sambaUsername;
	}

	public String getSambaPassword() {
		return sambaPassword;
	}

	public void setSambaPassword(String sambaPassword) {
		this.sambaPassword = sambaPassword;
	}

	public String getSambaIp() {
		return sambaIp;
	}

	public void setSambaIp(String sambaIp) {
		this.sambaIp = sambaIp;
	}

	public String getSharePathAlias() {
		return sharePathAlias;
	}

	public void setSharePathAlias(String sharePathAlias) {
		if (sharePathAlias != null && !"".equals(sharePathAlias)) {
			this.sharePathAlias = sharePathAlias;
		}
	}
	
}
