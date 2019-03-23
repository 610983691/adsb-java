package com.coulee.aicw.foundations.controller;

import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: Controller基类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * 
 * @author oblivion
 * @version 1.0
 */
public abstract class BaseController {

	/**
	 * Description: 构造分页参数<br>
	 * Created date: 2018年3月12日
	 * 
	 * @param entity BaseEntity实体类
	 * @return
	 * @author LanChao
	 */
	protected PageArg getPageArg(BaseEntity entity) {
		if (entity.get_limit() == null) {
			return null;
		}
		if (entity.get_page() == null && entity.get_offset() == null) {
			return null;
		}
		/***
		 * page参数会优先匹配
		 */
		if (entity.get_page() != null && entity.get_page() > 0) {
			int offset = entity.get_limit() * (entity.get_page() - 1);
			return this.getPageArg(offset, entity.get_limit());
		}
		return this.getPageArg(entity.get_offset(), entity.get_limit());
	}

	/**
	 * Definition:构造分页参数
	 * 
	 * @param offset 起始数据条数
	 * @param limit  页大小
	 * @return
	 * @author: LanChao
	 * @Created date: 2014年12月10日
	 */
	protected PageArg getPageArg(int offset, int limit) {
		PageArg pageArg = new PageArg();
		pageArg.setCurPage(offset, limit);
		pageArg.setPageSize(limit);
		return pageArg;
	}

	/**
	 * Description: 构造返回前端的分页形式数据<br>
	 * Created date: 2018年3月12日
	 * 
	 * @param pageList
	 * @return
	 * @author LanChao
	 */
	protected <E> PageEntity<E> makePageEntity(PageList<E> pageList) {
		return new PageEntity<E>(pageList);
	}
}
