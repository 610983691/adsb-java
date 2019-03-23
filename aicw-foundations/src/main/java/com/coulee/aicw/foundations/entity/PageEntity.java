package com.coulee.aicw.foundations.entity;

import java.util.ArrayList;
import java.util.List;

import com.coulee.aicw.foundations.utils.page.PageList;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Description: 返回前端的分页形式的对象<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@JsonNaming(PagingPropertyNamingStrategy.class)
public class PageEntity<E> implements java.io.Serializable {

	private static final long serialVersionUID = -1638021856188219528L;
	
	@SuppressWarnings("unused")
	private PageEntity() {
	}

	/**
	 * 总条数
	 */
	private Integer _total = 0;
	
	/**
	 * 本页数据
	 */
	private List<E> _rows = new ArrayList<>(0);
	
	/**
	 * Description: 实例化分页数据对象<br>
	 * @param pageList 分页后的数据集合
	 */
	public PageEntity(PageList<E> pageList) {
		if (pageList != null && !pageList.isEmpty()) {
			this._total = pageList.getTotalRow();
			this._rows = pageList;
		}
	}

	public Integer get_total() {
		return _total;
	}

	public void set_total(Integer _total) {
		this._total = _total;
	}

	public List<E> get_rows() {
		return _rows;
	}

	public void set_rows(List<E> _rows) {
		this._rows = _rows;
	}
	
}
