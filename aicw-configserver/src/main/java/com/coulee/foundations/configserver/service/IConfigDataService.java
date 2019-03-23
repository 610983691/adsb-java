package com.coulee.foundations.configserver.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigMain;
import com.coulee.foundations.configserver.entity.ConfigModules;
import com.coulee.foundations.configserver.entity.ConfigProduct;

/**
 * Description: 配置中心-配置导入service接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigDataService {

	/**
	 * Description: 根据数据文件，查询其内部的产品数据<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @return
	 * @author oblivion
	 */
	public List<ConfigProduct> findProduct(String dbFilePath);
	
	/**
	 * Description: 根据产品ID查询其下的模块数据<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @param productId 临时数据ID
	 * @param realId 真实数据ID
	 * @return
	 * @author oblivion
	 */
	public List<ConfigModules> findModules(String dbFilePath, Integer productId, Integer realId);
	
	/**
	 * Description: 根据产品ID、模块ID查询配置项分类<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @param productId 临时产品ID
	 * @param moduleId 临时模块ID
	 * @param realProductId 真实产品ID
	 * @param realModuleId 真实模块ID
	 * @return
	 * @author oblivion
	 */
	public List<ConfigMain> findConfigType(String dbFilePath, Integer productId, Integer moduleId, Integer realProductId, Integer realModuleId);
	
	/**
	 * Description: 根据配置类型ID获取配置列表<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @param configTypeId 临时分类ID
	 * @param realConfigTypeId 真实分类ID
	 * @return
	 * @author oblivion
	 */
	public List<ConfigItems> findConfigItemsByType(String dbFilePath, Integer configTypeId, Integer realConfigTypeId);
	
	/**
	 * Description: 查看某产品及其模块下是否存在配置项<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @param productId
	 * @param moduleId
	 * @return
	 * @author oblivion
	 */
	public Message checkHaveConfigItems(String dbFilePath, Integer productId, Integer moduleId);
	
	/**
	 * Description: 保存临时配置项信息<br> 
	 * Created date: 2018年1月11日
	 * @param dbFilePath
	 * @param configTypeId
	 * @param items
	 * @return
	 * @author oblivion
	 */
	public Message saveConfigItems(String dbFilePath, Integer configTypeId, List<ConfigItems> items);
	
	/**
	 * Description: 导入配置类别<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param params
	 * @return
	 * @author oblivion
	 */
	public Message importType(String dbFilePath, String params);
	
	/**
	 * Description: 导入全部配置<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param params
	 * @return
	 * @author oblivion
	 */
	public Message importAll(String dbFilePath, String params);
}
