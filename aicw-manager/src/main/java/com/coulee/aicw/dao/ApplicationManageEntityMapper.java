package com.coulee.aicw.dao;

import com.coulee.aicw.entity.ApplicationManageEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;

public interface ApplicationManageEntityMapper extends IBaseDao {
	/**
	 * 根据应用别名查询应用
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日下午3:27:22
	* @param 
	* @return
	 */
	public ApplicationManageEntity findByAppAlias(String appAlias);
}