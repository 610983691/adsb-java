package com.coulee.foundations.configserver.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.entity.ConfigTemplate;
import com.coulee.foundations.configserver.entity.ConfigTemplateItems;

/**
 * Description: 模板管理service接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigTemplateService extends IBaseService {

	/**
	 * Description: 增加模板<br> 
	 * Created date: 2018年1月16日
	 * @param configTemplate
	 * @param items
	 * @return
	 * @author oblivion
	 */
	public Message addConfigTemplate(ConfigTemplate configTemplate, List<ConfigTemplateItems> items);
	
	/**
	 * Description: 删除模板<br> 
	 * Created date: 2018年1月16日
	 * @param configTemplate
	 * @return
	 * @author oblivion
	 */
	public Message batchDelete(List<ConfigTemplate> configTemplate);
	
	/**
	 * Description: 查询模板配置项详情<br> 
	 * Created date: 2018年1月16日
	 * @param configTemplate
	 * @return
	 * @author oblivion
	 */
	public List<ConfigTemplateItems> detailConfigTemplate(ConfigTemplate configTemplate);

	/**
	 * Description: 更新模板<br> 
	 * Created date: 2018年1月16日
	 * @param configTemplate
	 * @param items
	 * @return
	 * @author oblivion
	 */
	public Message updateConfigTemplate(ConfigTemplate configTemplate, List<ConfigTemplateItems> items);
	
	/**
	 * Description: 查询模板<br> 
	 * Created date: 2018年1月16日
	 * @param entity
	 * @param pageArg
	 * @return
	 * @author oblivion
	 */
	public <T extends BaseEntity> PageList<T> findByParamsWithInner(T entity, PageArg pageArg);

	/**
	 * Description: 根据用户ID查询其可见的模板数据<br> 
	 * Created date: 2018年1月8日
	 * @param entity
	 * @param pageArg
	 * @return
	 * @author LanChao
	 */
	public List<ConfigTemplate> findByCreater(ConfigTemplate entity, PageArg pageArg);
	
	/**
	 * Description: 根据模板ID查询其下的配置项<br> 
	 * Created date: 2018年1月8日
	 * @param templateId
	 * @return
	 * @author LanChao
	 */
	public List<ConfigTemplateItems> findByTemplateId(Integer templateId);
}
