package com.coulee.foundations.configserver.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.foundations.configserver.entity.ConfigModules;

/**
 * Description: 产品模块管理接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigModulesService extends IBaseService {

	/**
	 * Description: 删除模块信息<br> 
	 * Created date: 2017年12月26日
	 * @param modules
	 * @return
	 * @author oblivion
	 */
	public Message batchDelete(List<ConfigModules> modules);
}
