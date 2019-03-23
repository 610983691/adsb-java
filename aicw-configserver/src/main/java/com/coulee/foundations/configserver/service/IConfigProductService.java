package com.coulee.foundations.configserver.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.foundations.configserver.entity.ConfigProduct;

/**
 * Description: 产品管理接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigProductService extends IBaseService {

	/**
	 * Description: 删除产品信息<br> 
	 * Created date: 2017年12月26日
	 * @param products
	 * @return
	 * @author oblivion
	 */
	public Message batchDelete(List<ConfigProduct> products);
}
