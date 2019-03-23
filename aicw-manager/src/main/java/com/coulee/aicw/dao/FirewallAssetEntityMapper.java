package com.coulee.aicw.dao;

import java.util.List;

import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;

public interface FirewallAssetEntityMapper extends IBaseDao {
	
	
	/***
	 * 查询各个状态的防火墙数量
	 * @return
	 */
	List<StatisticEntity> queryTestStatusCount();
}