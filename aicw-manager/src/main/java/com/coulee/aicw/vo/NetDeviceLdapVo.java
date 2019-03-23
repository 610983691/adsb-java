package com.coulee.aicw.vo;

import java.util.List;

/**
 * Description:ldap中数据网络设备对象VO
 * @author  
 *  Version 2.0
 */
public class NetDeviceLdapVo extends LdapMainVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 *设备名ID
	 */
	private String cn;
	
	/*
	 * 设备名称
	 */
	private String name;
	
	/*
	 * 自定义名称
	 */
	private String displayName;
	
	/*
	 * IP类型
	 */
	private String driverType;

	/*
	 * 描述
	 */
	private String desc;
	
	
	/*
	 * 
	 */
	private String resStatus;
 
	/*
	 * 
	 */
	private String createDate;

	/*
	 * 
	 */
	private String createUserKey;

	/*
	 * 
	 */
	private String modifyDate;

	/*
	 * 
	 */
	private String modifyUserKey;

	/*
	 * 
	 */
	private String iptype;

	/*
	 * 
	 */
	private String ip;

	/*
	 * 
	 */
	private String adminPort;

	/*
	 * 
	 */
	private String iamResAdminUserKey;

	/*
	 * 
	 */
	private String isOperative;

	/*
	 * 
	 */
	private String conType;

	/*
	 * 
	 */
	private String adminAccount;

	/*
	 * 
	 */
	private String adminPwd;
	
	public String getEnablePwd() {
		return enablePwd;
	}

	public void setEnablePwd(String enablePwd) {
		this.enablePwd = enablePwd;
	}

	private String enablePwd;

	/*
	 * 
	 */
	private String conPrompt;

	/*
	 * 
	 */
	private String adminPrompt;

	/*
	 * 
	 */
	private String logip;

	/*
	 * 
	 */
	private String authType;

	/*
	 * 
	 */
	private String mngURL;

	/*
	 * 
	 */
	private String iamSystemType;

	/*
	 * 
	 */
	private List standByIp;
	
	/*
	 * 跳转机
	 */
	private List<String> iamNetDeviceJump;
	
	/**
	 * 是否回车
	 */
	private String isenter;
	
	private String isGroup;
	
	private String isKvm;
	
	private String kvmSshPort;
	
	private String kvmTelnetPort;

	public String getIsenter() {
		return isenter;
	}

	public void setIsenter(String isenter) {
		this.isenter = isenter;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

	public String getIsKvm() {
		return isKvm;
	}

	public void setIsKvm(String isKvm) {
		this.isKvm = isKvm;
	}

	public String getKvmSshPort() {
		return kvmSshPort;
	}

	public void setKvmSshPort(String kvmSshPort) {
		this.kvmSshPort = kvmSshPort;
	}

	public String getKvmTelnetPort() {
		return kvmTelnetPort;
	}

	public void setKvmTelnetPort(String kvmTelnetPort) {
		this.kvmTelnetPort = kvmTelnetPort;
	}

	public List<String> getIamNetDeviceJump() {
		return iamNetDeviceJump;
	}

	public void setIamNetDeviceJump(List<String> iamNetDeviceJump) {
		this.iamNetDeviceJump = iamNetDeviceJump;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDriverType() {
		return driverType;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

	public String getResStatus() {
		return resStatus;
	}

	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserKey() {
		return createUserKey;
	}

	public void setCreateUserKey(String createUserKey) {
		this.createUserKey = createUserKey;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUserKey() {
		return modifyUserKey;
	}

	public void setModifyUserKey(String modifyUserKey) {
		this.modifyUserKey = modifyUserKey;
	}
 
	public String getIptype() {
		return iptype;
	}

	public void setIptype(String iptype) {
		this.iptype = iptype;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAdminPort() {
		return adminPort;
	}

	public void setAdminPort(String adminPort) {
		this.adminPort = adminPort;
	}

	public String getIamResAdminUserKey() {
		return iamResAdminUserKey;
	}

	public void setIamResAdminUserKey(String iamResAdminUserKey) {
		this.iamResAdminUserKey = iamResAdminUserKey;
	}

	public String getIsOperative() {
		return isOperative;
	}

	public void setIsOperative(String isOperative) {
		this.isOperative = isOperative;
	}

	public String getConType() {
		return conType;
	}

	public void setConType(String conType) {
		this.conType = conType;
	}

	public String getAdminAccount() {
		return adminAccount;
	}

	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	public String getAdminPwd() {
		return adminPwd;
	}

	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}

	public String getConPrompt() {
		return conPrompt;
	}

	public void setConPrompt(String conPrompt) {
		this.conPrompt = conPrompt;
	}

	public String getAdminPrompt() {
		return adminPrompt;
	}

	public void setAdminPrompt(String adminPrompt) {
		this.adminPrompt = adminPrompt;
	}

	public String getLogip() {
		return logip;
	}

	public void setLogip(String logip) {
		this.logip = logip;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getMngURL() {
		return mngURL;
	}

	public void setMngURL(String mngURL) {
		this.mngURL = mngURL;
	}

	public String getIamSystemType() {
		return iamSystemType;
	}

	public void setIamSystemType(String iamSystemType) {
		this.iamSystemType = iamSystemType;
	}

	public List getStandByIp() {
		return standByIp;
	}

	public void setStandByIp(List standByIp) {
		this.standByIp = standByIp;
	}
	
	

}
