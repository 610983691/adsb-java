package com.coulee.aicw.foundations.entity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Description: 基础实体类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@JsonNaming(PagingPropertyNamingStrategy.class)
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -9033707907877937177L;

	/**
	 * 前台传入的当前操作用户
	 */
	private String _username;

	/**
	 * 分页参数：页大小
	 */
	private Integer _limit;
	
	/**
	 * 分页参数：起始条数
	 */
	private Integer _offset;
	
	/**
	 * 分页参数：查询页码
	 */
	private Integer _page;

	public String get_username() {
		return _username;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public Integer get_limit() {
		return _limit;
	}

	public void set_limit(Integer _limit) {
		this._limit = _limit;
	}

	public Integer get_offset() {
		return _offset;
	}

	public void set_offset(Integer _offset) {
		this._offset = _offset;
	}

	public Integer get_page() {
		return _page;
	}

	public void set_page(Integer _page) {
		this._page = _page;
	}
	
	
}
