package com.coulee.aicw.foundations.utils.page;

import java.util.List;

/**
 * Description: 分页操作工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class PageUtils {

	/**
	 * Definition:构造PageList，适用于内存分页
	 * @param pageArg 分页参数
	 * @param retlist 所有的结果集合
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	public static <T> PageList<T> makeListPage(PageArg pageArg, List<T> retlist) {
		if (retlist == null) {
			return null;
		}
		PageList<T> retpl = null;
		if (pageArg != null) {
			if (pageArg.getTotalRow() > 0) {
				retpl = new PageList<T>(retlist, pageArg.getCurPage(), pageArg.getPageSize(), pageArg.getTotalRow());
			} else {
				retpl = new PageList<T>(retlist, pageArg.getCurPage(), pageArg.getPageSize());
			}
		} else {
			retpl = new PageList<T>(retlist);
		}
		return retpl;
	}

	/**
	 * Definition:构造分页参数
	 * @param start 起始数
	 * @param limit 页大小
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	public static PageArg getPageArg(String start, String limit) {
		PageArg pageArg = new PageArg();
		if (limit != null && !"".equals(limit) && !"".equals(start) && start != null) {
			pageArg.setCurPage(Integer.parseInt(start), Integer.parseInt(limit));
			pageArg.setPageSize(Integer.parseInt(limit));
		} else {
			pageArg.setCurPage(1);
		}
		return pageArg;
	}
	
}
