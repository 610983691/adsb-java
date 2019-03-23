package com.coulee.foundations.configserver.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 产品对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigProduct extends BaseEntity {
    private Integer id;

    private String productName;

    private String productMark;

    private String cryptType;

    private String cryptKey;

    private String createUuid;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductMark() {
        return productMark;
    }

    public void setProductMark(String productMark) {
        this.productMark = productMark;
    }

    public String getCryptType() {
        return cryptType;
    }

    public void setCryptType(String cryptType) {
        this.cryptType = cryptType;
    }

    public String getCryptKey() {
        return cryptKey;
    }

    public void setCryptKey(String cryptKey) {
        this.cryptKey = cryptKey;
    }

    public String getCreateUuid() {
        return createUuid;
    }

    public void setCreateUuid(String createUuid) {
        this.createUuid = createUuid;
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
        sb.append(", productName=").append(productName);
        sb.append(", productMark=").append(productMark);
        sb.append(", cryptType=").append(cryptType);
        sb.append(", cryptKey=").append(cryptKey);
        sb.append(", createUuid=").append(createUuid);
        sb.append("]");
        return sb.toString();
    }
}