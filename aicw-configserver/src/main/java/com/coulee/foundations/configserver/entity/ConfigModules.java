package com.coulee.foundations.configserver.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 模块对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigModules extends BaseEntity {
    private Integer id;

    private Integer productId;
    
    private String productName;

    private String moduleName;

    private String moduleMark;

    private Integer isExtends;

    private static final long serialVersionUID = 1L;
    
    /**
     * 页面数据：数据版本：new / old
     */
    private String dataVersion;
    
    /**
     * 页面数据：真实数据ID
     */
    private Integer realId;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleMark() {
        return moduleMark;
    }

    public void setModuleMark(String moduleMark) {
        this.moduleMark = moduleMark;
    }

    public Integer getIsExtends() {
        return isExtends;
    }

    public void setIsExtends(Integer isExtends) {
        this.isExtends = isExtends;
    }

    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public Integer getRealId() {
		return realId;
	}

	public void setRealId(Integer realId) {
		this.realId = realId;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", moduleName=").append(moduleName);
        sb.append(", moduleMark=").append(moduleMark);
        sb.append(", isExtends=").append(isExtends);
        sb.append("]");
        return sb.toString();
    }
}