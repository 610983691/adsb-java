package com.coulee.aicw.foundations.entity;

import java.io.Serializable;

/**
 * Description: 操作对象信息<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class OperObject implements Serializable {

	private static final long serialVersionUID = -3924881193597008448L;

	/**
	 * 操作对象ID
	 */
	private String id;
	
	/**
	 * 操作对象名
	 */
	private String name;
	
	public OperObject() {
	}
	
	public OperObject(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
