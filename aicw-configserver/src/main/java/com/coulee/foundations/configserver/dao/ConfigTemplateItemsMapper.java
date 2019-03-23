package com.coulee.foundations.configserver.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: 模板配置项dao<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ConfigTemplateItemsMapper extends IBaseDao {
	
	/**
	 * Description: 查询配置项<br> 
	 * Created date: 2018年1月16日
	 * @param params
	 * @param pageArg
	 * @return
	 * @author oblivion
	 */
	public <T extends BaseEntity> PageList<T> findByParamsOrderBySEQ(@Param("params") Map<String, Object> params, @Param("pageArg") PageArg pageArg);
	
	/**
	 * Description: 根据模板ID删除模板配置项<br> 
	 * Created date: 2018年1月16日
	 * @param templateId
	 * @return
	 * @author oblivion
	 */
	public int deleteByTemplateId(Integer templateId);
}