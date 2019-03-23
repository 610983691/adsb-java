package com.coulee.aicw.dao;

import java.util.List;
import com.coulee.aicw.entity.statistic.ChinaMapEntity;
import com.coulee.aicw.entity.statistic.UserLogTop10Entity;
import com.coulee.aicw.foundations.dao.IBaseDao;

public interface StatisticMapper extends IBaseDao {
	/***
	 * 查询各省份的告警数量
	 * @param entity
	 * @return
	 */
	public List<ChinaMapEntity> queryChinaMapData(ChinaMapEntity entity);
	
	/***
	 * 用户申请日志top10
	 * @param entity
	 * @return
	 */
	public List<UserLogTop10Entity> queryUserStatusTop10(UserLogTop10Entity entity);
}