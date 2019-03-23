package com.coulee.aicw.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="FirewallAssetEntity")
public class FirewallAssetEntity extends BaseEntity {
    /**
     * 防火墙ID
     */
    @ApiModelProperty(value = "防火墙ID")
    private String id;

    /**
     *设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String fwCode;

    /**
     * 防火墙名称
     */
    @ApiModelProperty(value = "防火墙名称")
    private String fwName;
    private String fwNameLike;
    /**
     * 防火墙别名
     */
    @ApiModelProperty(value = "防火墙别名")
    private String fwAlias;

    /**
     * 防火墙类型
     */
    @ApiModelProperty(value = "防火墙类型")
    private String fwType;
    private String fwTypeDes;
    private String fwTypeLike;

    /**
     * IP类型
     */
    @ApiModelProperty(value = "IP类型")
    private String ipType;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String fwIp;
    private String fwIpLike;
    /**
     * IPV6
     */
    @ApiModelProperty(value = "IPV6")
    private String fwIpv6;
    /**
     * 连接协议
     */
    @ApiModelProperty(value = "连接协议")
    private String fwProtocol;

    /**
     * 防火墙系统编码
     */
    @ApiModelProperty(value = "防火墙系统编码")
    private String encode;

    /**
     * 管理员账号
     */
    @ApiModelProperty(value = "管理员账号")
    private String adminAcount;

    /**
     * 管理员密码
     */
    @ApiModelProperty(value = "管理员密码")
    private String adminPwd;

    /**
     * 管理员提示符
     */
    @ApiModelProperty(value = "管理员提示符")
    private String adminPrompt;

    /**
     * 特权密码
     */
    @ApiModelProperty(value = "特权密码")
    private String rootPwd;

    /**
     * 特权提示符
     */
    @ApiModelProperty(value = "特权提示符")
    private String rootPrompt;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String uuid;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String createUser;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String updateUser;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     * 端口号
     */
    @ApiModelProperty(value = "端口号")
    private Integer fwPort;

    /**
     * 防火墙状态
     */
    @ApiModelProperty(value = "防火墙状态")
    private String fwStatus;
    private String fwStatusDes;
    /**
     * 拨测状态(0 失败 1 成功)
     * test_login_status
     */
    @ApiModelProperty(value = "拨测状态(0 失败 1 成功)")
    private String testLoginStatus;
    /**
     * 拨测日志 test_login_log
     */
    @ApiModelProperty(value = "拨测日志")
    private String testLoginLog;
    /**
     * 拨测时间 test_login_date
     */
    @ApiModelProperty(value = "拨测时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date testLoginDate;
    /**
     * fw_asset_info
     */
    private static final long serialVersionUID = 1L;

    /**
     * 防火墙ID
     * @return id 防火墙ID
     */
    public String getId() {
        return id;
    }

    /**
     * 防火墙ID
     * @param id 防火墙ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 编码
     * @return fw_code 编码
     */
    public String getFwCode() {
        return fwCode;
    }

    /**
     * 编码
     * @param fwCode 编码
     */
    public void setFwCode(String fwCode) {
        this.fwCode = fwCode;
    }

    /**
     * 防火墙名称
     * @return fw_name 防火墙名称
     */
    public String getFwName() {
        return fwName;
    }

    /**
     * 防火墙名称
     * @param fwName 防火墙名称
     */
    public void setFwName(String fwName) {
        this.fwName = fwName;
    }

    /**
     * 防火墙别名
     * @return fw_alias 防火墙别名
     */
    public String getFwAlias() {
        return fwAlias;
    }

    /**
     * 防火墙别名
     * @param fwAlias 防火墙别名
     */
    public void setFwAlias(String fwAlias) {
        this.fwAlias = fwAlias;
    }

    /**
     * 防火墙类型
     * @return fw_type 防火墙类型
     */
    public String getFwType() {
        return fwType;
    }

    /**
     * 防火墙类型
     * @param fwType 防火墙类型
     */
    public void setFwType(String fwType) {
        this.fwType = fwType;
    }

    /**
     * IP类型
     * @return ip_type IP类型
     */
    public String getIpType() {
        return ipType;
    }

    /**
     * IP类型
     * @param ipType IP类型
     */
    public void setIpType(String ipType) {
        this.ipType = ipType;
    }

    /**
     * IP地址
     * @return fw_ip IP地址
     */
    public String getFwIp() {
        return fwIp;
    }

    /**
     * IP地址
     * @param fwIp IP地址
     */
    public void setFwIp(String fwIp) {
        this.fwIp = fwIp;
    }

    /**
     * 连接协议
     * @return fw_protocol 连接协议
     */
    public String getFwProtocol() {
        return fwProtocol;
    }

    /**
     * 连接协议
     * @param fwProtocol 连接协议
     */
    public void setFwProtocol(String fwProtocol) {
        this.fwProtocol = fwProtocol;
    }

    /**
     * 防火墙系统编码
     * @return encode 防火墙系统编码
     */
    public String getEncode() {
        return encode;
    }

    /**
     * 防火墙系统编码
     * @param encode 防火墙系统编码
     */
    public void setEncode(String encode) {
        this.encode = encode;
    }

    /**
     * 管理员账号
     * @return admin_acount 管理员账号
     */
    public String getAdminAcount() {
        return adminAcount;
    }

    /**
     * 管理员账号
     * @param adminAcount 管理员账号
     */
    public void setAdminAcount(String adminAcount) {
        this.adminAcount = adminAcount;
    }

    /**
     * 管理员密码
     * @return admin_pwd 管理员密码
     */
    public String getAdminPwd() {
        return adminPwd;
    }

    /**
     * 管理员密码
     * @param adminPwd 管理员密码
     */
    public void setAdminPwd(String adminPwd) {
        this.adminPwd = adminPwd;
    }

    /**
     * 管理员提示符
     * @return admin_prompt 管理员提示符
     */
    public String getAdminPrompt() {
        return adminPrompt;
    }

    /**
     * 管理员提示符
     * @param adminPrompt 管理员提示符
     */
    public void setAdminPrompt(String adminPrompt) {
        this.adminPrompt = adminPrompt;
    }

    /**
     * 特权密码
     * @return root_pwd 特权密码
     */
    public String getRootPwd() {
        return rootPwd;
    }

    /**
     * 特权密码
     * @param rootPwd 特权密码
     */
    public void setRootPwd(String rootPwd) {
        this.rootPwd = rootPwd;
    }

    /**
     * 特权提示符
     * @return root_prompt 特权提示符
     */
    public String getRootPrompt() {
        return rootPrompt;
    }

    /**
     * 特权提示符
     * @param rootPrompt 特权提示符
     */
    public void setRootPrompt(String rootPrompt) {
        this.rootPrompt = rootPrompt;
    }

    /**
     * 
     * @return uuid 
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 
     * @param uuid 
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 
     * @return create_user 
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 
     * @param createUser 
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 
     * @return create_date 
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 
     * @param createDate 
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 
     * @return update_user 
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 
     * @param updateUser 
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 
     * @return update_date 
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 
     * @param updateDate 
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 端口号
     * @return fw_port 端口号
     */
    public Integer getFwPort() {
        return fwPort;
    }

    /**
     * 端口号
     * @param fwPort 端口号
     */
    public void setFwPort(Integer fwPort) {
        this.fwPort = fwPort;
    }

    /**
     * 防火墙状态
     * @return fw_status 防火墙状态
     */
    public String getFwStatus() {
        return fwStatus;
    }

    /**
     * 防火墙状态
     * @param fwStatus 防火墙状态
     */
    public void setFwStatus(String fwStatus) {
        this.fwStatus = fwStatus;
    }

    public String getFwNameLike() {
		return fwNameLike;
	}

	public void setFwNameLike(String fwNameLike) {
		this.fwNameLike = fwNameLike;
	}

	public String getFwTypeLike() {
		return fwTypeLike;
	}

	public void setFwTypeLike(String fwTypeLike) {
		this.fwTypeLike = fwTypeLike;
	}

	public String getFwIpLike() {
		return fwIpLike;
	}

	public void setFwIpLike(String fwIpLike) {
		this.fwIpLike = fwIpLike;
	}

	public String getFwStatusDes() {
		return fwStatusDes;
	}

	public void setFwStatusDes(String fwStatusDes) {
		this.fwStatusDes = fwStatusDes;
	}

	public String getFwIpv6() {
		return fwIpv6;
	}

	public void setFwIpv6(String fwIpv6) {
		this.fwIpv6 = fwIpv6;
	}

	public String getTestLoginStatus() {
		return testLoginStatus;
	}

	public void setTestLoginStatus(String testLoginStatus) {
		this.testLoginStatus = testLoginStatus;
	}

	public String getTestLoginLog() {
		return testLoginLog;
	}

	public void setTestLoginLog(String testLoginLog) {
		this.testLoginLog = testLoginLog;
	}

	public Date getTestLoginDate() {
		return testLoginDate;
	}

	public void setTestLoginDate(Date testLoginDate) {
		this.testLoginDate = testLoginDate;
	}

	public String getFwTypeDes() {
		return fwTypeDes;
	}

	public void setFwTypeDes(String fwTypeDes) {
		this.fwTypeDes = fwTypeDes;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fwCode=").append(fwCode);
        sb.append(", fwName=").append(fwName);
        sb.append(", fwAlias=").append(fwAlias);
        sb.append(", fwType=").append(fwType);
        sb.append(", ipType=").append(ipType);
        sb.append(", fwIp=").append(fwIp);
        sb.append(", fwProtocol=").append(fwProtocol);
        sb.append(", encode=").append(encode);
        sb.append(", adminAcount=").append(adminAcount);
        sb.append(", adminPwd=").append(adminPwd);
        sb.append(", adminPrompt=").append(adminPrompt);
        sb.append(", rootPwd=").append(rootPwd);
        sb.append(", rootPrompt=").append(rootPrompt);
        sb.append(", uuid=").append(uuid);
        sb.append(", createUser=").append(createUser);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", fwPort=").append(fwPort);
        sb.append(", fwStatus=").append(fwStatus);
        sb.append("]");
        return sb.toString();
    }
}