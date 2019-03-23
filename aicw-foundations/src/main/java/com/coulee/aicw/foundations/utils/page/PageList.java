package com.coulee.aicw.foundations.utils.page;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 分页结果集合<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class PageList<E> extends ArrayList<E> {
	
    private static final long serialVersionUID = -9159597235071471209L;

    /**
     * 总行数
     */
    private int totalRow;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页
     */
    private int curPage = 0;
    /**
     * 页大小
     */
    private int pageSize = Integer.MAX_VALUE;
    

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getTotalRow() {
        return totalRow;
    }


    public int getTotalPage() {
        return totalPage;
    }


    public int getCurPage() {
        return curPage;
    }

    public int getPageSize() {
        return pageSize;
    }
    
    /**
     *  实现不分页查询
     * @param totalRow 总行数
     * 默认curPage=1,pageSize为最大值
     */
    public PageList(int totalRow) {
        this(totalRow, 1, Integer.MAX_VALUE);
    }

    /**
     *  实现不分页查询
     * @param totalRow 总行数
     * 默认curPage=1,pageSize为最大值
     */
    public PageList(List<E> totalList) {
        this(totalList, 1,Integer.MAX_VALUE);
    }

    public PageList() {

    }
    
    /**
     * 实现分页
     * @param totalRow int 总行数
     * @param curPage int 当前页
     * @param pageSize int 页大小
     */
    public PageList(int totalRow, int curPage, int pageSize) {
        if (totalRow < 0) {
            totalRow = 0;
        }
        if (pageSize < 0) {
            pageSize = 0;
        }
        if (this.curPage <= 0) {
            this.curPage = 1;
        }
        this.totalRow = totalRow;
        this.pageSize = pageSize;
        this.curPage = curPage;
        // 计算总页数
        this.totalPage = totalRow / pageSize;
        if (this.totalRow % pageSize > 0) {
            this.totalPage += 1;
        }
        // 当前页 > 总页数 设置当前页为总页数
        if (curPage > this.totalPage) {
            this.curPage = this.totalPage;
        }
    }


    /**
     * 实现分页查询
     * @param totalList List
     * @param curPage int
     * @param pageSize int
     */
    public PageList(List<E> totalList, int curPage, int pageSize) {
        if (totalList == null) {
            return;
        }
        this.totalRow = totalList.size();
        if (this.totalRow < 0) {
            this.totalRow = 0;
        }
        if (pageSize < 0) {
            pageSize = 0;
        }
        this.pageSize = pageSize;
        // 计算 总页数
        this.totalPage = totalRow / pageSize;
        if (this.totalRow % pageSize > 0) {
            this.totalPage += 1;
        }
        this.curPage = curPage;
        if (this.curPage <= 0) {
            this.curPage = 1;
        }
        // 当前页 > 总页数 设置当前页为总页数
        if (this.curPage > this.totalPage) {
            this.curPage = this.totalPage;
        }
        // 根据当前页和页大小，取到List中的相应一块数据
        this.iniPageList(totalList);
    }
    
    /**
     * 在LDAP查询将结果转为POJO对象时已按分页信息进行转换，
     * 所以使用此构造方法，将总条数传递进来，并将所有记录放到该PageList中
     * @param totalList List
     * @param curPage int
     * @param pageSize int
     * @param totalRow int
     */
    public PageList(List<E> totalList, int curPage, int pageSize, int totalRow) {
		if (totalList == null) {
			return;
		}
		this.totalRow = totalRow;
		if (this.totalRow < 0) {
			this.totalRow = 0;
		}
		if (pageSize < 0) {
			pageSize = 0;
		}
		this.pageSize = pageSize;
		// 计算 总页数
		this.totalPage = totalRow / pageSize;
		if (this.totalRow % pageSize > 0) {
			this.totalPage += 1;
		}
		this.curPage = curPage;
		if (this.curPage <= 0) {
			this.curPage = 1;
		}
		// 当前页 > 总页数 设置当前页为总页数
		if (this.curPage > this.totalPage) {
			this.curPage = this.totalPage;
		}
		this.addAll(totalList);
	}

    /**
     * 根据当前页和页大小，取到List中的相应一块数据
     * @param totalList List
     */
    private void iniPageList(List<E> totalList) {
        if (totalList == null || totalList.size() <= 0) {
            return;
        }
        for (int i = (this.getCurPage() - 1) * this.getPageSize()
                     , j = 0; j < this.getPageSize() &&
                           i < this.getTotalRow(); i++, j++) {
            this.add(totalList.get(i));
        }
    }

}
