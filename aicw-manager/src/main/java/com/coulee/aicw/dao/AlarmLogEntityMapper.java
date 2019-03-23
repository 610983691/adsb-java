package com.coulee.aicw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

public interface AlarmLogEntityMapper extends IBaseDao {
	/**
	 * 根据手机号码查询告警日志
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日上午11:32:59
	* @param 
	* @return
	 */
	public AlarmLogEntity findByTel(String telphone);
	
	
	/***
	 * 根据接收时间，查询接收到告警数量的前10告警电话号码
	 * @param entity
	 * @return
	 */
	public List<StatisticEntity> queryTop10Phone(StatisticEntity entity );
	
	
	
	/***
	 * 根据号码/时间查询对应的半年、上月、历史的告警数量
	 * @param entity
	 * @return
	 */
	public List<StatisticEntity> queryCountByPhoneNumber(StatisticEntity entity);
	
}