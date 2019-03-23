package com.coulee.foundations.configserver.dao;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.foundations.configserver.entity.ConfigUser;

/**
 * Description: 用户操作DAO<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ConfigUserMapper extends IBaseDao {
	
	/**
	 * Description: 根据用户UID查询用户<br> 
	 * Created date: 2017年12月22日
	 * @param uuid
	 * @return
	 * @author oblivion
	 */
	public ConfigUser findByUuid(String uuid);
}