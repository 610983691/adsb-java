package com.coulee.aicw.dao;

import java.util.List;
import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;

public interface ControlLogEntityMapper extends IBaseDao {
	 public ControlLogEntity findBySmsNum(String smsnumber);
	 
	 public List<StatisticEntity> statisticByStatus(ControlLogEntity entity);
	 
	 public List<StatisticEntity> statisticByIP(ControlLogEntity entity);
	 
	 /***
	  * 根据条件查询最近的10条策略日志
	  * @param entity 查询条件实体
	  * @return
	  */
	 List<ControlLogEntity> queryLatest10ControlLogs(ControlLogEntity entity);
}