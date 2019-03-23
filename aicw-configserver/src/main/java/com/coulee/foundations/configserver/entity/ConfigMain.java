package com.coulee.foundations.configserver.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 配置类别对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigMain extends BaseEntity {
    private Integer id;

    private Integer productId;

    private Integer moduleId;

    private String configType;

    private String createUuid;
    
    private Integer extendsId;

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

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getCreateUuid() {
        return createUuid;
    }

    public void setCreateUuid(String createUuid) {
        this.createUuid = createUuid;
    }

    public Integer getExtendsId() {
		return extendsId;
	}

	public void setExtendsId(Integer extendsId) {
		this.extendsId = extendsId;
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
        sb.append(", moduleId=").append(moduleId);
        sb.append(", configType=").append(configType);
        sb.append(", createUuid=").append(createUuid);
        sb.append(", extendsId=").append(extendsId);
        sb.append("]");
        return sb.toString();
    }
}