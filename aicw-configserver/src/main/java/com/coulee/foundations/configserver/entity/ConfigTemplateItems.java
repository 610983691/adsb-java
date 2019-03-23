package com.coulee.foundations.configserver.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 模板配置项对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigTemplateItems extends BaseEntity {
    private Integer id;

    private Integer templateId;

    private String itemDesc;

    private String itemIsCrypt;

    private String itemKey;

    private String itemValue;

    private Integer itemSeq;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemIsCrypt() {
        return itemIsCrypt;
    }

    public void setItemIsCrypt(String itemIsCrypt) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", templateId=").append(templateId);
        sb.append(", itemDesc=").append(itemDesc);
        sb.append(", itemIsCrypt=").append(itemIsCrypt);
        sb.append(", itemKey=").append(itemKey);
        sb.append(", itemValue=").append(itemValue);
        sb.append(", itemSeq=").append(itemSeq);
        sb.append("]");
        return sb.toString();
    }
}