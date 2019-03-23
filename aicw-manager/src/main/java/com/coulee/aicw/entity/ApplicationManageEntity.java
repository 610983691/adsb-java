package com.coulee.aicw.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="ApplicationManageEntity")
public class ApplicationManageEntity extends BaseEntity {
    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    private String id;

    /**
     * 出口防火墙
     */
    @ApiModelProperty(value = "出口防火墙")
    private String fwId;
    private String fwName;
    private String fwIp;

    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    private String approveUser;
    private String approveUserName;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    private String appName;
    private String appNameLike;

    /**
     * 资产IP/URL
     */
    @ApiModelProperty(value = "资产IP/URL")
    private String appIpUrl;

    /**
     * 是否审批
     */
    @ApiModelProperty(value = "是否审批")
    private String isApprove;

    /**
     * 应用简称
     */
    @ApiModelProperty(value = "应用简称")
    private String appAlias;
    private String appAliasLike;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 访问内部IP
     */
    @ApiModelProperty(value = "访问内部IP")
    private String conIp;
    /**
     * 访问IP使用端口号
     */
    @ApiModelProperty(value = "访问IP使用端口号")
    private Integer conPort;
    /**
     * 是否域名访问
     */
    @ApiModelProperty(value = "是否域名访问")
    private String isUseUrl;
    /**
     * fw_application_manage
     */
    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     * @return id 应用ID
     */
    public String getId() {
        return id;
    }

    /**
     * 应用ID
     * @param id 应用ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 出口防火墙
     * @return fw_id 出口防火墙
     */
    public String getFwId() {
        return fwId;
    }

    /**
     * 出口防火墙
     * @param fwId 出口防火墙
     */
    public void setFwId(String fwId) {
        this.fwId = fwId;
    }

    public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	/**
     * 审批人
     * @return approve_user 审批人
     */
    public String getApproveUser() {
        return approveUser;
    }

    /**
     * 审批人
     * @param approveUser 审批人
     */
    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getApproveUserName() {
		return approveUserName;
	}

	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}

	/**
     * 应用名称
     * @return app_name 应用名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 应用名称
     * @param appName 应用名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 资产IP/URL
     * @return app_ip_url 资产IP/URL
     */
    public String getAppIpUrl() {
        return appIpUrl;
    }

    /**
     * 资产IP/URL
     * @param appIpUrl 资产IP/URL
     */
    public void setAppIpUrl(String appIpUrl) {
        this.appIpUrl = appIpUrl;
    }

    /**
     * 是否审批
     * @return is_approve 是否审批
     */
    public String getIsApprove() {
        return isApprove;
    }

    /**
     * 是否审批
     * @param isApprove 是否审批
     */
    public void setIsApprove(String isApprove) {
        this.isApprove = isApprove;
    }

    /**
     * 应用简称
     * @return app_alias 应用简称
     */
    public String getAppAlias() {
        return appAlias;
    }

    /**
     * 应用简称
     * @param appAlias 应用简称
     */
    public void setAppAlias(String appAlias) {
        this.appAlias = appAlias;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppAliasLike() {
		return appAliasLike;
	}

	public void setAppAliasLike(String appAliasLike) {
		this.appAliasLike = appAliasLike;
	}

	public String getAppNameLike() {
		return appNameLike;
	}

	public void setAppNameLike(String appNameLike) {
		this.appNameLike = appNameLike;
	}

	public String getFwIp() {
		return fwIp;
	}

	public void setFwIp(String fwIp) {
		this.fwIp = fwIp;
	}

 

	public String getConIp() {
		return conIp;
	}

	public void setConIp(String conIp) {
		this.conIp = conIp;
	}

	public Integer getConPort() {
		return conPort;
	}

	public void setConPort(Integer conPort) {
		this.conPort = conPort;
	}

	public String getIsUseUrl() {
		return isUseUrl;
	}

	public void setIsUseUrl(String isUseUrl) {
		this.isUseUrl = isUseUrl;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fwId=").append(fwId);
        sb.append(", approveUser=").append(approveUser);
        sb.append(", appName=").append(appName);
        sb.append(", appIpUrl=").append(appIpUrl);
        sb.append(", isApprove=").append(isApprove);
        sb.append(", appAlias=").append(appAlias);
        sb.append(", remark=").append(remark);
        sb.append(", conIp=").append(conIp);
        sb.append(", conPort=").append(conPort);
        sb.append(", isUseUrl=").append(isUseUrl);
        sb.append("]");
        return sb.toString();
    }
}