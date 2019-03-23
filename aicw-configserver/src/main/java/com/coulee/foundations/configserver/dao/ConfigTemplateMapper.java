package com.coulee.foundations.configserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.entity.ConfigTemplate;

/**
 * Description: 模板主表dao<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ConfigTemplateMapper extends IBaseDao {
	
	/**
	 * Description: 查询模板列表<br> 
	 * Created date: 2018年1月16日
	 * @param params
	 * @param pageArg
	 * @return
	 * @author oblivion
	 */
	public <T extends BaseEntity> PageList<T> findByParamsWithInner(@Param("params") Map<String, Object> params, @Param("pageArg") PageArg pageArg);
	
	
	/**
	 * 
	 * Description: 查询用户可见的模板主数据<br> 
	 * Created date: 2018年1月8日
	 * @param params
	 * @param pageArg
	 * @return
	 * @author LanChao
	 */
	public List<ConfigTemplate> findByCreater(@Param("params")Map<String, Object> params, @Param("pageArg")PageArg pageArg);
	
	/**
	 * Description: 判断模板名是否存在<br> 
	 * Created date: 2018年1月16日
	 * @param params
	 * @return
	 * @author LanChao
	 */
	public List<ConfigTemplate> checkExist(@Param("params")Map<String, Object> params);
}