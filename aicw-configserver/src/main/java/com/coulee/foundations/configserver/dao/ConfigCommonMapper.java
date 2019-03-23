package com.coulee.foundations.configserver.dao;

import com.coulee.aicw.foundations.dao.IBaseDao;

/**
 * Description: 通用dao<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ConfigCommonMapper extends IBaseDao {
	
	/**
	 * Description: 获取当前线程内，上一个执行成功的insert语句产生的新主键ID<br> 
	 * Created date: 2017年12月29日
	 * @return
	 * @author oblivion
	 */
	public Integer getLastId();
}