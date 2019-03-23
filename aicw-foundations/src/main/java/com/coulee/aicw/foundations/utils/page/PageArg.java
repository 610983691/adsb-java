package com.coulee.aicw.foundations.utils.page;

import java.io.Serializable;

/**
 * Description: 分页参数<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class PageArg implements Cloneable, Serializable {

	private static final long serialVersionUID = 4557087473442735213L;
	
	/**
	 * 总条数
	 */
	private int totalRow;
	
	 /**
     * 总页数
     */
    private int totalPage;

	/**
	 * 起始页
	 */
	private int start;

	/**
	 * 当前页
	 */
	private int curPage;
	
	/**
	 *  页大小
	 */
	private int pageSize = Integer.MAX_VALUE;

	public PageArg() {

	}

	/**
	 * Access method for the curPage property.
	 * 
	 * @return the current value of the curPage property
	 */
	public int getCurPage() {
		return curPage;
	}

	public int getStart() {
		return start;
	}

	/**
	 * Sets the value of the curPage property.
	 * 
	 * @param aCurPage
	 *            the new value of the curPage property
	 */
	public void setCurPage(int aCurPage) {
		curPage = aCurPage;
	}

	public void setCurPage(int start, int limit) {
		this.start = start;
		if (limit > 0) {
			curPage = start / limit + 1;
		} else {
			curPage = 1;
		}
	}

	/**
	 * Access method for the pageSize property.
	 * 
	 * @return the current value of the pageSize property
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the value of the pageSize property.
	 * 
	 * @param aPageSize
	 *            the new value of the pageSize property
	 */
	public void setPageSize(int aPageSize) {
		pageSize = aPageSize;
	}


	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
