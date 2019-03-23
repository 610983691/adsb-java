package com.coulee.foundations.configserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.foundations.configserver.entity.ConfigItems;

/**
 * Description: 配置项dao<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface ConfigItemsMapper extends IBaseDao {
	
	/**
	 * Description: 根据产品ID或模块ID查询配置项<br> 
	 * Created date: 2017年12月28日
	 * @param params
	 * @return
	 * @author oblivion
	 */
	public List<ConfigItems> findConfigItems(@Param("params")Map<String, Object> params);
	
	/**
	 * Description: 根据产品或模块删除配置项<br> 
	 * Created date: 2017年12月28日
	 * @param params
	 * @return
	 * @author oblivion
	 */
	public int deleteByProductOrMoudle(@Param("params")Map<String, Object> params);
	
	/**
	 * Description: 根据配置项ID集合批量修改配置项信息<br> 
	 * Created date: 2018年1月3日
	 * @param params
	 * @return
	 * @author oblivion
	 */
	public int updateExtendsItemsByParentIds(@Param("params")Map<String, Object> params);
	
	/**
	 * Description: 根据ConfigMainId查询最大排序值<br> 
	 * Created date: 2018年1月5日
	 * @param configMainId
	 * @return
	 * @author oblivion
	 */
	public Integer findMaxSeqByMainId(Integer configMainId);
}