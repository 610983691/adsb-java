package com.coulee.foundations.configserver.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 配置项实体类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigItems extends BaseEntity {
	
    private Integer id;

    private Integer configMainId;

    private String itemDesc;

    private Integer itemIsCrypt;

    private String itemKey;

    private String itemValue;

    private Integer itemSeq;

    private Integer isExtends;

    private Integer extendsId;
    
//  =====================================以下为非数据库属性=====================================
    
    /**
     * 产品标识
     */
    private String productMark;
    
    /**
     * 模块标识
     */
    private String moduleMark;
    
    /**
     * 页面数据：数据版本：new / old
     */
    private String dataVersion;
    
    /**
     * 页面数据：真实数据ID
     */
    private Integer realId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConfigMainId() {
        return configMainId;
    }

    public void setConfigMainId(Integer configMainId) {
        this.configMainId = configMainId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Integer getItemIsCrypt() {
        return itemIsCrypt;
    }

    public void setItemIsCrypt(Integer itemIsCrypt) {
        this.itemIsCrypt = itemIsCrypt;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(Integer itemSeq) {
        this.itemSeq = itemSeq;
    }

    public Integer getIsExtends() {
        return isExtends;
    }

    public void setIsExtends(Integer isExtends) {
        this.isExtends = isExtends;
    }

    public String getProductMark() {
		return productMark;
	}

	public void setProductMark(String productMark) {
		this.productMark = productMark;
	}

	public String getModuleMark() {
		return moduleMark;
	}

	public void setModuleMark(String moduleMark) {
		this.moduleMark = moduleMark;
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
        sb.append(", configMainId=").append(configMainId);
        sb.append(", itemDesc=").append(itemDesc);
        sb.append(", itemIsCrypt=").append(itemIsCrypt);
        sb.append(", itemKey=").append(itemKey);
        sb.append(", itemValue=").append(itemValue);
        sb.append(", itemSeq=").append(itemSeq);
        sb.append(", isExtends=").append(isExtends);
        sb.append(", extendsId=").append(extendsId);
        sb.append("]");
        return sb.toString();
    }
}