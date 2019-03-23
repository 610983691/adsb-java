package com.coulee.foundations.configserver.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 模板对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigTemplate extends BaseEntity {
	
	private Integer id;

	private String templateName;

	private String templateDesc;

	private String configType;

	private String createUuid;

	private Integer isInner;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
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

	public Integer getIsInner() {
		return isInner;
	}

	public void setIsInner(Integer isInner) {
		this.isInner = isInner;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", templateName=").append(templateName);
		sb.append(", templateDesc=").append(templateDesc);
		sb.append(", configType=").append(configType);
		sb.append(", createUuid=").append(createUuid);
		sb.append(", isInner=").append(isInner);
		sb.append("]");
		return sb.toString();
	}
	
}