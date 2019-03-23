package com.coulee.foundations.configserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.foundations.configserver.entity.ConfigMain;

/**
 * Description: 配置项主表dao<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ConfigMainMapper extends IBaseDao {
	
	/**
	 * Description: 根据产品或模块删除配置项主表信息<br> 
	 * Created date: 2017年12月28日
	 * @param params
	 * @return
	 * @author oblivion
	 */
	public int deleteByProductOrMoudle(@Param("params")Map<String, Object> params);
	
	/**
	 * Description: 根据产品ID查询产品级别配置主信息<br> 
	 * Created date: 2017年12月29日
	 * @param productId
	 * @return
	 * @author oblivion
	 */
	public List<ConfigMain> findProductLevelConfig(Integer productId);
}