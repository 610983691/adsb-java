package com.coulee.aicw.foundations.entity;

/**
 * Description: 下拉框数据结构<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class JsonCombo extends BaseEntity {

	private static final long serialVersionUID = -1380307231202583399L;

	/**
	 * 显示的内容
	 */
	private String text;

	/**
	 * 实际值
	 */
	private String value;
	
	public JsonCombo() {
	}
	
	/**
	 * Description :实例化JsonComboVo对象
	 * @param text 下拉列表显示文本
	 * @param value 下拉列表值
	 */
	public JsonCombo(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}

	