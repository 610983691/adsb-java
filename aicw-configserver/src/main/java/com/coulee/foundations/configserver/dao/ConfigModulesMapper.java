package com.coulee.foundations.configserver.dao;

import com.coulee.aicw.foundations.dao.IBaseDao;


/**
 * Description: 模块操作dao<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * 
 * @author oblivion
 * @version 1.0
 */
public interface ConfigModulesMapper extends IBaseDao {

	/**
	 * Description: 根据产品ID删除模块数据<br>
	 * Created date: 2017年12月28日
	 * 
	 * @param productId
	 * @return
	 * @author oblivion
	 */
	public int deleteByProductId(Integer productId);

}