package com.coulee.aicw.vo;

import java.io.Serializable;
import java.util.List;


/**
 * @Description:ldap安全对象同步公用vo
 */
public class LdapMainVo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cn = "";
	private String name = "";
	private String displayName = "";
	private String desc = "";
	private String progDicKey = "";
	private String driverType = "";
	private String status = "";
	private String createDate = "";
	private String createUserKey = "";
	private String modifyDate = "";
	private String modifyUserKey = "";
	private List resourceGroupKey;
	private String manufacturer = "";
	private String iamResouceVersionDicKey = "";
	private String iptype = "";
	private String ip = "";
	private String adminPort = "";
	private String url = "";
	private String iamResAdminUserKey = "";
	/**
	 * 地区代码
	 */
	private String areaDicKey;

	public String getAreaDicKey()
	{
		return areaDicKey;
	}

	public void setAreaDicKey(String areaDicKey)
	{
		this.areaDicKey = areaDicKey;
	}

	public String getCn()
	{
		return cn;
	}

	public void setCn(String cn)
	{
		this.cn = cn;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getProgDicKey()
	{
		return progDicKey;
	}

	public void setProgDicKey(String progDicKey)
	{
		this.progDicKey = progDicKey;
	}

	public String getDriverType()
	{
		return driverType;
	}

	public void setDriverType(String driverType)
	{
		this.driverType = driverType;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getCreateUserKey()
	{
		return createUserKey;
	}

	public void setCreateUserKey(String createUserKey)
	{
		this.createUserKey = createUserKey;
	}

	public String getModifyDate()
	{
		return modifyDate;
	}

	public void setModifyDate(String modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	public String getModifyUserKey()
	{
		return modifyUserKey;
	}

	public void setModifyUserKey(String modifyUserKey)
	{
		this.modifyUserKey = modifyUserKey;
	}

	public List getResourceGroupKey()
	{
		return resourceGroupKey;
	}

	public void setResourceGroupKey(List resourceGroupKey)
	{
		this.resourceGroupKey = resourceGroupKey;
	}

	public String getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public String getIamResouceVersionDicKey()
	{
		return iamResouceVersionDicKey;
	}

	public void setIamResouceVersionDicKey(String iamResouceVersionDicKey)
	{
		this.iamResouceVersionDicKey = iamResouceVersionDicKey;
	}

	public String getIptype()
	{
		return iptype;
	}

	public void setIptype(String iptype)
	{
		this.iptype = iptype;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getAdminPort()
	{
		return adminPort;
	}

	public void setAdminPort(String adminPort)
	{
		this.adminPort = adminPort;
	}

	public String getIamResAdminUserKey()
	{
		return iamResAdminUserKey;
	}

	public void setIamResAdminUserKey(String iamResAdminUserKey)
	{
		this.iamResAdminUserKey = iamResAdminUserKey;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

}

