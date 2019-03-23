package com.coulee.foundations.configserver.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigMain;

/**
 * Description: 配置项管理<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigItemsService {

	/**
	 * Description: 保存配置信息临时方法<br> 
	 * Created date: 2017年12月25日
	 * @param productId
	 * @param moduleId
	 * @param configInfo
	 * @return
	 * @author oblivion
	 */
	public Message saveConfigTemp(Integer productId, Integer moduleId, String configInfo);
	
	/**
	 * Description: 查询某产品所有模块下的配置项信息<br> 
	 * Created date: 2017年12月28日
	 * @param productId
	 * @return
	 * @author oblivion
	 */
	public List<ConfigItems> findConfigItems(Integer productId);
	
	/**
	 * Description: 查询某产品某模块下的配置项信息<br> 
	 * Created date: 2017年12月28日
	 * @param productId
	 * @param moduleId
	 * @return
	 * @author oblivion
	 */
	public List<ConfigItems> findConfigItems(Integer productId, Integer moduleId);
	
	/**
	 * Description: 删除某产品所有配置项信息<br> 
	 * Created date: 2017年12月28日
	 * @param productId
	 * @return
	 * @author oblivion
	 */
	public Message deleteConfigItems(Integer productId);
	
	/**
	 * Description: 删除某产品下某模块所有配置信息<br> 
	 * Created date: 2017年12月28日
	 * @param productId
	 * @param moduleId
	 * @return
	 * @author oblivion
	 */
	public Message deleteConfigItems(Integer productId, Integer moduleId);
	
	/**
	 * Description: 根据产品ID处理其下某模块继承的配置信息<br> 
	 * Created date: 2017年12月28日
	 * @param productId 待继承的产品ID
	 * @param moduleId 要继承的模块ID
	 * @param productMark 待继承的产品标识
	 * @param moduleMark 要继承的模块标识
	 * @author oblivion
	 */
	public void processExtendsConfigItems(Integer productId, Integer moduleId, String productMark, String moduleMark);
	
	/**
	 * Description: 根据产品ID、模块ID获取配置类别列表<br> 
	 * Created date: 2018年1月2日
	 * @param productId
	 * @param moduleId
	 * @return
	 * @author oblivion
	 */
	public List<ConfigMain> findConfigType(Integer productId, Integer moduleId);
	
	/**
	 * Description: 根据配置类型ID获取配置列表<br> 
	 * Created date: 2018年1月2日
	 * @param configMainId
	 * @return
	 * @author oblivion
	 */
	public List<ConfigItems> findConfigItemsByType(Integer configMainId);
	
	/**
	 * Description: 保存配置项信息<br> 
	 * Created date: 2018年1月2日
	 * @param configMainId
	 * @param configItems
	 * @return
	 * @author oblivion
	 */
	public Message saveConfigItems(Integer configMainId, List<ConfigItems> configItems);
	
	/**
	 * Description: 删除配置项分类及其配置项信息<br> 
	 * Created date: 2018年1月3日
	 * @param configMainId
	 * @return
	 * @author oblivion
	 */
	public Message deleteConfig(Integer configMainId);
	
	/**
	 * Description: 判断配置类别是否不存在<br> 
	 * Created date: 2018年1月8日
	 * @param productId
	 * @param moduleId
	 * @param configType
	 * @return
	 * @author oblivion
	 */
	public Message checkConfigType (Integer productId, Integer moduleId, String configType);
	
	/**
	 * Description: 增加配置类别及配置项<br> 
	 * Created date: 2018年1月8日
	 * @param configMain
	 * @param configItems
	 * @return
	 * @author oblivion
	 */
	public Message addConfigTypeAndItems(ConfigMain configMain, List<ConfigItems> configItems);
	
	/**
	 * Description: 重新向ZK内写入配置<br> 
	 * Created date: 2018年1月17日
	 * @param productId
	 * @param moduleId
	 * @return
	 * @author oblivion
	 */
	public Message overWriteConfigItems(Integer productId, Integer moduleId);
}
