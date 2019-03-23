package com.coulee.foundations.configserver.service;

import java.util.Map;

/**
 * Description: 维护zookeeper服务上配置信息<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigService {

	/**
	 * Description:设置配置值，并写入zk相应路径<br> 
	 * Created date: 2017年11月24日
	 * @param product 产品名
	 * @param appName 产品模块名
	 * @param key 配置项
	 * @param value 配置项值
	 * @return 是否配置成功
	 * @author oblivion
	 */
	public boolean setConfigValue(String product, String appName, String key, String value);
	
	/**
	 * Description:从zk读取配置值<br> 
	 * Created date: 2017年11月24日
	 * @param product 产品名
	 * @param appName 产品模块名
	 * @param key 配置项
	 * @return 配置项值
	 * @author oblivion
	 */
	public String getConfigValue(String product, String appName, String key);
	
	/**
	 * Description:获取某产品模块所有配置<br> 
	 * Created date: 2017年11月24日
	 * @param product 产品名
	 * @param appName 产品模块名
	 * @return 配置项及值
	 * @author oblivion
	 */
	public Map<String, String> getAllConfigValue(String product, String appName);
	
	/**
	 * Description: 向zk写入加解密配置<br> 
	 * Created date: 2017年12月28日
	 * @param product
	 * @param appName
	 * @param cryptType
	 * @param cryptKey
	 * @return
	 * @author oblivion
	 */
	public boolean setCryptoConfig(String product, String appName, String cryptType, String cryptKey);
	
	/**
	 * Description: 删除整个产品配置信息<br> 
	 * Created date: 2017年12月28日
	 * @param product
	 * @return
	 * @author oblivion
	 */
	public boolean deleteConfig(String product);
	
	/**
	 * Description: 删除指定产品下指定模块配置信息<br> 
	 * Created date: 2017年12月28日
	 * @param product
	 * @param appName
	 * @return
	 * @author oblivion
	 */
	public boolean deleteConfig(String product, String appName);
	
	/**
	 * Description: 删除指定产品指定模块下指定配置信息<br> 
	 * Created date: 2017年12月28日
	 * @param product
	 * @param appName
	 * @param key
	 * @return
	 * @author oblivion
	 */
	public boolean deleteConfig(String product, String appName, String key);
}
